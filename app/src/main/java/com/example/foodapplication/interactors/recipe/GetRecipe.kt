package com.example.foodapplication.interactors.recipe

import com.example.foodapplication.cache.RecipeDao
import com.example.foodapplication.cache.model.RecipeEntityMapper
import com.example.foodapplication.domain.data.DataState
import com.example.foodapplication.domain.model.Recipe
import com.example.foodapplication.network.RecipeService
import com.example.foodapplication.network.model.RecipeDtoMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Retrieve a recipe from the cache given it's unique id.
 */
class GetRecipe(
    private val recipeDao: RecipeDao,
    private val entityMapper: RecipeEntityMapper,
    private val recipeService: RecipeService,
    private val recipeDtoMapper: RecipeDtoMapper,
) {

    fun execute(
        recipeId: Int,
        token: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Recipe>> = flow {
        try {
            emit(DataState.loading())

            // just to show loading, cache is fast
            delay(1000)

            var recipe = getRecipeFromCache(recipeId = recipeId)

            if (recipe != null) {
                emit(DataState.success(recipe))
            }
            else {
                if (isNetworkAvailable) {
                    val networkRecipe = getRecipeFromNetwork(token, recipeId) // dto -> domain
                    recipeDao.insertRecipe(
                        entityMapper.mapFromDomainModel(networkRecipe)
                    )
                }
                recipe = getRecipeFromCache(recipeId = recipeId)

                if (recipe != null) {
                    emit(DataState.success(recipe))
                } else {
                    throw Exception("Unable to get recipe from the cache.")
                }
            }

        } catch (e: Exception) {
            emit(DataState.error<Recipe>(e.message ?: "Unknown Error"))
        }
    }

    private suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(recipeId)?.let { recipeEntity ->
            entityMapper.mapToDomainModel(recipeEntity)
        }
    }

    private suspend fun getRecipeFromNetwork(token: String, recipeId: Int): Recipe {
        return recipeDtoMapper.mapToDomainModel(recipeService.get(token, recipeId))
    }
}