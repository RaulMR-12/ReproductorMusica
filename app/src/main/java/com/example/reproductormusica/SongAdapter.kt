package com.example.reproductormusica

import android.media.MediaPlayer
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.SeekBar

class SongAdapter(private val context: Context, private val songList: List<Song>) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var volumeChangeListener: OnVolumeChangeListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.textViewTitle.text = song.title

        holder.buttonPlay.setOnClickListener{
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, song.audioResourceId)
            mediaPlayer?.start()
        }

        holder.buttonPause.setOnClickListener{
            mediaPlayer?.pause()
        }

        holder.buttonStop.setOnClickListener{
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }

        holder.volumeSeekBar.progress = 100
        holder.volumeSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mediaPlayer?.setVolume(progress / 100f, progress / 100f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun getItemCount(): Int{
        return songList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.song_title)
        val buttonPlay: Button = itemView.findViewById(R.id.button_play)
        val buttonPause: Button = itemView.findViewById(R.id.button_pause)
        val buttonStop: Button = itemView.findViewById(R.id.button_stop)
        val volumeSeekBar: SeekBar = itemView.findViewById(R.id.volume_slider)
    }

    interface OnVolumeChangeListener {
        fun onVolumeChanged(position: Int, volume: Int)
    }

    fun setOnVolumeChangeListener(listener: OnVolumeChangeListener) {
        this.volumeChangeListener = listener
    }

    fun getMediaPlayer(position: Int): MediaPlayer? {
        return if (position >= 0 && position < songList.size) {
            MediaPlayer.create(context, songList[position].audioResourceId)
        } else {
            null
        }
    }
}