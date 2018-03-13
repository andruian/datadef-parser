package cz.melkamar.andruian.ddfparser.model;

public class PropertyPath {
    private final String[] pathElements;

    public PropertyPath(String... pathElements) {
        this.pathElements = pathElements;
    }

    public String[] getPathElements() {
        return pathElements;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pathElements.length; i++) {
            builder.append("<").append(pathElements[i]).append(">");
            if (i < pathElements.length - 1) builder.append("/");
        }
        return builder.toString();
    }
}
