package fr.nicopico.kmmspacex.shared.cache

import fr.nicopico.kmmspacex.shared.entity.Links
import fr.nicopico.kmmspacex.shared.entity.RocketLaunch
import fr.nicopico.kmmspacex.shared.entity.Rocket

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = SpaceXDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.spaceXDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllRockets()
            dbQuery.removeAllLaunches()
        }
    }

    internal fun getAllLaunches(): List<RocketLaunch> {
        return dbQuery.selectAllLaunchesInfo(::mapSelectLaunches).executeAsList()
    }

    private fun mapSelectLaunches(
        flightNumber: Long,
        missionName: String,
        launchYear: Int,
        rocketId: String,
        details: String?,
        launchSuccess: Boolean?,
        launchDateUTC: String,
        missionPatchUrl: String?,
        articleUrl: String?,
        rocket_id: String?,
        name: String?,
        type: String?
    ): RocketLaunch {
        return RocketLaunch(
            flightNumber = flightNumber.toInt(),
            missionName = missionName,
            launchYear = launchYear,
            details = details,
            launchDateUTC = launchDateUTC,
            launchSuccess = launchSuccess,
            rocket = Rocket(
                id = rocketId,
                name = name!!,
                type = type!!
            ),
            links = Links(
                missionPatchUrl = missionPatchUrl,
                articleUrl = articleUrl
            )
        )
    }

    internal fun createLaunches(launches: List<RocketLaunch>) {
        dbQuery.transaction {
            launches.forEach { launch ->
                val rocket = dbQuery.selectRocketById(launch.rocket.id).executeAsOneOrNull()
                if (rocket == null) {
                    insertRocket(launch)
                }

                insertLaunch(launch)
            }
        }
    }

    private fun insertRocket(launch: RocketLaunch) {
        dbQuery.insertRocket(
            id = launch.rocket.id,
            name = launch.rocket.name,
            type = launch.rocket.type
        )
    }

    private fun insertLaunch(launch: RocketLaunch) {
        dbQuery.insertLaunch(
            flightNumber = launch.flightNumber.toLong(),
            missionName = launch.missionName,
            launchYear = launch.launchYear,
            rocketId = launch.rocket.id,
            details = launch.details,
            launchSuccess = launch.launchSuccess ?: false,
            launchDateUTC = launch.launchDateUTC,
            missionPatchUrl = launch.links.missionPatchUrl,
            articleUrl = launch.links.articleUrl
        )
    }
}