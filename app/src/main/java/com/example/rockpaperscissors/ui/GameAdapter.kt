package com.example.rockpaperscissors.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissors.R
import com.example.rockpaperscissors.model.Game

class GameAdapter(private val games: List<Game>) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_game,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])
    }

    // Hier maken we variables aan, context is de huidige "staat" van de applicatie.
    // De andere zijn gebonden aan de UI elements.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context: Context = itemView.context.applicationContext
        private val tvResult: TextView = itemView.findViewById(R.id.tvResult)
        private val tvDate: TextView = itemView.findViewById(R.id.tvTime)
        private val ivComputer: ImageView = itemView.findViewById(R.id.ivPC)
        private val ivYou: ImageView = itemView.findViewById(R.id.ivHuman)

        // in this when statement we check what the result of the game is.
        // -> is used as a condition
        fun bind(game: Game) {
            when (game.result) {
                Game.Result.LOSE -> tvResult.text = context.getString(R.string.lose)
                Game.Result.DRAW -> tvResult.text = context.getString(R.string.draw)
                Game.Result.WIN -> tvResult.text = context.getString(R.string.win)
            }

            // Here we fill the date with a string of the current date.
            // We also set the images of rpc based on the current state (last choices)
            tvDate.text = game.date.toString()
            ivComputer.setImageDrawable(itemView.context.applicationContext.getDrawable(game.pcChoice))
            ivYou.setImageDrawable(itemView.context.applicationContext.getDrawable(game.humanChoice))
        }
    }
}