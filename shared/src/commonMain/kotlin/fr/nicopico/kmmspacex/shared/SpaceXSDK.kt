package fr.nicopico.kmmspacex.shared

import fr.nicopico.kmmspacex.shared.cache.Database
import fr.nicopico.kmmspacex.shared.cache.DatabaseDriverFactory
import fr.nicopico.kmmspacex.shared.entity.RocketLaunch
import fr.nicopico.kmmspacex.shared.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    // @Throws annotation is necessary for proper error handling in Swift
    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}