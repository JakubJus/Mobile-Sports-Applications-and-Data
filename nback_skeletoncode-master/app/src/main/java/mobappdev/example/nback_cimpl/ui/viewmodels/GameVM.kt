package mobappdev.example.nback_cimpl.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import mobappdev.example.nback_cimpl.utils.mapNumberToLetter
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.NBackHelper
import mobappdev.example.nback_cimpl.data.UserPreferencesRepository
import android.content.Context
import android.content.SharedPreferences
import android.speech.tts.TextToSpeech
/**
 * This is the GameViewModel.
 *
 * It is good practice to first make an interface, which acts as the blueprint
 * for your implementation. With this interface we can create fake versions
 * of the viewmodel, which we can use to test other parts of our app that depend on the VM.
 *
 * Our viewmodel itself has functions to start a game, to specify a gametype,
 * and to check if we are having a match
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


interface GameViewModel {
    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val nBack: Int

    fun setGameType(gameType: GameType)
    fun startGame()

    fun checkMatch()

}
@SuppressLint("StaticFieldLeak")
class GameVM(
    private val userPreferencesRepository: UserPreferencesRepository,
    application: Application
): GameViewModel, ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()

    private val context = application.applicationContext
    private val tts by lazy {
        TextToSpeech(context){ status ->
            if (status == TextToSpeech.ERROR){
                Log.e("ViewModel", "TTS not working")
                return@TextToSpeech
            }
        }
    }

    private val highScoreKey = "high_score_key"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("game_preferences", Context.MODE_PRIVATE)

    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score

    private val _highscore = MutableStateFlow(0)
    override val highscore: StateFlow<Int>
        get() = _highscore


    // nBack is currently hardcoded
    override val nBack: Int =  gameState.value.nback

    private var job: Job? = null  // coroutine job for the game event
    private val eventInterval: Long = 1500L  // 2000 ms (2s)

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var events = emptyArray<Int>()  // Array with all events

    override fun setGameType(gameType: GameType) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }

    override fun startGame() {
        job?.cancel()  // Cancel any existing game loop

        // Get the events from our C-model (returns IntArray, so we need to convert to Array<Int>)
        events = nBackHelper.generateNBackString(gameState.value.maxSize, gameState.value.sizeOfGame*gameState.value.sizeOfGame, 30, gameState.value.nback).toList().toTypedArray()  // Todo Higher Grade: currently the size etc. are hardcoded, make these based on user input
        Log.d("GameVM", "The following sequence was generated: ${events.contentToString()}")

        // reset score
        _gameState.value = _gameState.value.copy(currentScore = 0)
        _gameState.value = _gameState.value.copy(currentState = -1)
        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame(events)
                GameType.AudioVisual -> runAudioVisualGame(events)
                GameType.Visual -> runVisualGame(events)
            }

            // Update high score if the current score is higher
            if (_highscore.value < _gameState.value.currentScore) {
                _highscore.value = _gameState.value.currentScore
                saveHighScore()  // Save the high score to SharedPreferences
            }
        }
    }



    override fun checkMatch() {
        if(gameState.value.eventNumber >= gameState.value.nback) {
            if (events[gameState.value.eventNumber] == events[gameState.value.eventNumber - gameState.value.nback]) {
                if(_gameState.value.checkIfAlreadyMatched) {
                    _gameState.value.checkIfAlreadyMatched=false
                    _gameState.value.currentScore = _gameState.value.currentScore + 1
                    _gameState.value.currentState=1
                }else {
                    if(_gameState.value.currentScore>0) {
                        _gameState.value.currentScore=_gameState.value.currentScore-1
                        _gameState.value.currentState=0

                    }
                }
            }else{
                if(_gameState.value.currentScore>0) {
                    _gameState.value.currentScore=_gameState.value.currentScore-1
                    _gameState.value.currentState=0
                }
            }
        }
    }
    private suspend fun runAudioGame(events: Array<Int>) {
        for ((index,value) in events.withIndex()) {
            _gameState.value = _gameState.value.copy(eventValue = value, eventNumber = index, checkIfAlreadyMatched = true)
            if (tts != null) {
                tts.speak(mapNumberToLetter(_gameState.value.eventValue), TextToSpeech.QUEUE_FLUSH,null,null)
            } else {
                Log.e("VM", "tts not working")
                return
            }
            delay(eventInterval)
        }
        _gameState.value = _gameState.value.copy(eventNumber  = 0)
        _gameState.value = _gameState.value.copy(eventValue = -1)
    }

    private suspend fun runVisualGame(events: Array<Int>){
        for ((index,value) in events.withIndex()) {
            _gameState.value.checkIfAlreadyMatched = true
            _gameState.value = _gameState.value.copy(eventValue = value, eventNumber = index, checkIfAlreadyMatched = true)
            delay(eventInterval)

            _gameState.value = _gameState.value.copy(eventValue = 0)

            delay(eventInterval/3)


        }
        // Todo: Replace this code for actual game code
        _gameState.value = _gameState.value.copy(eventValue = -1)
        _gameState.value = _gameState.value.copy(eventNumber  = 0)

    }

    private suspend fun runAudioVisualGame(events: Array<Int>){
        for ((index,value) in events.withIndex()) {

            if (tts != null) {
                tts.speak(mapNumberToLetter(value), TextToSpeech.QUEUE_FLUSH,null,null)
            } else {
                Log.e("VM", "tts not working")
                return
            }
            _gameState.value = _gameState.value.copy(eventValue = value, eventNumber = index, checkIfAlreadyMatched = true)
            delay(eventInterval)
            _gameState.value = _gameState.value.copy(eventValue = 0)
            delay(eventInterval/3)
        }
        _gameState.value = _gameState.value.copy(eventNumber  = 0)
        _gameState.value = _gameState.value.copy(eventValue = -1)
    }


    private fun saveHighScore() {
        val editor = sharedPreferences.edit()
        editor.putInt(highScoreKey, _highscore.value)
        editor.apply()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(application.userPreferencesRespository, application)
            }
        }
    }

    init {
        // Code that runs during creation of the vm
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect {
                _highscore.value = it
            }
        }
        _highscore.value = sharedPreferences.getInt(highScoreKey, 0)
    }
}

// Class with the different game types
enum class GameType{
    Audio,
    Visual,
    AudioVisual
}

data class GameState(
    // You can use this state to push values from the VM to your UI.
    val gameType: GameType = GameType.Visual,  // Type of the game
    val eventValue: Int = -1,  // The value of the array string
    val eventNumber: Int = 0,
    var maxSize: Int = 10,
    var sizeOfGame: Int =3,
    var checkIfAlreadyMatched: Boolean = false,
    var currentScore: Int = 0,
    var nback: Int = 1,
    var currentState: Int=-1
)

class FakeVM: GameViewModel{
    override val gameState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val score: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val highscore: StateFlow<Int>
        get() = MutableStateFlow(42).asStateFlow()
    override val nBack: Int
        get() =  gameState.value.nback

    override fun setGameType(gameType: GameType) {
    }

    override fun startGame() {
    }

    override fun checkMatch() {
    }
}
