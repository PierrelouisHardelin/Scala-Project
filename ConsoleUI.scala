package ui

import parser.CsvParser
import model.{Country, Airport, Runway}

object ConsoleUI {

  def run(): Unit = {
    println("Welcome to the Data Parser!")

    // Display menu options
    var continue = true
    while (continue) {
      println("\nPlease choose an option:")
      println("1. Query")
      println("2. Reports")
      println("3. Exit")
      print("> ")
      val choice = scala.io.StdIn.readInt()

      choice match {
        case 1 => query()
        case 2 => generateReports()
        case 3 =>
          println("Exiting the program.")
          continue = false
        case _ =>
          println("Invalid option. Please try again.")
      }
    }
  }

  def query(): Unit = {
    println("\nEnter the country name or code to query airports and runways:")
    val input = scala.io.StdIn.readLine().trim.toLowerCase

    // Try to find the country by partial name or code match
    val countries = CsvParser.loadCountries()
    val matchingCountries = countries.filter(c => c.name.toLowerCase.contains(input) || c.code.toLowerCase.contains(input))

    matchingCountries match {
      case Nil =>
        println(s"No countries found matching the input: $input")
        
      case _ =>
        matchingCountries.foreach { country =>
          println(s"\nCountry found: ${country.name} (${country.code})")

          // Filter airports and runways for this country
          val airports = CsvParser.loadAirports().filter(_.iso_country == country.code)
          val runways = CsvParser.loadRunways().filter(r => airports.exists(_.id == r.airport_ref))

          // Display airports and runways
          if (airports.nonEmpty) {
            println("\nAirports in this country:")
            airports.foreach(airport => println(s"- ${airport.name} (ID: ${airport.id})"))
          } else {
            println("No airports found for this country.")
          }

          if (runways.nonEmpty) {
            println("\nRunways at these airports:")
            runways.foreach(runway => println(s"- Runway ID: ${runway.id}, Airport Ref: ${runway.airport_ref}, Length: ${runway.length_ft} ft"))
          } else {
            println("No runways found for these airports.")
          }
        }
    }
  }

  def generateReports(): Unit = {
    println("\nGenerating Reports...")

    // Report 1: 10 countries with the highest number of airports
    val countries = CsvParser.loadCountries()
    val airports = CsvParser.loadAirports()
    val airportsByCountry = airports.groupBy(_.iso_country).view.mapValues(_.size).toMap

    val highestAirports = airportsByCountry.toSeq.sortBy(-_._2).take(10)
    println("\nTop 10 countries with the highest number of airports:")
    highestAirports.foreach { case (countryCode, count) =>
      val country = countries.find(_.code == countryCode).getOrElse(Country(0, countryCode, "", "", "", ""))
      println(s"${country.name} (${country.code}) - $count airports")
    }

    // Report 2: Countries with the lowest number of airports
    val lowestAirports = airportsByCountry.toSeq.sortBy(_._2).take(10)
    println("\nTop 10 countries with the lowest number of airports:")
    lowestAirports.foreach { case (countryCode, count) =>
      val country = countries.find(_.code == countryCode).getOrElse(Country(0, countryCode, "", "", "", ""))
      println(s"${country.name} (${country.code}) - $count airports")
    }

    // Report 3: Type of runways per country
    val runways = CsvParser.loadRunways()
    val runwaysByCountry = runways.groupBy(r => airports.find(_.id == r.airport_ref).map(_.iso_country).getOrElse(""))

    println("\nRunway types per country:")
    runwaysByCountry.foreach { case (countryCode, runways) =>
      val country = countries.find(_.code == countryCode).getOrElse(Country(0, countryCode, "", "", "", ""))
      val runwayTypes = runways.map(_.surface).distinct.mkString(", ")
      println(s"${country.name} (${country.code}) - Types of runways: $runwayTypes")
    }

    // Report 4: Top 10 most common runway latitudes
    val runwayLatitudes = runways.groupBy(_.le_ident).view.mapValues(_.size).toMap
    val topLatitudes = runwayLatitudes.toSeq.sortBy(-_._2).take(10)

    println("\nTop 10 most common runway latitudes:")
    topLatitudes.foreach { case (latitude, count) =>
      println(s"Latitude: $latitude - $count occurrences")
    }
  }
}
