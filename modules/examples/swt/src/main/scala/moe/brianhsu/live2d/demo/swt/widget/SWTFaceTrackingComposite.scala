package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.demo.openSeeFace.{CameraListing, ExternalOpenSeeFaceDataReader}
import moe.brianhsu.live2d.demo.swt.widget.faceTracking.{SWTOpenSeeFaceAdvance, SWTOpenSeeFaceBundle, SWTOpenSeeFaceSetting}
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData.Point
import org.eclipse.swt.SWT
import org.eclipse.swt.custom.StackLayout
import org.eclipse.swt.events.PaintEvent
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.layout.{FillLayout, GridData, GridLayout}
import org.eclipse.swt.widgets.{Button, Canvas, Combo, Composite, Event, Group, Label, MessageBox}

import scala.annotation.unused
import scala.util.Try

class SWTFaceTrackingComposite(parent: Composite) extends Composite(parent, SWT.NONE) {


  private var demoAppHolder: Option[DemoApp] = None
  private var openSeeFaceDataHolder: Option[OpenSeeFaceData] = None
  private var openSeeFaceReaderHolder: Option[ExternalOpenSeeFaceDataReader] = None

  private val combo = createModeSelector(this)
  private val openSeeFaceGroup = new Group(this, SWT.BORDER)
  private val stackLayout = new StackLayout
  private val openSeeFacePanel = new Composite(openSeeFaceGroup, SWT.NONE)
  private val bundle = new SWTOpenSeeFaceBundle(openSeeFacePanel, CameraListing.createByOS())
  private val advanced = new SWTOpenSeeFaceAdvance(openSeeFacePanel)
  private val (startButton, stopButton, buttonComposite, buttonStackLayout) = createStartStopButton(openSeeFaceGroup)
  private val outlineGroup = new Group(this, SWT.BORDER)
  private val canvas = new Canvas(outlineGroup, SWT.NONE)


  {
    this.setLayout(new GridLayout(1, false))
    this.openSeeFaceGroup.setLayout(new GridLayout(2, false))
    this.openSeeFaceGroup.setText("OpenSeeFace Settings")

    this.outlineGroup.setText("Outline")
    this.outlineGroup.setLayout(new FillLayout)

    this.openSeeFacePanel.setLayout(stackLayout)

    this.stackLayout.topControl = bundle

    val gridData1 = new GridData()
    gridData1.horizontalAlignment = GridData.FILL
    gridData1.grabExcessHorizontalSpace = true
    this.openSeeFaceGroup.setLayoutData(gridData1)
    this.outlineGroup.setLayoutData(gridData1)

    val gridData2 = new GridData()
    gridData2.horizontalAlignment = GridData.FILL
    gridData2.grabExcessHorizontalSpace = true
    gridData2.horizontalSpan = 2

    this.openSeeFacePanel.setLayoutData(gridData2)

    this.combo.addListener(SWT.Selection, onModeSelected)
    this.startButton.addListener(SWT.Selection, onStartSelected)
    this.stopButton.addListener(SWT.Selection, onStopSelected)
    this.canvas.addPaintListener(onCanvasPaint)
  }

  def setDemoApp(demoApp: DemoApp): Unit = {
    this.demoAppHolder = Some(demoApp)
  }

  def enableStartButton(): Unit = {
    this.startButton.setEnabled(true)
  }

  private def createStartStopButton(parent: Composite) = {
    val composite = new Composite(parent, SWT.NONE)
    val stackLayout = new StackLayout

    val startButton = new Button(composite, SWT.PUSH)
    val stopButton = new Button(composite, SWT.PUSH)

    val gridData = new GridData()
    gridData.horizontalAlignment = GridData.FILL
    gridData.grabExcessHorizontalSpace = true
    gridData.horizontalSpan = 2

    composite.setLayout(stackLayout)
    composite.setLayoutData(gridData)

    stackLayout.topControl = startButton

    stopButton.setText("Stop")
    stopButton.setEnabled(false)

    startButton.setText("Start")
    startButton.setEnabled(false)

    (startButton, stopButton, composite, stackLayout)
  }

  private def getOpenSeeFaceSetting: SWTOpenSeeFaceSetting = {
    this.combo.getSelectionIndex match {
      case 0 => bundle
      case 1 => advanced
    }
  }

  private def onCanvasPaint(e: PaintEvent): Unit = {

    if (openSeeFaceReaderHolder.isDefined && openSeeFaceDataHolder.isEmpty) {

      if (System.currentTimeMillis() / 500 % 2 == 0) {
        val oldBackground = e.gc.getBackground
        e.gc.setBackground(new Color(255, 0, 0))
        e.gc.fillOval(10, 10, 20, 20)
        e.gc.setBackground(oldBackground)
      }

      if (!this.canvas.getDisplay.isDisposed) {
        this.canvas.getDisplay.timerExec(500, () => {
          if (!this.canvas.isDisposed) {
            this.canvas.redraw()
            this.canvas.update()
          }
        })
      }
    }

    openSeeFaceDataHolder.foreach { data =>

      if (System.currentTimeMillis() / 500 % 2 == 0) {
        val oldBackground = e.gc.getBackground
        e.gc.setBackground(new Color(255, 0, 0))
        e.gc.fillOval(10, 10, 20, 20)
        e.gc.setBackground(oldBackground)
      }

      e.gc.setForeground(new Color(0, 0, 255))
      val scaleX = e.width / data.resolution.width
      val scaleY = e.height / data.resolution.height

      def convert(p: Point): List[Int] = {
        List((p.x * scaleX - (e.width / 4)).toInt * 2, (p.y * scaleY - (e.height / 3)).toInt * 2)
      }

      val faceOutline: Array[Int] = data.landmarks.slice(0, 17).flatMap(convert).toArray
      val noseVertical: Array[Int] = data.landmarks.slice(27, 31).flatMap(convert).toArray
      val noseHorizontal: Array[Int] = data.landmarks.slice(31, 36).flatMap(convert).toArray
      val rightEyeBrow: Array[Int] = data.landmarks.slice(17, 22).flatMap(convert).toArray
      val leftEyeBrow: Array[Int] = data.landmarks.slice(22, 27).flatMap(convert).toArray
      val rightEye: Array[Int] = data.landmarks.slice(36, 42).appended(data.landmarks(36)).flatMap(convert).toArray
      val leftEye: Array[Int] = data.landmarks.slice(42, 48).appended(data.landmarks(42)).flatMap(convert).toArray
      val mouth: Array[Int] = data.landmarks.slice(48, 59).appended(data.landmarks(48)).flatMap(convert).toArray
      val upperLip: Array[Int] = data.landmarks.slice(59, 62).flatMap(convert).toArray
      val lowerLip: Array[Int] = data.landmarks.slice(63, 66).flatMap(convert).toArray

      e.gc.setLineWidth(2)
      e.gc.drawPolyline(faceOutline)
      e.gc.drawPolyline(noseVertical)
      e.gc.drawPolyline(noseHorizontal)
      e.gc.drawPolyline(rightEyeBrow)
      e.gc.drawPolyline(leftEyeBrow)
      e.gc.drawPolyline(rightEye)
      e.gc.drawPolyline(leftEye)
      e.gc.drawPolyline(mouth)
      e.gc.drawPolyline(upperLip)
      e.gc.drawPolyline(lowerLip)
    }
  }

  private def onModeSelected(@unused event: Event): Unit = {
    val topControl = this.combo.getSelectionIndex match {
      case 0 => bundle
      case 1 => advanced
    }
    stackLayout.topControl = topControl
    openSeeFacePanel.layout(true)
  }

  private def onStopSelected(@unused event: Event): Unit = {
    this.demoAppHolder.foreach(_.disableFaceTracking())
    this.openSeeFaceReaderHolder = None

    this.openSeeFaceDataHolder = None

    this.canvas.redraw()
    this.canvas.update()

    this.startButton.setEnabled(true)
    this.stopButton.setEnabled(false)
    this.buttonStackLayout.topControl = this.startButton
    this.buttonComposite.layout(true)
  }

  private def onStartSelected(@unused event: Event): Unit = {
    demoAppHolder.foreach(_.disableFaceTracking())

    val settings = getOpenSeeFaceSetting
    val dataReader: Try[ExternalOpenSeeFaceDataReader] = for {
      reader <- Try(ExternalOpenSeeFaceDataReader(settings.getCommand, settings.getHostname, settings.getPort, onDataRead))
      startedReader <- reader.ensureStarted()
    } yield {
      startedReader
    }

    this.openSeeFaceReaderHolder = dataReader.toOption

    dataReader.failed.foreach { e =>
      val messageBox = new MessageBox(this.getShell, SWT.OK)
      messageBox.setText("Failed to start OpenSeeFace")
      messageBox.setMessage(e.getMessage)
      messageBox.open()
    }

    for {
      demoApp <- demoAppHolder
      reader <- dataReader
    } {
      demoApp.enableFaceTracking(reader)
      this.startButton.setEnabled(false)
      this.stopButton.setEnabled(true)
      this.buttonStackLayout.topControl = this.stopButton
      this.buttonComposite.layout(true)
    }

    this.canvas.redraw()
    this.canvas.update()

  }


  private def onDataRead(data: OpenSeeFaceData): Unit = {
    this.openSeeFaceDataHolder = Some(data)
    if (!this.getDisplay.isDisposed) {
      this.getDisplay.asyncExec(() => {
        if (!this.canvas.isDisposed) {
          this.canvas.redraw()
          this.canvas.update()
        }
      })
    }
  }

  private def createModeSelector(parent: Composite): Combo = {
    val composite = new Composite(parent, SWT.NONE)
    val label = new Label(composite, SWT.NONE)
    val combo = new Combo(composite, SWT.DROP_DOWN|SWT.READ_ONLY)

    composite.setLayout(new GridLayout(2, false))
    combo.setItems("Bundle", "Advanced")
    combo.select(0)

    val gridData = new GridData
    gridData.horizontalAlignment = GridData.FILL
    gridData.grabExcessHorizontalSpace = true
    combo.setLayoutData(gridData)
    composite.setLayoutData(gridData)

    label.setText("Mode: ")
    combo
  }

}
