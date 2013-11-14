package singleclient

import nl.in4392.master.MasterActor
import com.typesafe.config.ConfigFactory
import akka.kernel.Bootable
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }
import main.scala.nl.in4392.models.Task.Task
import java.io._
import java.util.UUID
import nl.tudelft.ec2interface.taskmonitor.TaskInfo
import main.scala.nl.in4392.worker.{MonitorActor, WorkerActor}

class OneClientWorker extends Bootable {


  val masterActorPath = new RemoteActorInfo().getInfoFromFile("conf/masterInfo").getActorPath()
  val masterActor = context.actorSelection(ActorPath.fromString(masterActorPath))


  def startup() {
  }

  /**
   * Test using 400 × 297 20 KB Image
   * Workload 100 Requests
   */
  def testRequests100Small: Unit = {

    val bis = new BufferedInputStream(new FileInputStream("./src/main/resources/TEST_2.JPG"))
    val byteArray = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray

    var tInfo = new TaskInfo()

    tInfo.setMasterId("DefaultMasterID")
    tInfo.setWorkerId("DefaultWorkerID")
    tInfo.setTaskSize(12)

    for (i <- 0 until 100) {
      val taskId = UUID.randomUUID().toString
      tInfo.setUuid(taskId)
      masterActor ! new Task(taskId, byteArray, tInfo.ToJson(tInfo))
    }
  }


  /**
   * Test using 738 x 1119 139KB KB Image
   * Workload 100 Requests
   */
  def testRequests100Large: Unit = {

    val bis = new BufferedInputStream(new FileInputStream("./src/main/resources/TEST_3.JPG"))
    val byteArray = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray

    var tInfo = new TaskInfo()

    tInfo.setMasterId("DefaultMasterID")
    tInfo.setWorkerId("DefaultWorkerID")
    tInfo.setTaskSize(12)

    for (i <- 0 until 100) {
      val taskId = UUID.randomUUID().toString
      tInfo.setUuid(taskId)
      masterActor ! new Task(taskId, byteArray, tInfo.ToJson(tInfo))
    }
  }


  /**
   * Test using 400 × 297 20 KB Image
   * Workload 1000 Requests
   */
  def testRequests1000Small: Unit = {

    val bis = new BufferedInputStream(new FileInputStream("./src/main/resources/TEST_2.JPG"))
    val byteArray = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray

    var tInfo = new TaskInfo()

    tInfo.setMasterId("DefaultMasterID")
    tInfo.setWorkerId("DefaultWorkerID")
    tInfo.setTaskSize(12)

    for (i <- 0 until 1000) {
      val taskId = UUID.randomUUID().toString
      tInfo.setUuid(taskId)
      masterActor ! new Task(taskId, byteArray, tInfo.ToJson(tInfo))
    }

    println()
  }

  /**
   * Test using 738 x 1119 139KB KB Image
   * Workload 1000 Requests
   */
  def testRequests1000Large: Unit = {

    val bis = new BufferedInputStream(new FileInputStream("./src/main/resources/TEST_3.JPG"))
    val byteArray = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray

    var tInfo = new TaskInfo()

    tInfo.setMasterId("DefaultMasterID")
    tInfo.setWorkerId("DefaultWorkerID")
    tInfo.setTaskSize(12)

    for (i <- 0 until 1000) {
      val taskId = UUID.randomUUID().toString
      tInfo.setUuid(taskId)
      masterActor ! new Task(taskId, byteArray, tInfo.ToJson(tInfo))
    }
  }

  /**
   * Test using 1000 random mix of small and large image
   */
 def testRandom1000: Unit = {

   val bis = new BufferedInputStream(new FileInputStream("./src/main/resources/TEST_2.JPG"))
   val byteArraySmall = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray

    val bis = new BufferedInputStream(new FileInputStream("./src/main/resources/TEST_3.JPG"))
    val byteArrayLarge = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray

   var tInfo = new TaskInfo()

   tInfo.setMasterId("DefaultMasterID")
   tInfo.setWorkerId("DefaultWorkerID")
   tInfo.setTaskSize(12)

    val ran = scala.util.Random
   for (i <- 0 until 1000) {
     val taskId = UUID.randomUUID().toString
     tInfo.setUuid(taskId)
     if(ran.nextInt(1) == 0)
       masterActor ! new Task(taskId, byteArraySmall, tInfo.ToJson(tInfo))
     else
       masterActor ! new Task(taskId, byteArrayLarge, tInfo.ToJson(tInfo))
   }
 }

  def shutdown() {
    system.shutdown()
  }
}

object MasterApp {
  def main(args: Array[String]) {
    val app = new OneClientWorker
    println("[TEST][OneClientWorker] Started")
    app.testRandom1000()

  }
}
