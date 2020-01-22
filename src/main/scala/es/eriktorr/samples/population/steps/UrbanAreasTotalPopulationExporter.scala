package es.eriktorr.samples.population.steps

import es.eriktorr.samples.population.models.UrbanAreaPopulation
import org.apache.spark.sql.Dataset

object UrbanAreasTotalPopulationExporter extends SparkSessionProvider {
  val URBAN_AREAS_TOTAL_POPULATION_VIEW = "urban_areas_total_population_view"

  def saveUrbanAreasTotalPopulationView(dataSet: Dataset[UrbanAreaPopulation]): Unit = {
    dataSet.createOrReplaceTempView(URBAN_AREAS_TOTAL_POPULATION_VIEW)
  }
}
