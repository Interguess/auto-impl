# Auto-impl

> [!WARNING]  
> Auto-impl is currently still under active development, we do not yet have a version that is fully operational and
> performant.<br>
> You can use auto-impl yourself to test it or support us in the development. There will still be some changes and whole
> structures can be revised.

Auto-impl is a Java annotation processor that writes standard implementations for interfaces based on different method
types, thereby reducing boilerplate code.

## How to install

### Use with gradle

If you are using gradle, add this snippet to your build.gradle.

```groovy
repositories {
    maven {
        url 'https://maven.pkg.github.com/interguess/auto-impl'
    }
}
```

```groovy
dependencies {
    compileOnly 'com.interguess:auto-impl:VERSION'
}
```

### Use with maven

If you are using maven, add this snippet to your pom.xml.

```xml

<repositories>
    <repository>
        <id>auto-impl-github-packages</id>
        <name>Github Packages for auto-impl repository</name>
        <url>https://maven.pkg.github.com/interguess/javaflow</url>
    </repository>
</repositories>
```

```xml

<dependencies>
    <dependency>
        <groupId>com.interguess</groupId>
        <artifactId>auto-impl</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```