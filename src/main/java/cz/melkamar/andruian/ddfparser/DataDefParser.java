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

import cz.melkamar.andruian.ddfparser.exception.DataDefFormatException;
import cz.melkamar.andruian.ddfparser.exception.RdfFormatException;
import cz.melkamar.andruian.ddfparser.model.*;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.util.RDFCollections;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataDefParser {
    private static final Logger L = LoggerFactory.getLogger(DataDefParser.class);

    public List<DataDef> parse(String text, RDFFormat rdfFormat)
            throws RdfFormatException, DataDefFormatException, IOException {
        L.debug("Parsing a string DataDef");
        L.trace(text);

        Model model = modelFromString(text, rdfFormat);
        return parse(model);
    }

    public List<DataDef> parse(InputStream textStream, RDFFormat rdfFormat)
            throws RdfFormatException, DataDefFormatException, IOException {
        L.debug("Parsing a textStream DataDef");
        Model model = modelFromStream(textStream, rdfFormat);
        return parse(model);
    }

    public List<DataDef> parse(Model model) throws RdfFormatException, DataDefFormatException, IOException {
        List<DataDef> result = new ArrayList<>();

        // For each datadef, get associated elements
        Model dataDefs = model.filter(null, URIs.RDF.type, URIs.ANDR.DataDef);
        L.debug("Found {} DataDef objects in given text", dataDefs.size());
        for (Statement st : dataDefs) {
            IRI dataDefIri = (IRI) st.getSubject();
            L.debug("Parsing DataDef {}", dataDefIri.toString());

            Resource sourceClassDefResource = getSingleObjectAsResource(dataDefIri, URIs.ANDR.sourceClassDef, model);
            SourceClassDef sourceClassDef = parseSourceClassDef(sourceClassDefResource, model);

            Resource locationClassDefResource = getSingleObjectAsResource(dataDefIri,
                                                                          URIs.ANDR.locationClassDef,
                                                                          model);
            LocationClassDef locationClassDef = parseLocationClassDef(locationClassDefResource, model);

            Set<Resource> indexServerSet = Models.getPropertyResources(model, dataDefIri, URIs.ANDR.indexServer);
            IndexServer indexServer = null;
            if (indexServerSet.size() == 1) indexServer = parseIndexServer(indexServerSet.iterator().next(), model);
            else if (indexServerSet.size() > 1)
                throw new DataDefFormatException("Expected zero or one properties of type andr:uri. Found " + indexServerSet
                        .size());

            DataDefBuilder builder = new DataDefBuilder(dataDefIri.toString(), locationClassDef, sourceClassDef)
                    .addIndexServer(indexServer);

            // Process labels
            Set<Literal> labels = Models.getPropertyLiterals(model, dataDefIri, URIs.SKOS.prefLabel);
            for (Literal label : labels) {
                String lang = label.getLanguage().orElse(DataDef.NO_LANG);
                builder.addLabel(lang, label.getLabel());
            }

            result.add(builder.createDataDef());
        }

        return result;
    }

    public Model modelFromString(String text, RDFFormat rdfFormat) throws IOException, RdfFormatException {
        InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        try {
            return Rio.parse(stream, "", rdfFormat);
        } catch (RDFParseException e) {
            throw new RdfFormatException(e);
        }
    }

    public Model modelFromStream(InputStream stream, RDFFormat rdfFormat) throws IOException, RdfFormatException {
        try {
            return Rio.parse(stream, "", rdfFormat);
        } catch (RDFParseException e) {
            throw new RdfFormatException(e);
        }
    }

    public IndexServer parseIndexServer(Resource indexServerResource, Model model) throws DataDefFormatException {
        L.debug("Parsing an IndexServer " + indexServerResource.toString());
        Literal indexServerUri = getSingleObjectAsLiteral(indexServerResource, URIs.ANDR.uri, model);
        Set<Literal> versionSet = Models.getPropertyLiterals(model, indexServerResource, URIs.ANDR.version);
        switch (versionSet.size()) {
            case 0:
                return new IndexServer(indexServerUri.getLabel());
            case 1:
                Literal versionLiteral = versionSet.iterator().next();
                try {
                    return new IndexServer(indexServerUri.getLabel(), versionLiteral.intValue());
                } catch (NumberFormatException e) {
                    throw new DataDefFormatException("A version must be an integer, got " + versionLiteral.getLabel());
                }
            default:
                throw new DataDefFormatException("Expected zero or one properties of type andr:uri. Found " + versionSet
                        .size());
        }
    }


    /**
     * Construct an instance of {@link SourceClassDef} from the given {@link Resource} in a {@link Model}.
     *
     * @param sourceClassDefResource An andr:SourceClassDef {@link Resource}.
     * @param model                  A RDF4J model.
     * @return A {@link SourceClassDef} constructed from the model with the given IRI.
     */
    public SourceClassDef parseSourceClassDef(Resource sourceClassDefResource, Model model)
            throws DataDefFormatException {
        L.debug("Parsing a SourceClassDef {}", sourceClassDefResource.toString());

        Literal sparqlEndpoint = getSingleObjectAsLiteral(sourceClassDefResource, URIs.ANDR.sparqlEndpoint, model);
        L.debug("Found sparqlEndpoint " + sparqlEndpoint);

        IRI clazz = getSingleObjectAsIri(sourceClassDefResource, URIs.ANDR._class, model);
        L.debug("Found class " + clazz);

        Resource pathToLocClassResource = getSingleObjectAsResource(sourceClassDefResource,
                                                                    URIs.ANDR.pathToLocationClass,
                                                                    model);
        L.debug("Found pathToLocationClass " + pathToLocClassResource);
        PropertyPath pathToLocationClass = parsePropertyPath(pathToLocClassResource, model);

        // Parse andr:selectProperty linking to andr:SelectPropertyDef
        Set<Resource> selectPropsResources = Models.getPropertyResources(model,
                                                                         sourceClassDefResource,
                                                                         URIs.ANDR.selectProperty);
        L.debug("Found {} selectProperties", selectPropsResources.size());
        SelectProperty[] selectProperties = new SelectProperty[selectPropsResources.size()];
        int i = 0;
        for (Resource selectPropResource : selectPropsResources) {
            selectProperties[i++] = parseSelectProperty(selectPropResource, model);
        }

        return new SourceClassDef(sparqlEndpoint.getLabel(), clazz.toString(), pathToLocationClass, selectProperties);
    }

    /**
     * Construct an instance of {@link SelectProperty} from the given andr:SelectPropertyDef resource.
     *
     * @param selectPropertyId An andr:SelectPropertyDef resource in the given model.
     * @param model            A RDF4J model.
     * @return a {@link SelectProperty} construted from the given resource.
     * @throws DataDefFormatException When the data is malformed, e.g. a mandatory property is not provided.
     */
    public SelectProperty parseSelectProperty(Resource selectPropertyId, Model model) throws DataDefFormatException {
        L.debug("Parsing a selectProperty " + selectPropertyId);
        String name = Models.getPropertyLiteral(model, selectPropertyId, URIs.SCHEMA.name)
                .orElseThrow(() -> new DataDefFormatException("Could not find a literal linked to SelectPropertyDef "
                                                                      + selectPropertyId + " via property " + URIs.SCHEMA.name))
                .getLabel();

        L.trace("Found name " + name);
        Resource propPathId = getSingleObjectAsResource(selectPropertyId, URIs.ANDR.propertyPath, model);
        PropertyPath propertyPath = parsePropertyPath(propPathId, model);
        return new SelectProperty(name, propertyPath);
    }

    /**
     * Parse a <a href="https://www.w3.org/TR/shacl/#property-paths">SHACL property path</a>.
     * The supported property paths are:
     * <ul>
     * <li>Predicate path (a single predicate)</li>
     * <li>Sequence path (a RDF list of predicates)</li>
     * </ul>
     *
     * @param propertyPathId A Resource in the given model that is a SHACL property path (a single property or a list).
     * @param model          A RDF4J model.
     * @return A {@link PropertyPath} constructed from the given SHACL path.
     * @throws DataDefFormatException When the property path cannot be parsed. Possibly malformed or not supported.
     */
    public PropertyPath parsePropertyPath(Resource propertyPathId, Model model) throws DataDefFormatException {
        L.debug("Parsing a propertyPath " + propertyPathId);
        if (model.filter(propertyPathId, URIs.RDF.first, null).isEmpty()) {
            // Does not have property rdf:first -> is not a rdf:List
            L.trace("Is a single property path element");
            if (!(propertyPathId instanceof IRI))
                throw new DataDefFormatException("A property in Property Path cannot be cast to an IRI: " + propertyPathId +
                                                         ". Make sure you use <http://iri> instead of \"http://iri\"");
            return new PropertyPath(propertyPathId.toString());
        } else {
            List<Value> values = RDFCollections.asValues(model, propertyPathId, new ArrayList<>());
            L.trace("Found path elements: " + values);
            String[] pathElements = new String[values.size()];
            int i = 0;
            for (Value value : values) {
                if (!(value instanceof IRI))
                    throw new DataDefFormatException("A property in Property Path cannot be cast to an IRI: " + value +
                                                             ". Make sure you use <http://iri> instead of \"http://iri\"");
                pathElements[i++] = value.toString();
            }
            return new PropertyPath(pathElements);
        }
    }

    /**
     * Construct an instance of {@link LocationClassDef} from a andr:LocationClassDef resource.
     *
     * @param locationClassDefResource A andr:LocationClassDef {@link Resource}.
     * @param model                    A RDF4J model.
     * @return A parsed {@link LocationClassDef} object.
     * @throws DataDefFormatException When the data is malformed, e.g. a mandatory property is not provided.
     */
    public LocationClassDef parseLocationClassDef(Resource locationClassDefResource, Model model)
            throws DataDefFormatException, RdfFormatException {
        L.debug("Parsing a LocationClassDef " + locationClassDefResource);

        // Include data from any RDFs linked
        Set<Literal> includeRdfsSet = Models.getPropertyLiterals(model, locationClassDefResource, URIs.ANDR.includeRdf);
        L.debug("Found " + includeRdfsSet.size() + " rdf files to include.");
        for (Literal rdfUrl : includeRdfsSet) {
            processIncludeRdf(rdfUrl, model);
        }

        // Get ClassDef attributes
        Literal sparqlEndpoint = getSingleObjectAsLiteral(locationClassDefResource, URIs.ANDR.sparqlEndpoint, model);
        L.debug("Found sparqlEndpoint " + sparqlEndpoint);

        IRI clazz = getSingleObjectAsIri(locationClassDefResource, URIs.ANDR._class, model);
        L.debug("Found class " + clazz);

        // All ClassToLocPath objects attached with this LocationClassDef will be stored here
        Map<String, ClassToLocPath> classToLocPaths = new HashMap<>();

        // Process andr:classToLocPath properties
        Set<Resource> classToLocPathResourceSet = Models.getPropertyResources(model,
                                                                              locationClassDefResource,
                                                                              URIs.ANDR.classToLocPath);
        L.debug("Found " + classToLocPathResourceSet.size() + " classToLocPath properties.");
        for (Resource classToLocPathResource : classToLocPathResourceSet) {
            ClassToLocPath ctlp = parseClassToLocPath(classToLocPathResource, model);
            classToLocPaths.put(ctlp.getForClassUri(), ctlp);
        }

        /*
         Process andr:locationClassPathsSource (each may contain several ClassToLocPath entries)
           First get a set of all andr:LocationClassPathsSource. For each of them (first loop) get a set of
           andr:ClassToLocPath linked to them. Parse each andr:ClassToLocPath (second loop).
        */
        Set<Resource> lcpSourceSet = Models.getPropertyResources(model,
                                                                 locationClassDefResource,
                                                                 URIs.ANDR.locationClassPathsSource);
        L.debug("Found " + lcpSourceSet.size() + " locationClassPathsSource properties.");
        for (Resource lcpSourceResource : lcpSourceSet) {
            Set<Resource> classToLocPathSet = Models.getPropertyResources(model,
                                                                          lcpSourceResource,
                                                                          URIs.ANDR.classToLocPath);
            for (Resource classToLocPathResource : classToLocPathSet) {
                ClassToLocPath ctlp = parseClassToLocPath(classToLocPathResource, model);
                classToLocPaths.put(ctlp.getForClassUri(), ctlp);
            }
        }

        return new LocationClassDef(sparqlEndpoint.getLabel(), clazz.toString(), classToLocPaths);
    }

    /**
     * Construct an instance of {@link ClassToLocPath} from a andr:ClassToLocPath resource.
     *
     * @param classToLocPath A andr:ClassToLocPath resource.
     * @param model          A model containing the resource.
     * @return A {@link ClassToLocPath} object.
     * @throws DataDefFormatException When the data is malformed, e.g. a mandatory property is not provided.
     */
    public ClassToLocPath parseClassToLocPath(Resource classToLocPath, Model model) throws DataDefFormatException {
        L.debug("Parsing a classToLocPath " + classToLocPath);
        IRI clazz = getSingleObjectAsIri(classToLocPath, URIs.ANDR._class, model);

        Resource latResource = getSingleObjectAsResource(classToLocPath, URIs.ANDR.lat, model);
        PropertyPath latPath = parsePropertyPath(latResource, model);

        Resource lngResource = getSingleObjectAsResource(classToLocPath, URIs.ANDR._long, model);
        PropertyPath lngPath = parsePropertyPath(lngResource, model);

        return new ClassToLocPath(latPath, lngPath, clazz.toString());
    }

    /**
     * Process all andr:inludeRdf properties linked from the given resource.
     * Add data from the files to the current model.
     *
     * @param fileUrl URL of the RDF file to be added to the model.
     * @param model  A model to expand with the data from a RDF file.
     */
    protected void processIncludeRdf(Literal fileUrl, Model model) throws DataDefFormatException, RdfFormatException {
        L.debug("Including data from RDF " + fileUrl);
        try {
            InputStream is = Util.getHttp(fileUrl.getLabel());
            Model newModel = Rio.parse(is, "", RDFFormat.TURTLE);
            model.addAll(newModel);
        } catch (IOException e) {
            L.error("Could not HTTP GET a file", e);
            throw new DataDefFormatException("Error while including a remote RDF file", e);
        } catch (RDFParseException e) {
            throw new RdfFormatException(e);
        }
    }

    /**
     * Read a {@link Value} linked to the given subject via a property. Assert that one and only one such value exists.
     *
     * @param subject     A {@link Resource} from which to read.
     * @param propertyIri A {@link IRI} of a property to read from the subject.
     * @param model       A RDF4J model.
     * @return A single {@link Value} if only one exists. Otherwise an exception is thrown.
     * @throws DataDefFormatException If none or more than one such value exists.
     */
    protected Value getSingleObject(Resource subject, IRI propertyIri, Model model) throws DataDefFormatException {
        Model objects = model.filter(subject, propertyIri, null);
        if (objects.size() != 1)
            throw new DataDefFormatException("For object with IRI " + subject + " expected one property of iri " +
                                                     propertyIri + " but found " + objects.size());

        return objects.iterator().next().getObject();
    }

    /**
     * Read an {@link IRI} linked to the given subject via a property. Assert that one and only one such IRI exists.
     *
     * @param subject     A {@link Resource} from which to read.
     * @param propertyIri A {@link IRI} of a property to read from the subject.
     * @param model       A RDF4J model.
     * @return A single {@link Value} if only one exists. Otherwise an exception is thrown.
     * @throws DataDefFormatException If none or more than one such IRI exists or if the linked value cannot be cast to an IRI.
     */
    protected IRI getSingleObjectAsIri(Resource subject, IRI propertyIri, Model model) throws DataDefFormatException {
        Value val = getSingleObject(subject, propertyIri, model);
        if (val instanceof IRI) return (IRI) val;
        else throw new DataDefFormatException("Could not cast object " + val + " to an IRI.");
    }

    /**
     * Read a {@link Resource} linked to the given subject via a property. Assert that one and only one such Resource exists.
     *
     * @param subject     A {@link Resource} from which to read.
     * @param propertyIri A {@link IRI} of a property to read from the subject.
     * @param model       A RDF4J model.
     * @return A single {@link Value} if only one exists. Otherwise an exception is thrown.
     * @throws DataDefFormatException If none or more than one such Resource exists or if the linked value cannot be cast to a Resource.
     */
    protected Resource getSingleObjectAsResource(Resource subject, IRI propertyIri, Model model)
            throws DataDefFormatException {
        Value val = getSingleObject(subject, propertyIri, model);
        if (val instanceof Resource) return (Resource) val;
        else throw new DataDefFormatException("Could not cast object " + val + " to a Resource.");
    }

    /**
     * Read a {@link Resource} linked to the given subject via a property. Assert that one and only one such Resource exists.
     *
     * @param subject     A {@link Resource} from which to read.
     * @param propertyIri A {@link IRI} of a property to read from the subject.
     * @param model       A RDF4J model.
     * @return A single {@link Value} if only one exists. Otherwise an exception is thrown.
     * @throws DataDefFormatException If none or more than one such Resource exists or if the linked value cannot be cast to a Resource.
     */
    protected Literal getSingleObjectAsLiteral(Resource subject, IRI propertyIri, Model model)
            throws DataDefFormatException {
        Value val = getSingleObject(subject, propertyIri, model);
        if (val instanceof Literal) return (Literal) val;
        else throw new DataDefFormatException("Could not cast object " + val + " to a Literal.");
    }
}
