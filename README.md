# Auto-impl

<img width="1500" height="500" alt="Interguess-Auto-impl" src="https://github.com/user-attachments/assets/37912c0a-a55c-4ac2-b0e0-02b9612e6e1c" />

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
    compileOnly 'com.interguess.autoimpl:api:VERSION'

    annotationProcessor 'com.interguess.autoimpl:annotationprocessor:VERSION'
}
```

### Use with maven

If you are using maven, add this snippet to your pom.xml.

```xml
<repositories>
    <repository>
        <id>auto-impl-github-packages</id>
        <name>Github Packages for auto-impl repository</name>
        <url>https://maven.pkg.github.com/interguess/auto-impl</url>
    </repository>
</repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>com.interguess.autoimpl</groupId>
        <artifactId>api</artifactId>
        <version>VERSION</version>
    </dependency>
    <dependency>
        <groupId>com.interguess.autoimpl</groupId>
        <artifactId>annotationprocessor</artifactId>
        <version>VERSION</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

<div>
    <img src="https://img.shields.io/codefactor/grade/github/Interguess/auto-impl?style=for-the-badge&logo=codefactor&logoColor=white" alt="Codefactor">
    <img src="https://img.shields.io/codecov/c/github/Interguess/auto-impl?style=for-the-badge&logo=codefactor&logoColor=white" alt="Code coverage">
    <img src="https://img.shields.io/github/license/Interguess/auto-impl?style=for-the-badge&logo=codefactor&logoColor=white" alt="License">
</div>