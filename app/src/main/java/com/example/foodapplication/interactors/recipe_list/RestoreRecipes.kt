package com.example.foodapplication.interactors.recipe_list

import com.example.foodapplication.cache.RecipeDao
import com.example.foodapplication.cache.model.RecipeEntityMapper
import com.example.foodapplication.domain.data.DataState
import com.example.foodapplication.domain.model.Recipe
import com.example.foodapplication.util.RECIPE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RestoreRecipes(
    private val recipeDao: RecipeDao,
    private val entityMapper: RecipeEntityMapper,
) {
    fun execute(
        page: Int,
        query: String,
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading())

            // just to show pagination, api is fast
            delay(1000)

            val cacheResult = if (query.isBlank()) {
                recipeDao.restoreAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.restoreRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            val list = entityMapper.fromEntityList(cacheResult)
            emit(DataState.success(list))

        } catch (e: Exception) {
            emit(DataState.error<List<Recipe>>(e.message ?: "Unknown Error"))
        }
    }
}