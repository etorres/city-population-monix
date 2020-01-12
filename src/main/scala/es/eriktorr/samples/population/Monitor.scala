package es.eriktorr.samples.population

import cats.data.WriterT
import monix.eval.Task

import scala.concurrent.duration.FiniteDuration

class Monitor {

  case class Timings(data: Map[String, FiniteDuration])

  type TimedTask[A] = WriterT[Task, Timings, A]

}
