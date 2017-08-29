package main

import game.TicTacToeClient

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object ClientMain {
  import ExecutionContext.Implicits.global

  def main(args: Array[String]) {
    val clients = for (_ <- 0 until 2) yield
      new TicTacToeClient(null, 9999)

    Await.ready(Future.sequence { clients.map {
      client => Future { client.run() }
    }}, Duration.Inf)

  }
}
