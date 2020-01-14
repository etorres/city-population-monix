package es.eriktorr.samples

import cats.data.StateT
import es.eriktorr.samples.population.tasks.TaskState
import monix.eval.Task

package object population {
  type StateAction[A] = StateT[Task, TaskState, A]
}
