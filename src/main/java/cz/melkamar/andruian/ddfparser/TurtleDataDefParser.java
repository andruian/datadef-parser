package cz.melkamar.andruian.ddfparser;

import cz.melkamar.andruian.ddfparser.exception.DataDefFormatException;
import cz.melkamar.andruian.ddfparser.exception.RdfFormatException;
import cz.melkamar.andruian.ddfparser.model.DataDef;
import cz.melkamar.andruian.ddfparser.model.LocationClassDef;
import cz.melkamar.andruian.ddfparser.model.SourceClassDef;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TurtleDataDefParser implements DataDefParser {
    private static final Logger L = LoggerFactory.getLogger(TurtleDataDefParser.class);

    public static void main(String[] args) throws IOException, DataDefFormatException, RdfFormatException {
        TurtleDataDefParser parser = new TurtleDataDefParser();
//        String text = Util.readStringFromResource("rdf/test-parse-datadef.ttl", TurtleDataDefParser.class);
        byte[] encoded = Files.readAllBytes(Paths.get(
                "d:\\cvut-checkouted\\_andruian\\ddfparser\\src\\test\\resources\\rdf\\test-parse-datadef.ttl"));
        String text = new String(encoded, "utf-8");
        parser.parse(text);
    }

    @Override
    public DataDef parse(String text) throws RdfFormatException, DataDefFormatException, IOException {
        L.debug("Parsing a string");
        L.trace(text);
        InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        Model model = Rio.parse(stream, "", RDFFormat.TURTLE);


        ValueFactory vf = SimpleValueFactory.getInstance();
        IRI DATADEF = URIs.ANDR.DataDef;
        IRI type = URIs.RDF.type;

        // For each datadef, get associated elements
        Model dataDefs = model.filter(null, type, DATADEF);
        L.debug("Found {} DataDef objects in given text", dataDefs.size());
        for (Statement st : dataDefs) {
            IRI dataDefIri = (IRI) st.getSubject();
            L.debug("Parsing DataDef {}", dataDefIri.toString());

            Model srcClassDefs = model.filter(dataDefIri, URIs.ANDR.sourceClassDef, null);
            if (srcClassDefs.size() != 1)
                throw new DataDefFormatException("A DataDef must have one linked andr:sourceClassDef property.");
            IRI sourceClassDefIri = (IRI) srcClassDefs.objects().iterator().next();
            SourceClassDef sourceClassDef = parseSourceClassDef(sourceClassDefIri, model);

            Model locClassDefs = model.filter(dataDefIri, URIs.ANDR.locationDef, null);
            if (locClassDefs.size() != 1)
                throw new DataDefFormatException("A DataDef must have one linked andr:locationClassDef property.");
            IRI locationClassDefIri = (IRI) locClassDefs.objects().iterator().next();
            LocationClassDef locationClassDef = parseLocationClassDef(locationClassDefIri, model);

            DataDef dataDef = new DataDef(dataDefIri.toString(), locationClassDef, sourceClassDef);
            return dataDef; // TODO allow multiple datadefs
        }


        return null;
    }

    /**
     * Given a {@link Model} and an IRI of an andr:SourceClassDef resource in this model, construct an instance of
     * {@link SourceClassDef} from this resource.
     *
     * @param sourceClassDefIri An IRI of an andr:SourceClassDef resource.
     * @param model             A RDF4J model.
     * @return A {@link SourceClassDef} constructed from the model with the given IRI.
     */
    public SourceClassDef parseSourceClassDef(IRI sourceClassDefIri, Model model) {
        L.debug("Parsing a SourceClassDef {}", sourceClassDefIri.toString());
        throw new NotImplementedException();
    }

    public LocationClassDef parseLocationClassDef(IRI locationClassDefIri, Model model) {
        throw new NotImplementedException();
    }


//    public String parse() {
//        InputStream stream = new ByteArrayInputStream(foostr().getBytes(StandardCharsets.UTF_8));
//        String lines[] = foostr().split("\\r?\\n");
//        String result = "nothing";
//        try {
//            Model model = Rio.parse(stream, "", RDFFormat.TURTLE);
//            IRI datadefClass = SimpleValueFactory.getInstance()
//                    .createIRI("http://purl.org/net/andruian/datadef#DataDef");
//            Model dataDefs = model.filter(null, null, datadefClass);
//            for (Statement st : dataDefs) {
//                // the subject is always `ex:VanGogh`, an IRI, so we can safely cast it
//                IRI subject = (IRI) st.getSubject();
//                // the property predicate is always an IRI
//                IRI predicate = st.getPredicate();
//
//                // the property value could be an IRI, a BNode, or a Literal. In RDF4J,
//                // Value is is the supertype of all possible kinds of RDF values.
//                Value object = st.getObject();
//
//                // let's print out the statement in a nice way. We ignore the namespaces
//                // and only print the local name of each IRI
//                System.out.print(subject.getLocalName() + " " + predicate.getLocalName() + " ");
//                result = subject.getLocalName() + " " + predicate.getLocalName() + " ";
//                if (object instanceof Literal) {
//                    // It's a literal. print it out nicely, in quotes, and without any ugly
//                    // datatype stuff
//                    System.out.println("\"" + ((Literal) object).getLabel() + "\"");
//                } else if (object instanceof IRI) {
//                    // It's an IRI. Print it out, but leave off the namespace part.
//                    System.out.println(((IRI) object).getLocalName());
//                    result += ((IRI) object).getLocalName();
//                } else {
//                    // It's a blank node. Just print out the internal identifier.
//                    System.out.println(object);
//                }
//            }
//            System.out.println("foo");
//        } catch (IOException e) {
//            e.printStackTrace();
//            result = e.toString();
//        }
//
//        return result + "1";
//    }
//
//    public String foostr() {
//        return "@prefix andr: <http://purl.org/net/andruian/datadef#> .\n" +
//                "@prefix ruian: <http://ruian.linked.opendata.cz/ontology/> .\n" +
//                "@prefix sp: <http://spinrdf.org/sp#> .\n" +
//                "@prefix s: <http://schema.org/> .\n" +
//                "@prefix ex: <http://example.org/> .\n" +
//                "@prefix : <http://foo/> .\n" +
//                "\n" +
//                ":dataDef\n" +
//                "    a                 andr:DataDef;\n" +
//                "    andr:locationDef  :locationDef;\n" +
//                "    andr:sourceClassDef :sourceClassDef;\n" +
//                "    andr:indexServer  :indexServer;\n" +
//                "    .\n" +
//                "\n" +
//                "#\n" +
//                "# INDEX SERVER\n" +
//                "#\n" +
//                ":indexServer\n" +
//                "    a        andr:IndexServer;\n" +
//                "    andr:uri <http://localhost:8080>;\n" +
//                "    andr:version 1;\n" +
//                "    .\n" +
//                "\n" +
//                "#\n" +
//                "# DATA DEFINITION\n" +
//                "#\n" +
//                ":sourceClassDef\n" +
//                "    a                        andr:SourceClassDef;\n" +
//                "    andr:sparqlEndpoint      <http://localhost:3030/foo/query>;\n" +
//                "    andr:class               ex:MyObject;\n" +
//                "    andr:pathToLocationClass [ a sp:SeqPath;\n" +
//                "                               sp:path1 ex:someLink;\n" +
//                "                               sp:path2 ex:linksTo;\n" +
//                "                             ];\n" +
//                "    andr:selectProperty [ a andr:SelectProperty;\n" +
//                "                          s:name \"labelForIdProperty\";\n" +
//                "                          sp:path [ a        sp:SeqPath;\n" +
//                "                                    sp:path1 ex:id\n" +
//                "                                  ];\n" +
//                "                        ];\n" +
//                "    .\n" +
//                "\n" +
//                "\n" +
//                "#\n" +
//                "# LOCATION DEFINITON\n" +
//                "#\n" +
//                ":locationDef\n" +
//                "    a                   andr:LocationClassDef;\n" +
//                "    andr:sparqlEndpoint <http://ruian.linked.opendata.cz/sparql>;\n" +
//                "    andr:class          ruian:AdresniMisto;\n" +
//                "    andr:classToLocPath :adresniMistoClassToLocPath;\n" +
//                "    .\n" +
//                "\n" +
//                "# Description of how to get from object ruian:AdresniMisto to its lat/long coordinates\n" +
//                ":adresniMistoClassToLocPath\n" +
//                "    a          andr:ClassToLocPath;\n" +
//                "    andr:class ruian:AdresniMisto;\n" +
//                "    andr:lat   [ a sp:SeqPath;\n" +
//                "                 sp:path1 ruian:adresniBod;\n" +
//                "                 sp:path2 [\n" +
//                "                     sp:path1 s:geo;\n" +
//                "                     sp:path2 s:latitude;\n" +
//                "                 ]\n" +
//                "               ];\n" +
//                "    andr:long  [ a sp:SeqPath;\n" +
//                "                 sp:path1 ruian:adresniBod;\n" +
//                "                 sp:path2 [\n" +
//                "                     sp:path1 s:geo;\n" +
//                "                     sp:path2 s:longitude;\n" +
//                "                 ]\n" +
//                "               ];\n" +
//                "    .";
//    }
}
