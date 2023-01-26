package com.example.adygall2.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.adygall2.R
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.SoundButtonsBinding
import com.example.adygall2.domain.model.Source

class SoundButtonsView(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int,
    defResAttr: Int
): LinearLayout(context, attributeSet, defStyleAttr, defResAttr) {

    private val binding: SoundButtonsBinding
    private var soundsPlayer: SoundsPlayer? = null
    private var sound: Source? = null

    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        defResAttr: Int): this(context, attributeSet, defResAttr, 0)

    constructor(
        context: Context,
        attributeSet: AttributeSet?
    ): this(context, attributeSet, 0)

    constructor(context: Context): this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.sound_buttons, this, true)
        binding = SoundButtonsBinding.bind(this)
        soundsPlayer = SoundsPlayer(context)
        initButtons()
    }

    fun addSound(addedSound: Source) {
        sound = addedSound
    }

    private fun initButtons() {
        val playIcon = context.getDrawable(R.drawable.play_sound)
        val stopIcon = context.getDrawable(R.drawable.stop_play)

        binding.apply {
            soundsPlayer?.let { player ->
                playSoundButton.apply {
                    setOnClickListener {
                        if (player.isPlayingNow) {
                            player.stopPlay()
                            if (icon == stopIcon) {
                                icon = playIcon
                            }
                        } else {
                            player.normalPlaybackSpeed()
                            icon = stopIcon
                            sound?.let { player.playSound(it) }
                        }
                    }

                    player.setCompletionListener {
                        icon = playIcon
                        player.reset()
                    }
                }

                slowPlayButton.setOnClickListener {
                    if (player.isPlayingNow) {
                        player.stopPlay()
                    } else {
                        player.slowPlaybackSpeed()
                        sound?.let { player.playSound(it) }
                    }
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        soundsPlayer = null
    }
}