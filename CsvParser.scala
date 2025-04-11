package parser

import model.{Country, Airport, Runway}
import com.github.tototoshi.csv._

import scala.io.Source

object CsvParser {
  def loadCountries(): List[Country] = {
    val source = Source.fromResource("countries.csv")
    val reader = CSVReader.open(source.bufferedReader())
    val rows = reader.allWithHeaders() // Get rows with headers as a map
    rows.map { row =>
      try {
        val id = row("id").toInt
        val code = row("code")
        val name = row("name")
        val continent = row("continent")
        val wikipedia_link = row("wikipedia_link")
        val keywords = row("keywords")
        Country(id, code, name, continent, wikipedia_link, keywords)
      } catch {
        case e: Exception =>
          println(s"Error parsing country line: ${row.toString}")
          throw e
      }
    }
  }

  def loadAirports(): List[Airport] = {
    val source = Source.fromResource("airports.csv")
    val reader = CSVReader.open(source.bufferedReader())
    val rows = reader.allWithHeaders() // Get rows with headers as a map
    rows.map { row =>
      try {
        val id = row("id").toInt
        val ident = row("ident")
        val _type = row.getOrElse("_type", "") // Use empty string if _type is missing
        val name = row("name")
        val latitude_deg = try {
          if (row("latitude_deg").isEmpty) 0.0 else row("latitude_deg").toDouble
        } catch {
          case _: NumberFormatException => 0.0
        }
        val longitude_deg = try {
          if (row("longitude_deg").isEmpty) 0.0 else row("longitude_deg").toDouble
        } catch {
          case _: NumberFormatException => 0.0
        }
        
        // Handle empty or invalid elevation_ft
        val elevation_ft = try {
          if (row("elevation_ft").isEmpty) 0
          else row("elevation_ft").toInt
        } catch {
          case _: NumberFormatException => 0
        }
        
        val continent = row("continent")
        val iso_country = row("iso_country")
        val iso_region = row("iso_region")
        val municipality = row("municipality")
        val scheduled_service = row("scheduled_service")
        val gps_code = row("gps_code")
        val iata_code = row("iata_code")
        val local_code = row("local_code")
        val home_link = row("home_link")
        val wikipedia_link = row("wikipedia_link")
        val keywords = row("keywords")
        
        Airport(id, ident, _type, name, latitude_deg, longitude_deg, elevation_ft, continent, iso_country, iso_region, municipality, scheduled_service, gps_code, iata_code, local_code, home_link, wikipedia_link, keywords)
      } catch {
        case e: Exception =>
          println(s"Error parsing airport line: ${row.toString}")
          throw e
      }
    }
  }

  def loadRunways(): List[Runway] = {
    val source = Source.fromResource("runways.csv")
    val reader = CSVReader.open(source.bufferedReader())
    val rows = reader.allWithHeaders() // Get rows with headers as a map
    rows.map { row =>
      try {
        val id = row("id").toInt
        val airport_ref = row("airport_ref").toInt
        val airport_ident = row("airport_ident")
        val length_ft = try {
          if (row("length_ft").isEmpty) 0 else row("length_ft").toInt
        } catch {
          case _: NumberFormatException => 0
        }
        val width_ft = try {
          if (row("width_ft").isEmpty) 0 else row("width_ft").toInt
        } catch {
          case _: NumberFormatException => 0
        }
        val surface = row("surface")
        
        // Convert "1" and "0" to boolean for lighted and closed fields
        val lighted = row("lighted") match {
          case "1" => true
          case "0" => false
          case _   => throw new IllegalArgumentException(s"Invalid value for lighted: ${row("lighted")}")
        }
        
        val closed = row("closed") match {
          case "1" => true
          case "0" => false
          case _   => throw new IllegalArgumentException(s"Invalid value for closed: ${row("closed")}")
        }
        
        val le_ident = row("le_ident")
        val le_latitude_deg = try {
          if (row("le_latitude_deg").isEmpty) 0.0 else row("le_latitude_deg").toDouble
        } catch {
          case _: NumberFormatException => 0.0
        }
        val le_longitude_deg = try {
          if (row("le_longitude_deg").isEmpty) 0.0 else row("le_longitude_deg").toDouble
        } catch {
          case _: NumberFormatException => 0.0
        }
        val le_elevation_ft = try {
          if (row("le_elevation_ft").isEmpty) 0 else row("le_elevation_ft").toInt
        } catch {
          case _: NumberFormatException => 0
        }
        val le_heading_degT = try {
          if (row("le_heading_degT").isEmpty) 0 else row("le_heading_degT").toInt
        } catch {
          case _: NumberFormatException => 0
        }
        val le_displaced_threshold_ft = try {
          if (row("le_displaced_threshold_ft").isEmpty) 0 else row("le_displaced_threshold_ft").toInt
        } catch {
          case _: NumberFormatException => 0
        }
        
        val he_ident = row("he_ident")
        val he_latitude_deg = try {
          if (row("he_latitude_deg").isEmpty) 0.0 else row("he_latitude_deg").toDouble
        } catch {
          case _: NumberFormatException => 0.0
        }
        val he_longitude_deg = try {
          if (row("he_longitude_deg").isEmpty) 0.0 else row("he_longitude_deg").toDouble
        } catch {
          case _: NumberFormatException => 0.0
        }
        val he_elevation_ft = try {
          if (row("he_elevation_ft").isEmpty) 0 else row("he_elevation_ft").toInt
        } catch {
          case _: NumberFormatException => 0
        }
        val he_heading_degT = try {
          if (row("he_heading_degT").isEmpty) 0 else row("he_heading_degT").toInt
        } catch {
          case _: NumberFormatException => 0
        }
        val he_displaced_threshold_ft = try {
          if (row("he_displaced_threshold_ft").isEmpty) 0 else row("he_displaced_threshold_ft").toInt
        } catch {
          case _: NumberFormatException => 0
        }

        Runway(id, airport_ref, airport_ident, length_ft, width_ft, surface, lighted, closed, le_ident, le_latitude_deg, le_longitude_deg, le_elevation_ft, le_heading_degT, le_displaced_threshold_ft, he_ident, he_latitude_deg, he_longitude_deg, he_elevation_ft, he_heading_degT, he_displaced_threshold_ft)
      } catch {
        case e: Exception =>
          println(s"Error parsing runway line: ${row.toString}")
          throw e
      }
    }
  }
}
