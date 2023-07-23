package com.zhengzhou.cashflow.ui.manageCategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ManageCategoriesUiState(
    val isLoading: Boolean = true,
    val listCategories: List<Category> = listOf()
)

class ManageCategoriesViewModel : ViewModel() {

    private var jobLoadCategories: Job

    private var _uiState = MutableStateFlow(ManageCategoriesUiState())
    val uiState: StateFlow<ManageCategoriesUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    init {
        jobLoadCategories = loadCategories()
    }

    private fun loadCategories(): Job {
        return viewModelScope.launch {
            repository.getCategoryList().collect {
                _uiState.value = uiState.value.copy(
                    listCategories = it,
                    isLoading = false
                )
            }
        }
    }

}