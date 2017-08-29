package main

import game.TicTacToeServer

object ServerMain {

  def main(args: Array[String]): Unit = {
    val server = new TicTacToeServer(9999)
    server.run()
  }

}
