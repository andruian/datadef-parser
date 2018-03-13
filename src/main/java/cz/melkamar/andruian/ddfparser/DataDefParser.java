package cz.melkamar.andruian.ddfparser;

import cz.melkamar.andruian.ddfparser.exception.DataDefFormatException;
import cz.melkamar.andruian.ddfparser.exception.RdfFormatException;
import cz.melkamar.andruian.ddfparser.model.DataDef;
import org.eclipse.rdf4j.model.Model;

import java.io.IOException;
import java.io.InputStream;

public interface DataDefParser {
    DataDef parse(String text) throws RdfFormatException, DataDefFormatException, IOException;
    DataDef parse(InputStream textStream) throws RdfFormatException, DataDefFormatException, IOException;
    DataDef parse(Model model) throws RdfFormatException, DataDefFormatException, IOException;
}
