package app

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object TestApp
{
    def main(args: Array[String]): Unit = {
        val inputFiles: String = "/home/ville/spark-test/data"
        val spark: SparkSession = SparkSession
            .builder
            .appName("Test Application")
            .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
            .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
            .getOrCreate()

        // NOTE: does not seem to work
        spark.sparkContext.setLogLevel(org.apache.log4j.Level.WARN.toString())

        spark.conf.set("spark.sql.shuffle.partitions", "5")

        import spark.implicits._

        val lines: Dataset[String] = spark
            .read
            .textFile(inputFiles)

        val words: Dataset[String] = lines
            .flatMap(line => line.split(" "))
            .filter(word => word != "")

        val lineCount: Long = lines.count()
        val wordCount: Long = words.count()

        println(s"Total number of lines: ${lineCount}")
        println(s"Total number of words: ${wordCount}")

        val outputDF: DataFrame = Seq(
            ("lines", lineCount),
            ("words", wordCount)
        )
            .toDF("name", "value")

        // write the results to "output-csv" folder in CSV format
        outputDF
            .coalesce(1)  // only one output file
            .write
            .mode("overwrite")
            .option("header", true)
            .option("sep", ";")
            .csv("output-csv")

        // write the results to "output-delta" folder in Delta format
        outputDF
            .write
            .format("delta")
            .mode("overwrite")
            .save("output-delta")

        spark.stop()
    }
}
