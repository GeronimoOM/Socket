package net
import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.Socket
import java.util.Collections
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

//TODO Refactor everything

abstract class MessageServer(override val port: Int) extends AbstractServer(port: Int)
  with ObjectInput with ObjectOutput with AsyncProcessing {

  case class Client(in: ObjectInputStream, out: ObjectOutputStream, socket: Socket) {
    def send(msg: OutMessage) = out.writeObject(Some(msg))
  }

  override protected def work(in: ObjectInputStream, out: ObjectOutputStream, socket: Socket) {
    val client = Client(in, out, socket)
    val mailbox = new LinkedBlockingQueue[Option[InMessage]](Collections.singleton(None))
    val continue = new AtomicBoolean(true)
    Future { while(continue.get) {
      val msg = mailbox.take()
      continue.set(receive(msg, client))
    }}
    while(continue.get) {
      val msg = in.readObject()
      if(msg.isInstanceOf[Option[InMessage]]) {
        mailbox.add(msg.asInstanceOf[Option[InMessage]])
      }
    }
  }

  type OutMessage <: AnyRef with Serializable
  type InMessage <: AnyRef with Serializable
  protected def receive(msg: Option[InMessage], client: Client): Boolean
}

abstract class MessageClient(override val host: String, override val port: Int)
  extends AbstractClient(host, port) with ObjectInput with ObjectOutput {

  case class Server(in: ObjectInputStream, out: ObjectOutputStream) {
    def send(msg: OutMessage) = out.writeObject(Some(msg))
  }

  override protected def work(in: ObjectInputStream, out: ObjectOutputStream) {
    val server = Server(in, out)
    val mailbox = new LinkedBlockingQueue[Option[InMessage]](Collections.singleton(None))
    val continue = new AtomicBoolean(true)
    Future { while(continue.get) {
      val msg = mailbox.take()
      continue.set(receive(msg, server))
    }}
    while(continue.get) {
      val msg = in.readObject()
      if(msg.isInstanceOf[Option[InMessage]]) {
        mailbox.add(msg.asInstanceOf[Option[InMessage]])
      }
    }
  }

  type OutMessage <: AnyRef with Serializable
  type InMessage <: AnyRef with Serializable
  protected def receive(msg: Option[InMessage], server: Server): Boolean
}