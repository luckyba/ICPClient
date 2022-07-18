// IMusicService.aidl
package com.lucky.icp;

// Declare any non-default types here with import statements

interface IMusicService {
    String getSongName();

    void changeMediaStatus();

    void playSong();

    void play();

    void pause();

    void stopService();

    int getCurrentDuration();

    int getTotalDuration();
}
