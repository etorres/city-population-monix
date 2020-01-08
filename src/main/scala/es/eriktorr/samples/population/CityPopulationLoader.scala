package es.eriktorr.samples.population

import org.apache.spark.sql.{Dataset, Encoders, SparkSession}

object CityPopulationLoader {
  def loadFrom(pathToFile: String)(implicit spark: SparkSession): Dataset[CityPopulation] = {
    import spark.implicits._
    val schema = Encoders.product[CityPopulation].schema
    spark.read.schema(schema).csv(pathToFile).as[CityPopulation]
  }
}
