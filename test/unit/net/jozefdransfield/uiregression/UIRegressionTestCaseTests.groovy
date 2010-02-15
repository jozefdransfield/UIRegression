package net.jozefdransfield.uiregression

import org.gmock.GMockTestCase
import com.thoughtworks.selenium.Selenium
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import grails.util.BuildSettings
import grails.util.BuildSettingsHolder
import com.thoughtworks.selenium.DefaultSelenium
import java.awt.image.RenderedImage
import org.apache.commons.codec.binary.Base64
import javax.imageio.IIOException
import org.codehaus.groovy.grails.commons.ConfigurationHolder

public class UIRegressionTestCaseTests extends GMockTestCase {

  UIRegressionTestCase uiRegressionTestCase
  Selenium selenium

  void setUp() {
    ConfigurationHolder.config = new ConfigObject()
    ConfigurationHolder.config.uiregression.reference.path = '/path/to/reference'
    ConfigurationHolder.config.uiregression.result.path = '/path/to/result'
    ConfigurationHolder.config.uiregression.selenium.host = 'localhost'
    ConfigurationHolder.config.uiregression.selenium.port = 4444

    uiRegressionTestCase = new UIRegressionTestCase()
    selenium = mock(Selenium)
    uiRegressionTestCase.selenium = selenium
  }

  void testSetUpInitialisesSelenium() {
    ordered {
      def mockSelenium = mock(DefaultSelenium, constructor("localhost", 4444, "*firefox", "http://localhost:8080"))
      mockSelenium.start()
    }
    play {
      uiRegressionTestCase.setUp()  
    }
  }

  void testNavigateToAndAssertScreenShotFailsIfScreenShotsAreNotEqual() {
    def partialUIRegressionTestCase = mock(uiRegressionTestCase)
    def mockClosure = mock(Closure)
    def mockImage = mock(BufferedImage)

    ordered {
      partialUIRegressionTestCase.initialiseReportDirectory("screen_id")
      mockClosure.call()
      selenium.captureEntirePageScreenshotToString("").returns("picture")

      mock(ImageIO).static.read(match { return true } ).returns(mockImage)
      def mockImageFile = mock(File, constructor("/path/to/result/screen_id/result.png"))
      mock(ImageIO).static.write(mockImage, "png", mockImageFile)

      partialUIRegressionTestCase.loadScreenShotsAndCompare("screen_id").returns(false)

      def mockResultFile = mock(File, constructor("/path/to/result/screen_id/result.png"))
      def mockReferenceFile = mock(File, constructor("/path/to/reference/screen_id/reference.png"))
      def mockDiffFile = mock(File, constructor("/path/to/result/screen_id/diff.jpg"))

      mockResultFile.absolutePath.returns("/path/to/result/screen_id/result.png")
      mockReferenceFile.absolutePath.returns("/path/to/reference/screen_id/reference.png")
      mockDiffFile.absolutePath.returns("/path/to/result/screen_id/diff.jpg")

      partialUIRegressionTestCase.static.fail("Result Image at [result:/path/to/result/screen_id/result.png]\n did not match [reference:/path/to/reference/screen_id/reference.png]\n for ID: [screen_id]\n [diff:/path/to/result/screen_id/diff.jpg]")

    }
    play {
        uiRegressionTestCase.navigateToAndAssertScreenShot("screen_id", mockClosure)
      
    }
  }

  void testNavigateToAndAssertScreenShotPassesIfScreenShotsAreEqual() {
    def partialUIRegressionTestCase = mock(uiRegressionTestCase)
    def mockClosure = mock(Closure)
    def mockImage = mock(BufferedImage)
      
    ordered {
      partialUIRegressionTestCase.initialiseReportDirectory("screen_id")
      mockClosure.call()
      selenium.captureEntirePageScreenshotToString("").returns("picture")
      mock(ImageIO).static.read(match { return true } ).returns(mockImage)
      def mockImageFile = mock(File, constructor("/path/to/result/screen_id/result.png"))
      mock(ImageIO).static.write(mockImage, "png", mockImageFile)
      partialUIRegressionTestCase.loadScreenShotsAndCompare("screen_id").returns(true)
    }
    play {
      uiRegressionTestCase.navigateToAndAssertScreenShot("screen_id", mockClosure)
    }
  }

  void testLoadScreenShotsAndCompareReturnsTrueIfImagesAreEqual() {
    def partialUIRegressionTestCase = mock(uiRegressionTestCase)
    def mockResultFile = mock(File)
    def mockReferenceFile = mock(File)
    ordered {
      partialUIRegressionTestCase.loadResultFile("screen_id").returns(mockResultFile)
      partialUIRegressionTestCase.loadReferenceFile("screen_id").returns(mockReferenceFile)
      partialUIRegressionTestCase.compareResultToReference(mockResultFile, mockReferenceFile, "screen_id").returns(true)
    }
    play {
      assertTrue uiRegressionTestCase.loadScreenShotsAndCompare("screen_id")
    }
  }

  void testLoadScreenShotsAndCompareReturnsFalseIfImagesAreNotEqual() {
    def partialUIRegressionTestCase = mock(uiRegressionTestCase)
    def mockResultFile = mock(File)
    def mockReferenceFile = mock(File)
    ordered {
      partialUIRegressionTestCase.loadResultFile("screen_id").returns(mockResultFile)
      partialUIRegressionTestCase.loadReferenceFile("screen_id").returns(mockReferenceFile)
      partialUIRegressionTestCase.compareResultToReference(mockResultFile, mockReferenceFile, "screen_id").returns(false)
    }
    play {
      assertFalse uiRegressionTestCase.loadScreenShotsAndCompare("screen_id")
    }
  }

  void testLoadScreenShotsAndCompareReplacesReferenceAndReturnsTrueWhenRegenerateProperty() {
    System.setProperty("uiregression.regenerate", "true")
    def partialUIRegressionTestCase = mock(uiRegressionTestCase)
    def mockResultFile = mock(File)
    def mockReferenceFile = mock(File)
    ordered {
      partialUIRegressionTestCase.loadResultFile("screen_id").returns(mockResultFile)
      partialUIRegressionTestCase.loadReferenceFile("screen_id").returns(mockReferenceFile)
      mock(FileUtils).static.copyFile(mockResultFile, mockReferenceFile)
    }
    play {
      assertTrue uiRegressionTestCase.loadScreenShotsAndCompare("screen_id")
    }
  }

  void testInitialiseReportDirectoryCreatesDirectoryWhenDoesNotExist() {
    ordered {
      def mockReferenceDirectory = mock(File, constructor("/path/to/reference/filename/"))
      mockReferenceDirectory.exists().returns(false)
      mockReferenceDirectory.mkdirs()
      
      def mockResultDirectory = mock(File, constructor("/path/to/result/filename/"))
      mockResultDirectory.exists().returns(false)
      mockResultDirectory.mkdirs()
    }
    play {
      uiRegressionTestCase.initialiseReportDirectory("filename")
    }
  }

  void testInitialiseReportDirectoryDoesNothingIfExists() {
    ordered {
      def mockReferenceDirectory = mock(File, constructor("/path/to/reference/filename/"))
      mockReferenceDirectory.exists().returns(true)

      def mockResultDirectory = mock(File, constructor("/path/to/result/filename/"))
      mockResultDirectory.exists().returns(true)
    }
    play {
      uiRegressionTestCase.initialiseReportDirectory("filename")
    }
  }

  void testloadReferenceFileReturnsReferenceFile() {
    def mockFile
    ordered {
      mockFile = mock(File, constructor("/path/to/reference/screen_id/reference.png"))
    }
    play {
      assertEquals mockFile, uiRegressionTestCase.loadReferenceFile("screen_id")
    }
  }

  void testLoadResultFileReturnsResultFile() {
    def mockFile
    ordered {
      mockFile = mock(File, constructor("/path/to/result/screen_id/result.png"))
    }
    play {
      assertEquals mockFile, uiRegressionTestCase.loadResultFile("screen_id")
    }
  }

  void testCompareResultToReferenceFailsIfReferenceImageNotPresent() {
    File result
    File reference

    def mockResultImage = mock(BufferedImage)

    ordered {
      mock(ImageIO).static.read(result).returns(mockResultImage)
      mock(ImageIO).static.read(reference).raises(new IIOException("I broke"))
      mock(uiRegressionTestCase).static.fail("Reference Image does not exist, rerun with regenerate switch")
    }

    play {
       assertFalse uiRegressionTestCase.compareResultToReference(result, reference, "screen_id")
    }
  }

  void testCompareResultToReferenceReturnsFalseAndGeneratesComparisonImageIfImagesNotEqual() {
    File result
    File reference
    BufferedImage mockResultImage = mock(BufferedImage)
    BufferedImage mockReferenceImage = mock(BufferedImage)
    RenderedImage mockComparisonImage = mock(RenderedImage)
    ImageIO mockImageIO = mock(ImageIO)
    ImageUtils mockImageUtils = mock(ImageUtils)
    ordered {
      mockImageIO.static.read(result).returns(mockResultImage)
      mockImageIO.static.read(reference).returns(mockReferenceImage)
      mockImageUtils.static.compareImages(mockResultImage, mockReferenceImage).returns(false)
      mockImageUtils.static.generateComparisonImage(mockResultImage, mockReferenceImage).returns(mockComparisonImage)
      def mockDiffFile = mock(File, constructor("/path/to/result/screen_id/diff.jpg"))
      mockImageIO.static.write(mockComparisonImage, "jpeg", mockDiffFile)
    }
    play {
      assertFalse uiRegressionTestCase.compareResultToReference(result, reference, "screen_id")
    }
  }

  void testCompareResultToReferenceReturnsTrueIfImagesEqual() {
    File result
    File reference
    BufferedImage mockResultImage = mock(BufferedImage)
    BufferedImage mockReferenceImage = mock(BufferedImage)
    ImageIO mockImageIO = mock(ImageIO)
    ImageUtils mockImageUtils = mock(ImageUtils)
    ordered {
      mockImageIO.static.read(result).returns(mockResultImage)
      mockImageIO.static.read(reference).returns(mockReferenceImage)
      mockImageUtils.static.compareImages(mockResultImage, mockReferenceImage).returns(true)
    }
    play {
      assertTrue uiRegressionTestCase.compareResultToReference(result, reference, "screen_id")
    }
  }

  void testTearDownStopsSelenium() {
    def mockSelenium = mock(DefaultSelenium)

    uiRegressionTestCase.selenium = mockSelenium

    ordered {
      mockSelenium.close()      
      mockSelenium.stop()
    }
    play {
      uiRegressionTestCase.tearDown()    
    }
  }
}
