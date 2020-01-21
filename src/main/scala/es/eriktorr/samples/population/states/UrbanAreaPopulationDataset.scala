package es.eriktorr.samples.population.states

import es.eriktorr.samples.population.flow.TaskState
import es.eriktorr.samples.population.models.UrbanAreaPopulation
import org.apache.spark.sql.Dataset

case class UrbanAreaPopulationDataset(dataSet: Dataset[UrbanAreaPopulation]) extends TaskState
