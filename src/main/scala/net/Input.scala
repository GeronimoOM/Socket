package net

import java.io.{InputStream, ObjectInputStream}

trait Input {
  type In <: InputStream
  def input: InputStream => In
}

trait DefaultInput extends Input {
  type In = InputStream
  override def input = identity
}

trait ObjectInput extends Input {
  type In = ObjectInputStream
  override def input: (InputStream) => In = new ObjectInputStream(_)
}