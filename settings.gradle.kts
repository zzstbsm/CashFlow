pluginManagement {
    repositories {
        google()
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
rootProject.name = "Cash Flow"
include ("app")
include("core:data")
include("core:database")
include("core:tools")
include("feature:total_balance")
include("core:themes")
include("feature:common_transactions")
include("feature:transaction_edit")
include("feature:profile")
include("feature:manage_categories")
include("feature:about_me")
include("feature:all_transactions")
include("feature:transaction_report")
include("feature:settings")
include("feature:wallet_edit")
include("feature:wallet_overview")
include("core:navigation")
