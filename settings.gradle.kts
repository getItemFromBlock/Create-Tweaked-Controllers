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
            name = "forge"
            url = uri("https://maven.minecraftforge.net/")
        }
        gradlePluginPortal()
    }
    plugins {
        kotlin("jvm") version "2.1.0"
    }
}

include("common")
include("fabric")
include("forge")

rootProject.name = "create_tweaked_controllers"

