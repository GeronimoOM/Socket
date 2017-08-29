package game

import game.TicTacToe._
import game.TicTacToe.Piece._
import game.TicTacToeClient._
import game.TicTacToeServer._
import net.MessageServer

class TicTacToeServer(override val port: Int) extends MessageServer(port) {
  private val game: TicTacToe = new TicTacToe
  private var players: Map[Piece, Client] = Map()

  override type OutMessage = TicTacToeServerMessage
  override type InMessage = TicTacToeClientMessage

  override protected def receive(msg: Option[TicTacToeClientMessage], player: Client): Boolean = {
    msg match {case Some(m) => m match {
      case TicTacToeClient.Join => synchronized {
        val piece = addPlayer(player)
        player.send(TicTacToeServer.Join(piece))
        if(players.size == 2) {
          players(Piece.X).send(TicTacToeServer.Move(game.board()))
        }
      }
      case TicTacToeClient.Move(x, y) =>
        try {
          game.move(x, y)
          val board = game.board()
          println(board.pretty + "\n")
          val winner = game.winner()
          if(winner.isDefined)
            players.values.foreach { _.send(TicTacToeServer.Winner(winner.get)) }
          else {
            otherPlayer(player).send(TicTacToeServer.Move(board))
          }
        } catch {
          case e: IllegalMoveException => player.send(TicTacToeServer.Move(game.board()))
        }
      }
      case None =>
    }
    true
  }

  private def addPlayer(client: Client): Option[Piece] = {
    if(players.isEmpty) {
      players += (Piece.X -> client)
      Some (Piece.X)
    } else if(players.size == 1) {
      players += (Piece.O -> client)
      Some (Piece.O)
    } else None
  }

  private def otherPlayer(client: Client): Client = {
    if (players(Piece.X).eq(client))
      players(Piece.O)
    else players(Piece.X)
  }
}

object TicTacToeServer {
  sealed abstract class TicTacToeServerMessage extends Serializable
  case class Join(piece: Option[Piece]) extends TicTacToeServerMessage
  case class Move(board: BoardData) extends TicTacToeServerMessage
  case class Winner(piece: Piece) extends TicTacToeServerMessage

  implicit class BoardData(board: Board) extends Serializable {
    val size: Int = board.length
    val data: Array[Int] = board.flatten.map(p => if(p == null) Piece.maxId else p.id)
  }

  implicit def boardData2Board(bd: BoardData): Board = {
    val board = Array.ofDim[Piece](bd.size, bd.size)
    bd.data.zipWithIndex.foreach { case (p, i) =>
      board(i / bd.size)(i % bd.size) = if (p == Piece.maxId) null else Piece(p)
    }
    board
  }
}