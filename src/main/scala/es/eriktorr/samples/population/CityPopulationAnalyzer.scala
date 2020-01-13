package es.eriktorr.samples.population

import es.eriktorr.samples.population.models.CityPopulation
import es.eriktorr.samples.population.steps.CityPopulationLoader.loadFrom
import monix.eval.Task
import org.apache.spark.sql.{Dataset, SparkSession}

object CityPopulationAnalyzer {
  def cityPopulationStatsFrom(femalePopulationFile: String, malePopulationFile: String): Task[Unit] = Task {
    
  }

  def buildSession: Task[SparkSession] = Task {
    SparkSession.builder
      .appName("city-population")
      .master("local[*]")
      .getOrCreate()
  }.memoizeOnSuccess
}
