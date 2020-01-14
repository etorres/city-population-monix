package es.eriktorr.samples.population

import cats.data.IndexedStateT
import cats.data.IndexedStateT.modifyF
import es.eriktorr.samples.population.steps.CityPopulationLoader.loadFrom
import es.eriktorr.samples.population.steps.RowCounter.countRowsIn
import es.eriktorr.samples.population.tasks.Retryable.implicits._
import es.eriktorr.samples.population.tasks.{CityPopulationCount, CityPopulationData, SourceFiles, TaskState}
import monix.eval.Task
import monix.eval.Task.gatherN
import org.apache.spark.sql.SparkSession

object CityPopulationAnalyzer {
  def countCityPopulationIn(femaleSourceFile: String, maleSourceFile: String): Task[(TaskState, Unit)] = {
    val initialState = SourceFiles(Seq(femaleSourceFile, maleSourceFile))
    cityPopulationCounter.run(initialState)
  }

  def cityPopulationCounter: IndexedStateT[Task, SourceFiles, CityPopulationCount, Unit] = for {
    _ <- loadCityPopulation
    _ <- countCityPopulation
  } yield ()

  def loadCityPopulation: IndexedStateT[Task, SourceFiles, CityPopulationData, Unit] = modifyF { state =>
    buildSession.bracket { spark =>
      implicit val sparkSession: SparkSession = spark
      val loadFileLTasks = state.files.map(file => Task { loadFrom(file) }.retryOnFailure())
      gatherN(2)(loadFileLTasks).map(dataSets => CityPopulationData(dataSets))
    } {
      doNothing()
    }
  }

  def buildSession: Task[SparkSession] = Task {
    SparkSession.builder
      .appName("city-population")
      .master("local[*]")
      .getOrCreate()
  }.memoizeOnSuccess

  def countCityPopulation: IndexedStateT[Task, CityPopulationData, CityPopulationCount, Unit] = modifyF { state =>
    Task {
      val count = countRowsIn(state.dataSets)
      println(s"\n\n >> City population count: $count\n")
      CityPopulationCount(count = count)
    }
  }

  private def doNothing(): SparkSession => Task[Unit] = _ => Task.unit
}
