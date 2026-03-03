package org.delcom.pam_p4_ifs23020.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23020.R
import org.delcom.pam_p4_ifs23020.helper.ConstHelper
import org.delcom.pam_p4_ifs23020.helper.RouteHelper
import org.delcom.pam_p4_ifs23020.helper.SuspendHelper
import org.delcom.pam_p4_ifs23020.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23020.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23020.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23020.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetActionUIState
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetViewModel

@Composable
fun PlanetsAddScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    planetViewModel: PlanetViewModel
) {
    val uiState by planetViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        uiState.planetAction = PlanetActionUIState.Loading
    }

    LaunchedEffect(uiState.planetAction) {
        when (val state = uiState.planetAction) {
            is PlanetActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.SUCCESS, state.message)
                RouteHelper.to(navController, ConstHelper.RouteNames.Planets.path, true)
                isLoading = false
            }
            is PlanetActionUIState.Error -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.ERROR, state.message)
                isLoading = false
            }
            else -> {}
        }
    }

    if (isLoading) { LoadingUI(); return }

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        TopAppBarComponent(navController = navController, title = "Tambah Planet", showBackButton = true)
        Box(modifier = Modifier.weight(1f)) {
            PlanetsAddUI(
                onSave = {
                    namaPlanet,
                    deskripsi,
                    jarak,
                    diameter,
                    satelit,
                    tipe,
                    context,
                    file
                    ->

                    isLoading = true
                    val filePart = uriToMultipart(context, file, "file")

                    planetViewModel.postPlanet(
                        namaPlanet,
                        deskripsi,
                        jarak,
                        diameter,
                        satelit,
                        tipe,
                        filePart
                    )
                }
            )
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun PlanetsAddUI(
    onSave: (String, String, String, String, Int, String, Context, Uri) -> Unit
) {
    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var namaPlanet by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var jarakDariMatahari by remember { mutableStateOf("") }
    var diameter by remember { mutableStateOf("") }
    var jumlahSatelit by remember { mutableStateOf("") }
    var tipePlanet by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf("") }
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        dataFile = uri
    }

    val focusManager = LocalFocusManager.current
    val f2 = remember { FocusRequester() }
    val f3 = remember { FocusRequester() }
    val f4 = remember { FocusRequester() }
    val f5 = remember { FocusRequester() }
    val f6 = remember { FocusRequester() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("🪐", style = MaterialTheme.typography.displaySmall)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Data Planet Baru",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            if (showError.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = showError,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // File Gambar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            )
            {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable {
                            imagePicker.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (dataFile != null) {
                        AsyncImage(
                            model = dataFile,
                            contentDescription = "Pratinjau Gambar",
                            placeholder = painterResource(R.drawable.img_placeholder),
                            error = painterResource(R.drawable.img_placeholder),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = "Pilih Gambar",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tap untuk mengganti gambar",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            PlanetTextField(value = namaPlanet, onValueChange = { namaPlanet = it },
                label = "Nama Planet", imeAction = ImeAction.Next,
                onNext = { f2.requestFocus() })

            PlanetTextField(value = deskripsi, onValueChange = { deskripsi = it },
                label = "Deskripsi", imeAction = ImeAction.Next, minLines = 3, maxLines = 5,
                modifier = Modifier.height(120.dp).focusRequester(f2),
                onNext = { f3.requestFocus() })

            PlanetTextField(value = jarakDariMatahari, onValueChange = { jarakDariMatahari = it },
                label = "Jarak dari Matahari (contoh: 149.6 juta km)",
                imeAction = ImeAction.Next,
                modifier = Modifier.focusRequester(f3),
                onNext = { f4.requestFocus() })

            PlanetTextField(value = diameter, onValueChange = { diameter = it },
                label = "Diameter (contoh: 12.742 km)",
                imeAction = ImeAction.Next,
                modifier = Modifier.focusRequester(f4),
                onNext = { f5.requestFocus() })

            PlanetTextField(value = jumlahSatelit, onValueChange = { jumlahSatelit = it },
                label = "Jumlah Satelit", imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.focusRequester(f5),
                onNext = { f6.requestFocus() })

            PlanetTextField(value = tipePlanet, onValueChange = { tipePlanet = it },
                label = "Tipe Planet (contoh: Terestrial, Gas Giant)",
                imeAction = ImeAction.Done,
                modifier = Modifier.focusRequester(f6),
                onDone = { focusManager.clearFocus() })

            Spacer(modifier = Modifier.height(64.dp))
        }

        FloatingActionButton(
            onClick = {
                when {
                    namaPlanet.isBlank() -> showError = "Nama planet tidak boleh kosong!"
                    deskripsi.isBlank() -> showError = "Deskripsi tidak boleh kosong!"
                    jarakDariMatahari.isBlank() -> showError = "Jarak dari matahari tidak boleh kosong!"
                    diameter.isBlank() -> showError = "Diameter tidak boleh kosong!"
                    jumlahSatelit.isBlank() -> showError = "Jumlah satelit tidak boleh kosong!"
                    tipePlanet.isBlank() -> showError = "Tipe planet tidak boleh kosong!"
                    dataFile == null -> showError = "File harus dipilih!"
                    else -> {
                        showError = ""
                        onSave(namaPlanet, deskripsi, jarakDariMatahari, diameter,
                            jumlahSatelit.toIntOrNull() ?: 0, tipePlanet, context, dataFile!!)
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Save, contentDescription = "Simpan")
        }
    }
}

@Composable
fun PlanetTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    maxLines: Int = 1,
    modifier: Modifier = Modifier,
    onNext: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        modifier = Modifier.fillMaxWidth().then(modifier),
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = { onNext?.invoke() },
            onDone = { onDone?.invoke() }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}