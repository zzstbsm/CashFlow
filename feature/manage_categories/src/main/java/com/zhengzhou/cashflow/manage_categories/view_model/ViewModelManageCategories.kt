package com.zhengzhou.cashflow.manage_categories.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.CategoryUseCases
import com.zhengzhou.cashflow.manage_categories.R
import com.zhengzhou.cashflow.themes.icons.IconsMappedForDB
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.tools.removeSpaceFromStringEnd
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

internal class ManageCategoriesViewModel(
    repository: RepositoryInterface
) : ViewModel() {

    private val categoryUseCases = CategoryUseCases(repository)

    private var jobLoadCategories: Job

    private var writingOnUiState: Boolean = false

    private var _uiState = MutableStateFlow(ManageCategoriesUiState())
    val uiState: StateFlow<ManageCategoriesUiState> = _uiState.asStateFlow()

    init {
        jobLoadCategories = loadCategories()
    }

    private fun setUiState(
        isLoading: Boolean? = null,
        listCategories: List<Category>? = null,
        transactionType: TransactionType? = null,
        openCategory: Category? = null,
        openCategoryOccurrences: Int? = null,

        actOnOpenCategory: Boolean = false
    ) {
        viewModelScope.launch {
            while (writingOnUiState) delay(5)
            writingOnUiState = true

            val openCategoryToUpdate = if (actOnOpenCategory && uiState.value.openCategory != null && openCategory == null) null
                else openCategory ?: uiState.value.openCategory

            _uiState.value = ManageCategoriesUiState(
                isLoading = isLoading ?: uiState.value.isLoading,
                listCategories =  listCategories ?: uiState.value.listCategories,
                transactionType = transactionType ?: uiState.value.transactionType,
                openCategory = openCategoryToUpdate,
                openCategoryOccurrences = openCategoryOccurrences ?: uiState.value.openCategoryOccurrences
            )
            writingOnUiState = false
        }
    }

    private fun loadCategories(): Job {
        return viewModelScope.launch {
            categoryUseCases.getCategoryList().collect { categoryList ->
                setUiState(
                    listCategories = categoryList.sortedBy { category: Category -> category.name },
                    isLoading = false,
                )
            }
        }
    }

    private fun getOccurrences(category: Category?) {
        viewModelScope.launch {
            if (category == null) {
                setUiState(
                    openCategoryOccurrences = 0
                )
            } else {
                setUiState(
                    isLoading = true
                )
                val occurrences = categoryUseCases.getCategoryOccurrences(category = category)
                setUiState(
                    openCategoryOccurrences = occurrences,
                    isLoading = false,
                )
            }
        }
    }

    fun setTransactionType(transactionType: TransactionType) {
        setUiState(
            transactionType = transactionType
        )
    }
    fun setOpenCategory(category: Category?) {
        getOccurrences(category = category)
        setUiState(
            openCategory = category,
            actOnOpenCategory = true,
        )
    }

    fun createCategory(
        name: String,
        iconName: IconsMappedForDB,
        transactionType: TransactionType
    ) {
        val newCategory = Category(
            id = UUID.randomUUID(),
            name = name,
            iconName = iconName,
            transactionType = transactionType
        )
        viewModelScope.launch {
            categoryUseCases.addCategory(category = newCategory)
            loadCategories()
        }
    }

    fun editCategory(category: Category) {
        viewModelScope.launch {
            val tempCategory = category.copy(
                name = removeSpaceFromStringEnd(category.name)
            )
            categoryUseCases.updateCategory(category = tempCategory)
            loadCategories()
        }
    }

    fun deleteCategory(category: Category) {

        if (uiState.value.openCategoryOccurrences > 0) {
            EventMessages.sendMessageId(R.string.cannot_delete)
            return
        }

        if (uiState.value.isLoading) {
            EventMessages.sendMessageId(R.string.loading_category_occurrences)
            return
        }

        viewModelScope.launch {
            categoryUseCases.deleteCategory(category)
            loadCategories()
        }
    }

}