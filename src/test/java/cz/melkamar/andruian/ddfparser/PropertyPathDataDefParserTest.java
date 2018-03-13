package cz.melkamar.andruian.ddfparser;

import cz.melkamar.andruian.ddfparser.exception.DataDefFormatException;
import cz.melkamar.andruian.ddfparser.model.PropertyPath;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;


public class PropertyPathDataDefParserTest {
    SimpleValueFactory vf = SimpleValueFactory.getInstance();

    @Test
    public void parsePropertyPathSingle() throws IOException, DataDefFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/proppath/test-property-path-single.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);
        Model props = model.filter(vf.createIRI("http://anObject"), URIs.ANDR.propertyPath, null);

        assertEquals(1, props.size());
        Resource propertyPathResource = (Resource) props.iterator().next().getObject();

        PropertyPath propertyPath = dataDefParser.parsePropertyPath(propertyPathResource, model);

        assertEquals(1, propertyPath.getPathElements().length);
        assertEquals("http://aProperty", propertyPath.getPathElements()[0]);
    }

    @Test
    public void parsePropertyPathTwoProps() throws IOException, DataDefFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/proppath/test-property-path-two-props.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);
        Model props = model.filter(vf.createIRI("http://anObject"), URIs.ANDR.propertyPath, null);

        assertEquals(1, props.size());
        Resource propertyPathResource = (Resource) props.iterator().next().getObject();

        PropertyPath propertyPath = dataDefParser.parsePropertyPath(propertyPathResource, model);

        assertEquals(2, propertyPath.getPathElements().length);
        assertEquals("http://aProperty", propertyPath.getPathElements()[0]);
        assertEquals("http://secondProperty", propertyPath.getPathElements()[1]);
    }

    @Test
    public void parsePropertyPathThreeProps() throws IOException, DataDefFormatException {
        DataDefParser dataDefParser = new DataDefParser();
        InputStream is = Util.readInputStreamFromResource("rdf/proppath/test-property-path-three-props.ttl",
                                                          this.getClass());
        Model model = dataDefParser.modelFromStream(is, RDFFormat.TURTLE);
        Model props = model.filter(vf.createIRI("http://anObject"), URIs.ANDR.propertyPath, null);

        assertEquals(1, props.size());
        Resource propertyPathResource = (Resource) props.iterator().next().getObject();

        PropertyPath propertyPath = dataDefParser.parsePropertyPath(propertyPathResource, model);

        assertEquals(3, propertyPath.getPathElements().length);
        assertEquals("http://aProperty", propertyPath.getPathElements()[0]);
        assertEquals("http://secondProperty", propertyPath.getPathElements()[1]);
        assertEquals("http://thirdProperty", propertyPath.getPathElements()[2]);
    }
}