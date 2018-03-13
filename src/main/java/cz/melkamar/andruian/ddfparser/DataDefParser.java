package cz.melkamar.andruian.ddfparser;

import cz.melkamar.andruian.ddfparser.exception.DataDefFormatException;
import cz.melkamar.andruian.ddfparser.exception.RdfFormatException;
import cz.melkamar.andruian.ddfparser.model.DataDef;

import java.io.IOException;

public interface DataDefParser {
    DataDef parse(String text) throws RdfFormatException, DataDefFormatException, IOException;
}
