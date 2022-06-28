package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import org.eclipse.swt.SWT
import org.eclipse.swt.events.{SelectionEvent, SelectionListener}
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.{Composite, Group, List => SWTList}

class SWTExpressionSelector(parent: Composite) extends Composite(parent, SWT.NONE) {
  private var demoAppHolder: Option[DemoApp] = None
  private var expressions: List[String] = Nil

  val expressionGroup = new Group(this, SWT.SHADOW_ETCHED_IN)
  val expressionSelector = new SWTList(expressionGroup, SWT.V_SCROLL)

  {
    this.setLayout(new FillLayout)
    this.expressionGroup.setText("Expressions")
    this.expressionGroup.setLayout(new FillLayout)

    expressionSelector.addSelectionListener(new SelectionListener {
      override def widgetSelected(e: SelectionEvent): Unit = {}
      override def widgetDefaultSelected(e: SelectionEvent): Unit = onExpressionSelected()
    })
  }

  def setDemoApp(demoApp: DemoApp): Unit = {
    this.demoAppHolder = Some(demoApp)
  }

  def updateExpressionList(avatar: Avatar): Unit = {
    expressions = avatar.avatarSettings.expressions.keySet.toList.sorted
    expressionSelector.removeAll()
    expressions.foreach(expressionSelector.add)
  }

  private def onExpressionSelected(): Unit = {
    val expression = expressions(expressionSelector.getSelectionIndex)
    demoAppHolder.foreach(_.startExpression(expression))
  }

}
