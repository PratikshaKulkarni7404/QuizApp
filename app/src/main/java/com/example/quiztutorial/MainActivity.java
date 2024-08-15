package com.example.quiztutorial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView optionA, optionB, optionC, optionD;
    private TextView questionNumber, questionText, scoreText, timerText;
    private Button skipButton, hintButton;
    private ProgressBar progressBar;

    private int currentIndex = 0;
    private int mscore = 0;
    private int qn = 1;
    private CountDownTimer countDownTimer;

    private int CurrentQuestion, CurrentOptionA, CurrentOptionB, CurrentOptionC, CurrentOptionD;

    private answerclass[] questionBank = new answerclass[]{
            new answerclass(R.string.question_1, R.string.question_1A, R.string.question_1B, R.string.question_1C, R.string.question_1D, R.string.question_1A),
            new answerclass(R.string.question_2, R.string.question_2A, R.string.question_2B, R.string.question_2C, R.string.question_2D, R.string.question_2A),
            new answerclass(R.string.question_3, R.string.question_3A, R.string.question_3B, R.string.question_3C, R.string.question_3D, R.string.question_3B),
            new answerclass(R.string.question_4, R.string.question_4A, R.string.question_4B, R.string.question_4C, R.string.question_4D, R.string.question_4C),
            new answerclass(R.string.question_5, R.string.question_5A, R.string.question_5B, R.string.question_5C, R.string.question_5D, R.string.question_5A),
            new answerclass(R.string.question_6, R.string.question_6A, R.string.question_6B, R.string.question_6C, R.string.question_6D, R.string.question_6B),
            new answerclass(R.string.question_7, R.string.question_7A, R.string.question_7B, R.string.question_7C, R.string.question_7D, R.string.question_7D),
            new answerclass(R.string.question_8, R.string.question_8A, R.string.question_8B, R.string.question_8C, R.string.question_8D, R.string.question_8C)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        questionText = findViewById(R.id.question);
        scoreText = findViewById(R.id.score);
        questionNumber = findViewById(R.id.QuestionNumber);
        timerText = findViewById(R.id.timer);
        progressBar = findViewById(R.id.progress_bar);
        skipButton = findViewById(R.id.skipButton);
        hintButton = findViewById(R.id.hintButton);

        loadQuestion();

        optionA.setOnClickListener(v -> checkAnswer(CurrentOptionA));
        optionB.setOnClickListener(v -> checkAnswer(CurrentOptionB));
        optionC.setOnClickListener(v -> checkAnswer(CurrentOptionC));
        optionD.setOnClickListener(v -> checkAnswer(CurrentOptionD));
        skipButton.setOnClickListener(v -> skipQuestion());
        hintButton.setOnClickListener(v -> provideHint());
    }

    private void loadQuestion() {
        if (currentIndex < questionBank.length) {
            CurrentQuestion = questionBank[currentIndex].getQuestionid();
            questionText.setText(CurrentQuestion);
            CurrentOptionA = questionBank[currentIndex].getOptionA();
            optionA.setText(CurrentOptionA);
            CurrentOptionB = questionBank[currentIndex].getOptionB();
            optionB.setText(CurrentOptionB);
            CurrentOptionC = questionBank[currentIndex].getOptionC();
            optionC.setText(CurrentOptionC);
            CurrentOptionD = questionBank[currentIndex].getOptionD();
            optionD.setText(CurrentOptionD);

            questionNumber.setText(String.format("Question %d/%d", qn, questionBank.length));

            // Calculate progress as a percentage
            int progress = (int) (((double) (currentIndex + 1) / questionBank.length) * 100);
            progressBar.setProgress(progress);

            startTimer();
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateQuestion() {
        currentIndex++;
        qn++;

        if (currentIndex >= questionBank.length) {
            endQuiz();
        } else {
            loadQuestion();
            scoreText.setText("Score: " + mscore + "/" + questionBank.length);
        }
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.format("Time Left: %d", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Time's up! Moving to the next question.", Toast.LENGTH_SHORT).show();
                updateQuestion();
            }
        }.start();
    }

    private void checkAnswer(int userSelection) {
        int correctAnswer = questionBank[currentIndex].getAnswerid();

        if (userSelection == correctAnswer) {
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
            mscore++;
        } else {
            Toast.makeText(getApplicationContext(), "Wrong! The correct answer is: " + getString(correctAnswer), Toast.LENGTH_LONG).show();
        }
        updateQuestion();
    }

    private void skipQuestion() {
        updateQuestion();
    }

    private void provideHint() {
        if (currentIndex < questionBank.length) {
            int correctOption = questionBank[currentIndex].getAnswerid();
            List<Integer> options = new ArrayList<>();
            List<TextView> optionViews = new ArrayList<>();

            options.add(CurrentOptionA);
            options.add(CurrentOptionB);
            options.add(CurrentOptionC);
            options.add(CurrentOptionD);
            optionViews.add(optionA);
            optionViews.add(optionB);
            optionViews.add(optionC);
            optionViews.add(optionD);

            int wrongOptionsRemoved = 0;
            Random random = new Random();

            while (wrongOptionsRemoved < 2) {
                int index = random.nextInt(options.size());
                int option = options.get(index);

                if (option != correctOption) {
                    optionViews.get(index).setText("");
                    options.remove(index);
                    optionViews.remove(index);
                    wrongOptionsRemoved++;
                }
            }

            hintButton.setEnabled(false);
        }
    }

    private void endQuiz() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Quiz Finished");
        alert.setMessage("Your Score: " + mscore + " out of " + questionBank.length + "\n" + getFeedback());
        alert.setPositiveButton("Close", (dialog, which) -> finish());
        alert.setNegativeButton("Retry", (dialog, which) -> restartQuiz());
        alert.setCancelable(false);
        alert.show();
    }

    private String getFeedback() {
        if (mscore == questionBank.length) {
            return "Excellent! You got all the questions right!";
        } else if (mscore >= questionBank.length / 2) {
            return "Good job! You passed the quiz.";
        } else {
            return "Better luck next time. Keep practicing!";
        }
    }

    private void restartQuiz() {
        mscore = 0;
        currentIndex = 0;
        qn = 1;
        progressBar.setProgress(0);
        scoreText.setText("Score: 0/" + questionBank.length);
        timerText.setText("Time Left: 30");
        loadQuestion();
    }
}
