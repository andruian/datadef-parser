/*
 * MIT License
 *
 * Copyright (c) 2018 Martin Melka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        public final static String SKOS = "http://www.w3.org/2004/02/skos/core#";
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
        public final static IRI propertyPath = SimpleValueFactory.getInstance()
                .createIRI(Prefix.andr + "propertyPath");
        public final static IRI lat = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "lat");
        public final static IRI _long = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "long");
        public final static IRI locationClassDef = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "locationClassDef");
        public final static IRI includeRdf = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "includeRdf");
        public final static IRI uri = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "uri");
        public final static IRI version = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "version");
        public final static IRI indexServer = SimpleValueFactory.getInstance().createIRI(Prefix.andr + "indexServer");
    }

    public static class SP {
        public final static IRI path = SimpleValueFactory.getInstance().createIRI(Prefix.sp + "path");
        public final static IRI path1 = SimpleValueFactory.getInstance().createIRI(Prefix.sp + "path1");
        public final static IRI path2 = SimpleValueFactory.getInstance().createIRI(Prefix.sp + "path2");
        public final static IRI SeqPath = SimpleValueFactory.getInstance().createIRI(Prefix.sp + "SeqPath");
    }

    public static class SCHEMA {
        public final static IRI name = SimpleValueFactory.getInstance().createIRI(Prefix.s + "name");
    }

    public static class RDF {
        public final static IRI type = SimpleValueFactory.getInstance().createIRI(Prefix.rdf + "type");
        public final static IRI first = SimpleValueFactory.getInstance().createIRI(Prefix.rdf + "first");
    }

    public static class SKOS {
        public final static IRI prefLabel = SimpleValueFactory.getInstance().createIRI(Prefix.SKOS + "prefLabel");
    }
}
