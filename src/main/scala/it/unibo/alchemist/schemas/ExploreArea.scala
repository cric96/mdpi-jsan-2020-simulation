package it.unibo.alchemist.schemas

import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition
import it.unibo.alchemist.model.interfaces.{Action, Environment, Node, Reaction}
import it.unibo.alchemist.node.DroneNode
case class ExploreArea[T](env : Environment[T, Euclidean2DPosition], node : DroneNode[T, Euclidean2DPosition],
                          centerX : Double, centerY : Double, radius : Double, weight : Double, thr : Double)
  extends MotorSchema[T, Euclidean2DPosition](env, node, weight) {
  private val diameter = radius * 2
  private var targetPosition = randomPositionInCircle
  private var behaviour = Seek(env, node, targetPosition.getX, targetPosition.getY, weight)
  def this(env : Environment[T, Euclidean2DPosition], node : DroneNode[T, Euclidean2DPosition], centerX : Double, centerY : Double, radius : Double, weight : Double) {
    this(env, node, centerX, centerY, radius, weight, 0.01)
  }
  override def cloneAction(n: Node[T], r: Reaction[T]): Action[T] = ExploreArea[T](env, node, centerX, centerY, radius, weight, thr)
  override def unweightedVector: Euclidean2DPosition = {
    if(reached) {
      targetPosition = randomPositionInCircle
      behaviour = behaviour.copy(px = targetPosition.getX, py = targetPosition.getY)
    }
    behaviour.velocityVector
  }
  private def reached : Boolean = targetPosition.getDistanceTo(env.getPosition(node)) < thr
  private def randomPositionInCircle = new Euclidean2DPosition(randomCoordInCircle(centerX), randomCoordInCircle(centerY))
  private def randomCoordInCircle(centerCoord : Double) : Double = centerCoord + (radius - math.random() * diameter)
}
