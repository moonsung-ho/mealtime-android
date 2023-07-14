package com.sungho0205.geupsik.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sungho0205.geupsik.data.School
import com.sungho0205.geupsik.data.searchSchools

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var textFieldState by remember { mutableStateOf("") }
    val schools = remember { mutableStateListOf<School>() }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = textFieldState, onValueChange = {
                textFieldState = it
            }, placeholder = { Text("정확히 입력해주세요") }, singleLine = true)
            Spacer(modifier = Modifier.height(8.dp))
            Text("입력한 텍스트: $textFieldState")
            Button(onClick = { searchSchools(query = "영신", result = schools) }) {
                Text("검색")
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 10.dp),
            ) {
                items(schools) {
                    Card(modifier = Modifier.fillMaxWidth(), elevation = 10.dp) {
                        Row() {
                            Text(it.SCHUL_NM)
                        }
                    }
                }
            }
        }
    }
}