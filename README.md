# upb.crypto.benchmark
Benchmarking project for the upb.crypto libraries.
Contains benchmarks and utility methods helpful for implementing benchmarks.

## Prerequisites

Currently, this project uses Java 8. It also uses Gradle as build system so you will likely want to use an IDE with a Gradle plugin.

## Installation

First, clone this repo in a path of your choice.
Then continue with the installation instructions for the IDE of your choice.

### IntelliJ IDEA

To use the benchmarks, you will want to create a IDEA project. Since this project uses Gradle as its build system, you can create a new project using **File -> New -> Project from Existing Sources**. Then select the `build.gradle` file in the cloned repo folder and continue with the installation.

## Running Benchmarks

The benchmarks use JMH.
Since JMH needs to be run via its `Main` class, the benchmark project has a Gradle task called `jmh` that allows you to run the benchmarks contained in source set `jmh`.
You will need to run this task via the command line.
The `include` and `exclude` parameters can be used to include/exclude specific benchmarks in/from the run.
