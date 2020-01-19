package es.eriktorr.samples.population.steps

import es.eriktorr.samples.population.models.CityPopulation
import org.apache.spark.sql.Dataset

object RowCounter {
  def countRowsIn(cityPopulation: Seq[Dataset[CityPopulation]]): Long = {
    cityPopulation.map(_.count()).sum
  }
}
