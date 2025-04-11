package model

case class Runway(id: Int, airport_ref: Int, airport_ident: String, length_ft: Int, width_ft: Int, surface: String,
                  lighted: Boolean, closed: Boolean, le_ident: String, le_latitude_deg: Double, le_longitude_deg: Double,
                  le_elevation_ft: Int, le_heading_degT: Int, le_displaced_threshold_ft: Int, he_ident: String,
                  he_latitude_deg: Double, he_longitude_deg: Double, he_elevation_ft: Int, he_heading_degT: Int,
                  he_displaced_threshold_ft: Int)
