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

import cz.melkamar.andruian.ddfparser.exception.InvalidMockParameterException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;

public class Util {
    public static InputStream readInputStreamFromResource(String resourcePath, Class clazz) throws IOException {
        ClassLoader classLoader = clazz.getClassLoader();
        File file = new File(classLoader.getResource(resourcePath).getFile());
        return new FileInputStream(file);
    }

    private static HttpClient httpClient = new HttpClient();

    /**
     * Perform a HTTP GET and just return the file contents.
     */
    public static String getHttp(String uri) throws IOException {
        return Util.httpClient.getHttp(uri);
    }

    /**
     * A regular HTTP client which performs a connection and returns response as an InputStream.
     */
    static class HttpClient {
        private static final OkHttpClient client = new OkHttpClient();

        public String getHttp(String uri) throws IOException {
            Request request = new Request.Builder().url(uri).build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response + " | "+response.body());
                return response.body().string();
            }
        }
    }

    /**
     * For a single call replace the HttpClient with a mock one. It will expect to be called with an url and respond
     * with a predefined response. After one call the HTTP client is reverted back.
     *
     * Yes, mocking frameworks exist, but I did not want to add another dependency. On top of that, Mockito cannot
     * mock static methods... so this is quicker and dirtier '°__°'
     */
    static void mockHttpClientResponse(String uri, String response) {
        Util.httpClient = new MockHttpClient(uri, response);
    }

    static class MockHttpClient extends HttpClient {
        private final String expectUri;
        private final String mockResponse;

        MockHttpClient(String expectUri, String mockResponse) {
            this.expectUri = expectUri;
            this.mockResponse = mockResponse;
        }

        @Override
        public String getHttp(String uri) throws IOException {
            Util.httpClient = new HttpClient();

            if (!uri.equals(expectUri))
                throw new InvalidMockParameterException("Expected " + expectUri + " got " + uri);

            return mockResponse;
        }
    }

    // from https://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    static InputStream convertStringToStream(String str) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(str.getBytes("UTF-8"));
    }
}
