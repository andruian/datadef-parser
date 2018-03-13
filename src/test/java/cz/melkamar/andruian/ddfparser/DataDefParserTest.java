package cz.melkamar.andruian.ddfparser;

import cz.melkamar.andruian.ddfparser.exception.DataDefFormatException;
import cz.melkamar.andruian.ddfparser.exception.RdfFormatException;
import cz.melkamar.andruian.ddfparser.model.*;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class DataDefParserTest {
    SimpleValueFactory vf = SimpleValueFactory.getInstance();

    /**
     * Test {@link DataDefParser#parse(Model)}
     */
    @Test
    public void parse() throws IOException, DataDefFormatException, RdfFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/test-parse-datadef.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);
        List<DataDef> l = dataDefParser.parse(model);
        assertEquals(1, l.size());
        DataDef dataDef = l.get(0);

        assertEquals("http://ruian.linked.opendata.cz/ontology/AdresniMisto",
                     dataDef.getLocationClassDef().getClassUri());
        assertEquals("http://AClass", dataDef.getSourceClassDef().getClassUri());
        assertFalse(dataDef.getIndexServer().isPresent());
    }

    /**
     * Test {@link DataDefParser#parse(Model)} when there are multiple datadefs.
     */
    @Test
    public void parseMultipleDatadefs() throws IOException, DataDefFormatException, RdfFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/test-parse-multiple-datadef.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);
        List<DataDef> l = dataDefParser.parse(model);
        assertEquals(2, l.size());

        boolean firstChecked = false;
        boolean secondChecked = false;
        for (DataDef dataDef : l) {
            if (dataDef.getUri().equals("http://foo/dataDef")) {
                assertFalse(firstChecked);
                firstChecked = true;
                assertEquals("http://AClass", dataDef.getSourceClassDef().getClassUri());
                assertEquals("http://localhost:3030/test/query", dataDef.getSourceClassDef().getSparqlEndpoint());
            } else if (dataDef.getUri().equals("http://foo/dataDefB")) {
                assertFalse(secondChecked);
                secondChecked = true;
                assertEquals("http://AClassB", dataDef.getSourceClassDef().getClassUri());
                assertEquals("http://localhost:3030/test/queryB", dataDef.getSourceClassDef().getSparqlEndpoint());
            } else {
                assertTrue(false);
            }
        }
        assertTrue(firstChecked);
        assertTrue(secondChecked);
    }

    /**
     * Test {@link DataDefParser#parse(Model)}
     */
    @Test
    public void parseWithIndexServer() throws IOException, DataDefFormatException, RdfFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/test-parse-datadef-with-indexserver.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);
        List<DataDef> l = dataDefParser.parse(model);
        assertEquals(1, l.size());
        DataDef dataDef = l.get(0);

        assertEquals("http://ruian.linked.opendata.cz/ontology/AdresniMisto",
                     dataDef.getLocationClassDef().getClassUri());
        assertEquals("http://AClass", dataDef.getSourceClassDef().getClassUri());
        assertTrue(dataDef.getIndexServer().isPresent());
        assertEquals("http://someUri", dataDef.getIndexServer().get().getUri());
    }

    /**
     * Test {@link DataDefParser#parseIndexServer(Resource, Model)}.
     */
    @Test
    public void parseIndexServer() throws IOException, DataDefFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/test-parse-indexserver.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);

        IndexServer indexServer = dataDefParser.parseIndexServer(vf.createIRI("http://indexserver"), model);
        assertEquals("http://someUri", indexServer.getUri());
        assertEquals(new Integer(1), indexServer.getVersion().get());

        indexServer = dataDefParser.parseIndexServer(vf.createIRI("http://indexserver-noversion"), model);
        assertEquals("http://someUri", indexServer.getUri());
        assertFalse(indexServer.getVersion().isPresent());
    }

    /**
     * Test {@link DataDefParser#parseSelectProperty(Resource, Model)}. Test that both blank node and a link
     * to a SelectPropertyDef resource is parsed correctly.
     */
    @Test
    public void parseSelectPropertyDef() throws IOException, DataDefFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/selectprop/test-parse-selectprop.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);
        Model props = model.filter(vf.createIRI("http://anObject"), URIs.ANDR.selectProperty, null);

        // Keep track of what we checked - there is a blank node and a regular link in the test data
        boolean blankNodeChecked = false;
        boolean linkChecked = false;

        assertEquals(2, props.size());
        for (Statement prop : props) {
            Resource obj = (Resource) prop.getObject();
            SelectProperty selectProperty = dataDefParser.parseSelectProperty(obj, model);

            if (obj instanceof IRI) {
                linkChecked = true;
                assertEquals("foobarlinked", selectProperty.getName());
                assertEquals(2, selectProperty.getPath().getPathElements().length);
                assertEquals("http://firstlinked", selectProperty.getPath().getPathElements()[0]);
                assertEquals("http://secondlinked", selectProperty.getPath().getPathElements()[1]);
            } else if (!(obj instanceof Literal)) {
                blankNodeChecked = true; // A value is a blank node if not IRI nor Literal
                assertEquals("foobarblank", selectProperty.getName());
                assertEquals(2, selectProperty.getPath().getPathElements().length);
                assertEquals("http://firstblank", selectProperty.getPath().getPathElements()[0]);
                assertEquals("http://secondblank", selectProperty.getPath().getPathElements()[1]);
            }
        }

        assertTrue(blankNodeChecked);
        assertTrue(linkChecked);
    }

    /**
     * Test {@link DataDefParser#parseSourceClassDef(Resource, Model)}.
     */
    @Test
    public void parseSourceClassDef() throws IOException, DataDefFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/sourceclassdef/test-parse-sourceclassdef.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);

        SourceClassDef sourceClassDef = dataDefParser.parseSourceClassDef(vf.createIRI("http://sourceClassDef"), model);
        assertEquals("http://localhost:3030/test/query", sourceClassDef.getSparqlEndpoint());
        assertEquals("http://AClass", sourceClassDef.getClassUri());

        assertArrayEquals(new String[]{"http://A", "http://B", "http://C", "http://D"},
                          sourceClassDef.getPathToLocationClass().getPathElements());

        assertEquals(1, sourceClassDef.getSelectProperties().length);
        assertEquals("foobarblank", sourceClassDef.getSelectProperties()[0].getName());
        assertArrayEquals(new String[]{"http://firstblank", "http://secondblank"},
                          sourceClassDef.getSelectProperties()[0].getPath().getPathElements());
    }

    /**
     * Test {@link DataDefParser#parseClassToLocPath(Resource, Model)} .
     */
    @Test
    public void parseClassToLocPath() throws IOException, DataDefFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/classtolocpath/test-parse-class-to-loc-path.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);
        ClassToLocPath classToLocPath = dataDefParser.parseClassToLocPath(vf.createIRI("http://aClassToLocPath"),
                                                                          model);

        assertEquals("http://aClass", classToLocPath.getForClassUri());
        assertArrayEquals(new String[]{
                                  "http://ruian.linked.opendata.cz/ontology/adresniBod",
                                  "http://schema.org/geo",
                                  "http://schema.org/latitude"},
                          classToLocPath.getLatCoord().getPathElements());
        assertArrayEquals(new String[]{
                                  "http://ruian.linked.opendata.cz/ontology/adresniBod",
                                  "http://schema.org/geo",
                                  "http://schema.org/longitude"},
                          classToLocPath.getLongCoord().getPathElements());
    }

    /**
     * Test {@link DataDefParser#parseLocationClassDef(Resource, Model)}  .
     */
    @Test
    public void parseLocationClassDef() throws IOException, DataDefFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/locationclassdef/test-parse-locationclassdef.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);
        LocationClassDef locationClassDef = dataDefParser.parseLocationClassDef(vf.createIRI("http://locationClassDef"),
                                                                                model);

        assertEquals("http://ruian.linked.opendata.cz/sparql", locationClassDef.getSparqlEndpoint());
        assertEquals(URIs.Prefix.ruian + "AdresniMisto", locationClassDef.getClassUri());

        Map<String, ClassToLocPath> paths = locationClassDef.getPathsToGps();
        assertEquals(2, paths.size());

        assertEquals("http://classA", paths.get("http://classA").getForClassUri());
        assertArrayEquals(new String[]{"http://A"}, paths.get("http://classA").getLatCoord().getPathElements());
        assertArrayEquals(new String[]{"http://X", "http://Y", "http://Z"},
                          paths.get("http://classA").getLongCoord().getPathElements());

        assertEquals("http://classB", paths.get("http://classB").getForClassUri());
        assertArrayEquals(new String[]{"http://A", "http://B"},
                          paths.get("http://classB").getLatCoord().getPathElements());
        assertArrayEquals(new String[]{"http://X", "http://Y"},
                          paths.get("http://classB").getLongCoord().getPathElements());
    }

    /**
     * Test {@link DataDefParser#parseLocationClassDef(Resource, Model)}. Include a part of the model in an external
     * RDF file and check if including it works.
     */
    @Test
    public void parseLocationClassDefExternal() throws IOException, DataDefFormatException {
        InputStream externalRdfIs = Util.readInputStreamFromResource(
                "rdf/locationclassdef/test-parse-locationclassdef-external-child.ttl",
                this.getClass());
        Util.mockHttpClientResponse("http://example.org/some-rdf.ttl", Util.convertStreamToString(externalRdfIs));

        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource(
                "rdf/locationclassdef/test-parse-locationclassdef-external-parent.ttl",
                this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);
        LocationClassDef locationClassDef = dataDefParser.parseLocationClassDef(vf.createIRI("http://locationClassDef"),
                                                                                model);

        assertEquals("http://ruian.linked.opendata.cz/sparql", locationClassDef.getSparqlEndpoint());
        assertEquals(URIs.Prefix.ruian + "AdresniMisto", locationClassDef.getClassUri());

        Map<String, ClassToLocPath> paths = locationClassDef.getPathsToGps();
        assertEquals(3, paths.size());

        assertEquals("http://classA", paths.get("http://classA").getForClassUri());
        assertArrayEquals(new String[]{"http://A"}, paths.get("http://classA").getLatCoord().getPathElements());
        assertArrayEquals(new String[]{"http://X", "http://Y", "http://Z"},
                          paths.get("http://classA").getLongCoord().getPathElements());

        assertEquals("http://classB", paths.get("http://classB").getForClassUri());
        assertArrayEquals(new String[]{"http://A", "http://B"},
                          paths.get("http://classB").getLatCoord().getPathElements());
        assertArrayEquals(new String[]{"http://X", "http://Y"},
                          paths.get("http://classB").getLongCoord().getPathElements());

        assertEquals("http://classC", paths.get("http://classC").getForClassUri());
        assertArrayEquals(new String[]{"http://A"},
                          paths.get("http://classC").getLatCoord().getPathElements());
        assertArrayEquals(new String[]{"http://X"},
                          paths.get("http://classC").getLongCoord().getPathElements());
    }
}