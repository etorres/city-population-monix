package es.eriktorr.samples.population

import es.eriktorr.samples.population.CityPopulationAnalyzer.peopleLivingInUrbanAreasFrom
import es.eriktorr.samples.population.steps.UrbanAreasTotalPopulationExporter
import monix.execution.Scheduler.Implicits.global
import org.apache.spark.sql.DataFrame
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}

case class SampledUrbanAreaPopulation(countryOrArea: String,
                                      city: String,
                                      sum: Option[Double])

class CityPopulationAnalyzerSpec extends SetupDataset with ScalaFutures {
  "Task execution" should "create a view with the total population of the urban areas" in {
    val future = peopleLivingInUrbanAreasFrom(pathToFile("data/city_female_population"),
      pathToFile("data/city_male_population")).runToFuture
    whenReady(future, timeout(Span(10, Seconds))) {
      case (_, _) =>
        import spark.implicits._
        val expectedDataset = Seq(SampledUrbanAreaPopulation(
          countryOrArea = "Andorra", city = "ANDORRA LA VELLA", sum = Option(188738.0)
        )).toDF
        assertDataFrameEquals(expectedDataset, sampleView)
    }
  }

  def sampleView: DataFrame = {
    import spark.sql
    sql(s"""SELECT countryOrArea, city, SUM(total) AS sum
           |FROM ${UrbanAreasTotalPopulationExporter.URBAN_AREAS_TOTAL_POPULATION_VIEW}
           |WHERE countryOrArea = 'Andorra' AND city = 'ANDORRA LA VELLA'
           |GROUP BY countryOrArea, city""".stripMargin)
  }
}
