package net.jozefdransfield.uiregression

import java.awt.image.BufferedImage
import java.awt.image.PixelGrabber
import java.awt.image.renderable.ParameterBlock
import javax.media.jai.JAI

public class ImageUtils {
  public static boolean compareImages(BufferedImage image1, BufferedImage image2) {
    try {

      PixelGrabber pixelGrabberImage1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
      PixelGrabber pixelGrabberImage2 = new PixelGrabber(image2, 0, 0, -1, -1, false);

      if (pixelGrabberImage1.grabPixels() && pixelGrabberImage2.grabPixels()) {
        int[] pixelsImage1 = (int[]) pixelGrabberImage1.getPixels();
        int[] pixelsImage2 = (int[]) pixelGrabberImage2.getPixels();
        if (pixelsImage1.length == pixelsImage2.length) {
          boolean equal = true;
          for (int i = 0; i < pixelsImage1.length; i++) {
            if (pixelsImage1[i] != pixelsImage2[i]) {
              equal = false;
              break;
            }
          }
          return equal;
        }
      }
    }
    catch (InterruptedException e1) {
      throw e1
    }
    return false;
  }

  public static BufferedImage generateComparisonImage(BufferedImage image1, BufferedImage image2) {
     ParameterBlock pb = new ParameterBlock()
     pb.addSource(image1)
     pb.addSource(image2)

     return JAI.create("subtract", pb)
  }

}