plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val javafxVersion = "21.0.2"

javafx {
    version = javafxVersion
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web")
}

dependencies {
    implementation("org.openjfx:javafx-controls:$javafxVersion")
    implementation("org.openjfx:javafx-fxml:$javafxVersion")
    implementation("org.openjfx:javafx-web:$javafxVersion")

    implementation("org.freemarker:freemarker:2.3.32")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.13.0.202109080827-r")
    implementation("com.sun.mail:jakarta.mail:2.0.1")
}

application {
    mainClass.set("dev.abykov.pets.relmgr.App")
}
