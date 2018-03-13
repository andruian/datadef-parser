package cz.melkamar.andruian.ddfparser.model;

public class SelectProperty {
    private final String name;
    private final PropertyPath path;

    public SelectProperty(String name, PropertyPath path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public PropertyPath getPath() {
        return path;
    }
}
