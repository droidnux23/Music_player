package app.flowtune.android.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import app.flowtune.android.R
import app.flowtune.android.models.toUiMood
import app.flowtune.android.preferences.UIStatePreferences
import app.flowtune.android.ui.components.themed.Scaffold
import app.flowtune.android.ui.screens.GlobalRoutes
import app.flowtune.android.ui.screens.Route
import app.flowtune.android.ui.screens.albumRoute
import app.flowtune.android.ui.screens.artistRoute
import app.flowtune.android.ui.screens.builtInPlaylistRoute
import app.flowtune.android.ui.screens.builtinplaylist.BuiltInPlaylistScreen
import app.flowtune.android.ui.screens.localPlaylistRoute
import app.flowtune.android.ui.screens.localplaylist.LocalPlaylistScreen
import app.flowtune.android.ui.screens.mood.MoodScreen
import app.flowtune.android.ui.screens.mood.MoreAlbumsScreen
import app.flowtune.android.ui.screens.mood.MoreMoodsScreen
import app.flowtune.android.ui.screens.moodRoute
import app.flowtune.android.ui.screens.pipedPlaylistRoute
import app.flowtune.android.ui.screens.playlistRoute
import app.flowtune.android.ui.screens.searchRoute
import app.flowtune.android.ui.screens.settingsRoute
import app.flowtune.compose.persist.PersistMapCleanup
import app.flowtune.compose.routing.Route0
import app.flowtune.compose.routing.RouteHandler

private val moreMoodsRoute = Route0("moreMoodsRoute")
private val moreAlbumsRoute = Route0("moreAlbumsRoute")

@Route
@Composable
fun HomeScreen() {
    val saveableStateHolder = rememberSaveableStateHolder()

    PersistMapCleanup("home/")

    RouteHandler {
        GlobalRoutes()

        localPlaylistRoute { playlistId ->
            LocalPlaylistScreen(playlistId = playlistId)
        }

        builtInPlaylistRoute { builtInPlaylist ->
            BuiltInPlaylistScreen(builtInPlaylist = builtInPlaylist)
        }

        moodRoute { mood ->
            MoodScreen(mood = mood)
        }

        moreMoodsRoute {
            MoreMoodsScreen()
        }

        moreAlbumsRoute {
            MoreAlbumsScreen()
        }

        Content {
            Scaffold(
                key = "home",
                topIconButtonId = R.drawable.settings,
                onTopIconButtonClick = { settingsRoute() },
                tabIndex = UIStatePreferences.homeScreenTabIndex,
                onTabChange = { UIStatePreferences.homeScreenTabIndex = it },
                tabColumnContent = {
                    tab(0, R.string.quick_picks, R.drawable.sparkles)
                    tab(1, R.string.discover, R.drawable.globe)
                    tab(2, R.string.songs, R.drawable.musical_notes)
                    tab(3, R.string.playlists, R.drawable.playlist)
                    tab(4, R.string.artists, R.drawable.person)
                    tab(5, R.string.albums, R.drawable.disc)
                    tab(6, R.string.local, R.drawable.download)
                }
            ) { currentTabIndex ->
                saveableStateHolder.SaveableStateProvider(key = currentTabIndex) {
                    val onSearchClick = { searchRoute("") }
                    when (currentTabIndex) {
                        0 -> QuickPicks(
                            onAlbumClick = { albumRoute(it.key) },
                            onArtistClick = { artistRoute(it.key) },
                            onPlaylistClick = {
                                playlistRoute(
                                    p0 = it.key,
                                    p1 = null,
                                    p2 = null,
                                    p3 = it.channel?.name == "YouTube Music"
                                )
                            },
                            onSearchClick = onSearchClick
                        )

                        1 -> HomeDiscovery(
                            onMoodClick = { mood -> moodRoute(mood.toUiMood()) },
                            onNewReleaseAlbumClick = { albumRoute(it) },
                            onSearchClick = onSearchClick,
                            onMoreMoodsClick = { moreMoodsRoute() },
                            onMoreAlbumsClick = { moreAlbumsRoute() },
                            onPlaylistClick = { playlistRoute(it, null, null, true) }
                        )

                        2 -> HomeSongs(
                            onSearchClick = onSearchClick
                        )

                        3 -> HomePlaylists(
                            onBuiltInPlaylist = { builtInPlaylistRoute(it) },
                            onPlaylistClick = { localPlaylistRoute(it.id) },
                            onPipedPlaylistClick = { session, playlist ->
                                pipedPlaylistRoute(
                                    p0 = session.apiBaseUrl.toString(),
                                    p1 = session.token,
                                    p2 = playlist.id.toString()
                                )
                            },
                            onSearchClick = onSearchClick
                        )

                        4 -> HomeArtistList(
                            onArtistClick = { artistRoute(it.id) },
                            onSearchClick = onSearchClick
                        )

                        5 -> HomeAlbums(
                            onAlbumClick = { albumRoute(it.id) },
                            onSearchClick = onSearchClick
                        )

                        6 -> HomeLocalSongs(
                            onSearchClick = onSearchClick
                        )
                    }
                }
            }
        }
    }
}
