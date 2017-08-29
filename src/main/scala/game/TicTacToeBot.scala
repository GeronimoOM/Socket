package game

import game.TicTacToe.Board
import game.TicTacToe.Piece._

import scala.util.Random

trait TicTacToeBot {
  def move(board: Board): (Int, Int)
}

class DumbTicTacToeBot(val piece: Piece) extends TicTacToeBot {
  def move(board: Board): (Int, Int) = {
    val free = (for(x <- board.indices; y <- board.head.indices
                    if board(x)(y) == null) yield (x, y)).toList
    free(Random.nextInt(free.size))
  }
}



