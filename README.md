# spark-structured-streaming-dynamodb-custom-sink

A Sample of Spark structured streaming DynamoDb custom sink using ForEachWriter. 

## Requirements: 
  - Scala : 2.11.12
  - Sbt : 1.4.3
  - Localstack
  - Spark : 2.4.5
  - Intellij

## compile:

To compile the project do : *sbt compile*
## build:
To build the project please do : *sbt assembly*

Please note thant configuration parameters are defined in the file dev.conf under resources directory, do update them based on your environment

## deploy:

To deploy the job , go to target  directory where binary *spark-structured-streaming-custom-sink-assembly-0.1.0-SNAPSHOT.jar* is hosted and run the *spark-submit* command 