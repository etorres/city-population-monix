package es.eriktorr.samples.population.steps

import es.eriktorr.samples.population.models.CityPopulation
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions._

object CityPopulationCounter {
  def methodName(dataset: Dataset[CityPopulation])(implicit spark: SparkSession) = {
    import spark.implicits._

    dataset
      .where('area === "Total")
      .select('countryOrArea, 'city, 'year, 'value, 'sex)
      .groupBy('countryOrArea, 'city, 'year)
      .agg(sum('value).alias("total"), count('value).alias("count"), collect_list('sex).alias("gender"))
      .filter(('count === 2) && ('gender === Array("Female", "Male")))
      .select('countryOrArea, 'city, 'year, 'total)
      .sort('countryOrArea, 'city, 'year)


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