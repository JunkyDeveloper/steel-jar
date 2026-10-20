# steel-jar

A single runnable jar that bundles the [SteelMC](https://github.com/Steel-Foundation/SteelMC) server binaries for Linux (glibc and musl), macOS and Windows. At startup it extracts the binary matching your OS to a temp directory and runs it attached to your terminal. The binaries are downloaded from the latest SteelMC GitHub release at build time, so you only need a JVM to run it.

## Build and run

```sh
mvn package
java -jar target/steel.jar
```

Needs JDK 17+ and Maven. Steel's config and world are created in the directory you run the jar from.
