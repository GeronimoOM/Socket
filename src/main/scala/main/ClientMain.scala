package main

import net.impl.TestClient

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object ClientMain {
  import ExecutionContext.Implicits.global

  def main(args: Array[String]) {
    val clients = List("no rest", "for the", "wicked").map { msg =>
      new TestClient(null, 9999, msg)
    }

    Await.ready(Future.sequence { clients.map {
      client => Future { client.run() }
    }}, 5 seconds)

  }
}
