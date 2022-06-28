package moe.brianhsu.live2d.demo.swing

import com.jogamp.opengl.GLAutoDrawable

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

class FixedFPSAnimator(fps: Int, drawable: GLAutoDrawable) {
  private val scheduledThreadPool = new ScheduledThreadPoolExecutor(1)
  private val updateOpenGLCanvas = new Runnable {
    override def run(): Unit = {
      try {
        drawable.display()
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }

  def start(): Unit = {
    createScheduledFuture()
  }

  def stop(): Unit = {
    scheduledThreadPool.shutdown()
  }

  private def createScheduledFuture() = {
    scheduledThreadPool.scheduleAtFixedRate(
      updateOpenGLCanvas, 0, calculateExecutionPeriod,
      TimeUnit.MILLISECONDS
    )
  }

  private def calculateExecutionPeriod: Int = {
    val updateIntervalInSeconds = 1 / fps.toDouble
    val updateIntervalInMilliSeconds = updateIntervalInSeconds * 1000
    updateIntervalInMilliSeconds.toInt
  }

}
