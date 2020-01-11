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

```shell script
jenv exec sbt clean test
```

```shell script
jenv exec sbt console
```
