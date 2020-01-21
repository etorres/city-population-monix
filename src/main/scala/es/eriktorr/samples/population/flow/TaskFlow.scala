package es.eriktorr.samples.population.flow

import cats.data.IndexedStateT
import es.eriktorr.samples.population.steps.SparkSessionProvider
import monix.eval.Task
import org.apache.spark.sql.SparkSession

trait TaskFlow {
  lazy val buildSession: Task[SparkSession] = Task {
    SparkSessionProvider.localRunner
  }.memoizeOnSuccess

  def transform[SA <: TaskState, SB <: TaskState](fun: SA => SB): IndexedStateT[Task, SA, SB, Unit] = IndexedStateT.modifyF { state =>
    buildSession.flatMap(_ => {
      Task {
        fun.apply(state)
      }
    })
  }
}

object TaskFlow {
  object implicits {
    implicit class TaskFlowOps[SA <: TaskState, SB <: TaskState](val state: IndexedStateT[Task, SA, SB, Unit]) {
      def >>[SC <: TaskState](nextState: IndexedStateT[Task, SB, SC, Unit]): IndexedStateT[Task, SA, SC, Unit] =
        state.flatMap(_ => nextState)
    }
  }
}
