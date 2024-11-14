package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

/**
 * This is the Home screen composable
 *
 * Currently this screen shows the saved highscore
 * It also contains a button which can be used to show that the C-integration works
 * Furthermore it contains two buttons that you can use to start a game
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */

@Composable
fun HomeScreen(
    vm: GameViewModel,
    navigateToGame: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val highscore by vm.highscore.collectAsState()
    val gameState by vm.gameState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp,),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Button(
                    onClick = {
                        navigateToSettings()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB1C9D5)
                    )
                ) {
                    Text(text = "Settings")
                }
                Spacer(modifier = Modifier.width(80.dp)) // Add spacing between "Settings" button and "High-Score" text
                Text(
                    text = "High-Score\n $highscore",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Start Game".uppercase(),
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            navigateToGame()
                            vm.setGameType(GameType.Audio)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB1C9D5)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.sound_on),
                            contentDescription = "Sound",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f),
                        )
                    }

                    Button(
                        onClick = {
                            navigateToGame()
                            vm.setGameType(GameType.AudioVisual)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB1C9D5)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.vis_audio),
                            contentDescription = "Sound",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f),
                        )
                    }

                    Button(
                        onClick = {
                            navigateToGame()
                            vm.setGameType(GameType.Visual)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB1C9D5)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.visual),
                            contentDescription = "Visual",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f)
                        )
                    }
                }
            }
        }
    }
}
