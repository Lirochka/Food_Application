package com.example.foodapplication.di

import com.example.foodapplication.cache.RecipeDao
import com.example.foodapplication.cache.model.RecipeEntityMapper
import com.example.foodapplication.interactors.recipe.GetRecipe
import com.example.foodapplication.interactors.recipe_list.RestoreRecipes
import com.example.foodapplication.interactors.recipe_list.SearchRecipes
import com.example.foodapplication.network.RecipeService
import com.example.foodapplication.network.model.RecipeDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideSearchRecipe(
        recipeService: RecipeService,
        recipeDao: RecipeDao,
        recipeEntityMapper: RecipeEntityMapper,
        recipeDtoMapper: RecipeDtoMapper,
    ): SearchRecipes {
        return SearchRecipes(
            recipeService = recipeService,
            recipeDao = recipeDao,
            entityMapper = recipeEntityMapper,
            dtoMapper = recipeDtoMapper,
        )
    }

    @ViewModelScoped
    @Provides
    fun provideRestoreRecipes(
        recipeDao: RecipeDao,
        recipeEntityMapper: RecipeEntityMapper,
    ): RestoreRecipes {
        return RestoreRecipes(
            recipeDao = recipeDao,
            entityMapper = recipeEntityMapper,
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetRecipe(
        recipeDao: RecipeDao,
        recipeEntityMapper: RecipeEntityMapper,
        recipeService: RecipeService,
        recipeDtoMapper: RecipeDtoMapper,
    ): GetRecipe {
        return GetRecipe(
            recipeDao = recipeDao,
            entityMapper = recipeEntityMapper,
            recipeService = recipeService,
            recipeDtoMapper = recipeDtoMapper,
        )
    }
}
