package moe.brianhsu.live2d.usecase.updater

trait Updater {
  def executeOperations(operations: List[UpdateOperation]): Unit
}
