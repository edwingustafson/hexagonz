import processing.core._
import processing.core.PConstants._
import scala.math._

object Main extends App {
  override def main(args: Array[String]) =
    PApplet.runSketch(Array("Sketch"), Sketch);
}

object Sketch extends PApplet {
  val input = "input.jpg"

  override def settings = {
    val photo = loadImage(input)
    size(photo.width, photo.height)
  }

  override def setup = {
    noLoop
    noStroke

    val photo = loadImage(input)
    image(photo, 0, 0)

    photo.loadPixels
    val pixels = photo.pixels

    //  Point-topped hexagonal grid

    val dx = 15.0
    val w = dx
    val r = w / sqrt(3.0) + 1.0
    val h = r
    val dy = 3 * r

    //  Focal point, hard-coded for now

    val fx = 715.0
    val fy = 560.0

    for {
      x <- 0.0 to dx + width by dx
      y <- 0.0 to dy + height by dy
    } {
      hexagon(pixels, x, y, r, fx, fy)
      hexagon(pixels, x + 0.5 * w, y + 1.5 * h, r, fx, fy)
    }

    save("output.png")
  }

  def hexagon(pixels: Array[Int], x: Double, y: Double, r: Double, fx: Double, fy: Double) = {
    val x0: Int = clamp(x.toInt, 0, width - 1)
    val y0: Int = clamp(y.toInt, 0, height - 1)

    val angle = TWO_PI.toDouble / 6.0

    fill(pixels(min(y0 * width + x0, pixels.size - 1)), opacity(x, y, fx, fy).toFloat)

    beginShape

    (0.0 to TWO_PI.toDouble by angle).foreach(a => {
      val sa = a + angle / 2.0
      val sx = x.toDouble + cos(sa) * r
      val sy = y.toDouble + sin(sa) * r
      vertex(sx.toFloat, sy.toFloat)
    })

    endShape(CLOSE)
  }

  def clamp(n: Double, minimum: Double, maximum: Double): Double = minimum max n min maximum
  def clamp(n: Int, minimum: Int, maximum: Int): Int = minimum max n min maximum

  /** Calculate opacity based on distance from focal point  */
  def opacity(x: Double, y: Double, fx: Double, fy: Double): Double = {
    val distance = Math.sqrt(Math.pow(fx - x, 2) + Math.pow(fy - y, 2))
    clamp(0.25 * distance, 0.0, 255.0)
  }
}
