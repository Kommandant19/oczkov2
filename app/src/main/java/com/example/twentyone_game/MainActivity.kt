package com.example.twentyone_game

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.logging.Handler
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var player1Score = 0
    private var player2Score = 0
    private var currentPlayer = 1
    private var player1Cards = mutableListOf<Int>()
    private var player2Cards = mutableListOf<Int>()
    private val playerColors = mutableMapOf(1 to mutableSetOf<String>(), 2 to mutableSetOf<String>())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val drawButton: Button = findViewById(R.id.draw_button)
        val passButton: Button = findViewById(R.id.pass_button)
        val surrenderButton: Button = findViewById(R.id.surrender_button)
        val scoreTextView: TextView = findViewById(R.id.score)

        drawButton.setOnClickListener { drawCard() }
        passButton.setOnClickListener { passTurn() }
        surrenderButton.setOnClickListener { surrender() }
    }

    private fun drawCard() {
        var cardValue: Int
        var suit: String

        do {
            cardValue = Random.nextInt(1, 14)
            suit = getSuit(cardValue)
        } while (playerColors[currentPlayer]?.contains(suit) == true ||
            (currentPlayer == 1 && player1Cards.contains(cardValue)) ||
            (currentPlayer == 2 && player2Cards.contains(cardValue)))

        // Handle card value mapping
        val value = when {
            cardValue in 2..10 -> cardValue
            cardValue == 11 -> 2
            cardValue == 12 -> 4
            cardValue == 13 -> 11
            cardValue == 1 -> 11

            else -> {
                Toast.makeText(this, "Error: Invalid card value", Toast.LENGTH_SHORT).show()
                return
            }
        }

        if (currentPlayer == 1) {
            player1Score += value
            player1Cards.add(cardValue)
            playerColors[1]?.add(suit)
            updatePlayerUI(1, player1Cards.size - 1, cardValue)

            if (player1Cards.size >= 2 && player1Cards.takeLast(2).all { it == 1 }) {
                Toast.makeText(this, "Player 1 Wins with Oczko!", Toast.LENGTH_SHORT).show()
                resetGame()
                return
            }

            if (player1Score > 21) {
                showResultAndReset("Player 1 Bust! Player 2 Wins!")
            } else {
                currentPlayer = 2
                Toast.makeText(this, "Player 2's turn", Toast.LENGTH_SHORT).show()
            }
        } else {
            player2Score += value
            player2Cards.add(cardValue)
            playerColors[2]?.add(suit)
            updatePlayerUI(2, player2Cards.size - 1, cardValue)

            if (player2Cards.size >= 2 && player2Cards.takeLast(2).all { it == 1 }) {
                Toast.makeText(this, "Player 2 Wins with Oczko!", Toast.LENGTH_SHORT).show()
                resetGame()
                return
            }

            if (player2Score > 21) {
                showResultAndReset("Player 2 Bust! Player 1 Wins!")
            } else {
                currentPlayer = 1
                Toast.makeText(this, "Player 1's turn", Toast.LENGTH_SHORT).show()
            }
        }
        updateScoreDisplay()
    }

    private fun getSuit(cardValue: Int): String {
        return when (cardValue) {
            in 1..13 -> "hearts"
            in 14..26 -> "diamonds"
            in 27..39 -> "clubs"
            in 40..52 -> "spades"
            else -> ""
        }
    }

    private fun showResultAndReset(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        android.os.Handler().postDelayed({
            resetGame()
        }, 2000)
    }

    private fun updatePlayerUI(player: Int, cardIndex: Int, cardValue: Int) {
        // sprawdzamy czy karta jest w zakresie 0-5 czyli 6 kart
        if (cardIndex < 0 || cardIndex > 5) return

        // jaki kolor ma karta
        val suit = getSuit(cardValue)

        // mapujemy karty na obrazki w zależności od koloru i wartości
        val cardImageRes = when {
            suit == "hearts" -> when (cardValue) {
                1 -> R.drawable.hace
                2 -> R.drawable.h2
                3 -> R.drawable.h3
                4 -> R.drawable.h4
                5 -> R.drawable.h5
                6 -> R.drawable.h6
                7 -> R.drawable.h7
                8 -> R.drawable.h8
                9 -> R.drawable.h9
                10 -> R.drawable.h10
                11 -> R.drawable.hj
                12 -> R.drawable.hq
                13 -> R.drawable.hk

                else -> 0
            }
            suit == "diamonds" -> when (cardValue) {
                1 -> R.drawable.dace
                2 -> R.drawable.d2
                3 -> R.drawable.d3
                4 -> R.drawable.d4
                5 -> R.drawable.d5
                6 -> R.drawable.d6
                7 -> R.drawable.d7
                8 -> R.drawable.d8
                9 -> R.drawable.d9
                10 -> R.drawable.d10
                11 -> R.drawable.dj
                12 -> R.drawable.dq
                13 -> R.drawable.dk
                else -> 0
            }
            suit == "clubs" -> when (cardValue) {
                1 -> R.drawable.ca
                2 -> R.drawable.c2
                3 -> R.drawable.c3
                4 -> R.drawable.c4
                5 -> R.drawable.c5
                6 -> R.drawable.c6
                7 -> R.drawable.c7
                8 -> R.drawable.c8
                9 -> R.drawable.c9
                10 -> R.drawable.c10
                11 -> R.drawable.cj
                12 -> R.drawable.cq
                13 -> R.drawable.ck
                else -> 0
            }
            suit == "spades" -> when (cardValue) {
                1 -> R.drawable.sa
                2 -> R.drawable.s2
                3 -> R.drawable.s3
                4 -> R.drawable.s4
                5 -> R.drawable.s5
                6 -> R.drawable.s6
                7 -> R.drawable.s7
                8 -> R.drawable.s8
                9 -> R.drawable.s9
                10 -> R.drawable.s10
                11 -> R.drawable.sj
                12 -> R.drawable.sq
                13 -> R.drawable.sk

                else -> 0
            }
            else -> 0
        }
        val imageViewId = when (player) {
            1 -> when (cardIndex) {
                0 -> R.id.p1_img1
                1 -> R.id.p1_img2
                2 -> R.id.p1_img3
                3 -> R.id.p1_img4
                4 -> R.id.p1_img5
                5 -> R.id.p1_img6

                else -> return
            }
            2 -> when (cardIndex) {
                0 -> R.id.p2_img1
                1 -> R.id.p2_img2
                2 -> R.id.p2_img3
                3 -> R.id.p2_img4
                4 -> R.id.p2_img5
                5 -> R.id.p2_img6
                else -> return
            }
            else -> return
        }

        // upewnia się, że obrazek jest widoczny
        findViewById<ImageView>(imageViewId)?.visibility = ImageView.VISIBLE

        // zmienna imageViewId jest przypisana do obrazka karty
        findViewById<ImageView>(imageViewId)?.setImageResource(cardImageRes)
    }

    private fun updateScoreDisplay() {
        val scoreTextView: TextView = findViewById(R.id.score)
        scoreTextView.text = "Score: Player 1: $player1Score - Player 2: $player2Score"
    }

    private fun passTurn() {
        currentPlayer = if (currentPlayer == 1) 2 else 1
        Toast.makeText(this, "Player $currentPlayer's turn", Toast.LENGTH_SHORT).show()
    }

    private fun surrender() {
        Toast.makeText(this, "Player $currentPlayer surrendered! Player ${if (currentPlayer == 1) 2 else 1} Wins!", Toast.LENGTH_SHORT).show()
        resetGame()
    }

    private fun resetGame() {
        player1Score = 0
        player2Score = 0
        currentPlayer = 1
        player1Cards.clear()
        player2Cards.clear()
        clearPlayerImages()
        updateScoreDisplay()
    }

    private fun clearPlayerImages() {
        val player1Images = listOf(
            findViewById<ImageView>(R.id.p1_img1),
            findViewById<ImageView>(R.id.p1_img2),
            findViewById<ImageView>(R.id.p1_img3),
            findViewById<ImageView>(R.id.p1_img4),
            findViewById<ImageView>(R.id.p1_img5),
            findViewById<ImageView>(R.id.p1_img6)
        )

        val player2Images = listOf(
            findViewById<ImageView>(R.id.p2_img1),
            findViewById<ImageView>(R.id.p2_img2),
            findViewById<ImageView>(R.id.p2_img3),
            findViewById<ImageView>(R.id.p2_img4),
            findViewById<ImageView>(R.id.p2_img5),
            findViewById<ImageView>(R.id.p2_img6)
        )

        player1Images.forEach { it.setImageResource(0) }
        player2Images.forEach { it.setImageResource(0) }
    }
}