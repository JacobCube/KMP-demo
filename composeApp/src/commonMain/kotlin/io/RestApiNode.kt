package io

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/** Rest API request basic node */
open class RestApiNode(
    /** base route for the node */
    val route: String
) {
    /** full path to the specific node */
    open val path: String = route

    /** Demo tree */
    sealed class Demo: RestApiNode(
        route = "https://rickandmortyapi.com/api/"
    ) {

        data object Characters: Demo() {
            override val path: String = super.path + "/character/"

            @Serializable
            data class CharactersList(
                val info: PagingInfo? = null,
                val results: List<CharacterItem>? = null
            )

            /** Single character / detail */
            @Serializable
            data class CharacterItem(
                /** The id of the character. */
                val id: Long? = null,
                /** The name of the character. */
                val name: String? = null,
                /** The status of the character ('Alive', 'Dead' or 'unknown'). */
                val status: String? = null,
                /** The species of the character. */
                val species: String? = null,
                /** The type or subspecies of the character. */
                val type: String? = null,
                /** The gender of the character ('Female', 'Male', 'Genderless' or 'unknown'). */
                val gender: String? = null,
                /** Name and link to the character's origin location. */
                val origin: SourceIO? = null,
                /** Name and link to the character's last known location endpoint. */
                val location: SourceIO? = null,
                /** Link to the character's image. All images are 300x300px and most are medium shots or portraits since they are intended to be used as avatars. */
                val image: String? = null,
                /** List of episodes in which this character appeared. */
                val episode: List<String>? = null,
                /** Link to the character's own URL endpoint. */
                val url: String? = null,
                /** Time at which the character was created in the database. */
                val created: String? = null,
            )
        }

        data object Locations: Demo() {
            override val path: String = super.path + "/location/"

            @Serializable
            data class LocationsList(
                val info: PagingInfo? = null,
                val results: List<LocationItem>? = null
            )

            /** Single Location / detail */
            @Serializable
            data class LocationItem(
                /** The name of the location. */
                val name: String,
                /** The id of the location. */
                val id: Long,
                /** The type of the location. */
                val type: String,
                /** The dimension in which the location is located. */
                val dimension: String,
                /** List of character who have been last seen in the location. */
                val residents: List<String>,
                /** Link to the location's own endpoint. */
                val url: String,
                /** Time at which the location was created in the database. */
                val created: String,
            )
        }

        data object Episodes: Demo() {
            override val path: String = super.path + "/episode/"

            @Serializable
            data class EpisodesList(
                val info: PagingInfo? = null,
                val results: List<EpisodeItem>? = null
            )

            /** Single Episode / detail */
            @Serializable
            data class EpisodeItem(
                /** The id of the episode. */
                val id: Long,
                /** The name of the episode. */
                val name: String,
                /** The air date of the episode. */
                @SerialName("air_date")
                val airDate: String,
                /** The code of the episode. */
                val episode: String,
                /** List of characters who have been seen in the episode. */
                val characters: List<String>,
                /** Link to the episode's own endpoint. */
                val url: String,
                /** Time at which the episode was created in the database. */
                val created: String,
            )
        }
    }

    /** Casino tree */
    sealed class Casino: RestApiNode(
        route = "https://games.casino.czd1.k8s.tipsport.it/"
    ) {

        data object Vendors: Casino() {
            override val path: String = super.path + "api/v1/vendors"

            @Serializable
            data class VendorsIO(
                val id: Long,
                val name: String,
                val distinguishedGames: Boolean,
                val geoblockingEnabled: Boolean,
                val product: String
            )
        }

        data object Games: Casino() {
            override val path: String = super.path + "api/v1/games"
        }
    }

    /** Cats list */
    sealed class Cats: RestApiNode(
        route = "https://api.thecatapi.com/v1/"
    ) {

        data class CatImages(val limit: Int = 10): Cats() {
            override val path: String = super.path + "images/search?limit=$limit"

            @Serializable
            data class CatImageIO(
                val id: String,
                val url: String,
                val width: Int,
                val height: Int
            )
        }
    }
}