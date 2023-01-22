package com.example.android.unscramble.ui.test

import com.example.android.unscramble.data.MAX_NO_OF_WORDS
import com.example.android.unscramble.data.SCORE_INCREASE
import com.example.android.unscramble.data.getUnscrambledWord
import com.example.android.unscramble.ui.GameViewModel
import junit.framework.TestCase.*
import org.junit.Test


class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambleWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectPlayerWord = "and"

        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiState.value

        assertEquals(currentGameUiState.score, SCORE_AFTER_WRONG_ANSWER_FIRST)
        assertTrue(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUiState = viewModel.uiState.value
        val unScrambleWord = getUnscrambledWord(gameUiState.currentScrambleWord)

        assertNotSame(unScrambleWord, gameUiState.currentScrambleWord)
        assertEquals(gameUiState.currentWordCount,0)
        assertFalse(gameUiState.isGameOver)
        assertTrue(gameUiState.score == 0)
        assertFalse(gameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentUiState = viewModel.uiState.value
        var currentPlayerWord = getUnscrambledWord(currentUiState.currentScrambleWord)

        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE

            viewModel.updateUserGuess(currentPlayerWord)
            viewModel.checkUserGuess()

            currentUiState = viewModel.uiState.value
            currentPlayerWord = getUnscrambledWord(currentUiState.currentScrambleWord)

            assertEquals(expectedScore, currentUiState.score)
        }

//        assertEquals(expectedScore, MAX_NO_OF_WORDS* SCORE_INCREASE)
        assertEquals(MAX_NO_OF_WORDS, currentUiState.currentWordCount)
        assertTrue(currentUiState.isGameOver)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        var currentUiState = viewModel.uiState.value
        val currentPlayerWord = getUnscrambledWord(currentUiState.currentScrambleWord)
        viewModel.updateUserGuess(currentPlayerWord)
        viewModel.checkUserGuess()

        currentUiState = viewModel.uiState.value
        val lastWordCount = currentUiState.currentWordCount
        viewModel.skipWord()

        currentUiState = viewModel.uiState.value
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentUiState.score)
        assertEquals(currentUiState.currentWordCount, lastWordCount+1)
    }

        companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
        private const val SCORE_AFTER_WRONG_ANSWER_FIRST = 0
    }
}