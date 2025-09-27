package com.uecesar.qrscanner.presentation.screen.generate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uecesar.qrscanner.domain.model.QrCodeContent
import com.uecesar.qrscanner.presentation.components.CustomAppBar
import com.uecesar.qrscanner.presentation.components.TextForm
import com.uecesar.qrscanner.presentation.ui.enumerable.QrCodeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    onNavigateBack: () -> Unit,
    viewModel: GenerateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType

    Scaffold(
        topBar = {
            CustomAppBar(
                title = "Generate QR",
                onNavigateBack = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }

            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Select QR Code Type",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            item {
                FilterChipList(selectedType){ viewModel.onSelectedTypeChange(selectedType) }
            }
            item {
                when (selectedType) {
                    QrCodeType.TEXT -> TextForm(
                        keyboardType = KeyboardType.Text,
                        label = "Texto"
                    ) { content ->
                        viewModel.generateQrCode(QrCodeContent.Text(content))
                    }
                    QrCodeType.PHONE -> TextForm(
                        keyboardType = KeyboardType.Phone,
                        label = "Número de teléfono"
                    ) { number ->
                        viewModel.generateQrCode(QrCodeContent.Phone(number))
                    }
                    QrCodeType.URL -> TextForm(
                        keyboardType = KeyboardType.Uri,
                        label = "URL"
                    ) { url ->
                        viewModel.generateQrCode(QrCodeContent.Url(url))
                    }
                }
            }
            item {
                when (val state = uiState) {
                    is GenerateUiState.Loading -> { LoadingState() }
                    is GenerateUiState.Success -> { SuccessState(state) }
                    is GenerateUiState.Error -> { ErrorState(state) }
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun FilterChipList( selectedType: QrCodeType, onSelectedTypeChange: () -> Unit){
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(QrCodeType.entries.toTypedArray()) { type ->
            FilterChip(
                onClick = onSelectedTypeChange,
                label = { Text(type.name) },
                selected = selectedType == type,
                leadingIcon = {
                    Icon(
                        when (type) {
                            QrCodeType.PHONE -> Icons.Default.Phone
                            QrCodeType.URL -> Icons.Default.Link
                            else -> Icons.Default.TextFields
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun LoadingState(){
    Card {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Generating QR code...")
        }
    }
}

@Composable
private fun SuccessState(state: GenerateUiState.Success){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "QR Code Generated!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "ID: ${state.qrCode.id}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Content: ${state.qrCode.content.take(100)}${if (state.qrCode.content.length > 100) "..." else ""}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ErrorState(state: GenerateUiState.Error){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                state.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}