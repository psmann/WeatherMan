package one.mann.weatherman.framework.data

import one.mann.domain.model.Location
import one.mann.weatherman.framework.data.database.LocationTuple

internal fun LocationTuple.mapToDomain(): Location = Location(arrayOf(coordinatesLat, coordinatesLong))