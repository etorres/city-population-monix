package es.eriktorr.samples.population.tasks

import es.eriktorr.samples.population.models.CityPopulation
import org.apache.spark.sql.Dataset

case class TaskState(femaleSourceFile: String,
                     maleSourceFile: String,
                     femalePopulation: Dataset[CityPopulation],
                     malePopulation: Dataset[CityPopulation],
                     count: Long)
