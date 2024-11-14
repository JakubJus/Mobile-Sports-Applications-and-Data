package mobappdev.example.nback_cimpl.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel


@SuppressLint("RememberReturnType")
@Composable
fun Settings(
    vm: GameViewModel,
    navigateToHome: () -> Unit,
) {
    val gameState by vm.gameState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var duration by remember { mutableStateOf(gameState.maxSize) }
    var countN by remember { mutableStateOf(gameState.nback) }
    var countSize by remember { mutableStateOf(gameState.sizeOfGame) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingCard(
                title = "GRID: $countSize x $countSize",
                onIncrease = {
                    if (gameState.sizeOfGame < 10) {
                        gameState.sizeOfGame++
                        countSize++
                    }
                },
                onDecrease = {
                    if (gameState.sizeOfGame > 1) {
                        gameState.sizeOfGame--
                        countSize--
                    }
                }
            )

            SettingCard(
                title = "NUMBER OF EVENTS:",
                onIncrease = {
                    if (gameState.sizeOfGame < 10) {
                        gameState.sizeOfGame++
                        countSize++
                    }
                },
                onDecrease = {
                    if (gameState.sizeOfGame > 1) {
                        gameState.sizeOfGame--
                        countSize--
                    }
                }
            )

            SettingCard(
                title = "Time between events",
                onIncrease = {
                    if (gameState.sizeOfGame < 10) {
                        gameState.sizeOfGame++
                        countSize++
                    }
                },
                onDecrease = {
                    if (gameState.sizeOfGame > 1) {
                        gameState.sizeOfGame--
                        countSize--
                    }
                }
            )

            SettingCard(
                title = "N = $countN",
                onIncrease = {
                    gameState.nback++
                    countN++
                },
                onDecrease = {
                    if (gameState.nback > 1) {
                        gameState.nback--
                        countN--
                    }
                }
            )

            SettingCard(
                title = "Number of events $duration",
                onIncrease = {
                    if (gameState.maxSize < 50) {
                        gameState.maxSize++
                        duration++
                    }
                },
                onDecrease = {
                    if (gameState.maxSize > 2) {
                        gameState.maxSize--
                        duration--
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = navigateToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF84911)
                )
            ) {
                Text(text = "Home", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun SettingCard(
    title: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2F5))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onIncrease,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF701ECC))
                ) {
                    Text(text = "More")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDecrease,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF701ECC))
                ) {
                    Text(text = "Less")
                }
            }
        }
    }
}
