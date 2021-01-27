package com.company.spark.aws.dynamo

import com.company.spark.aws.dynamo.job.DynamoIngestionJob
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory
// please note that hardcoding variables will be removed

object Main {
  private lazy val logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    implicit val config: Config = ConfigFactory.load("dev.conf")
    val sparkConf: SparkConf =
      new org.apache.spark.SparkConf()
        .setAppName("dynamodb_sink")
        .set("spark.sql.streaming.schemaInference", "true") //scalastyle:ignore
        .set("spark.dynamicAllocation.enable", "true")
        .set("spark.scheduler.mode", "FAIR")
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .set("spark.io.compression.codec", "lzf")

    implicit val spark: SparkSession = SparkSession
      .builder().config(sparkConf)
      .getOrCreate()

    new DynamoIngestionJob()

  }
}
