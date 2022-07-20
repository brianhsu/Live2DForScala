package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.LipSyncFromMotionSound
import moe.brianhsu.live2d.usecase.updater.impl.GenericUpdateStrategy
import moe.brianhsu.live2d.usecase.updater.impl.GenericUpdateStrategy.EffectTiming.{AfterExpression, BeforeExpression}
import org.eclipse.swt.SWT
import org.eclipse.swt.events.{SelectionEvent, SelectionListener}
import org.eclipse.swt.layout.{FillLayout, GridData, GridLayout}
import org.eclipse.swt.widgets._

import scala.annotation.unused

class SWTMotionSelector(parent: Composite) extends Composite(parent, SWT.NONE) {

  private var demoAppHolder: Option[DemoApp] = None

  private val group = new Group(this, SWT.SHADOW_ETCHED_IN)
  private val loop = createCheckbox("Loop")
  private val lipSync = createCheckbox("Lip Sync")
  private val lipSyncWeight = new SWTSliderControl("Weight:", 0, 100, 50, group)
  private val lipSyncVolume = new SWTSliderControl("Volume:", 0, 100, 100, group)

  private val motionTree = new Tree(group, SWT.BORDER)

  {
    this.setLayout(new FillLayout)

    val gridLayout = new GridLayout(1, false)
    group.setLayout(gridLayout)
    group.setText("Motions")

    val loopLayoutData = new GridData
    loopLayoutData.horizontalAlignment = GridData.FILL
    loopLayoutData.grabExcessVerticalSpace = false
    loopLayoutData.grabExcessHorizontalSpace = true
    loop.setLayoutData(loopLayoutData)

    val loopLayoutData2 = new GridData
    loopLayoutData2.horizontalAlignment = GridData.FILL
    loopLayoutData2.grabExcessVerticalSpace = false
    loopLayoutData2.grabExcessHorizontalSpace = true
    lipSync.setLayoutData(loopLayoutData2)

    val lipSyncWeightData = new GridData
    lipSyncWeightData.horizontalAlignment = GridData.FILL
    lipSyncWeightData.grabExcessVerticalSpace = false
    lipSyncWeightData.grabExcessHorizontalSpace = true
    lipSyncWeight.setLayoutData(lipSyncWeightData)

    val lipSyncVolumeData = new GridData
    lipSyncVolumeData.horizontalAlignment = GridData.FILL
    lipSyncVolumeData.grabExcessVerticalSpace = false
    lipSyncVolumeData.grabExcessHorizontalSpace = true
    lipSyncVolume.setLayoutData(lipSyncVolumeData)

    val treeLayoutData = new GridData
    treeLayoutData.horizontalAlignment = GridData.FILL
    treeLayoutData.verticalAlignment = GridData.FILL
    treeLayoutData.grabExcessVerticalSpace = true
    treeLayoutData.grabExcessHorizontalSpace = true

    lipSync.addListener(SWT.Selection, onLipSyncSelected)
    lipSyncWeight.setEnabled(false)
    lipSyncWeight.addChangeListener(onWeightChanged)
    lipSyncVolume.setEnabled(false)
    lipSyncVolume.addChangeListener(onVolumeChanged)

    motionTree.setLayoutData(treeLayoutData)
    motionTree.addSelectionListener(new SelectionListener {
      override def widgetSelected(e: SelectionEvent): Unit = {}
      override def widgetDefaultSelected(e: SelectionEvent): Unit = onMotionSelected()
    })
  }

  def syncWithStrategy(strategy: GenericUpdateStrategy): Unit = {
    val effects = strategy.effects(BeforeExpression) ++ strategy.effects(AfterExpression)
    val lipSyncHolder = effects.find(_.isInstanceOf[LipSyncFromMotionSound]).map(_.asInstanceOf[LipSyncFromMotionSound])
    lipSync.setSelection(lipSyncHolder.isDefined)
    lipSyncHolder.foreach { effect =>
      lipSyncWeight.updatePercentage((effect.weight * 10).toInt)
      lipSyncVolume.updatePercentage(effect.volume)
      lipSyncWeight.setEnabled(true)
      lipSyncVolume.setEnabled(true)
    }
  }


  private def onLipSyncSelected(@unused event: Event): Unit = {
    for {
      demoApp <- demoAppHolder
    } {
      lipSyncWeight.setEnabled(lipSync.getSelection)
      lipSyncVolume.setEnabled(lipSync.getSelection)
      demoApp.enableLipSyncFromMotionSound(lipSync.getSelection)
    }
  }

  private def onWeightChanged(percentage: Int): Unit = {
    demoAppHolder.foreach(_.updateMotionLipSyncWeight(percentage))
  }

  private def onVolumeChanged(percentage: Int): Unit = {
    demoAppHolder.foreach(_.updateMotionLipSyncVolume(percentage))
  }

  private def createCheckbox(title: String): Button = {
    val button = new Button(group, SWT.CHECK)
    button.setText(title)
    button
  }

  private def onMotionSelected(): Unit = {
    val selectedItem = motionTree.getSelection()(0)
    for {
      motionGroup <- Option(selectedItem.getParentItem).map(_.getText)
      avatar <- demoAppHolder
    } {
      avatar.startMotion(motionGroup, selectedItem.getData.asInstanceOf[Int], loop.getSelection)
    }
  }

  def setDemoApp(demoApp: DemoApp): Unit = {
    this.demoAppHolder = Some(demoApp)
  }

  def updateMotionTree(avatar: Avatar): Unit = {
    motionTree.removeAll()
    val motionGroups = avatar.avatarSettings.motionGroups
    val groupNameList = motionGroups.keySet.toList.sorted
    for (groupName <- groupNameList) {
      val group = new TreeItem(motionTree, SWT.NONE)
      group.setText(groupName)
      for (i <- avatar.avatarSettings.motionGroups(groupName).indices) {
        val hasSound = motionGroups(groupName)(i).sound.isDefined
        val treeItem = new TreeItem(group, SWT.NONE)
        val title = (if (hasSound) "\uD83D\uDD09 " else "") + i.toString
        treeItem.setText(title)
        treeItem.setData(i)
      }
      group.setExpanded(true)
    }
  }

}
