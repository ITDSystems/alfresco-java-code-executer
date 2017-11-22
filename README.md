[![Build Status](https://travis-ci.org/ITDSystems/alfresco-java-code-executer.svg?branch=master)](https://travis-ci.org/ITDSystems/alfresco-java-code-executer)

# Overview

AJCE is Alfresco module that allows remote execution of an arbitrary Java class.
AJCE is just a webscript that receives Java byte-code as a request payload, loads this class and executes it.
Use-cases are similar to [JS Console](https://github.com/share-extras/js-console) ones but for pretty complex
cases when required JS API is not available.

**!!! Please keep in mind this extension is not supposed to be used in production environment**

# Installation

AJCE may be installed as a regular Alfresco JAR module. Keep in mind that it's disabled by default, thus
must be explicitly enabled via [alfresco-global.properties](https://github.com/ITDSystems/alfresco-java-code-executer/blob/master/platform-jar/src/test/properties/local/alfresco-global-h2.properties#L25). 

# Usage

There are actually two ways of using AJCE. The most straight forward one is to submit a request with a proper payload:

```bash
curl                                             \
    -X POST                                      \
    -u admin:password                            \
    --data-binary @PATH_TO_CLASS_FILE            \
    -H 'Content-Type: application/octect-stream' \
    http://localhost:8080/alfresco/s/api/ajce

```

A bit more convenient way is to use supplied Maven plugin. For more details look at the [example-project](https://github.com/ITDSystems/alfresco-java-code-executer/blob/master/example-project/pom.xml#L52).

# Downloads

Module and maven plugins builds are automatically published to [nexus.itdhq.com](http://nexus.itdhq.com) by Travis CI. To download follow links below:

* [AJCE latest release](http://nexus.itdhq.com/service/local/artifact/maven/redirect?r=releases&g=com.itdhq.ajce&a=ajce&e=jar&v=LATEST)
* [AJCE latest snapshot](http://nexus.itdhq.com/service/local/artifact/maven/redirect?r=snapshots&g=com.itdhq.ajce&a=ajce&e=jar&v=LATEST) 
