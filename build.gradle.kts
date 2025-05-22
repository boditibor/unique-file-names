val springdocVersion: String by extra("2.8.8")
val mockitoVersion: String by extra("5.2.0")

plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.tiborbodi"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocVersion")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    // Find the Byte Buddy agent JAR from the mockito-inline dependency
    doFirst {
        val agentJar = classpath
            .filter { it.name.contains("byte-buddy-agent") }
            .firstOrNull()
        if (agentJar != null) {
            jvmArgs("-javaagent:${agentJar.absolutePath}")
        }
    }
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
}

tasks.register<Javadoc>("generateJavadoc") {
    group = "Documentation"
    description = "Generates Javadoc for the project."
    source = sourceSets["main"].allJava
    classpath = sourceSets["main"].compileClasspath
    setDestinationDir(layout.buildDirectory.dir("docs/javadoc").get().asFile)
}

tasks.jar {
    dependsOn(tasks.named("generateJavadoc"))
    from("build/docs/javadoc") {
        into("docs/javadoc")
    }
}

