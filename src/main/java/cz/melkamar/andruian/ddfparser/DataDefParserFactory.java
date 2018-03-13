package cz.melkamar.andruian.ddfparser;

import cz.melkamar.andruian.ddfparser.exception.UnsupportedRdfFormatException;

public class DataDefParserFactory {
    public static DataDefParser getParser(RdfFormat rdfFormat) {
        switch (rdfFormat) {
            case TURTLE:
                return new TurtleDataDefParser();
            default:
                throw new UnsupportedRdfFormatException("This format is not supported");
        }
    }

    public static DataDefParser getTurtleParser(){
        return new TurtleDataDefParser();
    }
}
