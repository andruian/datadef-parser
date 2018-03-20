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

import java.util.Map;

public class DataDef {
    private final String uri;
    private final LocationClassDef locationClassDef;
    private final SourceClassDef sourceClassDef;
    private final IndexServer indexServer;
    private final Map<String, String> labels;

    public static final String NO_LANG = "";

    public DataDef(String uri,
                   LocationClassDef locationClassDef,
                   SourceClassDef sourceClassDef,
                   IndexServer indexServer,
                   Map<String, String> labels) {
        this.uri = uri;
        this.locationClassDef = locationClassDef;
        this.sourceClassDef = sourceClassDef;
        this.indexServer = indexServer;
        this.labels = labels;
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

    public IndexServer getIndexServer() {
        return indexServer;
    }



    /**
     * Get a skos:prefLabel of this DataDef of the given language.
     * @param language A language string as used in RDF, e.g. for "something"@en the language string will be "en"
     * @return A label in the given language or null if it does not exist.
     */
    public String getLabel(String language) {
        return labels.get(language);
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    /**
     * Get a skos:prefLabel of this DataDef without any language specified.
     * @return A label without a language tag or null if it does not exist.
     */
    public String getLabel() {
        return labels.get(NO_LANG);
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
