package com.company.spark.aws.dynamo.sink

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import org.apache.spark.sql.{ForeachWriter, Row}
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._

/**
 * Dynamodb Sink using  [[ ForeachWriter]]
 *
 * @author Haifa Ben Aouicha
 */
class DynamoSink(dynamoSinkOptions: DynamoSinkOptions) extends ForeachWriter[Row] {

  private lazy val logger = LoggerFactory.getLogger(getClass)
  private lazy val ddb = DynamoClient(dynamoSinkOptions.region, dynamoSinkOptions.localStackUrl)

  // This is called first when preparing to send multiple rows.
  // Put all the initialization code inside open() so that a fresh
  // copy of this class is initialized in the executor where open()
  // will be called.

  def open(partitionId: Long, epochId: Long): Boolean = {
    // force the initialization of the client
    true
  }

  // This is called for each row after open() has been called.
  // This implementation sends one row at a time.

  def process(row: Row): Unit = {
    logger.info("preparing to write the received record")
    val rowAsMap = Map(
      "id" -> row.getString(0),
      "name" -> row.getString(1)
    )
    val dynamoItem = rowAsMap.transform { (key, value) => toStringAtribute(value) }.asJava
    ddb.putItem(dynamoSinkOptions.tableName, dynamoItem)
    logger.info("record written successfully to dynamotable")
  }

  def toStringAtribute(v: Any): AttributeValue = {
    new AttributeValue().withS(v.toString)
  }

  // This is called after all the rows have been processed.

  def close(errorOrNull: Throwable): Unit = {
    ddb.shutdown()
  }
}
