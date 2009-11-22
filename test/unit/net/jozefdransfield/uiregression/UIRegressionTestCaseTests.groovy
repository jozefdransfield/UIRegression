package net.jozefdransfield.uiregression

import org.gmock.GMockTestCase
import com.thoughtworks.selenium.Selenium
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

public class UIRegressionTestCaseTests extends GMockTestCase {

  UIRegressionTestCase uiRegressionTestCase
  Selenium selenium

  void setUp() {
    uiRegressionTestCase = new UIRegressionTestCase()
    selenium = mock(Selenium)
    uiRegressionTestCase.selenium = selenium
  }

  void testNavigateToAndAssertScreenShotFailsIfScreenShotsAreNotEqual() {
    def partialUIRegressionTestCase = mock(uiRegressionTestCase)
    def mockClosure = mock(Closure)
    ordered {
      partialUIRegressionTestCase.initialiseReportDirectory("screen_id")
      mockClosure.call()
      selenium.captureEntirePageScreenshot("/Users/jozefdransfield/Desktop/screen_id/result.png", "")
      partialUIRegressionTestCase.loadScreenShotsAndCompare("screen_id").returns(false)
    }
    play {
      shouldFail(IllegalArgumentException) {
        uiRegressionTestCase.navigateToAssertScreenShot("screen_id", mockClosure)
      }
    }
  }

  void testNavigateToAndAssertScreenShotPassesIfScreenShotsAreEqual() {
    def partialUIRegressionTestCase = mock(uiRegressionTestCase)
    def mockClosure = mock(Closure)
    ordered {
      partialUIRegressionTestCase.initialiseReportDirectory("screen_id")
      mockClosure.call()
      selenium.captureEntirePageScreenshot("/Users/jozefdransfield/Desktop/screen_id/result.png", "")
      partialUIRegressionTestCase.loadScreenShotsAndCompare("screen_id").returns(true)
    }
    play {
      uiRegressionTestCase.navigateToAssertScreenShot("screen_id", mockClosure)
    }
  }

  void testLoadScreenShotsAndCompareReturnsTrueIfImagesAreEqual() {
    def partialUIRegressionTestCase = mock(uiRegressionTestCase)
    def mockResultFile = mock(File)
    def mockReferenceFile = mock(File)
    ordered {
      partialUIRegressionTestCase.loadResultFile("screen_id").returns(mockResultFile)
      partialUIRegressionTestCase.loadReferenceFile("screen_id").returns(mockReferenceFile)
      partialUIRegressionTestCase.compareResultToReference(mockResultFile, mockReferenceFile).returns(true)
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
      partialUIRegressionTestCase.compareResultToReference(mockResultFile, mockReferenceFile).returns(false)
    }
    play {
      assertFalse uiRegressionTestCase.loadScreenShotsAndCompare("screen_id")
    }
  }

  void testInitialiseReportDirectoryCreatesDirectoryWhenDoesNotExist() {
    ordered {
      def mockReportsDirectory = mock(File, constructor("/Users/jozefdransfield/Desktop/filename"))
      mockReportsDirectory.exists().returns(false)
      mockReportsDirectory.mkdir()
    }
    play {
      uiRegressionTestCase.initialiseReportDirectory("filename")
    }
  }

  void testInitialiseReportDirectoryDoesNothingIfExists() {
    ordered {
      def mockReportsDirectory = mock(File, constructor("/Users/jozefdransfield/Desktop/filename"))
      mockReportsDirectory.exists().returns(true)
    }
    play {
      uiRegressionTestCase.initialiseReportDirectory("filename")
    }
  }

  void testloadReferenceFileReturnsReferenceFile() {
    def mockFile
    ordered {
      mockFile = mock(File, constructor("/Users/jozefdransfield/Desktop/screen_id/reference.png"))
    }
    play {
      assertEquals mockFile, uiRegressionTestCase.loadReferenceFile("screen_id")
    }
  }

  void testLoadResultFileReturnsResultFile() {
    def mockFile
    ordered {
      mockFile = mock(File, constructor("/Users/jozefdransfield/Desktop/screen_id/result.png"))
    }
    play {
      assertEquals mockFile, uiRegressionTestCase.loadResultFile("screen_id")
    }
  }

  void testCompareResultToReferenceReturnsFalseIfImagesNotEqual() {
    File result
    File reference
    BufferedImage mockResultImage = mock(BufferedImage)
    BufferedImage mockReferenceImage = mock(BufferedImage)
    ImageIO mockImageIO = mock(ImageIO)
    ImageUtils mockImageUtils = mock(ImageUtils)
    ordered {
      mockImageIO.static.read(result).returns(mockResultImage)
      mockImageIO.static.read(reference).returns(mockReferenceImage)
      mockImageUtils.static.compareImages(mockResultImage, mockReferenceImage).returns(false)
    }
    play {
      assertFalse uiRegressionTestCase.compareResultToReference(result, reference)
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
      assertTrue uiRegressionTestCase.compareResultToReference(result, reference)
    }
  }
}