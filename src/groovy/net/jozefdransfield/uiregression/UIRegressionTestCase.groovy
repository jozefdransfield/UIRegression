package net.jozefdransfield.uiregression

import com.thoughtworks.selenium.SeleneseTestCase
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

public class UIRegressionTestCase extends SeleneseTestCase {

  public void navigateToAssertScreenShot(String screenShotName, Closure closure) {
    initialiseReportDirectory(screenShotName)
    closure.call()
    selenium.captureEntirePageScreenshot("/Users/jozefdransfield/Desktop/${screenShotName}/result.png", "")

    if (!loadScreenShotsAndCompare(screenShotName)) {
      throw new IllegalArgumentException("This should really be a call to fail")
    }
  }

  private boolean loadScreenShotsAndCompare(String screenShotName) {
    File result = loadResultFile(screenShotName)
    File reference = loadReferenceFile(screenShotName)

    return compareResultToReference(result, reference)
  }

  private void initialiseReportDirectory(String screenShotName) {
    File reportDir = new File("/Users/jozefdransfield/Desktop/${screenShotName}")
    if (!reportDir.exists()) {
      reportDir.mkdir()
    }
  }

  private File loadReferenceFile(String screenShotName) {
    return new File("/Users/jozefdransfield/Desktop/${screenShotName}/reference.png") 
  }

  private File loadResultFile(String screenShotName) {
    return new File("/Users/jozefdransfield/Desktop/${screenShotName}/result.png")     
  }

  private Boolean compareResultToReference(File result, File reference) {
    BufferedImage resultImage = ImageIO.read(result)
    BufferedImage referenceImage = ImageIO.read(reference)

    return ImageUtils.compareImages(resultImage, referenceImage) 
  }

}