# steel-jar

A single runnable jar that bundles the [SteelMC](https://github.com/Steel-Foundation/SteelMC) server binaries for Linux (glibc and musl), macOS and Windows. At startup it extracts the binary matching your OS to a temp directory and runs it attached to your terminal. The binaries are downloaded at build time from the SteelMC release whose tag matches the pom version (`v<version>`), so you only need a JVM to run it.

## Build and run

```sh
mvn package
java -jar target/steel.jar
```

Needs JDK 17+ and Maven. Steel's config and world are created in the directory you run the jar from.

## Releases

Bump `<version>` in `pom.xml` to a SteelMC release tag without the `v` (e.g. `0.17.0+mc26.3`) and push to `main`. The `release` workflow builds the jar and publishes it as GitHub release `v<version>`. Pushes that don't change the version are skipped.
