package com.sungho0205.geupsik.ui.setting

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.SettingsViewModel
import com.sungho0205.geupsik.model.Regions
import com.sungho0205.geupsik.model.School
import com.sungho0205.geupsik.service.searchSchools
import com.sungho0205.geupsik.ui.NavigationActions

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingSchoolScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    var context = LocalContext.current
    var openDialog = remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val schools = remember { mutableStateListOf<School>() }
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value
    val (region, setRegion) = remember { mutableStateOf(Regions.All) }
    val keyboardController = LocalSoftwareKeyboardController.current

    fun showRegionSelector() {
        openDialog.value = true
    }

    fun hideRegionSelector() {
        openDialog.value = false
    }

    val animatedProgress by animateFloatAsState(
        targetValue = settingsViewModel.fetchProgress.value,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    Scaffold(topBar = {
        TopAppBar(title = { Text("학교 설정") }, navigationIcon = {
            IconButton(onClick = { navigationActions.navigateToSetting() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
            }
        })
    }) { innerPadding ->
        if (settingsViewModel.fetchProgress.value > 0.0f) {
            LinearProgressIndicator(
                modifier = Modifier
                    .semantics(mergeDescendants = true) {}
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(horizontal = 0.dp)
                    .offset(x = 0.dp, y = 0.dp),
                progress = animatedProgress,
            )
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .absoluteOffset(y = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier
                        .width(60.dp)
                        .height(48.dp), onClick = {
                        keyboardController?.hide()
                        showRegionSelector()
                    }, shape = RoundedCornerShape(
                        topStart = 8.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 8.dp
                    ), contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(region.label)
                }
                BasicTextField(value = query,
                    onValueChange = {
                        query = it
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(Color.LightGray),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (query.isEmpty()) {
                                Text("학교 이름으로 검색하세요", fontSize = 14.sp)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            innerTextField()
                        }
                    })
                Button(
                    modifier = Modifier
                        .width(60.dp)
                        .height(48.dp), onClick = {
                        if (query.isNotEmpty()) {
                            keyboardController?.hide()
                            searchSchools(
                                query = query,
                                region = region.value,
                                result = schools,
                                progress = settingsViewModel.fetchProgress

                            )
                        } else {
                            Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }, shape = RoundedCornerShape(
                        topStart = 0.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 0.dp
                    ), contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "학교 검색",
                        tint = Color.White,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
            ) {
                items(schools) {
                    Column {
                        ListItem(modifier = Modifier.clickable {
                            settingsViewModel.updateSchool(
                                atptOfcdcScCode = it.ATPT_OFCDC_SC_CODE,
                                sdSchulCode = it.SD_SCHUL_CODE,
                                schulNm = it.SCHUL_NM,
                            )
                            navigationActions.navigateToSettingGradeClass()
                        }, headlineText = { Text(it.SCHUL_NM) }, trailingContent = {
                            Icon(
                                Icons.Filled.KeyboardArrowRight,
                                contentDescription = "학년 반 설정",
                            )
                        })
                        Divider()
                    }
                }
            }
        }
    }
    if (openDialog.value) {
        AlertDialog(onDismissRequest = { openDialog.value = false }) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .heightIn(max = 480.dp),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    RegionSelector(selectedRegion = region,
                        setRegion = setRegion,
                        hideRegionSelector = { hideRegionSelector() })
                }
            }
        }
    }
}

@Composable
fun RegionSelector(
    selectedRegion: Regions, setRegion: (Regions) -> Unit, hideRegionSelector: () -> Unit
) {
    val regions = enumValues<Regions>()
    var scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        regions.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(selected = selectedRegion == it, onClick = {
                        setRegion(it)
                        hideRegionSelector()
                    }), verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedRegion == it,
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = it.label,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}