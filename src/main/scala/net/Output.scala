package net

import java.io.{ObjectOutputStream, OutputStream}

trait Output {
  type Out <: OutputStream
  def output: OutputStream => Out
}

trait DefaultOutput extends Output {
  type Out = OutputStream
  override def output = identity
}

trait ObjectOutput extends Output {
  type Out = ObjectOutputStream
  override def output: (OutputStream) => ObjectOutputStream = os => new ObjectOutputStream(os)
}

