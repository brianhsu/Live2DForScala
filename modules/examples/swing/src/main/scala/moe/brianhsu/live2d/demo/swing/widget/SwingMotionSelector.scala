package moe.brianhsu.live2d.demo.swing.widget

import moe.brianhsu.live2d.demo.swing.Live2DUI
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.LipSyncFromMotionSound
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy

import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import java.awt.{Component, GridBagConstraints, GridBagLayout}
import javax.swing._
import javax.swing.event.ChangeEvent
import javax.swing.tree.{DefaultMutableTreeNode, DefaultTreeCellRenderer, DefaultTreeModel}
import scala.annotation.{tailrec, unused}

class SwingMotionSelector(live2DWidget: Live2DUI) extends JPanel {
  private val scroll = new JScrollPane()
  private val motionTree = new JTree(new DefaultMutableTreeNode("Motions"))
  private val repeatCheckbox = new JCheckBox("Repeat motion")
  private val lipSync = new JCheckBox("Lip Sync")
  private val lipSyncWeight = new SwingSliderControl("Weight", 0, 100, 50)
  private val lipSyncVolume = new SwingSliderControl("Volume", 0, 100, 50)

  {
    this.setBorder(BorderFactory.createTitledBorder("Motions"))
    this.setLayout(new GridBagLayout)

    this.scroll.setViewportView(motionTree)
    this.lipSync.addActionListener(handleLipSyncSelection)
    this.lipSyncWeight.setEnabled(false)
    this.lipSyncWeight.slider.addChangeListener(updateLipSyncWeight)
    this.lipSyncVolume.setEnabled(false)
    this.lipSyncVolume.slider.addChangeListener(updateLipSyncVolume)
    this.motionTree.setCellRenderer(new DefaultTreeCellRenderer {
      private val soundIcon = new ImageIcon(this.getClass.getResource("/icons/sound.png"))
      override def getTreeCellRendererComponent(tree: JTree, value: Any, sel: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean): Component = {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)
        value.asInstanceOf[DefaultMutableTreeNode].getUserObject match {
          case x: MotionData if x.hasSound => setIcon(soundIcon)
          case _ => // Use default icon
        }
        this
      }
    })
    this.motionTree.setRootVisible(false)
    this.motionTree.addMouseListener(
      new MouseAdapter {
        override def mouseClicked(e: MouseEvent): Unit = handleMotionSelection(e)
      }
    )

    val gc1 = new GridBagConstraints()
    gc1.gridx = 0
    gc1.gridy = 0
    gc1.anchor = GridBagConstraints.NORTHWEST
    gc1.fill = GridBagConstraints.HORIZONTAL
    gc1.weightx = 1
    this.add(repeatCheckbox, gc1)

    val gc2 = new GridBagConstraints()
    gc2.anchor = GridBagConstraints.NORTHWEST
    gc2.gridx = 0
    gc2.gridy = 1
    gc2.fill = GridBagConstraints.HORIZONTAL
    gc2.weightx = 1
    this.add(lipSync, gc2)

    val gc3 = new GridBagConstraints()
    gc3.gridx = 0
    gc3.gridy = 2
    gc3.fill = GridBagConstraints.HORIZONTAL
    gc3.weightx = 1

    this.add(lipSyncWeight, gc3)

    val gc4 = new GridBagConstraints()
    gc4.anchor = GridBagConstraints.NORTHWEST
    gc4.gridx = 0
    gc4.gridy = 3
    gc4.fill = GridBagConstraints.HORIZONTAL
    gc4.weightx = 1
    this.add(lipSyncVolume, gc4)

    val gc5 = new GridBagConstraints()
    gc5.anchor = GridBagConstraints.NORTHWEST
    gc5.gridx = 0
    gc5.gridy = 4
    gc5.fill = GridBagConstraints.BOTH
    gc5.weightx = 1
    gc5.weighty = 1
    this.add(scroll, gc5)


    expandAllNodes(motionTree, 0, motionTree.getRowCount)
  }

  def syncWithStrategy(basicUpdateStrategy: BasicUpdateStrategy): Unit = {
    val effects = basicUpdateStrategy.effects
    val lipSyncHolder = effects.find(_.isInstanceOf[LipSyncFromMotionSound]).map(_.asInstanceOf[LipSyncFromMotionSound])
    lipSync.setSelected(lipSyncHolder.isDefined)
    lipSyncHolder.foreach { effect =>
      lipSyncWeight.slider.setValue((effect.weight * 10).toInt)
      lipSyncVolume.slider.setValue(effect.volume)

      lipSyncWeight.updatePercentage((effect.weight * 10).toInt)
      lipSyncVolume.updatePercentage(effect.volume)

      lipSyncWeight.setEnabled(true)
      lipSyncVolume.setEnabled(true)
    }
  }

  private def updateLipSyncWeight(@unused e: ChangeEvent): Unit = {
    live2DWidget.demoAppHolder.foreach(_.updateMotionLipSyncWeight(lipSyncWeight.slider.getValue))
  }

  private def updateLipSyncVolume(@unused e: ChangeEvent): Unit = {
    live2DWidget.demoAppHolder.foreach(_.updateMotionLipSyncVolume(lipSyncVolume.slider.getValue))
  }

  private def handleLipSyncSelection(@unused e: ActionEvent): Unit = {
    for {
      demoApp <- live2DWidget.demoAppHolder
    } {
      lipSyncWeight.setEnabled(lipSync.isSelected)
      lipSyncVolume.setEnabled(lipSync.isSelected)

      if (lipSync.isSelected) {
        demoApp.enableLipSyncFromMotionSound()
      } else {
        demoApp.disableLipSyncFromMotionSound()
      }
    }
  }

  private def handleMotionSelection(e: MouseEvent): Unit = {
    if (e.getClickCount == 2) {
      val selectedPath = motionTree.getPathForLocation(e.getX, e.getY)
      if (selectedPath.getPathCount == 3) {
        live2DWidget.demoAppHolder.foreach { view =>
          val motionGroup = selectedPath.getParentPath.getLastPathComponent.toString
          val currentMotion = selectedPath.getLastPathComponent.asInstanceOf[DefaultMutableTreeNode]

          view.startMotion(
            motionGroup,
            currentMotion.getUserObject.asInstanceOf[MotionData].index,
            repeatCheckbox.isSelected
          )
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
    val motionGroups = avatar.avatarSettings.motionGroups
    val groupNameList = motionGroups.keySet.toList.sorted
    for (groupName <- groupNameList) {
      val group = new DefaultMutableTreeNode(groupName)
      for (i <- avatar.avatarSettings.motionGroups(groupName).indices) {
        val treeNode = new DefaultMutableTreeNode(i)
        treeNode.setUserObject(MotionData(i, motionGroups(groupName)(i).sound.isDefined))
        group.add(treeNode)
      }
      root.add(group)
    }
    motionTree.setModel(new DefaultTreeModel(root))
    expandAllNodes(motionTree, 0, motionTree.getRowCount)
  }

  case class MotionData(index: Int, hasSound: Boolean) {
    override def toString: String = index.toString
  }

}
