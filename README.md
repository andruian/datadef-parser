# Andruian Data Definition parser

[![Build Status](https://travis-ci.org/andruian/datadef-parser.svg?branch=master)](https://travis-ci.org/andruian/datadef-parser)

## Usage

The main class responsible for parsing is `DataDefParser`. Use it to convert a RDF file to a hierarchy of 
POJOs that correnspond to the Andruian Data definition schema.

[RDF4J](http://rdf4j.org/) is used as the RDF parser on the background. `DataDefParser` is capable of parsing any 
RDF format that RDF4J can. Pass the format to the `DataDefParser#parse()` method.

#### Example code:
```java
InputStream is = null;
List<DataDef> l = new DataDefParser().parse(is, RDFFormat.TURTLE);      
```

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

## TODO
- Link to the model class schema