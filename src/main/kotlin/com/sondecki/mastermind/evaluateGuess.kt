package com.sondecki.mastermind

import java.lang.StringBuilder

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    fun removeRightPositions(secret: String, guess: String) : Pair<String, String> {
        val length = guess.length
        val newSecret = StringBuilder()
        val newGuess = StringBuilder()

        for (position in 0 until length) {
            val selectedGuessLetter = guess[position]
            val selectedSecretLetter = secret[position]
            if (selectedGuessLetter != selectedSecretLetter) {
                newGuess.append(selectedGuessLetter)
                newSecret.append(selectedSecretLetter)
            }
        }
        return newSecret.toString() to newGuess.toString()
    }

    fun calculateWrongPosition(secret: String, guess: String) : Int {
        val length = guess.length
        val guessedLetters = mutableSetOf<Char>()
        var wrongPosition = 0
        for (position in 0 until length) {
            val selectedGuessLetter = guess[position]
            if (selectedGuessLetter !in guessedLetters) {
                val secretCount = secret.count { it == selectedGuessLetter }
                val guessCount = guess.count { it == selectedGuessLetter }
                wrongPosition += if(secretCount > guessCount) guessCount else secretCount
                guessedLetters.add(selectedGuessLetter)
            }
        }
        return wrongPosition
    }

    val calculateRightPosition: (String, Pair<String, String>) -> Int =
            {secret: String, notGuessedPositions: Pair<String, String> -> secret.length - notGuessedPositions.first.length}

    val notGuessedPositions = removeRightPositions(secret, guess)
    val wrongPosition = calculateWrongPosition(notGuessedPositions.first, notGuessedPositions.second)

    return Evaluation(calculateRightPosition(secret, notGuessedPositions), wrongPosition)
}