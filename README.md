# Andruian Data Definition parser

[![Build Status](https://travis-ci.org/andruian/datadef-parser.svg?branch=master)](https://travis-ci.org/andruian/datadef-parser)

## Installation
#### Plain Java
To use this library in a regular Java project, build a jar and include it on classpath: 
```
$ gradlew clean test jar
```
#### Maven or Gradle
To use this library in Maven or Gradle, run this task to put the jar in your local Maven repository:
```
$ gradlew publishToMavenLocal
```
You will then need to set up your build to use local Maven repository as a source repository. Put this in the root
of `build.gradle`:
```
repositories {
    mavenLocal()
}
```

## Usage

The main class responsible for parsing is `DataDefParser`. Use it to convert a RDF file to a hierarchy of 
POJOs that correnspond to the Andruian Data definition schema.

[RDF4J](http://rdf4j.org/) is used as the RDF parser on the background. `DataDefParser` is capable of parsing any 
RDF format that RDF4J can. Pass the format to the `DataDefParser#parse()` method. **However**, the RDF4J parser 
implementation must be present in the classpath of the library. By default, only Turtle is provided to keep the
library size small. If you need anything else, it must be added to [build.gradle](build.gradle) and the library 
rebuilt. 

#### Example code:
```java
InputStream is = null; // Provide your own
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

## Releasing a new version
- Change the version in `build.gradle`
- Set environment properties 
    - `BINTRAY_USER` as Bintray username
    - `BINTRAY_KEY` as the corresponding API key
- Run `gradlew bintrayUpload`
