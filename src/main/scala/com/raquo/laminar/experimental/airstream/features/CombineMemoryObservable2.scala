package com.raquo.laminar.experimental.airstream.features

import com.raquo.laminar.experimental.airstream.core.{InternalParentObserver, MemoryObservable}

trait CombineMemoryObservable2[A, B, O] extends MemoryObservable[O] with CombineObservable[O] {

  val combinator: (A, B) => O

  protected[this] val parent1: MemoryObservable[A]
  protected[this] val parent2: MemoryObservable[B]

  override protected[this] var currentValue: O = combinator(parent1.now(), parent2.now())

  parentObservers.push(
    InternalParentObserver[A](parent1, (nextParent1Value, transaction) => {
      internalObserver.onNext(combinator(nextParent1Value, parent2.now()), transaction)
    }),
    InternalParentObserver[B](parent2, (nextParent2Value, transaction) => {
      internalObserver.onNext(combinator(parent1.now(), nextParent2Value), transaction)
    })
  )
}
