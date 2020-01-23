package es.eriktorr.samples.population

import cats.data.{IndexedStateT, StateT}
import es.eriktorr.samples.population.flow.TaskFlow.implicits._
import es.eriktorr.samples.population.flow.{TaskFlow, TaskState}
import es.eriktorr.samples.population.states.{CityPopulationDataset, CityPopulationSources, UrbanAreaPopulationDataset}
import es.eriktorr.samples.population.steps.CityPopulationReader
import es.eriktorr.samples.population.steps.UrbanAreasAggregator
import es.eriktorr.samples.population.steps.UrbanAreasFilter
import es.eriktorr.samples.population.steps.UrbanAreasTotalPopulationExporter
import monix.eval.Task

object CityPopulationAnalyzer extends TaskFlow {
  def peopleLivingInUrbanAreasFrom(femaleSourceFile: String, maleSourceFile: String): Task[(TaskState, Unit)] = {
    val initialState = CityPopulationSources(femaleSourceFile, maleSourceFile)
    task.run(initialState)
  }

  def task: IndexedStateT[Task, CityPopulationSources, UrbanAreaPopulationDataset, Unit] = {
    loadFemalePopulation >> loadMalePopulation >> filterUrbanAreas >> findTotalPopulationLivingInUrbanAreas >> saveView
  }

  def loadFemalePopulation: StateT[Task, CityPopulationSources, Unit] = transform { state =>
    val femaleDataSet = CityPopulationReader().cityPopulationFrom(state.femaleSourceFile)
    state.copy(dataSets = state.dataSets :+ femaleDataSet)
  }

  def loadMalePopulation: StateT[Task, CityPopulationSources, Unit] = transform { state =>
    val maleDataSet = CityPopulationReader().cityPopulationFrom(state.maleSourceFile)
    state.copy(dataSets = state.dataSets :+ maleDataSet)
  }

  def filterUrbanAreas: IndexedStateT[Task, CityPopulationSources, CityPopulationDataset, Unit] = transform { state =>
    val urbanAreasPopulation = UrbanAreasFilter().onlyUrbanAreasFrom(state.dataSets)
    CityPopulationDataset(urbanAreasPopulation)
  }

  def findTotalPopulationLivingInUrbanAreas: IndexedStateT[Task, CityPopulationDataset, UrbanAreaPopulationDataset, Unit] = transform { state =>
    val totalPopulationLivingInUrbanAreas = UrbanAreasAggregator().urbanAreasTotalPopulationFrom(state.dataSet)
    UrbanAreaPopulationDataset(totalPopulationLivingInUrbanAreas)
  }

  def saveView: StateT[Task, UrbanAreaPopulationDataset, Unit] = transform { state =>
    UrbanAreasTotalPopulationExporter().saveView(state.dataSet)
    state
  }
}
