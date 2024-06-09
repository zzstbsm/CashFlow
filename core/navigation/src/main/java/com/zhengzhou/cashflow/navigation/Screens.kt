package com.zhengzhou.cashflow.navigation

import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TransactionType
import java.util.UUID

sealed class Screen(
    val route: String,
) {

    data object AboutMe: Screen(
        route = ApplicationScreensEnum.AboutMe.name
    )
    data object AllTransactions: Screen(
        route = ApplicationScreensEnum.AllTransactions.name +
                "/{${NavigationKeys.keyWalletUUID}}"
    ) {
        private fun createRoute(
            walletUUID: UUID,
        ): String = ApplicationScreensEnum.AllTransactions.name +
                "/${walletUUID}"

        fun navigate(
            walletUUID: UUID = UUID(0L, 0L),
            navController: NavController,
        ) {
            navController.navigate(
                createRoute(
                    walletUUID = walletUUID,
                )
            )
        }

    }
    data object Balance: Screen(
        route = ApplicationScreensEnum.Balance.name,
    )
    data object CommonTransactions: Screen(
        route = ApplicationScreensEnum.CommonTransactions.name,
    )
    data object ManageCategories: Screen(
        route = ApplicationScreensEnum.ManageCategories.name,
    )
    data object Profile: Screen(
        route = ApplicationScreensEnum.Profile.name,
    )
    data object ServerUi: Screen(
        route = ApplicationScreensEnum.ServerUi.name
    )
    data object Settings: Screen(
        route = ApplicationScreensEnum.Settings.name,
    )
    data object TransactionEdit: Screen(
        route = ApplicationScreensEnum.TransactionEdit.name +
                "/{${NavigationKeys.keyTransactionTypeID}}" +
                "/{${NavigationKeys.keyTransactionUUID}}" +
                "/{${NavigationKeys.keyCurrencyName}}" +
                "/{${NavigationKeys.keyIsBlueprint}}" +
                "/{${NavigationKeys.keyEditBlueprint}}",
    ) {
        private fun createRoute(
            transactionType: TransactionType,
            transactionUUID: UUID,
            currency: Currency,
            isBlueprint: Boolean,
            editBlueprint: Boolean,
        ) = ApplicationScreensEnum.TransactionEdit.name +
                "/${transactionType.id}/$transactionUUID/${currency.name}/$isBlueprint/$editBlueprint"

        fun navigate(
            transactionType: TransactionType,
            transactionUUID: UUID,
            currency: Currency,
            isBlueprint: Boolean,
            editBlueprint: Boolean,
            navController: NavController,
        )  {
            navController.navigate(
                createRoute(
                    transactionType = transactionType,
                    transactionUUID = transactionUUID,
                    currency = currency,
                    isBlueprint = isBlueprint,
                    editBlueprint = editBlueprint,
                )
            )
        }
    }
    data object TransactionReport: Screen(
        route = ApplicationScreensEnum.TransactionReport.name +
                "/{${NavigationKeys.keyTransactionUUID}}",
    ) {
        private fun createRoute(
            transactionUUID: UUID,
        ) = ApplicationScreensEnum.TransactionReport.name +
                "/$transactionUUID"

        fun navigate(
            transactionUUID: UUID,
            navController: NavController,
        ) {
            navController.navigate(
                createRoute(
                    transactionUUID
                )
            )
        }
    }
    data object WalletEdit: Screen(
        route = ApplicationScreensEnum.WalletEdit.name +
                "/{${NavigationKeys.keyWalletUUID}}",
    ) {
        private fun createRoute(
            walletID: UUID,
        ) : String {
            return ApplicationScreensEnum.WalletEdit.name +
                    "/${walletID}"
        }
        fun navigate(
            walletID: UUID,
            navController: NavController,
        ) {
            navController.navigate(
                createRoute(
                    walletID = walletID
                )
            )
        }
    }
    data object WalletOverview: Screen(
        route = ApplicationScreensEnum.WalletOverview.name,
    ) {
        const val handleKeyWalletUUID : String = "walletUUID"
    }
}