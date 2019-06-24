import processing.core._
import processing.core.PConstants._
import scala.math._

object Main extends App {
  override def main(args: Array[String]) = PApplet.runSketch(Array("Sketch"), Sketch);
}

object Sketch extends PApplet {
  override def settings = size(1920, 1080)
  
  override def setup = {
    noLoop
    noStroke

    val photo = loadImage("input.jpg")
    image(photo, 0, 0)

    photo.loadPixels
    val pixels = photo.pixels

    //  Point-topped hexagonal grid

    val dx = 12.0
    val w = dx
    val r = w / sqrt(3.0) + 1.0
    val h = r
    val dy = 3 * r

    for {
      x <- 0.0 to dx + width by dx
      y <- 0.0  to dy + height by dy
    } {
      hexagon(pixels, x, y, r)
      hexagon(pixels, x + 0.5 * w, y + 1.5 * h, r)
    }

    save("output.png")
  }

  def hexagon(pixels: Array[Int], x: Double, y: Double, r: Double) = {
    val x0: Int = clamp(x.toInt, 0, width - 1)
    val y0: Int = clamp(y.toInt, 0, height - 1)

    val angle = TWO_PI.toDouble / 6.0

    fill(pixels(min(y0 * width + x0, pixels.size -1)), 255.0f)

    beginShape

    (0.0 to TWO_PI.toDouble by angle).foreach(a => {
      val r0 = 1.0 * r

      val sa = a + angle / 2.0
      val sx = x.toDouble + cos(sa) * r0
      val sy = y.toDouble + sin(sa) * r0
      vertex(sx.toFloat, sy.toFloat)
    })

    endShape(CLOSE)
  }

  def clamp(n: Int, minimum: Int, maximum: Int) = if ( n < minimum )
    minimum
  else
    if ( n >= maximum )
      maximum
    else
      n
}