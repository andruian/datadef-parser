package cz.melkamar.andruian.ddfparser;

import cz.melkamar.andruian.ddfparser.exception.InvalidMockParameterException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Util {
    public static void main(String[] args) {
        Optional<Integer> x = Optional.of(456);
        System.out.println(x);
    }

    public static InputStream readInputStreamFromResource(String resourcePath, Class clazz) throws IOException {
//        return new Scanner(clazz.getResourceAsStream(resourcePath), "UTF-8").useDelimiter("\\A").next();
        ClassLoader classLoader = clazz.getClassLoader();
        File file = new File(classLoader.getResource(resourcePath).getFile());
        return new FileInputStream(file);
    }

    private static HttpClient httpClient = new HttpClient();

    /**
     * Perform a HTTP GET and just return the file contents.
     */
    public static InputStream getHttp(String uri) throws IOException {
        return Util.httpClient.getHttp(uri);
    }

    /**
     * A regular HTTP client which performs a connection and returns response as an InputStream.
     */
    static class HttpClient {
        public InputStream getHttp(String uri) throws IOException {
            return new URL(uri).openStream();
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
        public InputStream getHttp(String uri) throws IOException {
            Util.httpClient = new HttpClient();

            if (!uri.equals(expectUri))
                throw new InvalidMockParameterException("Expected " + expectUri + " got " + uri);

            return new ByteArrayInputStream(mockResponse.getBytes(StandardCharsets.UTF_8));
        }
    }

    // from https://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
