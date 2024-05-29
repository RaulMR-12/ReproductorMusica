package com.example.reproductormusica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter
    private lateinit var songList: MutableList<Song>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        songList = mutableListOf()
        songList.add(Song("See You Again - Tyler the Creator ft. Kali Uchis", R.raw.see_you_again))
        songList.add(Song("Circles - Post Malone", R.raw.circles))
        songList.add(Song("Sunflower - Post Malone & Swae Lee", R.raw.sunflower))
        songList.add(Song("Paradox - Survive Said The Prophet", R.raw.paradox))
        songList.add(Song("SpecialZ - King Gnu", R.raw.specialz))
        songList.add(Song("Father Stretch My Hands Pt1 - Kanye West", R.raw.father))
        songList.add(Song("Earfquake - Tyler The Creator", R.raw.earfquake))
        songList.add(Song("Carnival - Kanye West & TY Dolla Sign", R.raw.carnival))

        songAdapter = SongAdapter(this, songList)
        recyclerView.adapter = songAdapter

        songAdapter.setOnVolumeChangeListener(object : SongAdapter.OnVolumeChangeListener {
            override fun onVolumeChanged(position: Int, volume: Int) {
                val mediaPlayer = songAdapter.getMediaPlayer(position)
                mediaPlayer?.setVolume(volume / 100f, volume / 100f)
            }
        })
    }
}