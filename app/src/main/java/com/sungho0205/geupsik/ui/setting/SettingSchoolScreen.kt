package com.sungho0205.geupsik.ui.setting

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val schools = remember { mutableStateListOf<School>() }
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
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "fetching_progress_setting_school"
    )

    Scaffold(topBar = {
        TopAppBar(title = { Text("학교 설정하기") }, navigationIcon = {
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
                val focusManager = LocalFocusManager.current

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
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.contentColorFor(
                            MaterialTheme.colorScheme.background
                        )
                    ),
                    onValueChange = {
                        query = it
                    },
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        searchSchools(
                            query = query.trim(),
                            region = region.value,
                            result = schools,
                            progress = settingsViewModel.fetchProgress

                        )
                    }),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (query.isEmpty()) {
                                Text("학교를 찾을 수 없어요.", fontSize = 14.sp)
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
                                query = query.trim(),
                                region = region.value,
                                result = schools,
                                progress = settingsViewModel.fetchProgress

                            )
                        } else {
                            Toast.makeText(context, "학교 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }, shape = RoundedCornerShape(
                        topStart = 0.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 0.dp
                    ), contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "학교 검색하기",
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
                        }, headlineText = { Column {
                            Text(it.SCHUL_NM)
                            Text(it.ORG_RDNMA, fontSize = 14.sp, maxLines = 1, color = Color.Gray )
                        } }, trailingContent = {
                            Icon(
                                Icons.Filled.KeyboardArrowRight,
                                contentDescription = "학년/반 설정",
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
    val scrollState = rememberScrollState()

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