package es.eriktorr.samples.population.steps

import es.eriktorr.samples.population.models.UrbanAreaPopulation
import org.apache.spark.sql.Dataset

object UrbanAreasTotalPopulationExporter {
  val URBAN_AREAS_TOTAL_POPULATION_VIEW = "urban_areas_total_population_view"

  def apply(): UrbanAreasTotalPopulationExporter = new UrbanAreasTotalPopulationExporter()
}

class UrbanAreasTotalPopulationExporter extends SparkSessionProvider {
  def saveView(dataSet: Dataset[UrbanAreaPopulation]): Unit = {
    dataSet.createOrReplaceTempView(UrbanAreasTotalPopulationExporter.URBAN_AREAS_TOTAL_POPULATION_VIEW)
  }
}
