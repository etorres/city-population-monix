package es.eriktorr.samples.population.steps

import es.eriktorr.samples.population.models.{CityPopulation, UrbanAreaPopulation}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions._

object UrbanAreasAggregator {
  def totalUrbanAreaPopulationFrom(dataset: Dataset[CityPopulation])(implicit spark: SparkSession): Dataset[UrbanAreaPopulation] = {
    import spark.implicits._
    dataset
      .where('area === "Total")
      .groupBy('countryOrArea, 'city, 'year)
      .agg(sum('value).alias("total"), count('value).alias("count"), collect_list('sex).alias("gender"))
      .filter(('count === 2) && ('gender === Array("Female", "Male")))
      .select('countryOrArea, 'city, 'year, 'total)
      .sort('countryOrArea, 'city, 'year)
      .as[UrbanAreaPopulation]
  }
}
