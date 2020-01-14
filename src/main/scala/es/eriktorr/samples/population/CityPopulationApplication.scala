package es.eriktorr.samples.population

import cats.effect.ExitCase.{Canceled, Completed, Error}
import cats.effect.ExitCode
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import es.eriktorr.samples.population.CityPopulationAnalyzer.countCityPopulationIn
import monix.eval.{Task, TaskApp}

object CityPopulationApplication extends TaskApp with LazyLogging {
  override def run(args: List[String]): Task[ExitCode] = args match {
    case (femalePopulationFile: String) :: (malePopulationFile: String) :: _ => logOnComplete(
      countCityPopulationIn(femalePopulationFile, malePopulationFile)
    ).as(ExitCode.Success)
    case _ => printUsage().as(ExitCode.Error)
  }

  private def logOnComplete[A](source: Task[A]): Task[A] = {
    source.guaranteeCase {
      case Completed => Task(logger.info("Task completed"))
      case Canceled => Task(logger.info("Task canceled"))
      case Error(err) => Task(logger.error(err.getMessage))
      case _ => Task(logger.warn("Unknown exit condition"))
    }
  }

  private def printUsage(): Task[Unit] = Task {
    Console.err.println("Usage: <female population> <male population>")
  }
}
