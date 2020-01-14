package es.eriktorr.samples.population.tasks

import cats.data.WriterT
import monix.eval.Task

import scala.concurrent.duration.FiniteDuration

class Observable {

  case class Timings(data: Map[String, FiniteDuration])

  type TimedTask[A] = WriterT[Task, Timings, A]

}
