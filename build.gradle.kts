plugins {
    id("java")
    id("war")
}

group = "ru.ngs.summerjob"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.yaml:snakeyaml:2.1")
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.war {
    archiveFileName.set("CleverBank.war")
    //destinationDirectory=buildDir
}

tasks.test {
    useJUnitPlatform()
}