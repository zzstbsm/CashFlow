package com.zhengzhou.cashflow.ui.manageCategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ManageCategoriesUiState(
    val isLoading: Boolean = true,
    val listCategories: List<Category> = listOf(),

    val transactionType: TransactionType = TransactionType.Expense,
    val openCategory: Category? = null,
)

class ManageCategoriesViewModel : ViewModel() {

    private var jobLoadCategories: Job

    private var writingOnUiState: Boolean = false

    private var _uiState = MutableStateFlow(ManageCategoriesUiState())
    val uiState: StateFlow<ManageCategoriesUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    init {
        jobLoadCategories = loadCategories()
    }

    private fun setUiState(
        isLoading: Boolean? = null,
        listCategories: List<Category>? = null,
        transactionType: TransactionType? = null,
        openCategory: Category? = null,

        actOnOpenCategory: Boolean = false
    ) {
        viewModelScope.launch {
            while (writingOnUiState) delay(5)
            writingOnUiState = true

            val openCategoryToUpdate = if (actOnOpenCategory && uiState.value.openCategory != null) null
                else openCategory ?: uiState.value.openCategory

            _uiState.value = ManageCategoriesUiState(
                isLoading = isLoading ?: uiState.value.isLoading,
                listCategories =  listCategories ?: uiState.value.listCategories,
                transactionType = transactionType ?: uiState.value.transactionType,
                openCategory = openCategoryToUpdate
            )
            writingOnUiState = false
        }
    }

    private fun loadCategories(): Job {
        return viewModelScope.launch {
            repository.getCategoryList().collect { categoryList ->
                setUiState(
                    listCategories = categoryList.sortedBy { category: Category -> category.name },
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
        setUiState(
            openCategory = category,
            actOnOpenCategory = true,
        )
    }
    fun editCategory(category: Category) {
        viewModelScope.launch {
            repository.updateCategory(category = category)
            loadCategories()
        }
    }

}