plugins {
	`java-library`
	`maven-publish`
	id("io.spring.dependency-management") version "1.1.7"
}

group = "tech.axgiri"
version = "0.0.2-SNAPSHOT"
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
			val nexusUrl: String by project
			val nexusUsername: String by project
			val nexusPassword: String by project
			isAllowInsecureProtocol = true
			
			url = if (version.toString().endsWith("SNAPSHOT")) {
				uri("$nexusUrl/maven-snapshots")
			} else {
				uri("$nexusUrl/maven-releases")
			}

			credentials {
				username = nexusUsername
				password = nexusPassword
			}
		}
	}
	
	publications {
		register<MavenPublication>("maven") {
			from(components["java"])
		}
	}
}