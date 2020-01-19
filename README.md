# City Population

Write an application in Spark+Scala to load and count the records of a dataset of city population.

Initial execution plan:

* Load female population from CSV file.
* Load male population from CSV file.
* Count records.

Possible variation to the execution plan:

* Load both female and male population in parallel.
* Count records.

Improvements:

* Retry failed tasks.
* Timing individual operations.
* Unit test individual tasks.

More improvements

* Use >> operator instead of for comprehension.
* Catch exceptions of main.

## Feedback from:

@Ferran.Gali: Task composition could be useful if you reduce the boilerplate and also reduce the entry barrier (e.g state monad). Task decorators like retry and timing are not useful since they overlap with similar functionality already present in Spark.

## Getting Started

```shell script
jenv exec sbt clean test
```

```shell script
jenv exec sbt console
```

```shell script
sbt "runMain es.eriktorr.samples.population.CityPopulationApplication src/test/resources/data/city_female_population src/test/resources/data/city_male_population"
```

```shell script
sbt universal:packageBin
```