import com.live2d.framework.Cubism

object HelloWorld {

  def main(args: Array[String]): Unit = {
    val cubism = new Cubism()
    val model = cubism.loadModel("Haru.moc3")
    val canvasInfo = model.canvasInfo
    val parameters = model.parameters
    val parts = model.parts
    println(canvasInfo)
    println(parameters)
    println(parts)
    model.update()

    //println("================")
    //println(model.canvasInfo)

  }
}
