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

package cz.melkamar.andruian.ddfparser.model;

import java.util.HashMap;
import java.util.Map;

public class DataDefBuilder {
    private String uri;
    private LocationClassDef locationClassDef;
    private SourceClassDef sourceClassDef;
    private IndexServer indexServer;
    private Map<String, String> labels;

    public DataDefBuilder(String uri,
                          LocationClassDef locationClassDef,
                          SourceClassDef sourceClassDef) {
        this.uri = uri;
        this.locationClassDef = locationClassDef;
        this.sourceClassDef = sourceClassDef;
        this.labels = new HashMap<>();
    }

    public DataDefBuilder addIndexServer(IndexServer indexServer) {
        if (this.indexServer != null) throw new IllegalArgumentException("A DataDef may only have one index server");
        this.indexServer = indexServer;
        return this;
    }

    /**
     * Add a label to the DataDef. Language may be null in case of a no-language literal.
     */
    public DataDefBuilder addLabel(String language, String label) {
        if (language == null) this.labels.put(DataDef.NO_LANG, label);
        else this.labels.put(language, label);
        return this;
    }

    public DataDef createDataDef() {
        return new DataDef(uri, locationClassDef, sourceClassDef, indexServer, labels);
    }
}