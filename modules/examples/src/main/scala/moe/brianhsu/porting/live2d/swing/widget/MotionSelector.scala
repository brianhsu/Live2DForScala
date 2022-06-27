package moe.brianhsu.porting.live2d.swing.widget

import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.porting.live2d.swing.Live2DWidget

import java.awt.event.{MouseAdapter, MouseEvent}
import javax.swing.tree.{DefaultMutableTreeNode, DefaultTreeModel}
import javax.swing._
import scala.annotation.tailrec

class MotionSelector(live2DWidget: Live2DWidget) extends JPanel {
  private val scroll = new JScrollPane()
  private val tree = new JTree(new DefaultMutableTreeNode("Motions"))
  private val checkbox = new JCheckBox("Repeat motion")

  {
    this.setBorder(BorderFactory.createTitledBorder("Motions"))
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS))
    this.scroll.setAlignmentX(0.0f)
    this.checkbox.setAlignmentX(0.0f)
    this.scroll.setViewportView(tree)
    this.tree.setRootVisible(false)
    this.tree.addMouseListener(new MouseAdapter {
      override def mouseClicked(e: MouseEvent): Unit = {
        if (e.getClickCount == 2) {
          val selectedPath = tree.getPathForLocation(e.getX, e.getY)
          if (selectedPath.getPathCount == 3) {
            live2DWidget.doWithLive2DView { view =>
              val motionGroup = selectedPath.getParentPath.getLastPathComponent.toString
              val currentIndex = selectedPath.getLastPathComponent.toString.toInt

              view.startMotion(motionGroup, currentIndex, checkbox.isSelected)
            }
          }
        }
      }
    })
    this.add(checkbox)
    this.add(scroll)
    expandAllNodes(tree, 0, tree.getRowCount)
  }

  @tailrec
  private def expandAllNodes(tree: JTree, startingIndex: Int, rowCount: Int): Unit = {
    (startingIndex until rowCount).foreach(tree.expandRow)
    if (tree.getRowCount != rowCount) expandAllNodes(tree, rowCount, tree.getRowCount)
  }

  def updateMotionTree(avatar: Avatar): Unit = {
    val root = new DefaultMutableTreeNode("Motions")
    val groupNameList = avatar.avatarSettings.motionGroups.keySet.toList.sorted
    for (groupName <- groupNameList) {
      val group = new DefaultMutableTreeNode(groupName)
      for (i <- avatar.avatarSettings.motionGroups(groupName).indices) {
        group.add(new DefaultMutableTreeNode(i))
      }
      root.add(group)
    }
    tree.setModel(new DefaultTreeModel(root))
    expandAllNodes(tree, 0, tree.getRowCount)
  }

}
