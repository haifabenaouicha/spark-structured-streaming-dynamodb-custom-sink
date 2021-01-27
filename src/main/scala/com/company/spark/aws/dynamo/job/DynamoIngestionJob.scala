package com.company.spark.aws.dynamo.job

import com.company.spark.aws.dynamo.sink.{DynamoSinkOptions, DynamoSinkProvider}
import com.typesafe.config.Config
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * A sample basic job which read stream csv files load them into spark Dataframe then writes them in a streaming way to dynamoDB table
 *
 * @author Haifa Ben Aouicha
 */

class DynamoIngestionJob(implicit spark: SparkSession, conf: Config) {

  private val currentFolder  = new java.io.File(".").getCanonicalPath

  private val dynamoSinkOptions: Map[String, String] = Map(
    DynamoSinkOptions.REGION_OPTION -> "region",
    DynamoSinkOptions.TABLE_NAME_OPTION -> "tableName",
    DynamoSinkOptions.LOCAL_STACK_URL_OPTION -> "localStackUrl"
  )

  private lazy val schema: StructType = StructType(Seq(
    StructField("id", StringType, nullable = true),
    StructField("name", StringType, nullable = true)))

  def readStreamData: DataFrame = {
    spark.readStream.schema(schema).format("csv").option("delimiter", " ").option("header", "true").load(s"$currentFolder/src/main/resources/inputData")
  }

  def start(): Unit = {
    readStreamData.writeStream
      .foreach(DynamoSinkProvider.createSink(dynamoSinkOptions))
      .start().awaitTermination()
  }
}
