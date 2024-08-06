plugins {
    id("java")
}
val springBootVers = "3.3.2"
group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVers")
    implementation("org.springframework.boot:spring-boot-starter:$springBootVers")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVers")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:$springBootVers")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVers")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVers")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")
    implementation("org.flywaydb:flyway-core:10.16.0")
    implementation("org.postgresql:postgresql:42.7.3")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:10.16.0")
    compileOnly("org.projectlombok:lombok:1.18.34")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
}

tasks.test {
    useJUnitPlatform()
}