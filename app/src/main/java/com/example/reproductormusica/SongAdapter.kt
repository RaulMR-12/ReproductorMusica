package com.example.reproductormusica

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.SeekBar
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils



class SongAdapter(private val context: Context, private val songList: List<Song>) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var volumeChangeListener: OnVolumeChangeListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        //holder.textViewTitle.text = song.title
        animateTextChange(holder.textViewTitle, song.title)

        setAnimation(holder.itemView, position)

        holder.buttonPlay.setOnClickListener{
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, song.audioResourceId)
            mediaPlayer?.start()
            animateButton(holder.buttonPlay)
        }

        holder.buttonPause.setOnClickListener{
            mediaPlayer?.pause()
            animateButton(holder.buttonPause)
        }

        holder.buttonStop.setOnClickListener{
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            animateButton(holder.buttonStop)
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

    fun animateButton(button: Button) {
        val scaleX = ValueAnimator.ofFloat(1f, 1.2f, 1f)
        scaleX.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            button.scaleX = value
        }

        val scaleY = ValueAnimator.ofFloat(1f, 1.2f, 1f)
        scaleY.addUpdateListener {  animation ->
            val value = animation.animatedValue as Float
            button.scaleY = value
        }

        val colorAnimator = ValueAnimator.ofArgb(Color.BLUE, Color.MAGENTA, Color.BLUE)
        colorAnimator.addUpdateListener { animation ->
            button.setBackgroundColor(animation.animatedValue as Int)
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, colorAnimator)
        animatorSet.duration = 300
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.start()
    }

    fun animateTextChange(textView: TextView, newText: String) {
       val fadeOut = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f)
        fadeOut.duration = 200
        fadeOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                textView.text = newText
                val fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f)
                fadeIn.duration = 200
                fadeIn.start()
            }
        })
        fadeOut.start()
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
        viewToAnimate.startAnimation(animation)
    }

}
