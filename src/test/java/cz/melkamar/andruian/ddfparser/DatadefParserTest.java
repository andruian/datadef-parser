package cz.melkamar.andruian.ddfparser;

import org.junit.Test;

import static org.junit.Assert.*;

public class DatadefParserTest {
    @Test
    public void parse() throws Exception {
        DatadefParser parser = new DatadefParser();

        assertEquals("hello there", parser.parse());
    }
}