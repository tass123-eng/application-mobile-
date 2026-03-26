pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // jcenter est obsolète, ne l'utiliser que si une dépendance n'existe nulle part ailleurs
        // jcenter()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // jcenter peut être ajouté ici uniquement si tu utilises une dépendance legacy
        // jcenter()
    }
}

rootProject.name = "findhobbies"
include(":app")
