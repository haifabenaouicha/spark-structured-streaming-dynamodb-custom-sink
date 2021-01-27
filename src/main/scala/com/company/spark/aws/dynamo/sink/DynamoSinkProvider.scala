package com.company.spark.aws.dynamo.sink

/**
 * Dynamodb Sink Provider to create a dynamoSink
 * @author Haifa Ben Aouicha
 */
object DynamoSinkProvider {

  def createSink(parameters: Map[String, String]): DynamoSink = {
    val mandatoryOptions = List(
      DynamoSinkOptions.REGION_OPTION,
      DynamoSinkOptions.TABLE_NAME_OPTION,
      DynamoSinkOptions.LOCAL_STACK_URL_OPTION
    )
    mandatoryOptions.foreach { optionName =>
      if (!parameters.contains(optionName))
        throw new Exception(s"DynamoDbSink: No '$optionName' parameter key found on the Sink options")
    }
    new DynamoSink(DynamoSinkOptions(
      parameters(DynamoSinkOptions.REGION_OPTION),
      parameters(DynamoSinkOptions.TABLE_NAME_OPTION),
      parameters(DynamoSinkOptions.LOCAL_STACK_URL_OPTION)))
  }
}
