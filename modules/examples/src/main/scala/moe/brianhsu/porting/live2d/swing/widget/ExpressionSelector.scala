package moe.brianhsu.porting.live2d.swing.widget

import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.porting.live2d.swing.Live2DWidget

import java.awt.event.{MouseAdapter, MouseEvent}
import javax.swing.{BorderFactory, JList, JScrollPane}

class ExpressionSelector(live2DWidget: Live2DWidget) extends JScrollPane {
  private val expressionJList = new JList()
  private var expressions: List[String] = Nil

  {
    this.setViewportView(expressionJList)
    this.setBorder(BorderFactory.createTitledBorder("Expression"))
    this.expressionJList.addMouseListener(new MouseAdapter {
      override def mouseClicked(e: MouseEvent): Unit = {
        if (e.getClickCount == 2) {
          live2DWidget.doWithLive2DView { view =>
            val index = expressionJList.locationToIndex(e.getPoint)
            val expressionName = expressions(index)
            view.startExpression(expressionName)
          }
        }
      }
    })
  }

  def updateExpressionList(avatar: Avatar): Unit = {
    expressions = avatar.avatarSettings.expressions.keySet.toList.sorted
    expressionJList.setListData(expressions.toArray.asInstanceOf[Array[Nothing]])
  }
}
