package com.example.foodapplication.presentation.navigation

sealed class Screen(
    val route: String,
) {
    object RecipeList : Screen("recipeList")

    object RecipeDetail : Screen("recipeDetail")
}