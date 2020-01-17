package es.eriktorr.samples.population.steps

import es.eriktorr.samples.population.models.CityPopulation
import org.apache.spark.sql.{Dataset, SparkSession}

object BothGenderUrbanAgglomerationFilter {
  def filterPopulationIn(cityPopulation: Seq[Dataset[CityPopulation]])(implicit spark: SparkSession): Dataset[CityPopulation] = {
    import spark.implicits._
    cityPopulation.reduce((a, b) => a.union(b))
      .filter('cityType === "Urban agglomeration")
  }
}
