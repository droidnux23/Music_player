package app.flowtune.android.ui.screens.localplaylist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import app.flowtune.android.Database
import app.flowtune.android.R
import app.flowtune.android.models.Playlist
import app.flowtune.android.models.Song
import app.flowtune.android.ui.components.themed.Scaffold
import app.flowtune.android.ui.components.themed.adaptiveThumbnailContent
import app.flowtune.android.ui.screens.GlobalRoutes
import app.flowtune.android.ui.screens.Route
import app.flowtune.compose.persist.PersistMapCleanup
import app.flowtune.compose.persist.persist
import app.flowtune.compose.persist.persistList
import app.flowtune.compose.routing.RouteHandler
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Route
@Composable
fun LocalPlaylistScreen(playlistId: Long) {
    val saveableStateHolder = rememberSaveableStateHolder()

    PersistMapCleanup(prefix = "localPlaylist/$playlistId/")

    RouteHandler {
        GlobalRoutes()

        Content {
            var playlist by persist<Playlist?>("localPlaylist/$playlistId/playlist")
            var songs by persistList<Song>("localPlaylist/$playlistId/songs")

            LaunchedEffect(Unit) {
                Database
                    .playlist(playlistId)
                    .filterNotNull()
                    .distinctUntilChanged()
                    .collect { playlist = it }
            }

            LaunchedEffect(Unit) {
                Database
                    .playlistSongs(playlistId)
                    .filterNotNull()
                    .distinctUntilChanged()
                    .collect { songs = it.toImmutableList() }
            }

            val thumbnailContent = remember(playlist) {
                playlist?.thumbnail?.let { url ->
                    adaptiveThumbnailContent(
                        isLoading = false,
                        url = url
                    )
                } ?: { }
            }

            Scaffold(
                key = "localplaylist",
                topIconButtonId = R.drawable.chevron_back,
                onTopIconButtonClick = pop,
                tabIndex = 0,
                onTabChange = { },
                tabColumnContent = {
                    tab(0, R.string.songs, R.drawable.musical_notes)
                }
            ) { currentTabIndex ->
                saveableStateHolder.SaveableStateProvider(currentTabIndex) {
                    playlist?.let {
                        when (currentTabIndex) {
                            0 -> LocalPlaylistSongs(
                                playlist = it,
                                songs = songs,
                                thumbnailContent = thumbnailContent,
                                onDelete = pop
                            )
                        }
                    }
                }
            }
        }
    }
}
