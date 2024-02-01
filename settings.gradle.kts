pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }
}
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        maven(url = "https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

rootProject.name = "mvvm_prac"
include(":app")

 