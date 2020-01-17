package es.eriktorr.samples.population.steps

import es.eriktorr.samples.population.models.CityPopulation
import org.apache.spark.sql.Dataset

object RowCounter {
  def countRowsIn(cityPopulation: Seq[Dataset[CityPopulation]]): Long = {
    cityPopulation.map(_.count()).sum
  }
}
//    val a: Dataset[CityPopulation] = cityPopulation.head
//    val b: Dataset[CityPopulation] = cityPopulation.head
//
//    a.join(b, Seq("countryOrArea", "city", "cityType"), "inner").as[CityPopulation]
//    a.join(b, Seq("countryOrArea", "city", "cityType"), "inner")

//    a.groupBy(Seq(xxxxxx)).agg(xxxxxx)

//    a.joinWith(b, a("countryOrArea") === b("countryOrArea"), "inner")

//    cityPopulation.reduce((a, b) => a.joinWith(b, a("countryOrArea") === b("countryOrArea")))
// joinWith 'countryOrArea === 'countryOrArea && 'city === 'city && 'cityType === 'cityType
//      .filter('cityType === "Urban agglomeration")
//      .as[CityPopulation]