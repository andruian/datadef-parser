package cz.melkamar.andruian.ddfparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Util {
    public static InputStream readInputStreamFromResource(String resourcePath, Class clazz) throws IOException {
//        return new Scanner(clazz.getResourceAsStream(resourcePath), "UTF-8").useDelimiter("\\A").next();
        ClassLoader classLoader = clazz.getClassLoader();
        File file = new File(classLoader.getResource(resourcePath).getFile());
        return new FileInputStream(file);
    }
}
