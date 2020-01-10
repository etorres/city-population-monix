package es.eriktorr.samples.population

import cats.effect.ExitCode
import monix.eval.{Task, TaskApp}

object CityPopulationApplication extends TaskApp {
  override def run(args: List[String]): Task[ExitCode] = ???
}
