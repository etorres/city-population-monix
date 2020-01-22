package es.eriktorr.samples.population.steps

import es.eriktorr.samples.population.models.UrbanAreaPopulation
import org.apache.spark.sql.Dataset

object UrbanAreasTotalPopulationExporter extends SparkSessionProvider {
  def urbanAreasTotalPopulationTo(pathToFile: String, dataSet: Dataset[UrbanAreaPopulation]): Unit = {
//    dataSet.write
//      .jdbc()
  }
}
