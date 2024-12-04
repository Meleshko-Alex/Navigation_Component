package com.meleha.navcomponent.ui.screens.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meleha.navcomponent.model.LoadResult
import com.meleha.navcomponent.ui.components.LoadResultContent
import com.meleha.navcomponent.ui.screens.ItemsGraph
import com.meleha.navcomponent.ui.screens.LocalNavController
import com.meleha.navcomponent.ui.screens.items.ItemsViewModel.*

@Composable
fun ItemsScreen() {
    val viewModel: ItemsViewModel = hiltViewModel()
    val screenState = viewModel.stateFlow.collectAsState()
    val navController = LocalNavController.current
    ItemsContent(
        getLoadResult = { screenState.value },
        onItemClicked = { index ->
            navController.navigate(ItemsGraph.EditItemRoute(index))
        }
    )
}

@Composable
fun ItemsContent(
    getLoadResult: () -> LoadResult<ScreenState>,
    onItemClicked: (Int) -> Unit,
) {
    LoadResultContent(
        loadResult = getLoadResult(),
        content = { screenState ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(screenState.items) { index, item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .clickable { onItemClicked(index) }
                            .fillMaxWidth()
                            .padding(12.dp),
                    )
                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ItemsPreview() {
    ItemsContent(
        getLoadResult = { LoadResult.Loading },
        onItemClicked = {},
    )
}