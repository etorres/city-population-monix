package es.eriktorr.samples.population

import cats.data.{IndexedStateT, StateT}
import es.eriktorr.samples.population.flow.TaskFlow.implicits._
import es.eriktorr.samples.population.flow.{TaskFlow, TaskState}
import es.eriktorr.samples.population.states.{CityPopulationDataset, CityPopulationSources, UrbanAreaPopulationDataset}
import es.eriktorr.samples.population.steps.CityPopulationReader.cityPopulationFrom
import es.eriktorr.samples.population.steps.UrbanAreasAggregator.urbanAreasTotalPopulationFrom
import es.eriktorr.samples.population.steps.UrbanAreasFilter.urbanAreasPopulationFrom
import es.eriktorr.samples.population.steps.UrbanAreasTotalPopulationExporter.saveUrbanAreasTotalPopulationView
import monix.eval.Task

object CityPopulationAnalyzer extends TaskFlow {
  def peopleLivingInUrbanAreasFrom(femaleSourceFile: String, maleSourceFile: String): Task[(TaskState, Unit)] = {
    val initialState = CityPopulationSources(femaleSourceFile, maleSourceFile)
    task.run(initialState)
  }

  def task: IndexedStateT[Task, CityPopulationSources, UrbanAreaPopulationDataset, Unit] = {
    loadFemalePopulation >> loadMalePopulation >> filterUrbanAreas >> findUrbanAreasTotalPopulation >> saveView
  }

  def loadFemalePopulation: StateT[Task, CityPopulationSources, Unit] = transform { state =>
    val femaleDataSet = cityPopulationFrom(state.femaleSourceFile)
    state.copy(dataSets = state.dataSets :+ femaleDataSet)
  }

  def loadMalePopulation: StateT[Task, CityPopulationSources, Unit] = transform { state =>
    val maleDataSet = cityPopulationFrom(state.maleSourceFile)
    state.copy(dataSets = state.dataSets :+ maleDataSet)
  }

  def filterUrbanAreas: IndexedStateT[Task, CityPopulationSources, CityPopulationDataset, Unit] = transform { state =>
    val urbanAreasPopulation = urbanAreasPopulationFrom(state.dataSets)
    CityPopulationDataset(urbanAreasPopulation)
  }

  def findUrbanAreasTotalPopulation: IndexedStateT[Task, CityPopulationDataset, UrbanAreaPopulationDataset, Unit] = transform { state =>
    val totalPopulationLivingInUrbanAreas = urbanAreasTotalPopulationFrom(state.dataSet)
    UrbanAreaPopulationDataset(totalPopulationLivingInUrbanAreas)
  }

  def saveView: StateT[Task, UrbanAreaPopulationDataset, Unit] = transform { state =>
    saveUrbanAreasTotalPopulationView(state.dataSet)
    state
  }
}
