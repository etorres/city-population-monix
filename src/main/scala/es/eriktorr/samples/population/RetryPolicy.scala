package es.eriktorr.samples.population

import monix.eval.Task
import org.apache.spark.sql.Dataset

import scala.concurrent.duration._

trait RetryPolicy[A] {
  def retryPolicy(task: A, times: Int, delayBetweenAttempts: FiniteDuration): A
}

object RetryPolicy {
  def apply[A](retryFunction: (A, Int, FiniteDuration) => A): RetryPolicy[A] =
    (task: A, times: Int, delayBetweenAttempts: FiniteDuration) => retryFunction(task, times, delayBetweenAttempts)

  object implicits {
    implicit class RetryPolicyOps[A](val task: A) extends AnyVal {
      def retryPolicy(times: Int, delayBetweenAttempts: FiniteDuration = 2.seconds)(implicit retryable: RetryPolicy[A]): A =
        retryable.retryPolicy(task, times, delayBetweenAttempts)
    }
  }

  implicit val defaultRetry: RetryPolicy[Task[Dataset[CityPopulation]]] =
    RetryPolicy[Task[Dataset[CityPopulation]]]((task, times, delayBetweenAttempts) => withRetryPolicy(task, times, delayBetweenAttempts))

  def withRetryPolicy[V](source: Task[V], times: Int, delayBetweenAttempts: FiniteDuration): Task[V] =
    source.onErrorHandleWith { err =>
      if (times <= 0) Task.raiseError(err) else {
        withRetryPolicy(source, times - 1, delayBetweenAttempts)
          .delayExecution(delayBetweenAttempts)
      }
    }
}
