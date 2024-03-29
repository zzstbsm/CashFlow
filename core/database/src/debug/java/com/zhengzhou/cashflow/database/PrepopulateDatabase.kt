package com.zhengzhou.cashflow.database

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.DatabaseInstance
import com.zhengzhou.cashflow.database.api.complex_data.TransactionFullForUI
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.CategoryUseCases
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.WalletUseCases
import com.zhengzhou.cashflow.database.data.LoadDefaultCategories
import com.zhengzhou.cashflow.themes.icons.IconsMappedForDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class PrepopulateDatabase {

    private val repository = DatabaseInstance.getRepository()
    private val categoryUseCases = CategoryUseCases(repository)
    private val walletUseCases = WalletUseCases(repository)

    init {

        val date = Date()

        val wallets = setWallets(date).toMutableList()
        val categories = setCategories().toMutableList()

        val tagEntries = setTagEntries()
        val tagSky = tagEntries[0]
        val tagLunch = tagEntries[1]
        val tagGin = tagEntries[2]
        val tagGlasses = tagEntries[3]
        val tagLessons = tagEntries[4]


        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {

            categories.forEachIndexed { index, category ->
                categories[index] = categoryUseCases.addCategory(category)!!
            }
            wallets.forEachIndexed { index, wallet ->
                wallets[index] = walletUseCases.addWallet(wallet)
            }

            val catGrocery = categories[0]
            val catEatingOut = categories[1]
            val catEntertainment = categories[7]
            val catDeposit = categories[10]

            val walletEUR1 = wallets[0]
            val walletEUR2 = wallets[1]
            val walletSEK = wallets[2]

            val transactionFullForUIList = listOf(
                TransactionFullForUI.new(
                    description = "Esselunga",
                    amount = -20f,
                    date = date,
                    wallet = walletEUR1,
                    secondaryWallet = Wallet.newEmpty(),
                    category = catGrocery,
                    location = null,
                    tagEntryList = listOf(
                        tagLunch,
                    ),
                    transactionType = TransactionType.Expense,
                    isBlueprint = false,
                ),
                TransactionFullForUI.new(
                    description = "Agri",
                    amount = -10f,
                    date = date,
                    wallet = walletEUR1,
                    secondaryWallet = Wallet.newEmpty(),
                    category = catEatingOut,
                    location = null,
                    tagEntryList = listOf(
                        tagSky,
                        tagLunch
                    ),
                    transactionType = TransactionType.Expense,
                    isBlueprint = true,
                ),
                TransactionFullForUI.new(
                    description = "Agri",
                    amount = -10f,
                    date = date,
                    wallet = walletEUR1,
                    secondaryWallet = Wallet.newEmpty(),
                    category = catEatingOut,
                    location = null,
                    tagEntryList = listOf(
                        tagSky,
                        tagLunch
                    ),
                    transactionType = TransactionType.Expense,
                    isBlueprint = false,
                ),
                TransactionFullForUI.new(
                    description = "Ica",
                    amount = -100f,
                    date = date,
                    wallet = walletSEK,
                    secondaryWallet = Wallet.newEmpty(),
                    category = catEatingOut,
                    location = null,
                    tagEntryList = listOf(
                        tagGin
                    ),
                    transactionType = TransactionType.Expense,
                    isBlueprint = true,
                ),
                TransactionFullForUI.new(
                    description = "Private lessons",
                    amount = 50f,
                    date = date,
                    wallet = walletEUR2,
                    secondaryWallet = Wallet.newEmpty(),
                    category = catDeposit,
                    location = null,
                    tagEntryList = listOf(
                        tagLessons
                    ),
                    transactionType = TransactionType.Expense,
                    isBlueprint = true,
                ),
                TransactionFullForUI.new(
                    description = "Mercolegin",
                    amount = -15f,
                    date = date,
                    wallet = walletEUR2,
                    secondaryWallet = Wallet.newEmpty(),
                    category = catEntertainment,
                    location = null,
                    tagEntryList = listOf(
                        tagGin
                    ),
                    transactionType = TransactionType.Expense,
                    isBlueprint = true,
                ),
                TransactionFullForUI.new(
                    description = "Glasses",
                    amount = -200f,
                    date = date,
                    wallet = walletEUR1,
                    secondaryWallet = Wallet.newEmpty(),
                    category = catGrocery,
                    location = null,
                    tagEntryList = listOf(
                        tagGlasses,
                    ),
                    transactionType = TransactionType.Expense,
                    isBlueprint = true,
                ),
            )

            transactionFullForUIList.forEach {
                it.save(repository,newTransaction = true)
            }
        }
    }

    private fun setWallets(date: Date): List<Wallet> {
        return listOf(
            Wallet(
                id = UUID(0L, 0L),
                name = "Mastercard",
                startAmount = 500f,
                iconName = IconsMappedForDB.WALLET,
                currency = Currency.EUR,
                creationDate = date,
                lastAccess = Date(),
                budgetEnabled = false,
            ),
            Wallet(
                id = UUID(0L, 0L),
                name = "Visa",
                startAmount = 100f,
                iconName = IconsMappedForDB.WALLET,
                currency = Currency.EUR,
                creationDate = Date(date.time + 10000),
                lastAccess = Date(),
                budgetEnabled = false,
            ),
            Wallet(
                id = UUID(0L, 0L),
                name = "Sweden",
                startAmount = 1000f,
                iconName = IconsMappedForDB.WALLET,
                currency = Currency.SEK,
                creationDate = Date(date.time + 20000),
                lastAccess = Date(),
                budgetEnabled = false,
            )
        )
    }

    private fun setCategories(): List<Category> {

        return LoadDefaultCategories.setDefaultExpenseCategories() +
                LoadDefaultCategories.setDefaultDepositCategories() +
                LoadDefaultCategories.setDefaultMovementCategories()
    }

    private fun setTagEntries(): List<TagEntry> {
        return listOf(
            TagEntry(
                id = UUID.randomUUID(),
                name = "Sky",
                count = 0,
            ),
            TagEntry(
                id = UUID.randomUUID(),
                name = "Lunch",
                count = 0,
            ),
            TagEntry(
                id = UUID.randomUUID(),
                name = "Gin",
                count = 0,
            ),
            TagEntry(
                id = UUID.randomUUID(),
                name = "Glasses",
                count = 0,
            ),
            TagEntry(
                id = UUID.randomUUID(),
                name = "Lessons",
                count = 0,
            ),
        )
    }

}