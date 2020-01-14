package es.eriktorr.samples.population

import cats.data.StateT
import es.eriktorr.samples.population.steps.CityPopulationLoader.loadFrom
import es.eriktorr.samples.population.steps.RowCounter
import es.eriktorr.samples.population.tasks.TaskState
import monix.eval.Task
import org.apache.spark.sql.SparkSession

object CityPopulationAnalyzer {
  def cityPopulationStatsFrom(femaleSourceFile: String, maleSourceFile: String): Task[(TaskState, Unit)] = {
    val initialState = TaskState(femaleSourceFile, maleSourceFile, null, null, 0L)
    stateProgram.run(initialState)
  }

  def stateProgram: StateAction[Unit] = for {
    _ <- loadCityPopulation
    _ <- countCityPopulation
  } yield ()

  def loadCityPopulation: StateAction[Unit] = StateT.modifyF { state =>
    buildSession.bracket { spark =>
      implicit val sparkSession: SparkSession = spark
      Task {
        state.copy(femalePopulation = loadFrom(state.femaleSourceFile), malePopulation = loadFrom(state.maleSourceFile))
      }
    } { _ => Task.unit }
  }

  def buildSession: Task[SparkSession] = Task {
    SparkSession.builder
      .appName("city-population")
      .master("local[*]")
      .getOrCreate()
  }.memoizeOnSuccess

  def countCityPopulation: StateAction[Unit] = StateT.modifyF { state =>
    Task {
      val count = RowCounter.countRowsIn(Seq(state.femalePopulation, state.malePopulation))
      println(s"\n\n >> HERE: City population count: $count\n")
      state.copy(count = count)
    }
  }
}
