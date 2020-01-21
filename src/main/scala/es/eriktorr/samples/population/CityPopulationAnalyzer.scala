package es.eriktorr.samples.population

import cats.data.{IndexedStateT, StateT}
import es.eriktorr.samples.population.flow.TaskFlow.implicits._
import es.eriktorr.samples.population.flow.{TaskFlow, TaskState}
import es.eriktorr.samples.population.states.{CityPopulationDataset, CityPopulationSources, UrbanAreaPopulationDataset}
import es.eriktorr.samples.population.steps.CityPopulationLoader.loadFrom
import es.eriktorr.samples.population.steps.UrbanAreasAggregator.totalUrbanAreaPopulationFrom
import es.eriktorr.samples.population.steps.UrbanAreasFilter.urbanAreasPopulationFrom
import monix.eval.Task

object CityPopulationAnalyzer extends TaskFlow {
  def peopleLivingInUrbanAreasFrom(femaleSourceFile: String, maleSourceFile: String): Task[(TaskState, Unit)] = {
    val initialState = CityPopulationSources(femaleSourceFile, maleSourceFile)
    task.run(initialState)
  }

  def task: IndexedStateT[Task, CityPopulationSources, UrbanAreaPopulationDataset, Unit] = {
    loadFemalePopulation >> loadMalePopulation >> filterUrbanAreas >> totalPopulationInUrbanAreas
  }

  def loadFemalePopulation: StateT[Task, CityPopulationSources, Unit] = transform { state =>
    val femaleDataSet = loadFrom(state.femaleSourceFile)
    state.copy(dataSets = state.dataSets :+ femaleDataSet)
  }

  def loadMalePopulation: StateT[Task, CityPopulationSources, Unit] = transform { state =>
    val maleDataSet = loadFrom(state.maleSourceFile)
    state.copy(dataSets = state.dataSets :+ maleDataSet)
  }

  def filterUrbanAreas: IndexedStateT[Task, CityPopulationSources, CityPopulationDataset, Unit] = transform { state =>
    val urbanAreasPopulation = urbanAreasPopulationFrom(state.dataSets)
    CityPopulationDataset(urbanAreasPopulation)
  }

  def totalPopulationInUrbanAreas: IndexedStateT[Task, CityPopulationDataset, UrbanAreaPopulationDataset, Unit] = transform { state =>
    val totalPopulationLivingInUrbanAreas = totalUrbanAreaPopulationFrom(state.dataSet)
    UrbanAreaPopulationDataset(totalPopulationLivingInUrbanAreas)
  }
}
