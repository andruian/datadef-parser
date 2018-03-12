# Andruian Data Definition parser

## Using on Android

By default, Android does not provide a `javax.xml.datatype.DatatypeFactory` so attempting to use
RDF4J Rio API might crash if you do not provide it yourself.

To use this library on Android, declare an extra dependency in `build.gradle` (adjust version 
to your needs):

```
dependencies {
    implementation group: 'xerces', name: 'xercesImpl', version: '2.11.0'
}
```

The stacktrace fixed by this dependency contains these exceptions:
```
java.lang.Error: Could not instantiate javax.xml.datatype.DatatypeFactory
...
Caused by: javax.xml.datatype.DatatypeConfigurationException: Provider org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl not found
...
Caused by: java.lang.ClassNotFoundException: Didn't find class "org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl" on path: DexPathList ...
```

