package es.eriktorr.samples.population.tasks

import es.eriktorr.samples.population.models.CityPopulation
import monix.eval.Task
import org.apache.spark.sql.Dataset

import scala.concurrent.duration._

trait Retryable[A] {
  def retryOnFailure(task: A, times: Int, delayBetweenAttempts: FiniteDuration): A
}

object Retryable {
  def apply[A](retryPolicy: (A, Int, FiniteDuration) => A): Retryable[A] =
    (task: A, times: Int, delayBetweenAttempts: FiniteDuration) => retryPolicy(task, times, delayBetweenAttempts)

  object implicits {
    implicit class RetryableOps[A](val task: A) {
      def retryOnFailure(times: Int = 3, delayBetweenAttempts: FiniteDuration = 2.seconds)(implicit retryable: Retryable[A]): A =
        retryable.retryOnFailure(task, times, delayBetweenAttempts)
    }
  }

  implicit val cityPopulationRetry: Retryable[Task[Dataset[CityPopulation]]] =
    Retryable[Task[Dataset[CityPopulation]]]((task, times, delayBetweenAttempts) => withRetryPolicy(task, times, delayBetweenAttempts))

  private def withRetryPolicy[V](source: Task[V], times: Int, delayBetweenAttempts: FiniteDuration): Task[V] =
    source.onErrorHandleWith { err =>
      if (times <= 0) Task.raiseError(err) else {
        withRetryPolicy(source, times - 1, delayBetweenAttempts)
          .delayExecution(delayBetweenAttempts)
      }
    }
}
