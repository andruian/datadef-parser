package cz.melkamar.andruian.ddfparser;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

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
        public final static IRI sourceClassDef = SimpleValueFactory.getInstance()
                .createIRI(Prefix.andr + "sourceClassDef");
        public final static IRI sparqlEndpoint = SimpleValueFactory.getInstance()
                .createIRI(Prefix.andr + "sparqlEndpoint");
        public final static IRI _class = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "class");
        public final static IRI pathToLocationClass = SimpleValueFactory.getInstance()
                .createIRI(Prefix.andr + "pathToLocationClass");
        public final static IRI selectProperty = SimpleValueFactory.getInstance()
                .createIRI(Prefix.andr + "selectProperty");
        public final static IRI SourceClassDef = SimpleValueFactory.getInstance()
                .createIRI(Prefix.andr + "SourceClassDef");
        public final static IRI DataDef = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "DataDef");
        public final static IRI classToLocPath = SimpleValueFactory.getInstance()
                .createIRI(Prefix.andr + "classToLocPath");
        public final static IRI locationClassPathsSource = SimpleValueFactory.getInstance()
                .createIRI(Prefix.andr + "locationClassPathsSource");
        public final static IRI lat = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "lat");
        public final static IRI _long = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "long");
        public final static IRI locationDef = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "locationDef");
        public final static IRI includeRdf = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "includeRdf");
    }

    public static class SP {
        public final static IRI path = SimpleValueFactory.getInstance().createIRI(Prefix.sp + "path");
        public final static IRI path1 = SimpleValueFactory.getInstance().createIRI(Prefix.sp + "path1");
        public final static IRI path2 = SimpleValueFactory.getInstance().createIRI(Prefix.sp + "path2");
        public final static IRI SeqPath = SimpleValueFactory.getInstance().createIRI(Prefix.sp + "SeqPath");
    }

    public static class RDF {
        public final static IRI type = SimpleValueFactory.getInstance().createIRI(Prefix.rdf + "type");
    }
}
