package com.example.rockpaperscissors.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.rockpaperscissors.R
import com.example.rockpaperscissors.database.GameRepository
import com.example.rockpaperscissors.model.Game

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        gameRepository = GameRepository(this)
        initViews()
    }

    // here we detect which Choice the player has made by using onClickListeners.
    private fun initViews() {
        ibRock.setOnClickListener { onPlayerChoice(Game.Choice.ROCK) }
        ibPaper.setOnClickListener { onPlayerChoice(Game.Choice.PAPER) }
        ibScissors.setOnClickListener { onPlayerChoice(Game.Choice.SCISSORS) }
        getStatistics()
    }

    private fun onPlayerChoice(playerChoice: Game.Choice) {
        // Randomize one of the three choices to display for the pc.
        // we already use the on click to see the players choice.
        val computerChoice = Game.Choice.values().random()
        // Set images
        ivPC.setImageDrawable(getDrawable(getChoiceDrawableId(computerChoice)))
        ivHuman.setImageDrawable(getDrawable(getChoiceDrawableId(playerChoice)))
        // Create game / fill the data class
        val game = Game(
            date = Date(),
            humanChoice = getChoiceDrawableId(playerChoice),
            pcChoice = getChoiceDrawableId(computerChoice),
            result = getGameResult(playerChoice, computerChoice)
        )
        // Set result text based on a when with conditional statements.
        when {
            game.result == Game.Result.WIN -> tvResult.text = getString(R.string.win)
            game.result == Game.Result.DRAW -> tvResult.text = getString(R.string.draw)
            game.result == Game.Result.LOSE -> tvResult.text = getString(R.string.lose)
        }
        addGameToDatabase(game)
    }

    // "calculate" who has won
    private fun getGameResult(playerChoice: Game.Choice, computerChoice: Game.Choice): Game.Result {
        if (playerChoice == computerChoice) {
            return Game.Result.DRAW
        }

        return if (
            playerChoice == Game.Choice.ROCK && computerChoice == Game.Choice.SCISSORS ||
            playerChoice == Game.Choice.PAPER && computerChoice == Game.Choice.ROCK ||
            playerChoice == Game.Choice.SCISSORS && computerChoice == Game.Choice.PAPER
        ) {
            Game.Result.WIN
        } else Game.Result.LOSE
    }

    // select the image to display based on the game choice.
    private fun getChoiceDrawableId(choice: Game.Choice): Int {
        return when (choice) {
            Game.Choice.ROCK -> R.drawable.rock
            Game.Choice.PAPER -> R.drawable.paper
            Game.Choice.SCISSORS -> R.drawable.scissors
        }
    }

    // here we make use of a .IO to optimize desk / network changes.
    // we insert the game object into the database.
    private fun addGameToDatabase(game: Game) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
                getStatistics()
            }
        }
    }

    // here we instantiate the stat variables.
    // we then fill them based on the amount of wins / draws / losses in the database.
    private fun getStatistics() {
        CoroutineScope(Dispatchers.Main).launch {
            var wins = 0
            var draws = 0
            var losses = 0
            withContext(Dispatchers.IO) {
                wins = gameRepository.getNumberOfWins()
                draws = gameRepository.getNumberOfDraws()
                losses = gameRepository.getNumberOfLosses()
            }
            tvScore.text = getString(R.string.stats, wins, draws, losses)
        }
    }

    // start the history with an intent of the HistoryActivity
    private fun startHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        getStatistics()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // display the history icon instead of the garbage icon in the last app.
    // we also make use of ifRoom, otherwise it'll show 3 dots.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as human specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_show_history -> {
                startHistoryActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
