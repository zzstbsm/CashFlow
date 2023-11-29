package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces

import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.AddCategoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.DeleteCategoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.GetCategoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.GetCategoryListInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.GetCategoryOccurrencesInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.UpdateCategoryInterface

interface CategoryUseCasesInterface: AddCategoryInterface,
        DeleteCategoryInterface,
        GetCategoryInterface,
        GetCategoryListInterface,
        GetCategoryOccurrencesInterface,
        UpdateCategoryInterface