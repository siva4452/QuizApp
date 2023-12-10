package com.example.quizapp
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var feedbackTextView: TextView

    private val questions = listOf(
        Question("What is the capital of France?", listOf("Berlin", "Paris", "Madrid"), 1),
        Question("Which planet is known as the Red Planet?", listOf("Venus", "Mars", "Jupiter"), 1)
        // Add more questions as needed
    )

    private var currentQuestionIndex = 0
    private val userAnswers = mutableMapOf<Int, Int>() // Map to store user answers (questionIndex -> selectedOptionIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.questionTextView)
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup)
        submitButton = findViewById(R.id.submitButton)
        feedbackTextView = findViewById(R.id.feedbackTextView)

        displayQuestion()

        submitButton.setOnClickListener {
            checkAnswer()
        }
    }

    private fun displayQuestion() {
        val currentQuestion = questions[currentQuestionIndex]

        questionTextView.text = currentQuestion.text

        optionsRadioGroup.removeAllViews()

        currentQuestion.options.forEachIndexed { index, option ->
            val radioButton = RadioButton(this)
            radioButton.text = option
            radioButton.id = index
            optionsRadioGroup.addView(radioButton)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkAnswer() {
        val selectedOptionIndex = optionsRadioGroup.checkedRadioButtonId
        val currentQuestion = questions[currentQuestionIndex]

        if (selectedOptionIndex == -1) {
            // No option selected
            showFeedback("Please select an option.")
        } else {
            // Store the user's answer
            userAnswers[currentQuestionIndex] = selectedOptionIndex
            // Display feedback
            if (selectedOptionIndex == currentQuestion.correctOptionIndex) {
                showFeedback("Question ${currentQuestionIndex + 1}: Correct!")
            } else {
                showFeedback("Question ${currentQuestionIndex + 1}: Incorrect. The correct answer is ${currentQuestion.options[currentQuestion.correctOptionIndex]}.")
            }
        }

        // Move to the next question or finish the quiz
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            displayQuestion()
        } else {
            // End of the quiz
            submitButton.isEnabled = false
            questionTextView.text = "Quiz completed!"
            optionsRadioGroup.removeAllViews()
            displayResults()
        }
    }

    private fun showFeedback(message: String) {
        feedbackTextView.append("$message\n")
    }

    private fun displayResults() {
        feedbackTextView.append("\n\nResults:\n")
        for ((questionIndex, selectedOptionIndex) in userAnswers) {
            val currentQuestion = questions[questionIndex]
            val isCorrect = selectedOptionIndex == currentQuestion.correctOptionIndex
            val resultMessage = if (isCorrect) "Correct" else "Incorrect"
            feedbackTextView.append("Question ${questionIndex + 1}: $resultMessage\n")
        }
    }
}
