package base

import com.russhwolf.settings.Settings
import io.kodein
import io.ktor.client.HttpClient
import org.kodein.di.instance

open class BaseRepository {
    protected val httpClient: HttpClient by kodein.instance()
    protected val settings: Settings by kodein.instance()
}