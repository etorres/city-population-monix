lazy val SparkVersion = "2.4.3"
lazy val SparkTestingVersion = s"${SparkVersion}_0.12.0"
lazy val MonixVersion = "3.1.0"
lazy val ScalaLoggingVersion = "3.9.2"

def makeColorConsole() = {
  val ansi = System.getProperty("sbt.log.noformat", "false") != "true"
  if (ansi) System.setProperty("scala.color", "true")
}

lazy val root = project.in(file(".")).
  settings(
    organization := "es.eriktorr.samples",
    name := "city-population-monix",
    version := "1.0",

    scalaVersion := "2.12.10",

    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % SparkVersion % Provided,
      "org.apache.spark" %% "spark-sql"  % SparkVersion % Provided,
      "io.monix" %% "monix-eval" % MonixVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % ScalaLoggingVersion,
      "com.holdenkarau" %% "spark-testing-base" % SparkTestingVersion % Test
    ),

    logBuffered in Test := false,
    fork in Test := true,
    parallelExecution in Test := false,

    test in assembly := {},
    mainClass in assembly := Some("es.eriktorr.samples.population.CityPopulationApplication"),

    javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled"),
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-language:higherKinds",
      "-language:postfixOps",
      "-Xfatal-warnings"),

    initialize ~= { _ => makeColorConsole() },
    initialCommands in console :=
      """
        |import org.apache.spark.{SparkConf, SparkContext}
        |import org.apache.spark.sql.SparkSession
        |
        |val conf = new SparkConf().setMaster("local[*]").setAppName("monix-repl").set("spark.ui.enabled", "false")
        |implicit val spark = SparkSession.builder().config(conf).appName("city-population").getOrCreate()
        |
        |import spark.implicits._
        |
        |spark.sparkContext.setLogLevel("WARN")
      """.stripMargin,
    cleanupCommands in console :=
      """
        |spark.stop()
      """.stripMargin
  )
