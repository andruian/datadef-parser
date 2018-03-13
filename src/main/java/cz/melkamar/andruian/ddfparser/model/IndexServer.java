package cz.melkamar.andruian.ddfparser.model;

import java.util.Optional;

public class IndexServer {
    private final String uri;
    private final int version;
    private final boolean versionSet;

    public IndexServer(String uri, int version) {
        this.uri = uri;
        this.versionSet = true;
        this.version = version;
    }

    public IndexServer(String uri) {
        this.uri = uri;
        this.versionSet = false;
        this.version = -1;
    }

    public String getUri() {
        return uri;
    }

    public Optional<Integer> getVersion() {
        if (versionSet) return Optional.of(version);
        else return Optional.empty();
    }
}
