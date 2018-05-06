package io.orbit.plugin;

import io.orbit.api.PluginController;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Tyler Swann on Sunday March 11, 2018 at 15:38
 */
public class PluginLoader
{
    private PluginLoader() {  }

    public static List<PluginController> load(URL jarFile)
    {
        return loadInstancesOf(jarFile, PluginController.class);
    }

    public static <T> List<T> loadInstancesOf(URL jarFile, Class<T> type)
    {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setScanners(new SubTypesScanner(false));
        ClassLoader loader = new URLClassLoader(new URL[]{ jarFile });
        config.setUrls(ClasspathHelper.forClassLoader(loader)).addClassLoader(loader);
        Reflections reflections = new Reflections(config);
        Set<Class<? extends T>> typeClasses = reflections.getSubTypesOf(type);
        ArrayList<T> instances = new ArrayList<>();
        for (Class<? extends T> typeClass : typeClasses)
        {
            try
            {
                T instance = typeClass.newInstance();
                instances.add(instance);
            }
            catch (Exception ex)
            {
                System.err.println(String.format("Couldn't load instance of %s from %s", type.getName(), jarFile.toString()));
                ex.printStackTrace();
            }
        }
        return instances;
    }
}
