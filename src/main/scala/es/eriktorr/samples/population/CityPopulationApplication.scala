package es.eriktorr.samples.population

import cats.effect.ExitCase.{Canceled, Completed, Error}
import cats.effect.ExitCode
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import es.eriktorr.samples.population.CityPopulationAnalyzer.countCityPopulation
import monix.eval.{Task, TaskApp}

object CityPopulationApplication extends TaskApp with LazyLogging {
  override def run(args: List[String]): Task[ExitCode] = args match {
    case (femalePopulationFile: String) :: (malePopulationFile: String) :: _ =>
      countCityPopulation(femalePopulationFile, malePopulationFile).guaranteeCase {
        case Completed | Canceled => Task(logger.info("Completed"))
        case Error(err) => Task(logger.error(err.getMessage))
      }.as(ExitCode.Success)
    case _ => printUsage().as(ExitCode.Error)
  }

  private def printUsage(): Task[Unit] = Task {
    Console.err.println("Usage: <female population> <male population>")
  }
}
