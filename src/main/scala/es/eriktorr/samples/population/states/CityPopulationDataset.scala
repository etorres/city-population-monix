package es.eriktorr.samples.population.states

import es.eriktorr.samples.population.flow.TaskState
import es.eriktorr.samples.population.models.CityPopulation
import org.apache.spark.sql.Dataset

case class CityPopulationDataset(dataSet: Dataset[CityPopulation]) extends TaskState
