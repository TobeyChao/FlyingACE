package com.soul.flyingace;


import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

class SoundPlayer {
    private Context context;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> map;

    @SuppressLint("UseSparseArrays")
    SoundPlayer(Context context) {
        this.context = context;
        map = new HashMap<>();
        AudioAttributes audioAttributes;
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(16)
                .setAudioAttributes(audioAttributes)
                .build();
    }

    void initGameSound() {
        map.put(1, soundPool.load(context, R.raw.fireplayer, 1));
        map.put(2, soundPool.load(context, R.raw.explosion, 1));
        map.put(3, soundPool.load(context, R.raw.explosion01, 1));
        map.put(4, soundPool.load(context, R.raw.choose, 1));
    }

    //播放音效
    void playSound(int sound, int loop) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        assert am != null;
        float stramVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float stramMaxVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volume = stramVolumeCurrent / stramMaxVolumeCurrent;
        soundPool.play(map.get(sound), volume, volume, 1, loop, 1.0f);
    }
}