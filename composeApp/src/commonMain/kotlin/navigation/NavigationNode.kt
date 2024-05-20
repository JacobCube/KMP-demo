package navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import navigation.NavigationArguments.ARG_ID
import navigation.NavigationArguments.CHARACTER_FILTER_GENDER
import navigation.NavigationArguments.CHARACTER_FILTER_NAME
import navigation.NavigationArguments.CHARACTER_FILTER_SPECIES
import navigation.NavigationArguments.CHARACTER_FILTER_STATUS
import navigation.NavigationArguments.CHARACTER_FILTER_TYPE
import org.jetbrains.compose.resources.StringResource
import rododendron.composeapp.generated.resources.Res
import rododendron.composeapp.generated.resources.category_casino
import rododendron.composeapp.generated.resources.category_casino_alert_centre
import rododendron.composeapp.generated.resources.category_casino_banners
import rododendron.composeapp.generated.resources.category_casino_banners_dashboard
import rododendron.composeapp.generated.resources.category_casino_banners_new
import rododendron.composeapp.generated.resources.category_casino_banners_settings
import rododendron.composeapp.generated.resources.category_casino_free_spins
import rododendron.composeapp.generated.resources.category_casino_free_spins_campaigns
import rododendron.composeapp.generated.resources.category_casino_free_spins_conversion
import rododendron.composeapp.generated.resources.category_casino_free_spins_provider_campaigns
import rododendron.composeapp.generated.resources.category_casino_free_spins_yaadrasil
import rododendron.composeapp.generated.resources.category_casino_games
import rododendron.composeapp.generated.resources.category_casino_games_categories
import rododendron.composeapp.generated.resources.category_casino_games_genres
import rododendron.composeapp.generated.resources.category_casino_games_labels
import rododendron.composeapp.generated.resources.category_casino_games_list
import rododendron.composeapp.generated.resources.category_casino_games_types
import rododendron.composeapp.generated.resources.category_casino_jackpot_banner
import rododendron.composeapp.generated.resources.category_casino_jackpot_banner_levels
import rododendron.composeapp.generated.resources.category_casino_jackpot_banner_list
import rododendron.composeapp.generated.resources.category_casino_jackpot_banner_vendors
import rododendron.composeapp.generated.resources.category_casino_lobby
import rododendron.composeapp.generated.resources.category_casino_promo_calendar
import rododendron.composeapp.generated.resources.category_casino_promo_calendar_dashboard
import rododendron.composeapp.generated.resources.category_casino_promo_calendar_interval_dashboard
import rododendron.composeapp.generated.resources.category_casino_promo_calendar_new
import rododendron.composeapp.generated.resources.category_casino_tournaments
import rododendron.composeapp.generated.resources.category_casino_tournaments_dashboard
import rododendron.composeapp.generated.resources.category_casino_tournaments_new
import rododendron.composeapp.generated.resources.category_casino_tournaments_win_split
import rododendron.composeapp.generated.resources.category_casino_transactions
import rododendron.composeapp.generated.resources.category_casino_transactions_list
import rododendron.composeapp.generated.resources.category_casino_transactions_list_non_closed
import rododendron.composeapp.generated.resources.category_dashboard
import rododendron.composeapp.generated.resources.category_demo
import rododendron.composeapp.generated.resources.category_demo_characters
import rododendron.composeapp.generated.resources.category_demo_episodes
import rododendron.composeapp.generated.resources.category_demo_locations
import rododendron.composeapp.generated.resources.characters_list_filter

/**
 * Categories of navigation
 *
 * @param children indicates the given category has children categories
 * @param navigationNode indicates the category itself is clickable and has a screen
 */
data class CategoryNode(
    val titleRes: StringResource? = null,
    val children: List<CategoryNode> = listOf(),
    val navigationNode: NavigationNode? = null
)
object NavigationArguments {
    const val ARG_ID = "id"
    const val ARG_URL = "url"

    const val CHARACTER_FILTER_NAME = "name"
    const val CHARACTER_FILTER_STATUS = "status"
    const val CHARACTER_FILTER_SPECIES = "species"
    const val CHARACTER_FILTER_TYPE = "type"
    const val CHARACTER_FILTER_GENDER = "gender"
}


/** A single navigation node with all necessary global information */
sealed class NavigationNode(
    /** destination unique identifier */
    val identification: String,
    /** list of all required arguments */
    val arguments: List<String> = listOf(),
    val titleRes: StringResource,
    val categoryRoute: AppBarCategories
) {

    /** base path to this destination */
    val route: String
        get() {
            var route = identification
            arguments.forEach {
                route += "?$it={$it}"
            }
            return route
        }

    /** Seznam vsech argumentu do navigace */
    open val navArguments: List<NamedNavArgument>
        get() = mutableListOf<NamedNavArgument>().apply {
            arguments.forEach { argument ->
                add(navArgument(argument) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            }
        }

    /**
     * Vytvori cestu s argumenty
     * [Pair.first] nazev argumentu
     * [Pair.second] hodnota argumentu
     */
    fun createRoute(vararg argumentValues: Pair<String, String?>): String {
        var route = route
        argumentValues.forEach {
            if(it.second != null) {
                route = route.replace("{${it.first}}", it.second ?: "")
            }
        }
        return route
    }

    data object Demo : NavigationNode(
        identification = "/demo",
        titleRes = Res.string.category_demo,
        categoryRoute = AppBarCategories.None
    ) {
        data object Characters: NavigationNode(
            identification = "/characters",
            titleRes = Res.string.category_demo_characters,
            categoryRoute = AppBarCategories.None
        ) {
            data object CharactersDetail: NavigationNode(
                identification = "/character",
                titleRes = Res.string.category_demo_characters,
                categoryRoute = AppBarCategories.None,
                arguments = listOf(ARG_ID)
            )

            data object CharactersFilter: NavigationNode(
                identification = "/characters/filter",
                titleRes = Res.string.characters_list_filter,
                categoryRoute = AppBarCategories.None,
                arguments = listOf(
                    CHARACTER_FILTER_NAME,
                    CHARACTER_FILTER_STATUS,
                    CHARACTER_FILTER_SPECIES,
                    CHARACTER_FILTER_TYPE,
                    CHARACTER_FILTER_GENDER
                )
            )
        }

        data object Locations: NavigationNode(
            identification = "/locations",
            titleRes = Res.string.category_demo_locations,
            categoryRoute = AppBarCategories.None
        ) {
            data object LocationDetail: NavigationNode(
                identification = "/location",
                titleRes = Res.string.category_demo_locations,
                categoryRoute = AppBarCategories.None,
                arguments = listOf(ARG_ID)
            )
        }

        data object Episodes: NavigationNode(
            identification = "/episodes",
            titleRes = Res.string.category_demo_episodes,
            categoryRoute = AppBarCategories.None
        ) {
            data object EpisodeDetail: NavigationNode(
                identification = "/episode",
                titleRes = Res.string.category_demo_episodes,
                categoryRoute = AppBarCategories.None,
                arguments = listOf(ARG_ID)
            )
        }
    }

    /** Category Casino */
    data object Dashboard : NavigationNode(
        identification = "/home",
        titleRes = Res.string.category_dashboard,
        categoryRoute = AppBarCategories.None
    )

    /** Category Casino */
    data object Casino : NavigationNode(
        identification = "/Casino",
        titleRes = Res.string.category_casino,
        categoryRoute = AppBarCategories.Casino
    ) {

        /** Banners */
        data object Banners : NavigationNode(
            identification = "Banners",
            titleRes = Res.string.category_casino_banners,
            categoryRoute = AppBarCategories.Casino
        ) {

            /** Dashboard with all banners */
            data object Dashboard : NavigationNode(
                identification = "//Dashboard",
                titleRes = Res.string.category_casino_banners_dashboard,
                categoryRoute = AppBarCategories.Casino
            )

            /** Creation of a new banner */
            data object NewBanner : NavigationNode(
                identification = "//NewBanner",
                titleRes = Res.string.category_casino_banners_new,
                categoryRoute = AppBarCategories.Casino
            )

            /** Banner settings */
            data object Settings : NavigationNode(
                identification = "//Settings",
                titleRes = Res.string.category_casino_banners_settings,
                categoryRoute = AppBarCategories.Casino
            )
        }

        /** Free spins for casino games */
        data object FreeSpins : NavigationNode(
            identification = "FreeSpins",
            titleRes = Res.string.category_casino_free_spins,
            categoryRoute = AppBarCategories.Casino
        ) {

            /** Campaigns */
            data object Campaigns : NavigationNode(
                identification = "Campaigns",
                titleRes = Res.string.category_casino_free_spins_campaigns,
                categoryRoute = AppBarCategories.Casino
            )

            /** Conversion requests */
            data object Conversion : NavigationNode(
                identification = "Conversion",
                titleRes = Res.string.category_casino_free_spins_conversion,
                categoryRoute = AppBarCategories.Casino
            )

            /** Cmpaigns from providers */
            data object ProviderCampaigns : NavigationNode(
                identification = "ProviderCampaigns",
                titleRes = Res.string.category_casino_free_spins_provider_campaigns,
                categoryRoute = AppBarCategories.Casino
            )

            /** yaadrasil */
            data object Yaadrasil : NavigationNode(
                identification = "Yaadrasil",
                titleRes = Res.string.category_casino_free_spins_yaadrasil,
                categoryRoute = AppBarCategories.Casino
            )
        }

        /** Promo calendars */
        data object PromoCalendar : NavigationNode(
            identification = "PromoCalendar",
            titleRes = Res.string.category_casino_promo_calendar,
            categoryRoute = AppBarCategories.Casino
        ) {

            /** Creation of a new promo calendar */
            data object NewCalendar : NavigationNode(
                identification = "NewCalendar",
                titleRes = Res.string.category_casino_promo_calendar_new,
                categoryRoute = AppBarCategories.Casino
            )

            /** list of all promo calendars */
            data object Dashboard : NavigationNode(
                identification = "Dashboard",
                titleRes = Res.string.category_casino_promo_calendar_dashboard,
                categoryRoute = AppBarCategories.Casino
            )

            /** Dashboard of bonus intervals */
            data object IntervalDashboard : NavigationNode(
                identification = "IntervalDashboard",
                titleRes = Res.string.category_casino_promo_calendar_interval_dashboard,
                categoryRoute = AppBarCategories.Casino
            )
        }

        /** Casino transactions */
        data object Transactions : NavigationNode(
            identification = "Transactions",
            titleRes = Res.string.category_casino_transactions,
            categoryRoute = AppBarCategories.Casino
        ) {

            /** List of all transactions */
            data object List : NavigationNode(
                identification = "List",
                titleRes = Res.string.category_casino_transactions_list,
                categoryRoute = AppBarCategories.Casino
            )

            /** List of non-closed transactions */
            data object NonClosedList : NavigationNode(
                identification = "NonClosedList",
                titleRes = Res.string.category_casino_transactions_list_non_closed,
                categoryRoute = AppBarCategories.Casino
            )
        }

        /** Casino tournaments */
        data object Tournaments : NavigationNode(
            identification = "Tournaments",
            titleRes = Res.string.category_casino_tournaments,
            categoryRoute = AppBarCategories.Casino
        ) {

            /** Creation of a new tournament */
            data object NewTournament : NavigationNode(
                identification = "NewTournament",
                titleRes = Res.string.category_casino_tournaments_new,
                categoryRoute = AppBarCategories.Casino
            )

            /** List of all tournaments */
            data object Dashboard : NavigationNode(
                identification = "Dashboard",
                titleRes = Res.string.category_casino_tournaments_dashboard,
                categoryRoute = AppBarCategories.Casino
            )

            /** Templates for win splitting */
            data object WinSplit : NavigationNode(
                identification = "WinSplit",
                titleRes = Res.string.category_casino_tournaments_win_split,
                categoryRoute = AppBarCategories.Casino
            )
        }

        /** Lobby */
        data object Lobby : NavigationNode(
            identification = "Lobby",
            titleRes = Res.string.category_casino_lobby,
            categoryRoute = AppBarCategories.Casino
        )

        /** Casino games */
        data object Games : NavigationNode(
            identification = "Games",
            titleRes = Res.string.category_casino_games,
            categoryRoute = AppBarCategories.Casino
        ) {

            /** List of all games */
            data object List : NavigationNode(
                identification = "List",
                titleRes = Res.string.category_casino_games_list,
                categoryRoute = AppBarCategories.Casino
            )

            /** List of all game types */
            data object Types : NavigationNode(
                identification = "Types",
                titleRes = Res.string.category_casino_games_types,
                categoryRoute = AppBarCategories.Casino
            )

            /** List of all game genres */
            data object Genres : NavigationNode(
                identification = "Genres",
                titleRes = Res.string.category_casino_games_genres,
                categoryRoute = AppBarCategories.Casino
            )

            /** List of all game categories */
            data object Categories : NavigationNode(
                identification = "Categories",
                titleRes = Res.string.category_casino_games_categories,
                categoryRoute = AppBarCategories.Casino
            )

            /** List of all game labels */
            data object Labels : NavigationNode(
                identification = "Labels",
                titleRes = Res.string.category_casino_games_labels,
                categoryRoute = AppBarCategories.Casino
            )
        }

        /** Jackpot banners */
        data object JackpotBanners : NavigationNode(
            identification = "JackpotBanners",
            titleRes = Res.string.category_casino_jackpot_banner,
            categoryRoute = AppBarCategories.Casino
        ) {

            /** List of all jackpot banners */
            data object List : NavigationNode(
                identification = "List",
                titleRes = Res.string.category_casino_jackpot_banner_list,
                categoryRoute = AppBarCategories.Casino
            )

            /** List of all vendors */
            data object Vendors : NavigationNode(
                identification = "Vendors",
                titleRes = Res.string.category_casino_jackpot_banner_vendors,
                categoryRoute = AppBarCategories.Casino
            )

            /** List of all levels */
            data object Levels : NavigationNode(
                identification = "Levels",
                titleRes = Res.string.category_casino_jackpot_banner_levels,
                categoryRoute = AppBarCategories.Casino
            )
        }

        /** Alert centre */
        data object AlertCentre : NavigationNode(
            identification = "AlertCentre",
            titleRes = Res.string.category_casino_alert_centre,
            categoryRoute = AppBarCategories.Casino
        )
    }
}

/** Root component for base navigation within the app on the very top level */
/*
class NavigationRootComponent(
    initialNode: NavigationNodeDeprecated = NavigationNodeDeprecated.Dashboard,
    componentContext: ComponentContext,
): RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<NavigationNodeDeprecated>()

    override val stack: Value<ChildStack<*, NavigationNodeDeprecated>> = childStack(
        source = navigation,
        serializer = NavigationNodeDeprecated.serializer(),
        initialConfiguration = initialNode,
        handleBackButton = true,
        childFactory = ::child
    )

    private val _currentNode: MutableStateFlow<NavigationNodeDeprecated?> = MutableStateFlow(null)
    private val _currentStack: MutableStateFlow<List<Child.Created<Any, NavigationNodeDeprecated>>?> = MutableStateFlow(null)

    override val currentStack: StateFlow<List<Child.Created<Any, NavigationNodeDeprecated>>?> = _currentStack.asStateFlow()

    init {
        stack.subscribe {
            _currentNode.value = it.active.instance
            _currentStack.value = it.backStack
        }
    }

    override val currentNode: StateFlow<NavigationNodeDeprecated?>
        get() = _currentNode.asStateFlow()

    private fun child(
        node: NavigationNodeDeprecated,
        componentContext: ComponentContext
    ) = node

    override fun onBackPressed(toIndex: Int?) {
        if(toIndex != null) {
            navigation.popTo(index = toIndex)
            stack.subscribe {
                it.backStack
            }
        }else navigation.pop()
    }

    @OptIn(ExperimentalDecomposeApi::class)
    override fun navigate(node: NavigationNodeDeprecated) {
        navigation.pushToFront(node)
        //stack.value.items.any { it.instance == node }
        //push(node)
    }
}*/
