package moe.brianhsu.live2d.demo.swing.widget

import moe.brianhsu.live2d.demo.swing.Live2DUI
import moe.brianhsu.live2d.enitiy.avatar.Avatar

import java.awt.event.{MouseAdapter, MouseEvent}
import javax.swing.tree.{DefaultMutableTreeNode, DefaultTreeModel}
import javax.swing._
import scala.annotation.tailrec

class SwingMotionSelector(live2DWidget: Live2DUI) extends JPanel {
  private val scroll = new JScrollPane()
  private val motionTree = new JTree(new DefaultMutableTreeNode("Motions"))
  private val repeatCheckbox = new JCheckBox("Repeat motion")

  {
    this.setBorder(BorderFactory.createTitledBorder("Motions"))
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS))

    this.repeatCheckbox.setAlignmentX(0.0f)
    this.scroll.setViewportView(motionTree)
    this.scroll.setAlignmentX(0.0f)

    this.motionTree.setRootVisible(false)
    this.motionTree.addMouseListener(
      new MouseAdapter {
        override def mouseClicked(e: MouseEvent): Unit = handleMotionSelection(e)
      }
    )

    this.add(repeatCheckbox)
    this.add(scroll)

    expandAllNodes(motionTree, 0, motionTree.getRowCount)
  }

  private def handleMotionSelection(e: MouseEvent): Unit = {
    if (e.getClickCount == 2) {
      val selectedPath = motionTree.getPathForLocation(e.getX, e.getY)
      if (selectedPath.getPathCount == 3) {
        live2DWidget.demoAppHolder.foreach { view =>
          val motionGroup = selectedPath.getParentPath.getLastPathComponent.toString
          val currentIndex = selectedPath.getLastPathComponent.toString.toInt

          view.startMotion(motionGroup, currentIndex, repeatCheckbox.isSelected)
        }
      }
    }
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
    motionTree.setModel(new DefaultTreeModel(root))
    expandAllNodes(motionTree, 0, motionTree.getRowCount)
  }

}
