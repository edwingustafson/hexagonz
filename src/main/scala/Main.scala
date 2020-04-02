import processing.core._
import processing.core.PConstants._
import scala.math._

object Main extends App {
    PApplet.runSketch(Array("Sketch"), Sketch);
}

object Sketch extends PApplet {
  implicit def float2Double(x: Float): Double = x.toDouble
  implicit def double2Float(x: Double): Float = x.toFloat

  val input = "input.jpg"

  override def settings: Unit = {
    val photo = loadImage(input)
    size(photo.width, photo.height)
  }

  override def setup: Unit = {
    noLoop
    noStroke

    val photo = loadImage(input)
    image(photo, 0.0, 0.0)

    photo.loadPixels
    val pixels = photo.pixels

    //  Point-topped hexagonal grid

    val dx = 40.0
    val w = dx
    val r = w / sqrt(3.0) + 1.0
    val h = r
    val dy = 3.0 * r

    //  Focal point, hard-coded for now

    val fx = 715.0
    val fy = 560.0

    for {
      x <- BigDecimal(0.0) to BigDecimal(dx + width) by BigDecimal(dx)
      y <- BigDecimal(0.0) to BigDecimal(dy + height) by BigDecimal(dy)
    } {
      hexagon(pixels, x.doubleValue, y.doubleValue, r, fx, fy)
      hexagon(pixels, x.doubleValue + 0.5 * w, y.doubleValue + 1.5 * h, r, fx, fy)
    }

    save("output.png")
  }

  def hexagon(pixels: Array[Int], x: Double, y: Double, r: Double, fx: Double, fy: Double): Unit = {
    val x0: Int = clamp(x.toInt, 0, width - 1)
    val y0: Int = clamp(y.toInt, 0, height - 1)

    val angle = TWO_PI / 6.0

    fill(pixels(min(y0 * width + x0, pixels.size - 1)))

    beginShape

    (BigDecimal(0.0) to BigDecimal(TWO_PI) by BigDecimal(angle)).foreach(a => {
      val sa = a + angle / 2.0
      val sx = x + cos(sa.doubleValue) * r
      val sy = y + sin(sa.doubleValue) * r
      vertex(sx, sy)
    })

    endShape(CLOSE)
  }

  def clamp(n: Double, minimum: Double, maximum: Double): Double = minimum max n min maximum
  def clamp(n: Int, minimum: Int, maximum: Int): Int = minimum max n min maximum

  /** Calculate opacity based on distance from focal point  */
  def opacity(x: Double, y: Double, fx: Double, fy: Double): Double = {
    val distance = Math.sqrt(Math.pow(fx - x, 2.0) + Math.pow(fy - y, 2.0))
    clamp(0.25 * distance, 0.0, 255.0)
  }
}
