package es.eriktorr.samples.population

import es.eriktorr.samples.population.steps.CityPopulationReader.cityPopulationFrom
import org.scalatest.prop.TableDrivenPropertyChecks

class LoadCityPopulationSpec extends SetupDataset with TableDrivenPropertyChecks {
  private val csvPaths = Table(("pathName", "count"),
    ("data/city_female_population", 14181),
    ("data/city_male_population", 14185))

  "City population" should " be loaded from its CSV representation" in {
    forAll(csvPaths) { (pathName, count) =>
      cityPopulationFrom(pathToFile(pathName)).count() shouldBe count
    }
  }
}
