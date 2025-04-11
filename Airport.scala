package model

case class Airport(id: Int, ident: String, _type: String, name: String, latitude_deg: Double, longitude_deg: Double,
                   elevation_ft: Int, continent: String, iso_country: String, iso_region: String,
                   municipality: String, scheduled_service: String, gps_code: String, iata_code: String,
                   local_code: String, home_link: String, wikipedia_link: String, keywords: String)
