plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.21.10-fabric"

allprojects {
    repositories {
        maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
        maven("https://maven.terraformersmc.com/")
    }
}