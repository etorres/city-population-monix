package es.eriktorr.samples.population

import es.eriktorr.samples.population.CityPopulationAnalyzer.peopleLivingInUrbanAreasFrom
import es.eriktorr.samples.population.states.UrbanAreaPopulationDataset
import monix.execution.Scheduler.Implicits.global
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}

class CityPopulationAnalyzerSpec extends SetupDataset with ScalaFutures {
  "test" should "run" in {
    val future = peopleLivingInUrbanAreasFrom(pathToFile("data/city_female_population"),
      pathToFile("data/city_male_population")).runToFuture
    whenReady(future, timeout(Span(10, Seconds))) {
      case (taskState, _) => {

        // TODO
        taskState.asInstanceOf[UrbanAreaPopulationDataset].dataSet.show()
        // TODO

        taskState shouldBe UrbanAreaPopulationDataset(null)
      }
    }
  }
}
