pluginManagement {
    repositories {
        maven {
            name = "fabricmc"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "architectury"
            url = uri("https://maven.architectury.dev/")
        }
        maven {
            name = "forgemc"
            url = uri("https://maven.minecraftforge.net/")
        }
        gradlePluginPortal()
    }
}

include("common")
include("fabric")
include("forge")

rootProject.name = "create_tweaked_controllers"

