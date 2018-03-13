package cz.melkamar.andruian.ddfparser.model;

import java.util.Optional;

public class DataDef {
    private final String uri;
    private final LocationClassDef locationClassDef;
    private final SourceClassDef sourceClassDef;
    private final IndexServer indexServer;

    public DataDef(String uri,
                   LocationClassDef locationClassDef,
                   SourceClassDef sourceClassDef, IndexServer indexServer) {
        this.uri = uri;
        this.locationClassDef = locationClassDef;
        this.sourceClassDef = sourceClassDef;
        this.indexServer = indexServer;
    }

    public DataDef(String uri,
                   LocationClassDef locationClassDef,
                   SourceClassDef sourceClassDef) {
        this.uri = uri;
        this.locationClassDef = locationClassDef;
        this.sourceClassDef = sourceClassDef;
        indexServer = null;
    }

    public String getUri() {
        return uri;
    }

    public LocationClassDef getLocationClassDef() {
        return locationClassDef;
    }

    public SourceClassDef getSourceClassDef() {
        return sourceClassDef;
    }

    public Optional<IndexServer> getIndexServer() {
        return Optional.ofNullable(indexServer);
    }

    @Override
    public String toString() {
        return "DataDef{" +
                "uri='" + uri + '\'' +
                ", locationClassDef=" + locationClassDef +
                ", sourceClassDef=" + sourceClassDef +
                '}';
    }
}
