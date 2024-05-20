package navigation

import org.jetbrains.compose.resources.StringResource
import rododendron.composeapp.generated.resources.Res
import rododendron.composeapp.generated.resources.category_administration
import rododendron.composeapp.generated.resources.category_affiliate
import rododendron.composeapp.generated.resources.category_bonuses
import rododendron.composeapp.generated.resources.category_branches
import rododendron.composeapp.generated.resources.category_casino
import rododendron.composeapp.generated.resources.category_casino_banners
import rododendron.composeapp.generated.resources.category_casino_free_spins
import rododendron.composeapp.generated.resources.category_casino_games
import rododendron.composeapp.generated.resources.category_casino_jackpot_banner
import rododendron.composeapp.generated.resources.category_casino_promo_calendar
import rododendron.composeapp.generated.resources.category_casino_tournaments
import rododendron.composeapp.generated.resources.category_casino_transactions
import rododendron.composeapp.generated.resources.category_cem
import rododendron.composeapp.generated.resources.category_clients
import rododendron.composeapp.generated.resources.category_contests
import rododendron.composeapp.generated.resources.category_demo
import rododendron.composeapp.generated.resources.category_economics
import rododendron.composeapp.generated.resources.category_file
import rododendron.composeapp.generated.resources.category_file_about
import rododendron.composeapp.generated.resources.category_file_change_password
import rododendron.composeapp.generated.resources.category_file_exit
import rododendron.composeapp.generated.resources.category_file_localization
import rododendron.composeapp.generated.resources.category_file_login
import rododendron.composeapp.generated.resources.category_file_report_f1
import rododendron.composeapp.generated.resources.category_file_settings
import rododendron.composeapp.generated.resources.category_localization
import rododendron.composeapp.generated.resources.category_lottery
import rododendron.composeapp.generated.resources.category_odds
import rododendron.composeapp.generated.resources.category_odds_automat_classic
import rododendron.composeapp.generated.resources.category_odds_automat_new
import rododendron.composeapp.generated.resources.category_odds_bookmakers_message
import rododendron.composeapp.generated.resources.category_odds_branch_staff_message
import rododendron.composeapp.generated.resources.category_odds_codebook_order
import rododendron.composeapp.generated.resources.category_odds_confirmation_change
import rododendron.composeapp.generated.resources.category_odds_contest_categories
import rododendron.composeapp.generated.resources.category_odds_contest_list
import rododendron.composeapp.generated.resources.category_odds_creation
import rododendron.composeapp.generated.resources.category_odds_entity
import rododendron.composeapp.generated.resources.category_odds_game_settings
import rododendron.composeapp.generated.resources.category_odds_lineup
import rododendron.composeapp.generated.resources.category_odds_lineup_bookmaker_gusto
import rododendron.composeapp.generated.resources.category_odds_lineup_branch_contents
import rododendron.composeapp.generated.resources.category_odds_lineup_carpet_ng
import rododendron.composeapp.generated.resources.category_odds_lineup_contest_dashboard
import rododendron.composeapp.generated.resources.category_odds_lineup_libe_bet_recruitment
import rododendron.composeapp.generated.resources.category_odds_lineup_match_dashboard
import rododendron.composeapp.generated.resources.category_odds_lineup_offer
import rododendron.composeapp.generated.resources.category_odds_lineup_opportunity_deposit
import rododendron.composeapp.generated.resources.category_odds_lineup_prematch_events
import rododendron.composeapp.generated.resources.category_odds_lineup_recruitment
import rododendron.composeapp.generated.resources.category_odds_live_bet
import rododendron.composeapp.generated.resources.category_odds_ok_tip_results
import rododendron.composeapp.generated.resources.category_odds_ok_tip_results_generate
import rododendron.composeapp.generated.resources.category_odds_ok_tip_results_last_6
import rododendron.composeapp.generated.resources.category_odds_ok_tip_results_lineup
import rododendron.composeapp.generated.resources.category_odds_ok_tip_results_new_match
import rododendron.composeapp.generated.resources.category_odds_prematch_alerts
import rododendron.composeapp.generated.resources.category_odds_risk_control
import rododendron.composeapp.generated.resources.category_odds_service
import rododendron.composeapp.generated.resources.category_odds_sets
import rododendron.composeapp.generated.resources.category_odds_tv_station
import rododendron.composeapp.generated.resources.category_secretary
import rododendron.composeapp.generated.resources.category_services
import rododendron.composeapp.generated.resources.category_tests
import rododendron.composeapp.generated.resources.category_tickets
import rododendron.composeapp.generated.resources.category_window

/**
 * Cardinal categories of navigation
 *
 * @param children indicates the given category has children categories
 * @param navigationNode indicates the category itself is clickable and has a screen
 */
enum class AppBarCategories(
    val titleRes: StringResource?,
    val children: List<CategoryNode> = listOf(),
    val navigationNode: NavigationNode? = null
) {
    Demo(
        Res.string.category_demo,
        children = listOf(
            CategoryNode(
                NavigationNode.Demo.Characters.titleRes,
                navigationNode = NavigationNode.Demo.Characters
            ),
            CategoryNode(
                NavigationNode.Demo.Locations.titleRes,
                navigationNode = NavigationNode.Demo.Locations
            ),
            CategoryNode(
                NavigationNode.Demo.Episodes.titleRes,
                navigationNode = NavigationNode.Demo.Episodes
            )
        )
    ),
    File(
        Res.string.category_file,
        children = listOf(
            CategoryNode(Res.string.category_file_login),
            CategoryNode(Res.string.category_file_change_password),
            CategoryNode(Res.string.category_file_settings),
            CategoryNode(Res.string.category_file_localization),
            CategoryNode(Res.string.category_file_report_f1),
            CategoryNode(Res.string.category_file_about),
            CategoryNode(Res.string.category_file_exit)
        )
    ),
    Odds(
        Res.string.category_odds,
        children = listOf(
            CategoryNode(Res.string.category_odds_contest_list),
            CategoryNode(Res.string.category_odds_creation),
            CategoryNode(Res.string.category_odds_confirmation_change),
            CategoryNode(Res.string.category_odds_sets),
            CategoryNode(
                Res.string.category_odds_ok_tip_results,
                children = listOf(
                    CategoryNode(Res.string.category_odds_ok_tip_results_lineup),
                    CategoryNode(Res.string.category_odds_ok_tip_results_new_match),
                    CategoryNode(Res.string.category_odds_ok_tip_results_last_6),
                    CategoryNode(Res.string.category_odds_ok_tip_results_generate),
                )
            ),
            CategoryNode(Res.string.category_odds_service),
            CategoryNode(
                Res.string.category_odds_lineup,
                listOf(
                    CategoryNode(Res.string.category_odds_lineup_offer),
                    CategoryNode(Res.string.category_odds_lineup_recruitment),
                    CategoryNode(Res.string.category_odds_lineup_contest_dashboard),
                    CategoryNode(Res.string.category_odds_lineup_match_dashboard),
                    CategoryNode(Res.string.category_odds_lineup_carpet_ng),
                    CategoryNode(Res.string.category_odds_lineup_opportunity_deposit),
                    CategoryNode(Res.string.category_odds_lineup_libe_bet_recruitment),
                    CategoryNode(Res.string.category_odds_lineup_bookmaker_gusto),
                    CategoryNode(Res.string.category_odds_lineup_prematch_events),
                    CategoryNode(Res.string.category_odds_lineup_branch_contents)
                )
            ),
            CategoryNode(Res.string.category_odds_risk_control),
            CategoryNode(Res.string.category_odds_live_bet),
            CategoryNode(Res.string.category_odds_automat_classic),
            CategoryNode(Res.string.category_odds_automat_new),
            CategoryNode(Res.string.category_odds_branch_staff_message),
            CategoryNode(Res.string.category_odds_bookmakers_message),
            CategoryNode(Res.string.category_odds_tv_station),
            CategoryNode(Res.string.category_odds_prematch_alerts),
            CategoryNode(Res.string.category_odds_codebook_order),
            CategoryNode(Res.string.category_odds_game_settings),
            CategoryNode(Res.string.category_odds_contest_categories),
            CategoryNode(Res.string.category_odds_entity)
        )
    ),
    Secretary(
        Res.string.category_secretary,
        children = listOf(

        )
    ),
    Clients(
        Res.string.category_clients,
        children = listOf(

        )
    ),
    CEM(
        Res.string.category_cem,
        children = listOf(

        )
    ),
    Affiliate(
        Res.string.category_affiliate,
        children = listOf(

        )
    ),
    Economics(
        Res.string.category_economics,
        children = listOf(

        )
    ),
    Services(
        Res.string.category_services,
        children = listOf(

        )
    ),
    Branches(
        Res.string.category_branches,
        children = listOf(

        )
    ),
    Tickets(
        Res.string.category_tickets,
        children = listOf(

        )
    ),
    Lottery(
        Res.string.category_lottery,
        children = listOf(

        )
    ),
    Casino(
        Res.string.category_casino,
        children = listOf(
            CategoryNode(
                Res.string.category_casino_banners,
                children = listOf(
                    CategoryNode(navigationNode = NavigationNode.Casino.Banners.Dashboard),
                    CategoryNode(navigationNode = NavigationNode.Casino.Banners.NewBanner),
                    CategoryNode(navigationNode = NavigationNode.Casino.Banners.Settings),
                )
            ),
            CategoryNode(
                Res.string.category_casino_free_spins,
                children = listOf(
                    CategoryNode(navigationNode = NavigationNode.Casino.FreeSpins.Campaigns),
                    CategoryNode(navigationNode = NavigationNode.Casino.FreeSpins.Conversion),
                    CategoryNode(navigationNode = NavigationNode.Casino.FreeSpins.ProviderCampaigns),
                    CategoryNode(navigationNode = NavigationNode.Casino.FreeSpins.Yaadrasil)
                )
            ),
            CategoryNode(
                Res.string.category_casino_promo_calendar,
                children = listOf(
                    CategoryNode(navigationNode = NavigationNode.Casino.PromoCalendar.NewCalendar),
                    CategoryNode(navigationNode = NavigationNode.Casino.PromoCalendar.Dashboard),
                    CategoryNode(navigationNode = NavigationNode.Casino.PromoCalendar.IntervalDashboard),
                )
            ),
            CategoryNode(
                Res.string.category_casino_transactions,
                children = listOf(
                    CategoryNode(navigationNode = NavigationNode.Casino.Transactions.List),
                    CategoryNode(navigationNode = NavigationNode.Casino.Transactions.NonClosedList),
                )
            ),
            CategoryNode(
                Res.string.category_casino_tournaments,
                children = listOf(
                    CategoryNode(navigationNode = NavigationNode.Casino.Tournaments.NewTournament),
                    CategoryNode(navigationNode = NavigationNode.Casino.Tournaments.Dashboard),
                    CategoryNode(navigationNode = NavigationNode.Casino.Tournaments.WinSplit),
                )
            ),
            CategoryNode(navigationNode = NavigationNode.Casino.Lobby),
            CategoryNode(
                Res.string.category_casino_games,
                children = listOf(
                    CategoryNode(navigationNode = NavigationNode.Casino.Games.List),
                    CategoryNode(navigationNode = NavigationNode.Casino.Games.Types),
                    CategoryNode(navigationNode = NavigationNode.Casino.Games.Genres),
                    CategoryNode(navigationNode = NavigationNode.Casino.Games.Categories),
                    CategoryNode(navigationNode = NavigationNode.Casino.Games.Labels)
                )
            ),
            CategoryNode(
                Res.string.category_casino_jackpot_banner,
                children = listOf(
                    CategoryNode(navigationNode = NavigationNode.Casino.JackpotBanners.List),
                    CategoryNode(navigationNode = NavigationNode.Casino.JackpotBanners.Vendors),
                    CategoryNode(navigationNode = NavigationNode.Casino.JackpotBanners.Levels),
                )
            ),
            CategoryNode(navigationNode = NavigationNode.Casino.AlertCentre)
        )
    ),
    Bonuses(
        Res.string.category_bonuses,
        children = listOf(

        )
    ),
    Localization(
        Res.string.category_localization,
        children = listOf(

        )
    ),
    Contests(
        Res.string.category_contests,
        children = listOf(

        )
    ),
    Tests(
        Res.string.category_tests,
        children = listOf(

        )
    ),
    Administration(
        Res.string.category_administration,
        children = listOf(

        )
    ),
    Window(
        Res.string.category_window,
        children = listOf(

        )
    ),

    /** No parent category */
    None(null);
}