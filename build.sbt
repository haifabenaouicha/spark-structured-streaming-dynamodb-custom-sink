name := "spark-structured-streaming-custom-sink"

scalaVersion := "2.11.12"

val org: String = "com.company.spark"

scalacOptions ++= Seq("-encoding", "utf-8")

lazy val baseSettings = Seq(
  organization := s"$org.aws",
  scalaVersion := "2.11.12",
  fork in Test := true,
  logBuffered in Test := false,
  parallelExecution in Test := false
)
lazy val assemblySettings = Seq(
  test in assembly := {},
  assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),
  assemblyMergeStrategy in assembly := {
    case "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" => MergeStrategy.concat
    case PathList("META-INF", xs @ _*)   => MergeStrategy.discard
    case _                               => MergeStrategy.first
  }
)

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.typesafe.config.**" -> "my_conf.@1")
    .inLibrary("com.typesafe" % "config" % "1.3.3")
    .inProject
)

lazy val dependencies = new {
  val sparkOrg: String = "org.apache.spark"
  val kafkaOrg: String = "org.apache.kafka"

  val tsConfigV: String = "1.3.3"
  val sparkV: String = "2.4.5"
  val scalatestV: String = "3.0.5"
 
  lazy val tsConfig: ModuleID = "com.typesafe" % "config" % tsConfigV
  lazy val scalatest: ModuleID = "org.scalatest" %% "scalatest" % scalatestV
  lazy val sparkCore: ModuleID = sparkOrg %% "spark-core" % sparkV
  lazy val sparkSql: ModuleID = sparkOrg %% "spark-sql" % sparkV
  lazy val sparkStreaming: ModuleID = sparkOrg %% "spark-streaming" % sparkV

  def getConf: Seq[ModuleID] = Seq(tsConfig)

  def getTest: Seq[ModuleID] = Seq(scalatest).map(_ % "test,it")
  def getSparkBase: Seq[ModuleID] =  Seq(sparkCore, sparkSql, sparkStreaming).map(_ %Provided)
}

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(baseSettings ++ assemblySettings  ++ Seq(
    libraryDependencies ++= dependencies.getSparkBase,
    libraryDependencies ++= dependencies.getConf,
    libraryDependencies ++= dependencies.getTest,
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.10",
    libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.780"
 
  ))


