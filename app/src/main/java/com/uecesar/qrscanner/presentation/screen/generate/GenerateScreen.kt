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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Wifi
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uecesar.qrscanner.presentation.ui.enumerable.QrCodeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    onNavigateBack: () -> Unit,
    viewModel: GenerateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedType by remember { mutableStateOf(QrCodeType.TEXT) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate QR") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(QrCodeType.entries.toTypedArray()) { type ->
                        FilterChip(
                            onClick = { selectedType = type },
                            label = { Text(type.name) },
                            selected = selectedType == type,
                            leadingIcon = {
                                Icon(
                                    when (type) {
                                        QrCodeType.EMAIL -> Icons.Default.Email
                                        QrCodeType.PHONE -> Icons.Default.Phone
                                        QrCodeType.URL -> Icons.Default.Link
                                        QrCodeType.WIFI -> Icons.Default.Wifi
                                        QrCodeType.CONTACT -> Icons.Default.Person
                                        QrCodeType.LOCATION -> Icons.Default.LocationOn
                                        QrCodeType.SMS -> Icons.Default.Sms
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

//            item {
//                when (selectedType) {
//                    QrCodeType.TEXT -> TextForm { content ->
//                        viewModel.generateQrCode(QrCodeContent.Text(content))
//                    }
//                    QrCodeType.EMAIL -> EmailForm { email, subject, body ->
//                        viewModel.generateQrCode(QrCodeContent.Email(email, subject, body))
//                    }
//                    QrCodeType.PHONE -> PhoneForm { number ->
//                        viewModel.generateQrCode(QrCodeContent.Phone(number))
//                    }
//                    QrCodeType.URL -> UrlForm { url ->
//                        viewModel.generateQrCode(QrCodeContent.Url(url))
//                    }
//                    QrCodeType.WIFI -> WifiForm { ssid, password, security ->
//                        viewModel.generateQrCode(QrCodeContent.Wifi(ssid, password, security))
//                    }
//                    QrCodeType.CONTACT -> ContactForm { name, phone, email ->
//                        viewModel.generateQrCode(QrCodeContent.Contact(name, phone, email))
//                    }
//                    QrCodeType.LOCATION -> LocationForm { lat, lng ->
//                        viewModel.generateQrCode(QrCodeContent.Location(lat, lng))
//                    }
//                    QrCodeType.SMS -> SmsForm { number, message ->
//                        viewModel.generateQrCode(QrCodeContent.Sms(number, message))
//                    }
//                }
//            }

            // Show generation status
            when (val state = uiState) {
                is GenerateUiState.Loading -> {
                    item {
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
                }
                is GenerateUiState.Success -> {
                    item {
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
                }
                is GenerateUiState.Error -> {
                    item {
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
                }
                else -> {}
            }
        }
    }
}