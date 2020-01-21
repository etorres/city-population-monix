package es.eriktorr.samples.population.tasks

import cats.data.IndexedStateT
import es.eriktorr.samples.population.models.{CityPopulation, UrbanAreaPopulation}
import monix.eval.Task
import org.apache.spark.sql.Dataset

trait TaskState

trait TaskStateN[SA <: TaskState] {
  val states: Seq[SA]
}

case class CityPopulationSources(femaleSourceFile: String,
                                 maleSourceFile: String,
                                 dataSets: Seq[Dataset[CityPopulation]] = Seq.empty) extends TaskState
case class CityPopulationDataset(dataSet: Dataset[CityPopulation]) extends TaskState
case class UrbanAreaPopulationDataset(dataSet: Dataset[UrbanAreaPopulation]) extends TaskState

object TaskState {
  object implicits {
    implicit class TaskStateOps[SA <: TaskState, SB <: TaskState](val state: IndexedStateT[Task, SA, SB, Unit]) {
      def >>[SC <: TaskState](nextState: IndexedStateT[Task, SB, SC, Unit]): IndexedStateT[Task, SA, SC, Unit] =
        state.flatMap(_ => nextState)
    }
  }
}
