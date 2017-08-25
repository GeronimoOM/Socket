package utils

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object TryWith {
  def tryWith[C <: AutoCloseable, R](res: => C)(f: C => R): Try[R] =
    Try(res).flatMap(r => {
      try {
        val result = f(r)
        Try(r.close()).map(_ => result)
      } catch {
        case NonFatal(exceptionInFunction) =>
          r.close()
          Failure(exceptionInFunction)
      }
    })

  def tryWith[C1 <: AutoCloseable, C2 <: AutoCloseable, R](res1: => C1, res2: => C2)(f: (C1, C2) => R): Try[R] =
    Try((res1, res2)).flatMap({ case (r1, r2) => {
      try {
        val result = f(r1, r2)
        Try({r1.close(); r2.close()}).map(_ => result)
      } catch {
        case NonFatal(exceptionInFunction) =>
          Failure(exceptionInFunction)
      }
    }})


  def tryWith[C1 <: AutoCloseable, C2 <: AutoCloseable, C3 <: AutoCloseable, R](res1: => C1, res2: => C2, res3: => C3)(f: (C1, C2, C3) => R): Try[R] =
    Try((res1, res2, res3)).flatMap({ case (r1, r2, r3) => {
      try {
        val result = f(r1, r2, r3)
        Try({r1.close(); r2.close(); r3.close()}).map(_ => result)
      } catch {
        case NonFatal(exceptionInFunction) =>
          Failure(exceptionInFunction)
      }
    }})
}