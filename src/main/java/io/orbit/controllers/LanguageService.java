package io.orbit.controllers;

import io.orbit.api.LanguageDelegate;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.api.text.CodeEditor;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.EventStream;
import org.reactfx.Subscription;

import javax.swing.*;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tyler Swann on Friday July 06, 2018 at 16:35
 */
public class LanguageService
{
    private static ExecutorService service;
    private static Subscription formattingSubscription;
    private static Subscription highlightingSubscription;
    private static ObservableValue<OCodeEditorController> activeControllerProperty;
    private static LanguageDelegate language;
    private static ObjectProperty<Boolean> isHighlighting = new SimpleObjectProperty<>(true);

    private LanguageService(){}

    public static void open(ObservableValue<OCodeEditorController> activeControllerProperty, int threadCount)
    {
        if (service == null)
        {
            service = Executors.newFixedThreadPool(threadCount);
            LanguageService.activeControllerProperty =  activeControllerProperty;
            build();
        }
        else
            throw new RuntimeException("LanguageService is already open!");
    }

    private static void build()
    {
        if (activeControllerProperty.getValue() != null)
            renew();
        activeControllerProperty.addListener(__ -> renew());
    }

    private static void renew()
    {
        if (formattingSubscription != null)
            formattingSubscription.unsubscribe();
        if (highlightingSubscription != null)
            highlightingSubscription.unsubscribe();
        if (activeControllerProperty.getValue() == null)
            return;

        language = getActiveController().getLanguage();
        if (language == null || language.getSyntaxHighlighter() == null)
            return;
        SyntaxHighlighter highlighter = language.getSyntaxHighlighter();
        EventStream<PlainTextChange> changes = getActiveEditor().plainTextChanges();
//        getActiveEditor().plainTextChanges().addObserver(change -> {
//            StyleSpans<Collection<String>> spans = language.getSyntaxHighlighter().computeHighlighting(getActiveEditor().getText());
//            applyHighlighting(spans);
//        });
        highlightingSubscription = changes
                .successionEnds(highlighter.getHighlightingInterval())
                .conditionOn(isHighlighting)
                .supplyTask(LanguageService::computeHighlighting)
                .awaitLatest(changes)
                .filterMap(attempt -> {
                    if (attempt == null || attempt.isFailure() || attempt.get() == null)
                        return Optional.empty();
                    return Optional.of(attempt.get());
                })
                .subscribe(LanguageService::applyHighlighting);
        highlightForcibly();
    }

    private static Task<StyleSpans<Collection<String>>> computeHighlighting()
    {
        String text = getActiveEditor().getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>()
        {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception
            {
                return language.getSyntaxHighlighter().computeHighlighting(text);
            }
        };
        service.execute(task);
        return task;
    }

    private static void applyHighlighting(StyleSpans<Collection<String>> highlighting)
    {
        if (highlighting != null && highlighting.getSpanCount() > 0 && highlighting.length() < getActiveEditor().getText().length())
            getActiveEditor().setStyleSpans(0, highlighting);
    }

    public static void stopHighlightingForcibly()
    {
        isHighlighting.setValue(false);
    }
    public static void highlightForcibly()
    {
        isHighlighting.setValue(true);
        Task<StyleSpans<Collection<String>>> highlight = computeHighlighting();
        highlight.setOnSucceeded(event -> applyHighlighting(highlight.getValue()));
    }

    private static OCodeEditorController getActiveController() { return activeControllerProperty.getValue(); }
    private static CodeEditor getActiveEditor() { return getActiveController().getEditor(); }
}
