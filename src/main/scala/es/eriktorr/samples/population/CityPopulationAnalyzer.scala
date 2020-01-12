package es.eriktorr.samples.population

import monix.eval.Task
import org.apache.spark.sql.SparkSession

object CityPopulationAnalyzer {
  def countCityPopulation(femalePopulationFile: String, malePopulationFile: String): Task[Long] = Task {

    println("\n\n >> HERE\n")

    0L
  }

//  def countCityPopulation(femalePopulationFile: String, malePopulationFile: String): Task[Long] = for {
//    sparkSession <- buildSession
//    result <- {
//      implicit val session: SparkSession = sparkSession
//      for {
//        femalePopulation <- loadCityPopulation(femalePopulationFile)
//        malePopulation <- loadCityPopulation(malePopulationFile)
//        count <- count(Seq(femalePopulation, malePopulation))
//      } yield count
//    }
//  } yield result
}
