package es.eriktorr.samples.population.steps

import es.eriktorr.samples.population.models.CityPopulation
import org.apache.spark.sql.Dataset

object UrbanAreasFilter {
  def apply(): UrbanAreasFilter = new UrbanAreasFilter()
}

class UrbanAreasFilter extends SparkSessionProvider {
  def onlyUrbanAreasFrom(cityPopulation: Seq[Dataset[CityPopulation]]): Dataset[CityPopulation] = {
    import spark.implicits._
    cityPopulation.reduce((a, b) => a.union(b))
      .filter('cityType === "Urban agglomeration")
  }
}
