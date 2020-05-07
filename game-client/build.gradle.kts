plugins {
    id("application")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "com.runescape.Client"
}

dependencies {
    compileOnly("javax.xml.bind:jaxb-api:2.3.0")
}

tasks.register<JavaExec>("runClient") {
    group = "dodian-game"
    dependsOn(":run")
}
