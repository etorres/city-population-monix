package es.eriktorr.samples.population.tasks

import es.eriktorr.samples.population.models.CityPopulation
import org.apache.spark.sql.Dataset

trait TaskState

case class SourceFiles(files: Seq[String]) extends TaskState
case class CityPopulationData(dataSets: Seq[Dataset[CityPopulation]]) extends TaskState
case class CityPopulationCount(count: Long) extends TaskState
