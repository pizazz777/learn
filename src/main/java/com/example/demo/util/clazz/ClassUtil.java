package com.example.demo.util.clazz;

import com.example.demo.util.container.ContainerUtil;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @date 2020-07-09 15:06
 * @description: 根据报名扫描下面的所有Java类
 */
public class ClassUtil {

    private ClassUtil() {
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param packageName 包名
     * @return class类
     */
    public static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        // class类的集合
        List<Class<?>> classes = Lists.newArrayList();
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合并进行循环来处理这个目录下的
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        // 循环迭代下去
        while (dirs.hasMoreElements()) {
            // 获取下一个元素
            URL url = dirs.nextElement();
            // 得到协议的名称
            String protocol = url.getProtocol();
            // 如果是以文件的形式保存在服务器上
            if ("file".equals(protocol)) {
                // 获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                // 以文件的方式扫描整个包下的文件 并添加到集合中
                findAndAddClassesInPackageByFile(packageName, filePath, classes);
            } else if ("jar".equals(protocol)) {
                // 如果是jar包文件
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                // 从此jar包 得到一个枚举类
                Enumeration<JarEntry> entries = jar.entries();
                // 进行循环迭代
                while (entries.hasMoreElements()) {
                    // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    // 如果是以/开头的
                    if (name.charAt(0) == '/') {
                        //获取后面的字符串
                        name = name.substring(1);
                    }
                    // 如果前半部分和定义的包名相同
                    if (name.startsWith(packageDirName)) {
                        int index = name.lastIndexOf('/');
                        // 如果以"/"结尾 是一个包
                        if (index != -1) {
                            // 获取包名 把"/"替换成".", 递归迭代
                            packageName = name.substring(0, index).replace('/', '.');
                            // 如果是一个.class文件 而且不是目录
                            if (!entry.isDirectory() && name.endsWith(".class")) {
                                // 去掉后面的".class" 获取真正的类名
                                addFileToList(packageName, classes, name, packageName.length() + 1);
                            }
                        }
                    }
                }
            }
        }
        return classes;
    }


    /**
     * 取得某个接口下所有实现这个接口的类
     */
    public static List<Class<?>> getAllClassByInterface(Class<?> clz) throws IOException, ClassNotFoundException {
        List<Class<?>> returnClassList = Lists.newArrayList();
        if (clz.isInterface()) {
            // 获取当前的包名
            String packageName = clz.getPackage().getName();
            // 获取当前包下以及子包下所以的类
            List<Class<?>> allClass = getClasses(packageName);
            if (ContainerUtil.isNotEmpty(allClass)) {
                for (Class classes : allClass) {
                    // 判断是否是同一个接口
                    if (clz.isAssignableFrom(classes)) {
                        // 本身不加入进去
                        if (!clz.equals(classes)) {
                            returnClassList.add(classes);
                        }
                    }
                }
            }
        }
        return returnClassList;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName 包名
     * @param packagePath 包路径
     * @param classList   class类集合
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, List<Class<?>> classList) throws ClassNotFoundException {
        // 获取此包的目录 建立一个File
        File file = new File(packagePath);
        // 不存在直接返回
        if (!file.exists()) {
            return;
        }
        // 是文件
        if (checkFileIsClass(file)) {
            addFileToList(packageName, classList, file.getName(), 0);
        } else {
            // 是目录并且不是空的
            File[] files = file.listFiles();
            if (ContainerUtil.isNotEmpty(files)) {
                // 筛选出class文件和目录
                List<File> childFileList = Arrays.stream(files)
                        .filter(childFile -> (childFile.isDirectory() || checkFileIsClass(childFile)))
                        .collect(Collectors.toList());
                //循环文件
                for (File childFile : childFileList) {
                    //如果是目录 则继续扫描
                    if (childFile.isDirectory()) {
                        findAndAddClassesInPackageByFile(packageName + "." + childFile.getName(), childFile.getAbsolutePath(), classList);
                    } else {
                        //如果是java类文件 去掉后面的.class 只留下类名
                        addFileToList(packageName, classList, childFile.getName(), 0);
                    }
                }
            }
        }
    }

    private static Boolean checkFileIsClass(File file) {
        return file.exists() && file.isFile() && file.getName().endsWith(".class");
    }

    private static void addFileToList(String packageName, List<Class<?>> classList, String name, int i) throws ClassNotFoundException {
        String classFileName = name.substring(i, name.length() - 6);
        //添加到集合中去
        classList.add(Class.forName(packageName + '.' + classFileName));
    }

}
