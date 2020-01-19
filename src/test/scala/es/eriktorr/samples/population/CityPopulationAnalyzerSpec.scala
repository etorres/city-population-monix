package es.eriktorr.samples.population

import es.eriktorr.samples.population.CityPopulationAnalyzer.countCityPopulationIn
import es.eriktorr.samples.population.tasks.CityPopulationCount
import monix.execution.Scheduler.Implicits.global
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}

class CityPopulationAnalyzerSpec extends SetupDataset with ScalaFutures {
  "test" should "run" in {
    val future = countCityPopulationIn(pathToFile("data/city_female_population"),
      pathToFile("data/city_male_population")).runToFuture
    whenReady(future, timeout(Span(10, Seconds))) {
      case (taskState, _) => taskState shouldBe CityPopulationCount(5633)
    }
  }
}
