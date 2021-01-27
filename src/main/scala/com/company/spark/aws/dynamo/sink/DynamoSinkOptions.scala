package com.company.spark.aws.dynamo.sink

/**
 * Dynamodb Sink options : dynamo table name, region , url of localstack
 *
 * @author Haifa Ben Aouicha
 */
case class DynamoSinkOptions(region: String, tableName: String, localStackUrl: String)

object DynamoSinkOptions {
  val TABLE_NAME_OPTION = "tableName"
  val REGION_OPTION = "region"
  val LOCAL_STACK_URL_OPTION = "localStackUrl"
}