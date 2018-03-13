package cz.melkamar.andruian.ddfparser.model;

public class DataDef {
    private final String uri;
    private final LocationClassDef locationClassDef;
    private final SourceClassDef sourceClassDef;

    public DataDef(String uri,
                   LocationClassDef locationClassDef,
                   SourceClassDef sourceClassDef) {
        this.uri = uri;
        this.locationClassDef = locationClassDef;
        this.sourceClassDef = sourceClassDef;
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

    @Override
    public String toString() {
        return "DataDef{" +
                "uri='" + uri + '\'' +
                ", locationClassDef=" + locationClassDef +
                ", sourceClassDef=" + sourceClassDef +
                '}';
    }
}
