package cz.melkamar.andruian.ddfparser;

public class URIs {
    public static class Prefix {
        public final static String andr = "http://purl.org/net/andruian/datadef#";
        public final static String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        public final static String ruian = "http://ruian.linked.opendata.cz/ontology/";
        public final static String sp = "http://spinrdf.org/sp#";
        public final static String s = "http://schema.org/";
        public final static String ex = "http://example.org/";
        public final static String BLANK = "http://foo/";
    }

    public static class ANDR {
        public final static String sourceClassDef = Prefix.andr + "sourceClassDef";
        public final static String sparqlEndpoint = Prefix.andr + "sparqlEndpoint";
        public final static String _class = Prefix.andr + "class";
        public final static String pathToLocationClass = Prefix.andr + "pathToLocationClass";
        public final static String selectProperty = Prefix.andr + "selectProperty";
        public final static String SourceClassDef = Prefix.andr + "SourceClassDef";
        public final static String DataDef = Prefix.andr + "DataDef";
        public final static String classToLocPath = Prefix.andr + "classToLocPath";
        public final static String locationClassPathsSource = Prefix.andr + "locationClassPathsSource";
        public final static String lat = Prefix.andr + "lat";
        public final static String _long = Prefix.andr + "long";
        public final static String locationDef = Prefix.andr + "locationDef";
        public final static String includeRdf = Prefix.andr + "includeRdf";
    }

    public static class SP {
        public final static String path = Prefix.sp + "path";
        public final static String path1 = Prefix.sp + "path1";
        public final static String path2 = Prefix.sp + "path2";
        public final static String SeqPath = Prefix.sp + "SeqPath";
    }

    public static class RDF {
        public final static String type = Prefix.rdf + "type";
    }
}
