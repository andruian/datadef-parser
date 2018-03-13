package cz.melkamar.andruian.ddfparser.model;

import java.util.Map;

public class LocationClassDef extends ClassDef {
    private final Map<String, ClassToLocPath> pathsToGps;

    public LocationClassDef(String sparqlEndpoint,
                            String classUri,
                            Map<String, ClassToLocPath> pathsToGps) {
        super(sparqlEndpoint, classUri);
        this.pathsToGps = pathsToGps;
    }

    public Map<String, ClassToLocPath> getPathsToGps() {
        return pathsToGps;
    }

    public ClassToLocPath getPathToGps(String locationClassUri) {
        return pathsToGps.get(locationClassUri);
    }
}
