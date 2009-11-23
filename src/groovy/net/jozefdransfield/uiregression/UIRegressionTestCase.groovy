package net.jozefdransfield.uiregression

import com.thoughtworks.selenium.SeleneseTestCase
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import com.thoughtworks.selenium.DefaultSelenium
import grails.util.BuildSettingsHolder

public class UIRegressionTestCase extends SeleneseTestCase {

  /* Not tested*/

  public void setUp(String rootUrl, String browserString) {
    String seleniumServer = BuildSettingsHolder.getSettings().config.uiregression.selenium.server
    int seleniumPort = BuildSettingsHolder.getSettings().config.uiregression.selenium.port

    selenium = new DefaultSelenium(seleniumServer, seleniumPort, browserString, rootUrl);
    selenium.start();
  }

  public void navigateToAssertScreenShot(String screenShotName, Closure closure) {
    initialiseReportDirectory(screenShotName)
    closure.call()
    selenium.captureEntirePageScreenshot(resultImagePath(screenShotName), "")

    if (!loadScreenShotsAndCompare(screenShotName)) {
      fail("I failed")
    }
  }

  private boolean loadScreenShotsAndCompare(String screenShotName) {
    File result = loadResultFile(screenShotName)
    File reference = loadReferenceFile(screenShotName)

    //println "Should regenerate " + System.getProperty("uiregression.regenerate")
    // Add code here to put result as reference and return true

    return compareResultToReference (result, reference)
  }

  private void initialiseReportDirectory(String screenShotName) {
    File referenceReportDir = new File(rootReferenceReportDir(screenShotName))
    if (!referenceReportDir.exists()) {
      referenceReportDir.mkdir()
    }

    File resultReportDir = new File(rootResultReportDir(screenShotName))
    if (!resultReportDir.exists()) {
      resultReportDir.mkdir()
    }
  }

  private File loadReferenceFile(String screenShotName) {
    return new File(referenceImagePath(screenShotName))
  }

  private File loadResultFile(String screenShotName) {
    return new File(resultImagePath(screenShotName))
  }

  private Boolean compareResultToReference(File result, File reference) {
    BufferedImage resultImage = ImageIO.read(result)
    BufferedImage referenceImage = ImageIO.read(reference)

    return ImageUtils.compareImages(resultImage, referenceImage)
  }

   private String referenceImagePath(String screenShotName) {
    return rootReferenceReportDir(screenShotName) + "reference.png"
  }

  private String resultImagePath(String screenShotName) {
    return rootResultReportDir(screenShotName) + "result.png"
  }

  private String rootReferenceReportDir(screenShotName) {
    return BuildSettingsHolder.getSettings().config.uiregression.reference.path + "/${screenShotName}/"
  }

   private String rootResultReportDir(screenShotName) {
    return BuildSettingsHolder.getSettings().config.uiregression.result.path + "/${screenShotName}/"
  }

}