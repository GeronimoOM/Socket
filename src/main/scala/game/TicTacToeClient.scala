package game

import game.TicTacToe.Piece._
import game.TicTacToeServer._
import game.TicTacToeClient._
import net.MessageClient

class TicTacToeClient(override val host: String, override val port: Int) extends MessageClient(host, port) {
  override type OutMessage = TicTacToeClientMessage
  override type InMessage = TicTacToeServerMessage

  private var piece: Piece = _
  private var bot: TicTacToeBot = _

  override protected def receive(msg: Option[TicTacToeServerMessage], server: Server): Boolean = {
    msg match {
      case None => server.send(TicTacToeClient.Join)
      case Some(m) => m match {
        case TicTacToeServer.Join(p) =>
          if(p.isDefined) {
            piece = p.get
            bot = new DumbTicTacToeBot(piece)
          }
          else return false
        case TicTacToeServer.Move(board) =>
          bot.move(board) match { case (x, y) => server.send(TicTacToeClient.Move(x, y)) }
        case TicTacToeServer.Winner(p) =>
          val result = if (p == null) "Draw"
          else if (piece.eq(p)) "Victory"
          else "Loss"
          println(result)
          return false
      }
    }
    true
  }
}

object TicTacToeClient {
  sealed abstract class TicTacToeClientMessage extends Serializable
  case object Join extends TicTacToeClientMessage
  case class Move(x: Int, y: Int) extends TicTacToeClientMessage
}

