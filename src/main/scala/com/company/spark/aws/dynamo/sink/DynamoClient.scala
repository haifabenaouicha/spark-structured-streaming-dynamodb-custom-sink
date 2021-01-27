package com.company.spark.aws.dynamo.sink

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDB, AmazonDynamoDBClientBuilder}
import org.slf4j.{Logger, LoggerFactory}

/**
 * Dynamodb client instantiation
 *
 * @author Haifa Ben Aouicha
 */
class DynamoClient(region: String, localStackUrl: String) {
  lazy val logger: Logger = LoggerFactory.getLogger(getClass)

  def getClient: AmazonDynamoDB = {
    lazy val client = AmazonDynamoDBClientBuilder.standard.withEndpointConfiguration(
      new AwsClientBuilder.EndpointConfiguration(localStackUrl, region)).build()
    logger.info("created dynamo client on localStack")
    client
  }
}

object DynamoClient {
  def apply(region: String, localStackUrl: String): AmazonDynamoDB = {
    new DynamoClient(region, localStackUrl).getClient
  }
}
