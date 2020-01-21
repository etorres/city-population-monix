package es.eriktorr.samples.population.steps

import es.eriktorr.samples.population.models.CityPopulation
import org.apache.spark.sql.{Dataset, Encoders}

object CityPopulationLoader extends SparkSessionProvider {
  def loadFrom(pathToFile: String): Dataset[CityPopulation] = {
    import spark.implicits._
    val schema = Encoders.product[CityPopulation].schema
    spark.read.schema(schema).csv(pathToFile).as[CityPopulation]
  }
}
