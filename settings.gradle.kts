pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SpendScan"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":navigation")
include(":core")
include(":core:ui")
include(":feature")
include(":core:domain")
include(":core:network")
include(":core:common")
include(":core:data")
include(":core:di")
include(":feature:expenses")
include(":feature:income")
include(":feature:history")
include(":feature:balance")
include(":feature:categories")
include(":feature:edit-balance")
include(":feature:settings")
include(":feature:manage-transaction")
include(":feature:income:myapplication")
