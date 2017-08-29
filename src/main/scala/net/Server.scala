package net

import java.net.{InetAddress, ServerSocket, Socket}

import utils.TryWith._

import scala.concurrent.{ExecutionContext, Future}

trait Server {
  def run()
  def shutdown()
}

abstract class AbstractServer(val port: Int) extends Server
  with Input with Output {

  val server: ServerSocket = new ServerSocket(port, 0, InetAddress.getLoopbackAddress)
  private var isShutdown: Boolean = _

  def run() { tryWith(server) { server => {
    while (!isShutdown) {
      process(server.accept())
    }
  }}}

  def shutdown() {
    isShutdown = true
  }

  protected def process(socket: Socket) {
    tryWith(socket) { socket => {
      tryWith(output(socket.getOutputStream()), input(socket.getInputStream())) { (out, in) => {
        work(in, out, socket)
      }}
    }}
  }

  protected def work(in: In, out: Out, client: Socket)
}

trait AsyncProcessing extends AbstractServer {
  import ExecutionContext.Implicits.global

  override protected def process(client: Socket) { Future {
    super.process(client)
  }}
}
