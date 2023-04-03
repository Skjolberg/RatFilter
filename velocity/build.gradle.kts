plugins {
    id("java")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    compileOnly("org.spongepowered:configurate-core:4.1.2")
    compileOnly("org.spongepowered:configurate-yaml:4.1.2")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")
}