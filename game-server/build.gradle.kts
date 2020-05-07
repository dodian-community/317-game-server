import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springVersion: String by project

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

application {
    mainClassName = "net.dodian.Server"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    runtimeOnly("com.h2database:h2:1.4.200")

    annotationProcessor("org.projectlombok:lombok:1.18.10")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:$springVersion")

    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-security:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.netty:netty-all:4.1.8.Final")
    implementation("com.google.guava:guava:28.0-jre")
    implementation("javax.xml.bind:jaxb-api:2.3.0")

    implementation("org.javassist:javassist:3.26.0-GA")
    implementation("org.apache.commons:commons-compress:1.18")
    implementation("log4j:log4j:1.2.17")

    compileOnly("org.projectlombok:lombok:1.18.10")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")

    implementation("io.reactivex.rxjava3:rxjava:3.0.3")
    implementation(kotlin("stdlib-jdk8"))

}

repositories {
    mavenCentral()
}

tasks.register<JavaExec>("runServer") {
    group = "dodian-game"
    dependsOn(":bootRun")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}