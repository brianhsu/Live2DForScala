package moe.brianhsu.live2d.demo.openSeeFace

import moe.brianhsu.live2d.demo.openSeeFace.CameraListing.CameraItem

class MacOSCameraListing extends CameraListing {

  override def listing: List[CameraItem] = {
    (1 to 10).map(i => CameraItem(i.toString, i)).toList
  }

}