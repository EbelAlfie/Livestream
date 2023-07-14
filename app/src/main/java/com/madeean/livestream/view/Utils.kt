package com.madeean.livestream.view

import android.view.View

object Utils {
    val BASE_TEST_URL = "https://op-group1-swiftservehd-1.dens.tv/h/h114/S4/mnf.m3u8?app_type=web&userid=jjj&chname=ANIPLUS_HD"

    val BASE_RTMP_URL =
        "rtmp://0.tcp.ap.ngrok.io:/live/" //"https://livesim.dashif.org/livesim/chunkdur_1/ato_7/testpic4_8s/Manifest.mpd"//"rtmp://0.tcp.ap.ngrok.io:$port/live/"
    val BASE_HLS_URL =
        "https://1dcd6b126c49-12390209840656915252.ngrok-free.app/hls/"
        //"https://stmv1.srvif.com/animetv/animetv/playlist.m3u8"
        //"https://nhkwlive-ojp.akamaized.net/hls/live/2003459/nhkwlive-ojp-en/index.m3u8"

    fun viewDisplayMode(isDisplay: Boolean): Int =
        if (isDisplay) View.GONE else View.VISIBLE
}