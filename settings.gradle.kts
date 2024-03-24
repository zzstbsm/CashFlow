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
include("core:navigation")
include("core:server")

include("core:themes")
include("core:tools")

include("feature:about_me")
include("feature:all_transactions")
include("feature:common_transactions")
include("feature:manage_categories")
include("feature:profile")
include("feature:server_ui")
include("feature:settings")
include("feature:total_balance")
include("feature:transaction_edit")
include("feature:transaction_report")
include("feature:wallet_edit")
include("feature:wallet_overview")
