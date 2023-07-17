package com.sungho0205.geupsik.ui.setting

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.*
import com.sungho0205.geupsik.service.searchSchools
import com.sungho0205.geupsik.ui.NavigationActions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingSchoolScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    var query by remember { mutableStateOf("") }
    val schools = remember { mutableStateListOf<School>() }
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val (region, setRegion) = remember { mutableStateOf(Regions.All) }

    fun showRegionSelector() {
        scope.launch {
            sheetState.apply {
                show()
            }
        }
    }

    fun hideRegionSelector() {
        scope.launch {
            sheetState.apply {
                hide()
            }
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            RegionSelector(
                selectedRegion = region,
                setRegion = setRegion,
                hideRegionSelector = { hideRegionSelector() }
            )
        }, sheetState = sheetState
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row() {
                    Button(onClick = {
                        showRegionSelector()
                    }) {
                        Text(region.label)
                    }
                    TextField(value = query, onValueChange = {
                        query = it
                    }, placeholder = { Text("학교 이름으로 검색하세요") }, singleLine = true)
                    Button(onClick = {
                        searchSchools(
                            query = query,
                            region = region.value,
                            result = schools
                        )
                    }) {
                        Column() {
                            Row(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(20.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = "학교 검색",
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }
                        }
                    }
                }
                Button(onClick = { navigationActions.navigateToSetting() }) {
                    Text("뒤로가기")
                }

                Text("학교 이름: ${data.schulNm}")
                Text("검색 결과 : ${schools.size}")
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 10.dp),
                ) {
                    items(schools) {
                        Card(modifier = Modifier.fillMaxWidth(), elevation = 10.dp, onClick = {
                            settingsViewModel.updateSchool(
                                atptOfcdcScCode = it.ATPT_OFCDC_SC_CODE,
                                sdSchulCode = it.SD_SCHUL_CODE,
                                schulNm = it.SCHUL_NM,
                            )
                        }) {
                            Row() {
                                Text(it.SCHUL_NM)
                            }
                        }
                    }
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

    Box() {
        Column() {
            regions.forEach {
                Row(modifier = Modifier.clickable {
                    setRegion(it)
                    hideRegionSelector()
                }) {
                    Text(it.label)
                    if (selectedRegion == it) {
                        Icon(Icons.Filled.Check, contentDescription = "지역 선택")
                    }
                }
            }
        }
    }
}