package it.unibo.alchemist.model.implementations.actions

import it.unibo.alchemist.model.implementations.nodes.{Animal2D, SimpleNodeManager}
import it.unibo.alchemist.model.interfaces.{Action, Context, Environment, Node, Position, Reaction}
import it.unibo.alchemist.toList
import org.apache.commons.math3.random.RandomGenerator

import scala.collection.mutable.ArrayBuffer
case class IllAnimal[T, P <: Position[P]](env : Environment[T, P], rand: RandomGenerator, node : Node[T], timeLimit : Double) extends AbstractAction[T](node) {
  def this(env : Environment[T, P], node : Node[T], rand : RandomGenerator) {
    this(env, rand, node, Double.PositiveInfinity)
  }
  override def cloneAction(n: Node[T], r: Reaction[T]): Action[T] = IllAnimal(env, rand, node, timeLimit) //todo fix this.. the clone must be done in the right place
  private lazy val animals : List[Animal2D[T]] = env.getNodes.collect { case n : Animal2D[T] => n }
  private lazy val shuffledAnimals = shuffle(animals).map(new SimpleNodeManager[T](_))
  private lazy val manager = new SimpleNodeManager[T](node)
  private var count = 0
  override def execute(): Unit = {
    if(env.getSimulation.getTime.toDouble < timeLimit) {
      shuffledAnimals.filter(_.has("danger"))
        .filterNot(_.get[Boolean]("danger"))
        .headOption
        .foreach(animal => {
          count += 1
          animal.put("danger", true)
        })
    }
    manager.put("dangerNodeSpawn", count)
  }
  override def getContext: Context = Context.LOCAL
  private def shuffle(animals: List[Animal2D[T]]) : List[Animal2D[T]] = {
    val buf = new ArrayBuffer[Animal2D[T]] ++= animals
    def swap(i1: Int, i2: Int) {
      val tmp = buf(i1)
      buf(i1) = buf(i2)
      buf(i2) = tmp
    }
    for (n <- buf.length to 2 by -1) {
      val k = rand.nextInt(n)
      swap(n - 1, k)
    }
    (buf).result().toList
  }
}
