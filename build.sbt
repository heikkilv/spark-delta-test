name := "Test-application"
version := "1.0.0"
scalaVersion := "2.12.20"

val MainClass: String = "app.TestApp"

Compile / mainClass := Some(MainClass)
assembly / mainClass := Some(MainClass)
assembly / assemblyJarName := s"${name.value}-${version.value}.jar"

val SparkVersion: String = "3.5.3"
val DeltaVersion: String = "3.3.1"

libraryDependencies += "io.delta" %% "delta-spark" % DeltaVersion
libraryDependencies += "org.apache.spark" %% "spark-core" % SparkVersion % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % SparkVersion % "provided"

// suppress all log messages for setting up the Spark Session
// NOTE: does not seem to work
javaOptions += "-Dlog4j.configurationFile=project/log4j.properties"

assembly / assemblyMergeStrategy := {
    case PathList("META-INF", "services", "org.apache.spark.sql.sources.DataSourceRegister") => MergeStrategy.concat
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
}
