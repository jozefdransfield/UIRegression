package net.jozefdransfield.uiregression

import com.thoughtworks.selenium.SeleneseTestCase
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import com.thoughtworks.selenium.DefaultSelenium
import grails.util.BuildSettingsHolder
import java.awt.image.RenderedImage
import org.apache.commons.codec.binary.Base64

public class UIRegressionTestCase extends SeleneseTestCase {

  public void setUp(String rootUrl, String browserString) {
    String seleniumServer = BuildSettingsHolder.getSettings().config.uiregression.selenium.host
    int seleniumPort = BuildSettingsHolder.getSettings().config.uiregression.selenium.port

    selenium = new DefaultSelenium(seleniumServer, seleniumPort, browserString, rootUrl)
    selenium.start()
  }

  public void navigateToAssertScreenShot(String screenShotName, Closure closure) {
    initialiseReportDirectory(screenShotName)
    closure.call()
    String screen = selenium.captureEntirePageScreenshotToString("")
    byte[] imageBytes = Base64.decodeBase64(screen.getBytes("UTF-8"))
    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes))
    ImageIO.write(image, "png", new File(resultImagePath(screenShotName)))

    if (!loadScreenShotsAndCompare(screenShotName)) {
      File resultImage = new File(resultImagePath(screenShotName))
      File referenceImage = new File(referenceImagePath(screenShotName))
      File diffImage = new File(diffImagePath(screenShotName))
      fail("Result Image at [result:${resultImage.absolutePath}]\n did not match [reference:${referenceImage.absolutePath}]\n for ID: [${screenShotName}]\n [diff:${diffImage.absolutePath}]")
    }
  }

  private boolean loadScreenShotsAndCompare(String screenShotName) {
    File result = loadResultFile(screenShotName)
    File reference = loadReferenceFile(screenShotName)

    println result

    if (System.getProperty("uiregression.regenerate")) {
      FileUtils.copyFile(result, reference)
      return true
    } else {
      return compareResultToReference(result, reference, screenShotName)
    }
  }

  private void initialiseReportDirectory(String screenShotName) {
    File referenceReportDir = new File(rootReferenceReportDir(screenShotName))
    if (!referenceReportDir.exists()) {
      referenceReportDir.mkdir()
    }

    File resultReportDir = new File(rootResultReportDir(screenShotName))
    if (!resultReportDir.exists()) {
      resultReportDir.mkdirs()
    }
  }

  private File loadReferenceFile(String screenShotName) {
    return new File(referenceImagePath(screenShotName))
  }

  private File loadResultFile(String screenShotName) {
    return new File(resultImagePath(screenShotName))
  }

  private Boolean compareResultToReference(File result, File reference, String screenShotName) {
    BufferedImage resultImage = ImageIO.read(result)

    BufferedImage referenceImage

    try {
      referenceImage = ImageIO.read(reference)
    } catch (javax.imageio.IIOException e) {
      fail("Reference Image does not exist, rerun with regenerate switch")
    }

    if (ImageUtils.compareImages(resultImage, referenceImage)) {
      return true
    } else {
      RenderedImage diff = ImageUtils.generateComparisonImage(resultImage, referenceImage)
      ImageIO.write(diff, "jpeg", new File(rootResultReportDir(screenShotName) + "diff.jpg"))
      return false
    }
  }

  private String referenceImagePath(String screenShotName) {
    return rootReferenceReportDir(screenShotName) + "reference.png"
  }

  private String resultImagePath(String screenShotName) {
    return rootResultReportDir(screenShotName) + "result.png"
  }

  private String diffImagePath(String screenShotName) {
    return rootResultReportDir(screenShotName) + "diff.jpg"
  }

  private String rootReferenceReportDir(screenShotName) {
    return BuildSettingsHolder.getSettings().config.uiregression.reference.path + "/${screenShotName}/"
  }

  private String rootResultReportDir(screenShotName) {
    return BuildSettingsHolder.getSettings().config.uiregression.result.path + "/${screenShotName}/"
  }

}