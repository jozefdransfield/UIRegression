package samples

import grails.test.GrailsUnitTestCase
import net.jozefdransfield.uiregression.UIRegressionTestCase

class SampleUIRegressionTests extends UIRegressionTestCase {

  public void setUp() throws Exception {
    super.setUp("https://skyid.sky.com", "*firefox");
  }

  protected void tearDown() {
    super.tearDown()
  }

  public void testUntitled() throws Exception {
    selenium.open("/signin");
//
//    selenium.captureEntirePageScreenshot("/Users/jozefdransfield/Desktop/image.png", "");
//
//    File file = new File("/Users/jozefdransfield/Desktop/image.png");
//
//    BufferedImage reference = ImageUtils.loadReferenceImage();
//
//    BufferedImage result = ImageIO.read(file);
//
//    if (ImageUtils.compareImages(reference, result)) {
//      System.out.println("IT WORKED!");
//    } else {
//      System.out.println("Aww bum");
//    }
//
//    RenderedImage image = ImageUtils.createComparisonImage(reference, result);
//
//    ImageIO.write(image, "jpeg", new File("/Users/jozefdransfield/Desktop/result.jpg"));
//
////               Robot robot = new Robot();
////
////               ImageIO.write(robot.createScreenCapture(new Rectangle(0,0, 500, 500)), "jpeg", new File("/Users/jozefdransfield/Desktop/screen.jpg"));

    //selenium.wait(5000);
  }


}