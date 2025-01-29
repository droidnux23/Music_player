package app.flowtune.android.ui.screens.builtinplaylist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.res.stringResource
import app.flowtune.android.R
import app.flowtune.android.preferences.DataPreferences
import app.flowtune.android.preferences.UIStatePreferences
import app.flowtune.android.ui.components.themed.Scaffold
import app.flowtune.android.ui.screens.GlobalRoutes
import app.flowtune.android.ui.screens.Route
import app.flowtune.compose.persist.PersistMapCleanup
import app.flowtune.compose.routing.RouteHandler
import app.flowtune.core.data.enums.BuiltInPlaylist

object BuiltInPlaylistScreen {
    internal const val KEY = "builtinplaylist"

    @Composable
    fun shownPlaylistsAsState(): State<List<BuiltInPlaylist>> {
        val hiddenPlaylistTabs by UIStatePreferences.mutableTabStateOf(KEY)

        return remember {
            derivedStateOf {
                BuiltInPlaylist.entries.filter { it.ordinal.toString() !in hiddenPlaylistTabs }
            }
        }
    }
}

@Route
@Composable
fun BuiltInPlaylistScreen(builtInPlaylist: BuiltInPlaylist) {
    val saveableStateHolder = rememberSaveableStateHolder()
    val (tabIndex, onTabIndexChanged) = rememberSaveable { mutableIntStateOf(builtInPlaylist.ordinal) }

    PersistMapCleanup(prefix = "${builtInPlaylist.name}/")

    RouteHandler {
        GlobalRoutes()

        Content {
            val topTabTitle = stringResource(R.string.format_top_playlist, DataPreferences.topListLength)

            Scaffold(
                key = BuiltInPlaylistScreen.KEY,
                topIconButtonId = R.drawable.chevron_back,
                onTopIconButtonClick = pop,
                tabIndex = tabIndex,
                onTabChange = onTabIndexChanged,
                tabColumnContent = {
                    tab(0, R.string.favorites, R.drawable.heart)
                    tab(1, R.string.offline, R.drawable.airplane)
                    tab(2, topTabTitle, R.drawable.trending_up)
                    tab(3, R.string.history, R.drawable.history)
                },
                tabsEditingTitle = stringResource(R.string.playlists)
            ) { currentTabIndex ->
                saveableStateHolder.SaveableStateProvider(key = currentTabIndex) {
                    BuiltInPlaylist
                        .entries
                        .getOrNull(currentTabIndex)
                        ?.let { BuiltInPlaylistSongs(builtInPlaylist = it) }
                }
            }
        }
    }
}
