package moe.brianhsu.live2d.enitiy.updater

trait Updater {
  def executeOperations(operations: List[UpdateOperation]): Unit
}
