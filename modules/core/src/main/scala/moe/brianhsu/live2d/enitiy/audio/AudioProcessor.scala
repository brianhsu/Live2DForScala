package moe.brianhsu.live2d.enitiy.audio

trait AudioProcessor {
  def process(event: AudioEvent)
  def end()
}
