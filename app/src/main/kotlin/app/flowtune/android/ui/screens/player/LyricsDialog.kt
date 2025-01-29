package app.flowtune.android.ui.screens.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.flowtune.android.Database
import app.flowtune.android.LocalPlayerServiceBinder
import app.flowtune.android.preferences.PlayerPreferences
import app.flowtune.android.ui.modifiers.PinchDirection
import app.flowtune.android.ui.modifiers.onSwipe
import app.flowtune.android.ui.modifiers.pinchToToggle
import app.flowtune.android.utils.FullScreenState
import app.flowtune.android.utils.forceSeekToNext
import app.flowtune.android.utils.forceSeekToPrevious
import app.flowtune.android.utils.thumbnail
import app.flowtune.android.utils.windowState
import app.flowtune.core.ui.LocalAppearance
import app.flowtune.core.ui.utils.px
import coil3.compose.AsyncImage

@Composable
fun LyricsDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) = Dialog(onDismissRequest = onDismiss) {
    val currentOnDismiss by rememberUpdatedState(onDismiss)

    FullScreenState(shown = PlayerPreferences.lyricsShowSystemBars)

    val (colorPalette, _, _, thumbnailShape) = LocalAppearance.current

    val player = LocalPlayerServiceBinder.current?.player ?: return@Dialog
    val (window, error) = windowState()

    LaunchedEffect(window, error) {
        if (window == null || error != null) currentOnDismiss()
    }

    window ?: return@Dialog

    AnimatedContent(
        targetState = window,
        transitionSpec = {
            if (initialState.mediaItem.mediaId == targetState.mediaItem.mediaId)
                return@AnimatedContent ContentTransform(
                    targetContentEnter = EnterTransition.None,
                    initialContentExit = ExitTransition.None
                )

            val direction = if (targetState.firstPeriodIndex > initialState.firstPeriodIndex)
                AnimatedContentTransitionScope.SlideDirection.Left
            else AnimatedContentTransitionScope.SlideDirection.Right

            ContentTransform(
                targetContentEnter = slideIntoContainer(
                    towards = direction,
                    animationSpec = tween(500)
                ),
                initialContentExit = slideOutOfContainer(
                    towards = direction,
                    animationSpec = tween(500)
                ),
                sizeTransform = null
            )
        },
        label = ""
    ) { currentWindow ->
        BoxWithConstraints(
            modifier = modifier
                .padding(all = 36.dp)
                .padding(vertical = 32.dp)
                .clip(thumbnailShape)
                .fillMaxSize()
                .background(colorPalette.background1)
                .pinchToToggle(
                    direction = PinchDirection.In,
                    threshold = 0.9f,
                    onPinch = { onDismiss() }
                )
                .onSwipe(
                    onSwipeLeft = {
                        player.forceSeekToNext()
                    },
                    onSwipeRight = {
                        player.seekToDefaultPosition()
                        player.forceSeekToPrevious()
                    }
                )
        ) {
            if (currentWindow.mediaItem.mediaMetadata.artworkUri != null) AsyncImage(
                model = currentWindow.mediaItem.mediaMetadata.artworkUri.thumbnail((maxHeight - 64.dp).px),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorPalette.background0)
                    .blur(radius = 8.dp)
            )

            Lyrics(
                mediaId = currentWindow.mediaItem.mediaId,
                isDisplayed = true,
                onDismiss = { },
                mediaMetadataProvider = currentWindow.mediaItem::mediaMetadata,
                durationProvider = player::getDuration,
                ensureSongInserted = { Database.insert(currentWindow.mediaItem) },
                onMenuLaunch = onDismiss,
                modifier = Modifier.height(maxHeight),
                shouldKeepScreenAwake = false, // otherwise the keepScreenOn flag resets after dialog closes
                shouldUpdateLyrics = false
            )
        }
    }
}
