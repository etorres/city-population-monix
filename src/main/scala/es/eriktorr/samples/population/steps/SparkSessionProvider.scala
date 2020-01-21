package es.eriktorr.samples.population.steps

import org.apache.spark.sql.SparkSession

trait SparkSessionProvider {
  lazy implicit val spark: SparkSession = SparkSession.builder().getOrCreate()
}

object SparkSessionProvider {
  def localRunner: SparkSession = SparkSession.builder
    .appName("city-population")
    .master("local[*]")
    .getOrCreate()
}
