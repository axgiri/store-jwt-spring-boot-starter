plugins {
	`java-library`
	`maven-publish`
	id("io.spring.dependency-management") version "1.1.7"
}

group = "tech.axgiri"
version = "0.0.2"
description = "jwt starter module for `store_core`, `store_chat`, `store_notificationreport`. github.com/axgiri and axgiri.tech"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.boot:spring-boot-dependencies:4.0.2")
	}
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly("org.springframework.boot:spring-boot-starter-webmvc")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
publishing {
	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/axgiri/store-jwt-spring-boot-starter")
			credentials {
				username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
				password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
			}
		}
	}

	publications {
		register<MavenPublication>("maven") {
			from(components["java"])
		}
	}
}