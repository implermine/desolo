plugins {
    java
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"

//    // 코드 품질 도구
//    id("checkstyle")
//    id("pmd")
//    id("com.github.spotbugs") version "6.0.26"
//
//    // 테스트 리포트
//    id("jacoco")
}

group = "io.implermine"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-batch")

    runtimeOnly("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")


    // ===
    // 테스트
    // ===

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    // TestContainers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    
    // 테스트에서도 PostgreSQL 드라이버 사용
    testRuntimeOnly("org.postgresql:postgresql")
    
    // H2는 필요시에만 (특정 단위 테스트용)
    testRuntimeOnly("com.h2database:h2")

    // 테스트 코드용 Lombok 추가
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // test faker
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.14")
}

// ===
// 테스트 설정
// ===
tasks.withType<Test> {
    useJUnitPlatform()
//    finalizedBy(tasks.jacocoTestReport)
}

// ===
// Jacoco 테스트 커버리지
// ===
//jacoco {
//    toolVersion = "0.8.12"
//}
//
//tasks.jacocoTestReport {
//    dependsOn(tasks.test)
//    reports {
//        xml.required = true
//        html.required = true
//        csv.required = false
//    }
//}
//
//tasks.jacocoTestCoverageVerification {
//    violationRules {
//        rule {
//            limit {
//                minimum = "0.80".toBigDecimal()
//            }
//        }
//    }
//}
//
//// ===
//// Checkstyle 설정
//// ===
//checkstyle {
//    toolVersion = "10.12.4"
//    configFile = file("config/checkstyle/checkstyle.xml")
//    isIgnoreFailures = false
//}
//
//// ===
//// PMD 설정
//// ===
//pmd {
//    toolVersion = "7.0.0"
//    isConsoleOutput = true
//    ruleSetFiles = files("config/pmd/pmd-rules.xml")
//    ruleSets = listOf()
//}
//
//// ===
//// SpotBugs 설정
//// ===
//spotbugs {
//    toolVersion = "4.8.6"
//    excludeFilter = file("config/spotbugs/exclude.xml")
//}
//
//tasks.withType<com.github.spotbugs.snom.SpotBugsTask> {
//    reports.create("html") {
//        required = true
//        outputLocation = file("$buildDir/reports/spotbugs/spotbugs.html")
//        setStylesheet("fancy-hist.xsl")
//    }
//}
//
//// ===
//// 코드 품질 검사 통합 태스크
//// ===
//tasks.register("codeQuality") {
//    dependsOn("checkstyleMain", "pmdMain", "spotbugsMain", "jacocoTestCoverageVerification")
//    group = "verification"
//    description = "Run all code quality checks"
//}
