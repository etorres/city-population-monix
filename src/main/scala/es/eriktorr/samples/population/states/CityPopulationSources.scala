package es.eriktorr.samples.population.states

import es.eriktorr.samples.population.flow.TaskState
import es.eriktorr.samples.population.models.CityPopulation
import org.apache.spark.sql.Dataset

case class CityPopulationSources(femaleSourceFile: String,
                                 maleSourceFile: String,
                                 dataSets: Seq[Dataset[CityPopulation]] = Seq.empty) extends TaskState
