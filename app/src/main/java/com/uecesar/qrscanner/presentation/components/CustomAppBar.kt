package com.uecesar.qrscanner.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    title: String,
    onNavigateBack: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null
){
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            onNavigateBack
        },
        actions = {
            actions
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun Previa(){
    CustomAppBar(title = "Title")
}