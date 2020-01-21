package es.eriktorr.samples.population

import es.eriktorr.samples.population.models.{CityPopulation, UrbanAreaPopulation}
import es.eriktorr.samples.population.steps.UrbanAreasAggregator.urbanAreasTotalPopulationFrom
import org.apache.spark.sql.SparkSession

class UrbanAreasAggregatorSpec extends SetupDataset {
  "Aggregator" should "count total urban area population" in {
    assertDatasetEquals(urbanAreaDataset, urbanAreasTotalPopulationFrom(cityPopulationDataset))
  }

  private def urbanAreaDataset(implicit spark: SparkSession) = {
    import spark.implicits._
    Seq(
      aUrbanAreaPopulation(2009, 24779),
      aUrbanAreaPopulation(2010, 23505),
      aUrbanAreaPopulation(2011, 22205)
    ).toDF.as[UrbanAreaPopulation]
  }

  private def aUrbanAreaPopulation(year: Int, total: Double) = {
    UrbanAreaPopulation(countryOrArea = "Andorra",
      city = "ANDORRA LA VELLA",
      year = year,
      total = total)
  }

  private def cityPopulationDataset(implicit spark: SparkSession) = {
    import spark.implicits._
    Seq(
      aCityPopulation(2011, "Female", 11149),
      aCityPopulation(2010, "Female", 11755),
      aCityPopulation(2009, "Female", 12240),
      aCityPopulation(2011, "Male", 11056),
      aCityPopulation(2010, "Male", 11750),
      aCityPopulation(2009, "Male", 12539),
    ).toDF.as[CityPopulation]
  }

  private def aCityPopulation(year: Int, sex: String, value: Double) = CityPopulation(countryOrArea = "Andorra",
    year = year,
    area = "Total",
    sex = sex,
    city = "ANDORRA LA VELLA",
    cityType = "Urban agglomeration",
    recordType = "Estimate - de jure",
    reliability = "Final figure, complete",
    sourceYear = year + 1,
    value = value,
    valueFootnotes = 0)
}
