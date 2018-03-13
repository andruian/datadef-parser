package cz.melkamar.andruian.ddfparser;

import java.io.IOException;
import java.util.Scanner;

public class Util {
    public static String readStringFromResource(String resourcePath, Class clazz) throws IOException {
        return new Scanner(clazz.getResourceAsStream(resourcePath), "UTF-8").useDelimiter("\\A").next();
    }
}
