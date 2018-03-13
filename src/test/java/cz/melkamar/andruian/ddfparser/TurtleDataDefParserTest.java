package cz.melkamar.andruian.ddfparser;

import cz.melkamar.andruian.ddfparser.exception.DataDefFormatException;
import cz.melkamar.andruian.ddfparser.model.ClassToLocPath;
import cz.melkamar.andruian.ddfparser.model.PropertyPath;
import cz.melkamar.andruian.ddfparser.model.SelectProperty;
import cz.melkamar.andruian.ddfparser.model.SourceClassDef;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;


public class TurtleDataDefParserTest {
    SimpleValueFactory vf = SimpleValueFactory.getInstance();

    /**
     * Test {@link TurtleDataDefParser#parseSelectProperty(Resource, Model)}. Test that both blank node and a link
     * to a SelectPropertyDef resource is parsed correctly.
     */
    @Test
    public void parseSelectPropertyDef() throws IOException, DataDefFormatException {
        TurtleDataDefParser dataDefParser = new TurtleDataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/selectprop/test-parse-selectprop.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is);
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
     * Test {@link TurtleDataDefParser#parseSourceClassDef(Resource, Model)}.
     */
    @Test
    public void parseSourceClassDef() throws IOException, DataDefFormatException {
        TurtleDataDefParser dataDefParser = new TurtleDataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/sourceclassdef/test-parse-sourceclassdef.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is);

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
     * Test {@link TurtleDataDefParser#parseClassToLocPath(Resource, Model)} .
     */
    @Test
    public void parseClassToLocPath() throws IOException, DataDefFormatException {
        TurtleDataDefParser dataDefParser = new TurtleDataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/classtolocpath/test-parse-class-to-loc-path.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is);
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

//    @Test
//    public void parseDoesNotThrowException() throws IOException, DataDefFormatException, RdfFormatException {
//        DataDefParser dataDefParser = DataDefParserFactory.getTurtleParser();
//        String text = Util.readStringFromResource("rdf/test-parse-selectprop.ttl", this.getClass());
//        dataDefParser.parse(text);
//    }
//
//    @Test
//    public void parseDataDefUri() throws DataDefFormatException, IOException, RdfFormatException {
//        DataDefParser dataDefParser = DataDefParserFactory.getTurtleParser();
//        String text = Util.readStringFromResource("rdf/test-parse-selectprop.ttl", this.getClass());
//        DataDef dataDef = dataDefParser.parse(text);
//
//        assertEquals("http://foo/dataDef", dataDef.getUri());
//    }
//
////    @Test
////    public void parsePathToLocationClass() {
////        DataDefParser dataDefParser = parserFactory.createParser(datadefModel);
////        Resource sourceClassDefResource = datadefModel.getResource(URIs.Prefix.BLANK + "sourceClassDef");
////        SourceClassDef sourceClassDef = dataDefParser.parseSourceClassDef(sourceClassDefResource);
////
////        assertEquals("<http://example.org/linksTo>/<http://example.org/linksTo2>/<http://example.org/linksTo3>",
////                     sourceClassDef.getPathToLocationClass().toString());
////    }
////
////    @Test
////    public void parseSelectProperties() {
////        DataDefParser dataDefParser = parserFactory.createParser(datadefModel);
////        Resource sourceClassDefResource = datadefModel.getResource(URIs.Prefix.BLANK + "sourceClassDef");
////        SourceClassDef sourceClassDef = dataDefParser.parseSourceClassDef(sourceClassDefResource);
////
////        SelectProperty[] selectProperties = sourceClassDef.getSelectProperties();
////        assertEquals(2, selectProperties.length);
////
////        for (SelectProperty selectProperty : selectProperties) {
////            assertTrue(selectProperty.getName().equals("foobar") || selectProperty.getName().equals("another"));
////            if (selectProperty.getName().equals("foobar")) {
////                assertEquals(1, selectProperty.getPath().getPathElements().length);
////                assertEquals(URIs.Prefix.ex + "id", selectProperty.getPath().getPathElements()[0]);
////            } else {
////                assertEquals(2, selectProperty.getPath().getPathElements().length);
////                assertEquals(URIs.Prefix.ex + "abc", selectProperty.getPath().getPathElements()[0]);
////                assertEquals(URIs.Prefix.ex + "def", selectProperty.getPath().getPathElements()[1]);
////            }
////        }
////    }
//
//    @Test
//    public void parseLocationDef() throws DataDefFormatException, RdfFormatException, IOException {
//        DataDefParser dataDefParser = DataDefParserFactory.getTurtleParser();
//        String text = Util.readStringFromResource("rdf/test-parse-selectprop.ttl", this.getClass());
//        DataDef dataDef = dataDefParser.parse(text);
//
//        assertEquals("http://ruian.linked.opendata.cz/sparql", dataDef.getLocationClassDef().getSparqlEndpoint());
//        assertEquals(URIs.Prefix.ruian + "AdresniMisto", dataDef.getLocationClassDef().getClassUri());
//        assertEquals(1, dataDef.getLocationClassDef().getPathsToGps().size());
//    }
//
//    /**
//     * Parse path from a Location Class to its GPS.
//     */
//    @Test
//    public void parsePropertyPathLocClass() throws DataDefFormatException, RdfFormatException, IOException {
//        DataDefParser dataDefParser = DataDefParserFactory.getTurtleParser();
//        String text = Util.readStringFromResource("rdf/test-parse-selectprop.ttl", this.getClass());
//        DataDef dataDef = dataDefParser.parse(text);
//
//        ClassToLocPath paths = dataDef.getLocationClassDef().getPathToGps(URIs.Prefix.ruian + "AdresniMisto");
//        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLatCoord().getPathElements()[0]);
//        assertEquals(URIs.Prefix.s + "geo", paths.getLatCoord().getPathElements()[1]);
//        assertEquals(URIs.Prefix.s + "latitude", paths.getLatCoord().getPathElements()[2]);
//
//        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLongCoord().getPathElements()[0]);
//        assertEquals(URIs.Prefix.s + "geo", paths.getLongCoord().getPathElements()[1]);
//        assertEquals(URIs.Prefix.s + "longitude", paths.getLongCoord().getPathElements()[2]);
//    }
//
//    /**
//     * Parse a DataDef that has a locationClassPathsSource containing ClassToLocPath.
//     */
//    @Test
//    public void parseLocationClassPathsSource() throws DataDefFormatException, IOException,
//            RdfFormatException {
//        DataDefParser dataDefParser = DataDefParserFactory.getTurtleParser();
//        String text = Util.readStringFromResource("rdf/test-parse-datadef-locationClassPathsSource.ttl", this.getClass());
//        DataDef dataDef = dataDefParser.parse(text);
//
//        ClassToLocPath paths = dataDef.getLocationClassDef().getPathToGps(URIs.Prefix.ruian + "AdresniMisto");
//        assertNotNull(paths);
//
//        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLatCoord().getPathElements()[0]);
//        assertEquals(URIs.Prefix.s + "geo", paths.getLatCoord().getPathElements()[1]);
//        assertEquals(URIs.Prefix.s + "latitude", paths.getLatCoord().getPathElements()[2]);
//
//        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLongCoord().getPathElements()[0]);
//        assertEquals(URIs.Prefix.s + "geo", paths.getLongCoord().getPathElements()[1]);
//        assertEquals(URIs.Prefix.s + "longitude", paths.getLongCoord().getPathElements()[2]);
//    }
//
//    /**
//     * Parse a DataDef that has an external locationClassPathsSource containing ClassToLocPath.
//     * Test that an external RDF linked via andr:includeRdf is included properly
//     */
//    @Test
//    public void parseExternalLocationClassPathsSource() throws DataDefFormatException, IOException {
////        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
////        String includeUri = "http://example.org/some-rdf.ttl";
////        String payload = Util.readStringFromResource("rdf/test-external-locationclasspathssource.ttl", this.getClass());
////        mockServer
////                .expect(MockRestRequestMatchers.requestTo(includeUri))
////                .andRespond(MockRestResponseCreators.withSuccess(payload, MediaType.valueOf("text/plain")));
////
////
////        Model datadefModel = Util.readModelFromResource("rdf/test-parse-datadef-links-to-extern-rdf.ttl",
////                                                        this.getClass());
////        DataDefParser dataDefParser = parserFactory.createParser(datadefModel);
////        DataDef dataDef = dataDefParser.parse();
////
////        ClassToLocPath paths = dataDef.getLocationClassDef().getPathToGps(URIs.Prefix.ruian + "AdresniMisto");
////        assertNotNull(paths);
////
////        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLatCoord().getPathElements()[0]);
////        assertEquals(URIs.Prefix.s + "geo", paths.getLatCoord().getPathElements()[1]);
////        assertEquals(URIs.Prefix.s + "latitude", paths.getLatCoord().getPathElements()[2]);
////
////        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLongCoord().getPathElements()[0]);
////        assertEquals(URIs.Prefix.s + "geo", paths.getLongCoord().getPathElements()[1]);
////        assertEquals(URIs.Prefix.s + "longitude", paths.getLongCoord().getPathElements()[2]);
//        assertTrue(false); // TODO need to include a HTTP client library and be able to mock it here
//    }
    }