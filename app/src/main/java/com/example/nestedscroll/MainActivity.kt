package com.example.nestedscroll

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nestedscroll.ui.theme.NestedScrollTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NestedScrollTheme {
                CollapsibleBoxScreen()

            }
        }
    }
}

@Composable
fun CollapsibleBoxScreen() {
    val maxBoxHeightDp = 300.dp
    val density = LocalDensity.current

    val listState = rememberLazyListState()

    val maxBoxHeightPx = with(density) { maxBoxHeightDp.toPx() }

    // Estado para a altura do Box em pixels
    var boxHeightPx by remember { mutableStateOf(maxBoxHeightPx) }

    // Definir o NestedScrollConnection
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                var consumedY = 0f

                // Ajustar a altura do Box apenas se o primeiro item estiver visível
                if (listState.firstVisibleItemScrollOffset == 0) {
                    val newHeight = (boxHeightPx + delta).coerceIn(0f, maxBoxHeightPx)
                    val consumed = newHeight - boxHeightPx
                    boxHeightPx = newHeight

                    // Se ajustamos a altura do Box, consumimos o scroll correspondente
                    if (consumed != 0f) {
                        consumedY = consumed
                    }
                }

                return Offset(0f, consumedY)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                var consumedY = 0f

                // Ajustar a altura do Box apenas se o primeiro item estiver visível
                if (listState.firstVisibleItemScrollOffset == 0) {
                    val newHeight = (boxHeightPx + delta).coerceIn(0f, maxBoxHeightPx)
                    val consumed = newHeight - boxHeightPx
                    boxHeightPx = newHeight

                    // Se ajustamos a altura do Box, consumimos o scroll correspondente
                    if (consumed != 0f) {
                        consumedY = consumed
                    }
                }

                return Offset(0f, consumedY)


            }
        }
    }

    // Converter a altura do Box para Dp
    val boxHeightDp = with(density) { boxHeightPx.toDp() }

    // Calcular o alfa com base na altura do Box
    val alpha = (boxHeightPx / maxBoxHeightPx).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.gato),
            contentDescription = "Teste",
            alpha = alpha, modifier = Modifier
                .height(boxHeightDp)
                .width(boxHeightDp)
                .align(Alignment.CenterHorizontally)
        )

        // LazyColumn com 20 itens
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(20) { index ->
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ícone no início do Card
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Título do Item $index",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Subtítulo do Item $index",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}