package io.orbit.ui.terminal;

import com.pty4j.PtyProcess;
import com.pty4j.WinSize;
import io.orbit.util.OS;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class EmbeddedTerminal extends Pane
{
    private static final File PTY_LIB;
    private static final String WIN_TERM_STARTER = "cmd.exe";
    private static final String UNIX_TERM_STARTER = "/bin/bash -i";

    static {
        URL ptyLib = EmbeddedTerminal.class.getClassLoader().getResource("libpty");
        assert ptyLib != null;
        PTY_LIB = new File(ptyLib.getFile());
    }

    private static final Semaphore uiSemaphore = new Semaphore(1);
    private static ExecutorService service;
    private ObjectProperty<Reader> inputReader;
    private ObjectProperty<Reader> errorReader;
    private ObjectProperty<Writer> outputWriter;
    private final CountDownLatch timer = new CountDownLatch(1);
    private final LinkedBlockingQueue<String> queue;
    private PtyProcess process;
    private Map<String, String> preferences = new HashMap<>();
    private final ObjectProperty<Integer> rows;
    private final ObjectProperty<Integer> columns;
    private WebView webView;

    public EmbeddedTerminal()
    {
        super();
        this.webView = new WebView();
        this.queue = new LinkedBlockingQueue<>();
        this.rows = new SimpleObjectProperty<>(5);
        this.columns = new SimpleObjectProperty<>(200);

    }
    protected void open()
    {
        if (this.webView == null)
            this.webView = new WebView();
        if (service == null)
            service = Executors.newSingleThreadExecutor();
        this.inputReader = new SimpleObjectProperty<>();
        this.errorReader = new SimpleObjectProperty<>();
        this.outputWriter = new SimpleObjectProperty<>();
        this.build();
        this.registerListeners();
    }

    protected void dispose()
    {
        try
        {
            this.process.destroy();
            this.inputReader.get().close();
            this.errorReader.get().close();
            this.outputWriter.get().close();
            this.queue.clear();
            this.inputReader = null;
            this.errorReader = null;
            this.outputWriter = null;
            this.process = null;
            service.shutdown();
            service = null;
            this.webView = null;
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    private void build()
    {
        this.webView.prefWidthProperty().bind(this.widthProperty());
        this.webView.prefHeightProperty().bind(this.heightProperty());
        this.webView.getEngine()
                .getLoadWorker()
                .stateProperty()
                .addListener((obs, oldV, newV) -> window().setMember("application", this));
        URL terminalPage = getClass().getClassLoader().getResource("terminal/terminal.html");
        assert terminalPage != null;
        this.webView.getEngine().load(terminalPage.toExternalForm());
        this.webView.getEngine().setOnAlert(event -> System.out.println(event.getData()));
        this.webView.getEngine().getLoadWorker().stateProperty().addListener((o, oldV, newV) -> {
            if (newV == Worker.State.SUCCEEDED && this.preferences.size() > 0)
            {
                this.preferences.forEach(this::setPreference);
                this.preferences.clear();
            }
        });
    }

    private void registerListeners()
    {
        this.inputReader.addListener((obs, oldV, newV) -> run(() -> printBuffer(newV)));
        this.errorReader.addListener((obs, oldV, newV) -> run(() -> printBuffer(newV)));
        this.rows.addListener(e -> updateWindowSize());
        this.columns.addListener(e -> updateWindowSize());
    }

    public void terminalInit() { Platform.runLater(() -> this.getChildren().add(this.webView)); }

    public void terminalReady()
    {
        run(this::initializeProcesses);
    }

    public void command(String command)
    {
        try { queue.put(command); }
        catch (InterruptedException ex) { throw new RuntimeException(ex); }
        run(() -> {
            try
            {
                String commandToExec = queue.poll();
                this.outputWriter.get().write(commandToExec);
                this.outputWriter.get().flush();
            }
            catch (IOException ex) { ex.printStackTrace(); }
        });
    }

    protected void setPreference(String key, String value)
    {
        if (this.webView.getEngine().getLoadWorker().getState() == Worker.State.SUCCEEDED)
            this.webView.getEngine().executeScript(String.format("setPreference(\"%s\", \"%s\")", key, value));
        else
            this.preferences.put(key, value);
    }


    private void initializeProcesses()
    {
        String term_starter = OS.isWindows ? WIN_TERM_STARTER : UNIX_TERM_STARTER;
        Map<String, String> envs = new HashMap<>(System.getenv());
        envs.put("TERM", "xterm");
        String ptyLibPath = PTY_LIB.getPath();
        if (System.getProperty("PTY_LIB_FOLDER") == null || !System.getProperty("PTY_LIB_FOLDER").equals(ptyLibPath))
            System.setProperty("PTY_LIB_FOLDER", ptyLibPath);
        try
        {
            this.process = PtyProcess.exec(term_starter.split("\\s+"), envs);
            this.inputReader.set(new BufferedReader(new InputStreamReader(process.getInputStream())));
            this.errorReader.set(new BufferedReader(new InputStreamReader(process.getErrorStream())));
            this.outputWriter.set(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())));
            updateWindowSize();
            focus();
            timer.countDown();
            process.waitFor();
        }
        catch (Exception e) { e.printStackTrace(); }

    }

    private void updateWindowSize()
    {
        try { this.process.setWinSize(new WinSize(this.columns.get(), this.rows.get())); }
        catch (Exception ex) {}
    }

    protected void focus()
    {
        Platform.runLater(() -> {
            this.webView.requestFocus();
            terminal().call("focus");
        });
    }

    private static void run(Runnable runnable)
    {
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static void runLater(Runnable action)
    {
        if (Platform.isFxApplicationThread())
        {
            action.run();
            return;
        }
        try
        {
            uiSemaphore.acquire();
            Platform.runLater(() -> {
                try
                {
                    action.run();
                    service.submit((Runnable) uiSemaphore::release);
                }
                catch (Exception ex)
                {
                    service.submit((Runnable) uiSemaphore::release);
                    throw new RuntimeException(ex);
                }
            });
        }
        catch (InterruptedException ex)
        {
            service.submit((Runnable) uiSemaphore::release);
            throw new RuntimeException(ex);
        }
    }

    private void print(String text)
    {
        try
        {
            this.timer.await();
            runLater(() -> terminalIO().call("print", text));
        }
        catch (InterruptedException ex) { ex.printStackTrace(); }
    }

    private void printBuffer(Reader buffer)
    {
        try
        {
            int n;
            char[] data = new char[1024];
            while ((n = buffer.read(data, 0, data.length)) != -1)
            {
                StringBuilder builder = new StringBuilder(n);
                builder.append(data, 0, n);
                print(builder.toString());
            }
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }
    private JSObject terminalIO() { return (JSObject) this.webView.getEngine().executeScript("terminal.io"); }
    private JSObject window() { return (JSObject) this.webView.getEngine().executeScript("window"); }
    private JSObject terminal() { return (JSObject) this.webView.getEngine().executeScript("terminal"); }
}
