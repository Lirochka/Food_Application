package com.example.foodapplication.interactors.recipe_list

import com.example.foodapplication.cache.RecipeDao
import com.example.foodapplication.cache.model.RecipeEntityMapper
import com.example.foodapplication.domain.data.DataState
import com.example.foodapplication.domain.model.Recipe
import com.example.foodapplication.network.RecipeService
import com.example.foodapplication.network.model.RecipeDtoMapper
import com.example.foodapplication.util.RECIPE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRecipes(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper,
) {

    fun execute(
        token: String,
        page: Int,
        query: String,
        b: Boolean,
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading())

            if (query == "error") {
                throw Exception("Search FAILED!")
            }

            try {
                // Convert: NetworkRecipeEntity -> Recipe -> RecipeCacheEntity
                val recipes = getRecipesFromNetwork(
                    token = token,
                    page = page,
                    query = query,
                )

                recipeDao.insertRecipes(entityMapper.toEntityList(recipes))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val cacheResult = if (query.isBlank()) {
                recipeDao.getAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.searchRecipes(
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

    private suspend fun getRecipesFromNetwork(
        token: String,
        page: Int,
        query: String,
    ): List<Recipe> {
        return dtoMapper.toDomainList(
            recipeService.search(
                token = token,
                page = page,
                query = query,
            ).recipes
        )
    }
}