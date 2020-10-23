package com.spark.scala.kafka

import java.util.Properties
import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConverters._

object KafkaConsumerWithUserObject extends App{
  val prop:Properties = new Properties()
  prop.put("group.id", "test")
  prop.put("bootstrap.servers","192.168.1.100:9092") 
  prop.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
  prop.put("value.deserializer","com.nelamalli.kafka.jackson.UserDeserializer")
  prop.put("enable.auto.commit", "true")
  prop.put("auto.commit.interval.ms", "1000")
  val consumer = new KafkaConsumer[String,User](prop)
  val topics = List("user_user")
  try{
    consumer.subscribe("user_user")
    while(true){
      val records = consumer.poll(10)
      for(record<-records.asScala){
        println("Key name - ", record._1)
        println("Topic name - ", record._2.topic())        
      }, 
    }
  }catch{
    case e:Exception => e.printStackTrace()
  }finally {
    consumer.close()
  }
}