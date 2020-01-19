package es.eriktorr.samples.population

import es.eriktorr.samples.population.models.CityPopulation
import es.eriktorr.samples.population.steps.PeopleLivingInUrbanAreasFilter.peopleLivingInUrbanAreasFrom
import es.eriktorr.samples.population.steps.{CityPopulationCounter, CityPopulationLoader, PeopleLivingInUrbanAreasFilter}
import org.apache.spark.sql.SparkSession

class CityPopulationCounterSpec extends SetupDataset {
  "test" should "work" in {
    val spark = SparkSession.builder().getOrCreate()
    import spark.implicits._
    val dataset = Seq(CityPopulation(countryOrArea = "Andorra",
      year = 2010,
      area = "Total",
      sex = "Female",
      city = "ANDORRA LA VELLA",
      cityType = "Urban agglomeration",
      recordType = "Estimate - de jure",
      reliability = "Final figure, complete",
      sourceYear = 2010,
      value = 11755.0,
      valueFootnotes = 0))
      .toDF.as[CityPopulation]

    val female = CityPopulationLoader.loadFrom(pathToFile("data/city_female_population"))
    val male = CityPopulationLoader.loadFrom(pathToFile("data/city_male_population"))
    val both = peopleLivingInUrbanAreasFrom(Seq(female, male))

    // TODO
    CityPopulationCounter.methodName(both).show()
    // TODO

  }
}
