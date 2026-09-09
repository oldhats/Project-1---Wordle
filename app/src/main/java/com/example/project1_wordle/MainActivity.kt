package com.example.project1_wordle

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.github.jinatonic.confetti.CommonConfetti
import android.graphics.Color

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find UI elements by their ID
        val inputField = findViewById<EditText>(R.id.inputField)
        val sendButton = findViewById<Button>(R.id.guessButton)
        val displayText = findViewById<TextView>(R.id.displayText)
        val answerText = findViewById<TextView>(R.id.answerView)
        val resetBtn = findViewById<Button>(R.id.resetBtn)
        val rootLayout = findViewById<ViewGroup>(R.id.main)
        answerText.isVisible = false

        var guessCount = 0
        val history = StringBuilder()



        sendButton.setOnClickListener {
            val userInput = inputField.text.toString().uppercase()

            // Make sure user input is 4 letters
            if (userInput.length != 4) {
                inputField.error = "Must be 4 letters"
                return@setOnClickListener
            }


            guessCount++

            val guessResult = checkGuess(userInput)

            if (guessCount < 7) {            // Build the new lines for this guess

            // Cant figure out how to get confetti to show
                if (userInput == wordToGuess){
                    CommonConfetti.rainingConfetti(rootLayout,
                        intArrayOf(Color.YELLOW, Color.GREEN, Color.MAGENTA))
                    }

                val newEntry =
                    "Guess #$guessCount \t\t\t\t\t\t\t\t\t\t\t\t\t\t $userInput\nGuess #$guessCount  Check \t\t\t $guessResult\n"

                // Append to history
                history.append(newEntry)

                // Update the TextView
                displayText.text = history.toString()
                answerText.text = "$wordToGuess"

                answerText.isVisible = true

            } else {
                answerText.text = "$wordToGuess"
                answerText.isVisible = true
            }
        }

        resetBtn.setOnClickListener {
            guessCount = 0
            history.clear()
            displayText.text = history.toString()
            wordToGuess = FourLetterWordList.getRandomFourLetterWord()
            answerText.isVisible = false
        }

    }

}



/**
 * Parameters / Fields:
 *   wordToGuess : String - the target word the user is trying to guess
 *   guess : String - what the user entered as their guess
 *
 * Returns a String of 'O', '+', and 'X', where:
 *   'O' represents the right letter in the right place
 *   '+' represents the right letter in the wrong place
 *   'X' represents a letter not in the target word
 */

var wordToGuess = FourLetterWordList.getRandomFourLetterWord()



private fun checkGuess(guess: String) : String {
    var result = ""
    for (i in 0..3) {
        if (guess[i] == wordToGuess[i]) {
            result += "O"
        }
        else if (guess[i] in wordToGuess) {
            result += "+"
        }
        else {
            result += "X"
        }
    }
    return result
}