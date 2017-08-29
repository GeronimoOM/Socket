package game

class TicTacToe {
  import game.TicTacToe._
  import game.TicTacToe.Piece._

  private val brd: Board = Array.ofDim(3, 3)
  private var turn: Piece = X

  def board() = brd.clone
  def current = turn

  def move(x: Int, y: Int) {
    if(brd(x)(y) != null)
      throw new IllegalMoveException(x, y)
    brd(x)(y) = turn
    turn = turn.other
  }

  def winner(): Option[Piece] = {
    def lineMatch(line: Array[Piece]): Option[Piece] =
      if (line.head != null && line.forall(_.eq(line.head)))
        Some(line.head) else None

    val lines: Stream[Array[Piece]] = brd.toStream #:::
      brd.head.indices.map(y => brd.map(_.apply(y))).toStream #:::
      Stream(brd.indices.map(i => brd(i)(i)).toArray,
        brd.indices.map(i => brd(i)(brd.length - 1 - i)).toArray)

    val winner = lines.map(lineMatch).find(m => m.isDefined).flatten
    if(!winner.isDefined && brd.flatten.forall(_ != null))
      Some(null) else winner
  }
}

object TicTacToe {
  import Piece.Piece

  object Piece extends Enumeration {
    type Piece = Value
    val X, O = Value

    implicit class PieceOps(piece: Piece) {
      def other: Piece = Piece((piece.id + 1) % maxId)
    }
  }

  type Board = Array[Array[Piece]]

  implicit class BoardOps(val board: Board) {
    def pretty = board.map(row =>
      row.map(p => if (p == null) "_" else p).mkString(" ")
    ).mkString("\n")
  }

  class IllegalMoveException(x: Int, y: Int) extends Exception(s"Cell [$x, $y] is occupied")
}
