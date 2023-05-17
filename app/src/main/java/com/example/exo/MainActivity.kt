package com.example.exo

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.exo.ui.theme.ExoTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.controller.VideoPlayerControllerConfig
import io.sanghun.compose.video.uri.VideoPlayerMediaItem
import javax.sql.DataSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setContent {
            ExoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    ExoPlayerVideoPlayer(videoResId = R.raw.my_video)
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "startScreen"){
        composable("startScreen"){
            MyVideoPart(navController)
        }
        composable("lastScreen")
        {
            LastScreen()
        }
    }
}

@Composable
fun MyVideoPart(navController:NavController)
{
    val videoCompleted = remember { mutableStateOf(false) }


    Box(){
        VideoPlayer( mediaItems = listOf(
                VideoPlayerMediaItem.RawResourceMediaItem(
                    resourceId = R.raw.video
                )),
            handleLifecycle = true,
            autoPlay = true,
            usePlayerController = true,
            enablePip = true,
            handleAudioFocus = true,
            controllerConfig = VideoPlayerControllerConfig(
                showSpeedAndPitchOverlay = false,
                showSubtitleButton = false,
                showCurrentTimeAndTotalTime = true,
                showBufferingProgress = false,
                showForwardIncrementButton = true,
                showBackwardIncrementButton = true,
                showBackTrackButton = true,
                showNextTrackButton = true,
                showRepeatModeButton = true,
                controllerShowTimeMilliSeconds = 5_000,
                controllerAutoShow = true,
                showFullScreenButton = true
            ),
            volume = 0.5f,
            repeatMode = RepeatMode.NONE,
            onCurrentTimeChanged = {
                Log.e("CurrentTime", it.toString())
            },
            playerInstance = {
                addAnalyticsListener(
                    object : AnalyticsListener {
                        // player logger
                        override fun onPlayerStateChanged(
                            eventTime: AnalyticsListener.EventTime,
                            playWhenReady: Boolean,
                            playbackState: Int
                        ) {
                            if(playbackState == Player.STATE_ENDED){
                                videoCompleted.value=true
                            }
                        }

                    }
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            )

        // Check if videoCompleted is true and navigate to another screen
        LaunchedEffect(videoCompleted.value) {
            if (videoCompleted.value) {
                navController.navigate("lastScreen")
            }
        }
    }
}

@Composable
fun LastScreen(){
    Text(text = "Hi! Welcome to 3rd screen")
}

//@Composable
//fun ExoPlayerVideoPlayer(videoResId: Int) {
//    val context = LocalContext.current
////    val player = remember { SimpleExoPlayer.Builder(context).build() }
//    val player = ExoPlayer.Builder(context).build()
//
//    val playerView = remember { PlayerView(context) }
//    val videoUri = "rawresource://${context.packageName}/$videoResId"
//
//    LaunchedEffect(player) {
//        val mediaItem = MediaItem.fromUri(
//            RawResourceDataSource.buildRawResourceUri(R.raw.my_video)
//        )
//        player.setMediaItem(mediaItem)
//        player.prepare()
//        player.play()
//    }
//
//    AndroidView(factory = { playerView }) { view ->
//        view.player = player
//    }
//}

//@Composable
//fun MyVideoPlayer(){
//    val context= LocalContext.current
//
//    val exoPlayer= remember {
//        SimpleExoPlayer.Builder(context).build().apply {
//            val dataSourceFactory: androidx.media3.datasource.DataSource.Factory =
//                DefaultDataSourceFactory(
//                    context, Util.getUserAgent(context, context.packageName)
//                )
//            val mediaItem = MediaItem.fromUri("")
//            val source =
//                ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
//            this.setMediaSource(source)
//            this.prepare()
//        }
//    }
//    AndroidView(factory = {
//        PlayerView(it).apply{
//            player=exoPlayer
//            (player as SimpleExoPlayer).playWhenReady=true
//        }
//    })
//}
//
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExoTheme {
    }
}