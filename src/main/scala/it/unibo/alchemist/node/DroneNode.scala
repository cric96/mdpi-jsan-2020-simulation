package it.unibo.alchemist.node

import it.unibo.alchemist.model.implementations.nodes.ScafiNode
import it.unibo.alchemist.model.interfaces.{Environment, Position}

abstract class DroneNode[T, P <: Position[P]](env : Environment[T, P]) extends ScafiNode[T, P](env) with DroneLike[P] {
  def setVector(v : P) : Unit
}