package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.enitiy.avatar.Avatar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.{SelectionEvent, SelectionListener}
import org.eclipse.swt.layout.{FillLayout, GridData, GridLayout}
import org.eclipse.swt.widgets._

class SWTMotionSelector(parent: Composite) extends Composite(parent, SWT.NONE) {

  private var demoAppHolder: Option[DemoApp] = None

  private val group = new Group(this, SWT.SHADOW_ETCHED_IN)
  private val loop = createLoopCheckbox()
  private val motionTree = new Tree(group, SWT.BORDER)

  {
    this.setLayout(new FillLayout)

    group.setLayout(new GridLayout)
    group.setText("Motions")

    val treeLayoutData = new GridData
    treeLayoutData.horizontalAlignment = GridData.FILL
    treeLayoutData.verticalAlignment = GridData.FILL
    treeLayoutData.grabExcessVerticalSpace = true
    treeLayoutData.grabExcessHorizontalSpace = true

    motionTree.setLayoutData(treeLayoutData)
    motionTree.addSelectionListener(new SelectionListener {
      override def widgetSelected(e: SelectionEvent): Unit = {}
      override def widgetDefaultSelected(e: SelectionEvent): Unit = onMotionSelected()
    })

  }

  private def createLoopCheckbox(): Button = {
    val button = new Button(group, SWT.CHECK)
    button.setText("Loop")
    button
  }

  private def onMotionSelected(): Unit = {
    val selectedItem = motionTree.getSelection()(0)
    for {
      motionGroup <- Option(selectedItem.getParentItem).map(_.getText)
      avatar <- demoAppHolder
    } {
      avatar.startMotion(motionGroup, selectedItem.getText.toInt, loop.getSelection)
    }
  }

  def setDemoApp(demoApp: DemoApp): Unit = {
    this.demoAppHolder = Some(demoApp)
  }

  def updateMotionTree(avatar: Avatar): Unit = {
    motionTree.removeAll()
    val groupNameList = avatar.avatarSettings.motionGroups.keySet.toList.sorted
    for (groupName <- groupNameList) {
      val group = new TreeItem(motionTree, SWT.NONE)
      group.setText(groupName)
      for (i <- avatar.avatarSettings.motionGroups(groupName).indices) {
        val treeItem = new TreeItem(group, SWT.NONE)
        treeItem.setText(i.toString)
      }
      group.setExpanded(true)
    }
  }

}
