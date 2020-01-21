package es.eriktorr.samples.population

//import es.eriktorr.samples.population.models.CityPopulation
//import es.eriktorr.samples.population.steps.CityPopulationLoader.loadFrom
//import .countRowsIn
//import es.eriktorr.samples.population.tasks.Retryable.implicits._
//import monix.eval.Task
//import monix.execution.Scheduler.Implicits.global
//import monix.execution.schedulers.SchedulerService
//import monix.execution.{CancelableFuture, Scheduler}
//import org.apache.spark.sql.{Dataset, SparkSession}
import org.scalatest.concurrent.ScalaFutures
//import org.scalatest.time.{Seconds, Span}
//
//import scala.util.control.NonFatal
//import scala.util.{Failure, Success}

class ExecutionSpec extends SetupDataset with ScalaFutures {
//  val blockingScheduler: SchedulerService = Scheduler.io()
//
//  "test" should "work" in {
//    whenReady(runCityPopulationPipeline, timeout(Span(10, Seconds))) { count =>
//      count shouldBe 28366
//    }
//  }
//
//  def buildSession: Task[SparkSession] = Task {
//    spark
//  }.memoizeOnSuccess
//
//  def loadCityPopulation(pathToFile: String): Task[Dataset[CityPopulation]] = Task {
//    loadFrom(pathToFile)
//  }.executeOn(blockingScheduler).retryOnFailure()
//
//  def count(cityPopulation: Seq[Dataset[CityPopulation]]): Task[Long] = Task {
//    countRowsIn(cityPopulation)
//  }
//
//  def countCityPopulation: Task[Long] = for {
//    sparkSession <- buildSession
//    result <- {
//      implicit val session: SparkSession = sparkSession
//      for {
//        femalePopulation <- loadCityPopulation(pathToFile("data/city_female_population"))
//        malePopulation <- loadCityPopulation(pathToFile("data/city_male_population"))
//        count <- count(Seq(femalePopulation, malePopulation))
//      } yield count
//    }
//  } yield result
//
//  def runCityPopulationPipeline: CancelableFuture[Long] = {
//    val cancelableFuture = countCityPopulation.runToFuture
//    cancelableFuture.onComplete {
//      case Success(count) => println(s"SUCCESS: $count")
//      case Failure(exception) => exception match {
//        case NonFatal(nonFatal) => println(s"WARNING: ${nonFatal.getMessage}")
//        case _ => println(s"ERROR: ${exception.getMessage}")
//      }
//    }
//    cancelableFuture
//  }
}
