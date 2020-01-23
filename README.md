# City Population

This small application written in `Scala` demonstrates the use of pure functional programming patterns to modularize and orchestrate an ETL pipeline based on the open-source analytics engine for big data processing `Apache Spark`. I got my motivation from reading the blog post: [Functional programming and Spark: do they mix?](https://iravid.com/posts/fp-and-spark.html)

Although Monix [Task](https://monix.io/api/3.1/monix/eval/Task.html) is a useful abstraction that allows us to move side-effects to the edge of our systems, it also increases the boilerplate code overhead. Furthermore, the introduction of the [state monad](https://typelevel.org/cats/datatypes/state.html) can act as an important barrier for the adoption of a side-effect free programming style.

In this example, I experimented using an infix operator to compose tasks in a readable way. It's an alternative to using the `flatMap` method or the for comprehension syntactic sugar.

Also, I'm not considering at this time tasks decorators like retry and timing. My understanding is that such functionality already exists in `Spark` and there is little value in rewriting them.

The resulting code is easier to reason about, and easier to test than its imperative equivalent. An acceptance test is provided as part of this example, as well as a few relevant unit tests. The acceptance test verifies at a high-level that the application has been successfully completed. On the other hand, each unit test covers an individual step of the application. In particular, complex operations with [Spark DataFrames](https://spark.apache.org/docs/latest/sql-programming-guide.html#datasets-and-dataframes) such as filtering relevant and correct information from the input data are always validated in this example with a unit test.

## Use case description

The United Nations Statistics Division (UNSD) collects city population yearly time-series for female and male: https://datahub.io/core/population-city

Write an application to read the data from two CSV files and write the sum of both female and male population living in urban agglomerations to a database view.

A possible implementation strategy could be as follows:

* Load female demographics from CSV file.
* Load male urban agglomeration from CSV file.
* Put both female and male population together, then select the population living in urban agglomerations.
* Group population by city and year.
* Export the resulting data to a temporary view.

Possible (future) variations to the strategy:

* Load both female and male population in parallel.

## Implementation with Apache Spark

Open a `Spark` interactive shell with the following command and don't forget to use the `:paste` command in the shell before entering your code:

```shell script
sbt console
```

A possible implementation of the use case could be as follows:

```scala
import org.apache.spark.sql.{Dataset, Encoders}
import org.apache.spark.sql.functions._
import spark.implicits._

case class CityPopulation(countryOrArea: String, year: Int, area: String, sex: String, city: String, cityType: String, recordType: String, reliability: String, sourceYear: Int, value: Double, valueFootnotes: Int)
case class UrbanAreaPopulation(countryOrArea: String, city: String, year: Int, total: Double)

val schema = Encoders.product[CityPopulation].schema

val femalePopulation = spark.read.schema(schema)
  .csv("src/test/resources/data/city_female_population")
  .as[CityPopulation]

val malePopulation = spark.read.schema(schema)
  .csv("src/test/resources/data/city_male_population")
  .as[CityPopulation]

val allGendersUrbanAreaPopulation = femalePopulation.union(malePopulation)
  .filter('cityType === "Urban agglomeration")

val totalUrbanAreaPopulation = allGendersUrbanAreaPopulation.where('area === "Total")
  .groupBy('countryOrArea, 'city, 'year)
  .agg(sum('value).alias("total"), count('value).alias("count"), collect_list('sex).alias("gender"))
  .filter(('count === 2) && ('gender === Array("Female", "Male")))
  .select('countryOrArea, 'city, 'year, 'total)
  .sort('countryOrArea, 'city, 'year)
  .as[UrbanAreaPopulation]

totalUrbanAreaPopulation.createOrReplaceTempView("urban_areas_total_population_view")

spark.sql(
  """SELECT countryOrArea, city, SUM(total) AS sum
    |FROM urban_areas_total_population_view
    |WHERE countryOrArea = 'Spain' AND city = 'Barcelona'
    |GROUP BY countryOrArea, city""".stripMargin).show
```

After entering the above code in your `Spark` interactive shell you should get an output similar to the following:

```text
+-------------+---------+---------+
|countryOrArea|     city|      sum|
+-------------+---------+---------+
|        Spain|Barcelona|2208652.0|
+-------------+---------+---------+
```

### What's the problem with this code?

Short answer: nothing. Many companies use similar code in production every day without any hesitation.

If you decided to keep reading, then you should know that there is a bunch of things that can be improved in this code. First, responsibilities are not clearly defined in the code and different concerns like loading the input records and aggregating them are part of the same component. The low level of modularity makes the code difficult to modify, and what is more important, an error in one line may cascade to other parts of the code and thus making error root cause tracing more difficult.

### Functional programming to the rescue

A possible approach to improving the modularity of and testability of 

__TODO__

## Acknowledgments

Thanks to Ferran Gali (@ferrangali) for giving me advice on `Spark`.

## Getting Started

Run tests locally in your laptop with the following command:

```shell script
sbt clean test
```

Or build a distributable package including launcher scripts for *nix operating systems and Windows with the following:

```shell script
sbt universal:packageBin
```

## Known Issues

1. It is not currently possible to run the application with SBT. Please, don't try this at home:

```shell script
sbt "runMain es.eriktorr.samples.population.CityPopulationApplication src/test/resources/data/city_female_population src/test/resources/data/city_male_population"
```
