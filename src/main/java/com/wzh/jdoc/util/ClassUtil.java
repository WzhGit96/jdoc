package com.wzh.jdoc.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * @author wzh
 * @since 2021/7/28
 */
public final class ClassUtil {
    private static final String FIL_PREFIX = "Controller";
    private static final String SUFFIX = ".class";

    private ClassUtil() {
        super();
    }

    /**
     * 获取class
     *
     * @param packageName
     * @return
     * @throws IOException
     */
    public static List<Class<?>> getClasses(String packageName) throws IOException {
        List<Class<?>> classes = new ArrayList<>();
        boolean recursive = true;
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
            }
            if ("jar".equals(protocol)) {
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String name = jarEntry.getName();
                    if (name.charAt(0) == '/') {
                        name = name.substring(1);
                    }
                    if (name.startsWith(packageDirName)) {
                        int idx = name.lastIndexOf('/');
                        if (idx != -1) {
                            packageName = name.substring(0, idx).replace('/', '.');
                        }
                        if (idx != -1 || recursive) {
                            if (name.endsWith(SUFFIX) && !jarEntry.isDirectory()) {
                                String className = name.substring(packageName.length() + 1, name.length() - 6);
                                try {
                                    classes.add(Class.forName(packageName + "." + className));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        return classes;
    }

    /**
     * 获取类
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirfFiles = dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(SUFFIX) && file.getName().contains(FIL_PREFIX)));
        if (dirfFiles == null || dirfFiles.length <= 0) {
            return;
        }
        Stream.of(dirfFiles).forEach(file -> {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(packageName + "." + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
