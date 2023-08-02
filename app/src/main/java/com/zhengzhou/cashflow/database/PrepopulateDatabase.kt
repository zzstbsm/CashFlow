package com.zhengzhou.cashflow.database

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.TransactionFullForUI
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.tools.ConfigurationFirstStartup
import com.zhengzhou.cashflow.tools.IconsMappedForDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class PrepopulateDatabase {

    val repository = DatabaseRepository.get()

    init {

        val date = Date()

        val wallets = setWallets(date)
        val walletEUR1 = wallets[0]
        val walletEUR2 = wallets[1]
        val walletSEK = wallets[2]

        val categories = setCategories()
        val catGrocery = categories[0]
        val catEatingOut = categories[1]
        val catEntertainment = categories[7]
        val catDeposit = categories[10]

        val tagEntries = setTagEntries()
        val tagSky = tagEntries[0]
        val tagLunch = tagEntries[1]
        val tagGin = tagEntries[2]
        val tagGlasses = tagEntries[3]
        val tagLessons = tagEntries[4]

        val transactionFullForUIList = listOf(
            TransactionFullForUI.new(
                description = "Esselunga",
                amount = -20f,
                date = date,
                wallet = walletEUR1,
                category = catGrocery,
                location = null,
                tagEntryList = listOf(),
                transactionType = TransactionType.Expense,
                isBlueprint = false,
            ),
            TransactionFullForUI.new(
                description = "Agri",
                amount = -10f,
                date = date,
                wallet = walletEUR1,
                category = catEatingOut,
                location = null,
                tagEntryList = listOf(),
                transactionType = TransactionType.Expense,
                isBlueprint = true,
            ),
            TransactionFullForUI.new(
                description = "Ica",
                amount = -100f,
                date = date,
                wallet = walletSEK,
                category = catEatingOut,
                location = null,
                tagEntryList = listOf(),
                transactionType = TransactionType.Expense,
                isBlueprint = true,
            ),
        )

        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            categories.forEach {
                repository.addCategory(it)
            }
            wallets.forEach {
                repository.addWallet(it)
            }
            transactionFullForUIList.forEach {
                it.save(repository,newTransaction = true)
            }
        }
    }

    private fun setWallets(date: Date): List<Wallet> {
        return listOf(
            Wallet(
                id = UUID.randomUUID(),
                name = "Mastercard",
                startAmount = 100f,
                iconName = IconsMappedForDB.WALLET,
                currency = Currency.EUR,
                creationDate = date,
                lastAccess = Date(),
                budgetEnabled = false,
            ),
            Wallet(
                id = UUID.randomUUID(),
                name = "Visa",
                startAmount = 100f,
                iconName = IconsMappedForDB.WALLET,
                currency = Currency.EUR,
                creationDate = Date(date.time + 10000),
                lastAccess = Date(),
                budgetEnabled = false,
            ),
            Wallet(
                id = UUID.randomUUID(),
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

        return ConfigurationFirstStartup.setDefaultExpenseCategories() +
                ConfigurationFirstStartup.setDefaultDepositCategories() +
                ConfigurationFirstStartup.setDefaultMovementCategories()
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