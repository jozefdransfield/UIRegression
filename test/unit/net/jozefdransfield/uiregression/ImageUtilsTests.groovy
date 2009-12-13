package net.jozefdransfield.uiregression

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.geom.Ellipse2D
import java.awt.Color
import java.awt.geom.Rectangle2D
import org.gmock.GMockTestCase
import java.awt.image.renderable.ParameterBlock
import javax.media.jai.JAI

public class ImageUtilsTests extends GMockTestCase {

  BufferedImage ellipse
  BufferedImage square

  void setUp() {
    ellipse = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB)
    square = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB)
    newEllipse(ellipse)
    newSquare(square)
  }

  private void newEllipse(BufferedImage bimage) {
    Graphics2D g2d = bimage.createGraphics();
    g2d.setColor(Color.red);
    g2d.fill(new Ellipse2D.Float(0, 0, 200, 100));
    g2d.dispose();
  }

  private void newSquare(BufferedImage bimage) {
    Graphics2D g2d = bimage.createGraphics();
    g2d.setColor(Color.red);
    g2d.fill(new Rectangle2D.Float(0, 0, 200, 100));
    g2d.dispose();
  }

  void testCompareImagesReturnsFalseWhenImagesNotEqual() {
    assertFalse ImageUtils.compareImages(ellipse, square)
  }

  void testCompareImagesReturnsTrueWhenImagesEquals() {
    assertTrue ImageUtils.compareImages(ellipse, ellipse)
  }

  void testGenerateComparisonImageCallsJAIAndReturnsSubtractedImage() {
    BufferedImage image1 = mock(BufferedImage)
    BufferedImage image2 = mock(BufferedImage)
    BufferedImage result = mock(BufferedImage)

    ordered {
      ParameterBlock mockPB = mock(ParameterBlock, constructor())
      mockPB.addSource(image1)
      mockPB.addSource(image2)

      mock(JAI).static.create("subtract", mockPB).returns(result)
    }

    def retVal
    play {
      retVal = ImageUtils.generateComparisonImage(image1, image2)
    }

    assertEquals(result, retVal)
  }

}