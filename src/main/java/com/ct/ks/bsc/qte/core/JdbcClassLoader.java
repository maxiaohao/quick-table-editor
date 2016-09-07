package com.ct.ks.bsc.qte.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

public class JdbcClassLoader extends URLClassLoader {

    static private final JdbcClassLoader inst = new JdbcClassLoader();
    static private final Set<String> loadedJarFileNames = new HashSet<String>();


    public static JdbcClassLoader getInstance() {
        return inst;
    }


    private JdbcClassLoader() {
        super(new URL[] {});
    }


    protected boolean addJar(File jar) throws MalformedURLException {
        if (jar.isFile()) {
            if (!loadedJarFileNames.contains(jar.getName())) {
                loadedJarFileNames.add(jar.getName());
                this.addURL(jar.toURI().toURL());
            }
            return true;
        } else {
            return false;
        }
    }
}
