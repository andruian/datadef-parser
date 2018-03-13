package cz.melkamar.andruian.ddfparser.model;

public class DataDef {
    private final String uri;
    private final LocationDef locationDef;
    private final SourceClassDef sourceClassDef;

    public DataDef(String uri,
                   LocationDef locationDef,
                   SourceClassDef sourceClassDef) {
        this.uri = uri;
        this.locationDef = locationDef;
        this.sourceClassDef = sourceClassDef;
    }

    public String getUri() {
        return uri;
    }

    public LocationDef getLocationDef() {
        return locationDef;
    }

    public SourceClassDef getSourceClassDef() {
        return sourceClassDef;
    }

    @Override
    public String toString() {
        return "DataDef{" +
                "uri='" + uri + '\'' +
                ", locationDef=" + locationDef +
                ", sourceClassDef=" + sourceClassDef +
                '}';
    }
}
