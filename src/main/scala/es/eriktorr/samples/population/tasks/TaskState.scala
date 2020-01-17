package es.eriktorr.samples.population.tasks

import cats.data.IndexedStateT
import es.eriktorr.samples.population.models.CityPopulation
import monix.eval.Task
import org.apache.spark.sql.Dataset

trait TaskState

case class SourceFiles(files: Seq[String]) extends TaskState
case class CityPopulationData(dataSets: Seq[Dataset[CityPopulation]]) extends TaskState
case class CityPopulationCount(count: Long) extends TaskState

object TaskState {
  object implicits {
    implicit class TaskStateOps[SA <: TaskState, SB <: TaskState](val state: IndexedStateT[Task, SA, SB, Unit]) {
      def >>[SC <: TaskState](nextState: IndexedStateT[Task, SB, SC, Unit]): IndexedStateT[Task, SA, SC, Unit] =
        state.flatMap(_ => nextState)
    }
  }
}
