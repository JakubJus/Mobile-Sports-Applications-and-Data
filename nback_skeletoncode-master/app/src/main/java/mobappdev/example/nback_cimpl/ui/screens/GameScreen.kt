
package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
fun GameScreen(
    vm: GameViewModel
) {
    val highscore by vm.highscore.collectAsState()
    val gameState by vm.gameState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var score = gameState.currentScore

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            /*   text = "",
                    modifier = Modifier
                        .height(10.dp)
                        .width(300.dp)
                        .background(Color.LightGray)
                        .align(alignment = Alignment.Center)
                )*/

            Row(
                modifier = Modifier
                    .padding(horizontal = 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    modifier = Modifier.padding(32.dp),
                    text = "Score = ${gameState.currentScore}",
                    style = MaterialTheme.typography.headlineLarge
                )
                if (gameState.eventValue != -1) {

                    if (gameState.currentState == 0) {

                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_delete),
                            contentDescription = "Wrong",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f)
                                .background(Color.Red)
                        )
                    }
                }

                if (gameState.currentState == 1) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 0.dp)

                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_input_add),
                            contentDescription = "Ok",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f)
                                .background(Color.Green)
                        )
                    }
                }
            }
        }
        Text(
            text = "",
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth((gameState.eventNumber.toFloat() / gameState.maxSize.toFloat()))
                .background(Color.Blue)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(

                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(gameState.gameType==GameType.Visual ||gameState.gameType==GameType.AudioVisual )
                        if (gameState.eventValue != -1) {
                            for (i in 1..gameState.sizeOfGame) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    for (j in 1..gameState.sizeOfGame) {
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = 8.dp)
                                        ) {
                                            if (gameState.eventValue == (i - 1) * gameState.sizeOfGame + j) {
                                                Text(
                                                    text = "",
                                                    modifier = Modifier
                                                        .size((300/gameState.sizeOfGame.toFloat()).dp)
                                                        .background(Color.LightGray)
                                                        .align(alignment = Alignment.Center)
                                                )
                                            } else {
                                                Text(
                                                    text = "",
                                                    modifier = Modifier
                                                        .size((300/gameState.sizeOfGame.toFloat()).dp)
                                                        .background(Color.Blue)
                                                        .align(alignment = Alignment.Center)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (gameState.eventValue != -1) {

                            Button(
                                onClick = { vm.checkMatch() },
                                modifier = Modifier
                                    .height(100.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF84911)
                                )
                            )

                            {
                                if (gameState.eventValue != -1) {
                                    Text(text = "MATCH")
                                } else {
                                    Text(text = "START")
                                }
                            }
                        }else {
                            Button(
                                onClick = { vm.startGame() },
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(300.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF84911)
                                )
                            )

                            {
                                if (gameState.eventValue != -1) {
                                    Text(text = "MATCH")

                                } else {
                                    Text(text = "START")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun test() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface(){
        GameScreen(FakeVM())
    }
}
