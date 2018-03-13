package cz.melkamar.andruian.ddfparser;

import cz.melkamar.andruian.ddfparser.exception.DataDefFormatException;
import cz.melkamar.andruian.ddfparser.exception.RdfFormatException;
import cz.melkamar.andruian.ddfparser.model.ClassToLocPath;
import cz.melkamar.andruian.ddfparser.model.DataDef;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class TurtleDataDefParserTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void parse() throws Exception {
    }

    @Test
    public void parseDoesNotThrowException() throws IOException, DataDefFormatException, RdfFormatException {
        DataDefParser dataDefParser = DataDefParserFactory.getTurtleParser();
        String text = Util.readStringFromResource("rdf/test-parse-datadef.ttl", this.getClass());
        dataDefParser.parse(text);
    }

    @Test
    public void parseDataDefUri() throws DataDefFormatException, IOException, RdfFormatException {
        DataDefParser dataDefParser = DataDefParserFactory.getTurtleParser();
        String text = Util.readStringFromResource("rdf/test-parse-datadef.ttl", this.getClass());
        DataDef dataDef = dataDefParser.parse(text);

        assertEquals("http://foo/dataDef", dataDef.getUri());
    }

//    @Test
//    public void parsePathToLocationClass() {
//        DataDefParser dataDefParser = parserFactory.createParser(datadefModel);
//        Resource sourceClassDefResource = datadefModel.getResource(URIs.Prefix.BLANK + "sourceClassDef");
//        SourceClassDef sourceClassDef = dataDefParser.parseSourceClassDef(sourceClassDefResource);
//
//        assertEquals("<http://example.org/linksTo>/<http://example.org/linksTo2>/<http://example.org/linksTo3>",
//                     sourceClassDef.getPathToLocationClass().toString());
//    }
//
//    @Test
//    public void parseSelectProperties() {
//        DataDefParser dataDefParser = parserFactory.createParser(datadefModel);
//        Resource sourceClassDefResource = datadefModel.getResource(URIs.Prefix.BLANK + "sourceClassDef");
//        SourceClassDef sourceClassDef = dataDefParser.parseSourceClassDef(sourceClassDefResource);
//
//        SelectProperty[] selectProperties = sourceClassDef.getSelectProperties();
//        assertEquals(2, selectProperties.length);
//
//        for (SelectProperty selectProperty : selectProperties) {
//            assertTrue(selectProperty.getName().equals("foobar") || selectProperty.getName().equals("another"));
//            if (selectProperty.getName().equals("foobar")) {
//                assertEquals(1, selectProperty.getPath().getPathElements().length);
//                assertEquals(URIs.Prefix.ex + "id", selectProperty.getPath().getPathElements()[0]);
//            } else {
//                assertEquals(2, selectProperty.getPath().getPathElements().length);
//                assertEquals(URIs.Prefix.ex + "abc", selectProperty.getPath().getPathElements()[0]);
//                assertEquals(URIs.Prefix.ex + "def", selectProperty.getPath().getPathElements()[1]);
//            }
//        }
//    }

    @Test
    public void parseLocationDef() throws DataDefFormatException, RdfFormatException, IOException {
        DataDefParser dataDefParser = DataDefParserFactory.getTurtleParser();
        String text = Util.readStringFromResource("rdf/test-parse-datadef.ttl", this.getClass());
        DataDef dataDef = dataDefParser.parse(text);

        assertEquals("http://ruian.linked.opendata.cz/sparql", dataDef.getLocationDef().getSparqlEndpoint());
        assertEquals(URIs.Prefix.ruian + "AdresniMisto", dataDef.getLocationDef().getClassUri());
        assertEquals(1, dataDef.getLocationDef().getPathsToGps().size());
    }

    /**
     * Parse path from a Location Class to its GPS.
     */
    @Test
    public void parsePropertyPathLocClass() throws DataDefFormatException, RdfFormatException, IOException {
        DataDefParser dataDefParser = DataDefParserFactory.getTurtleParser();
        String text = Util.readStringFromResource("rdf/test-parse-datadef.ttl", this.getClass());
        DataDef dataDef = dataDefParser.parse(text);

        ClassToLocPath paths = dataDef.getLocationDef().getPathToGps(URIs.Prefix.ruian + "AdresniMisto");
        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLatCoord().getPathElements()[0]);
        assertEquals(URIs.Prefix.s + "geo", paths.getLatCoord().getPathElements()[1]);
        assertEquals(URIs.Prefix.s + "latitude", paths.getLatCoord().getPathElements()[2]);

        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLongCoord().getPathElements()[0]);
        assertEquals(URIs.Prefix.s + "geo", paths.getLongCoord().getPathElements()[1]);
        assertEquals(URIs.Prefix.s + "longitude", paths.getLongCoord().getPathElements()[2]);
    }

    /**
     * Parse a DataDef that has a locationClassPathsSource containing ClassToLocPath.
     */
    @Test
    public void parseLocationClassPathsSource() throws DataDefFormatException, IOException,
            RdfFormatException {
        DataDefParser dataDefParser = DataDefParserFactory.getTurtleParser();
        String text = Util.readStringFromResource("rdf/test-parse-datadef-locationClassPathsSource.ttl", this.getClass());
        DataDef dataDef = dataDefParser.parse(text);

        ClassToLocPath paths = dataDef.getLocationDef().getPathToGps(URIs.Prefix.ruian + "AdresniMisto");
        assertNotNull(paths);

        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLatCoord().getPathElements()[0]);
        assertEquals(URIs.Prefix.s + "geo", paths.getLatCoord().getPathElements()[1]);
        assertEquals(URIs.Prefix.s + "latitude", paths.getLatCoord().getPathElements()[2]);

        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLongCoord().getPathElements()[0]);
        assertEquals(URIs.Prefix.s + "geo", paths.getLongCoord().getPathElements()[1]);
        assertEquals(URIs.Prefix.s + "longitude", paths.getLongCoord().getPathElements()[2]);
    }

    /**
     * Parse a DataDef that has an external locationClassPathsSource containing ClassToLocPath.
     * Test that an external RDF linked via andr:includeRdf is included properly
     */
    @Test
    public void parseExternalLocationClassPathsSource() throws DataDefFormatException, IOException {
//        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
//        String includeUri = "http://example.org/some-rdf.ttl";
//        String payload = Util.readStringFromResource("rdf/test-external-locationclasspathssource.ttl", this.getClass());
//        mockServer
//                .expect(MockRestRequestMatchers.requestTo(includeUri))
//                .andRespond(MockRestResponseCreators.withSuccess(payload, MediaType.valueOf("text/plain")));
//
//
//        Model datadefModel = Util.readModelFromResource("rdf/test-parse-datadef-links-to-extern-rdf.ttl",
//                                                        this.getClass());
//        DataDefParser dataDefParser = parserFactory.createParser(datadefModel);
//        DataDef dataDef = dataDefParser.parse();
//
//        ClassToLocPath paths = dataDef.getLocationDef().getPathToGps(URIs.Prefix.ruian + "AdresniMisto");
//        assertNotNull(paths);
//
//        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLatCoord().getPathElements()[0]);
//        assertEquals(URIs.Prefix.s + "geo", paths.getLatCoord().getPathElements()[1]);
//        assertEquals(URIs.Prefix.s + "latitude", paths.getLatCoord().getPathElements()[2]);
//
//        assertEquals(URIs.Prefix.ruian + "adresniBod", paths.getLongCoord().getPathElements()[0]);
//        assertEquals(URIs.Prefix.s + "geo", paths.getLongCoord().getPathElements()[1]);
//        assertEquals(URIs.Prefix.s + "longitude", paths.getLongCoord().getPathElements()[2]);
        assertTrue(false); // TODO need to include a HTTP client library and be able to mock it here
    }
}