package samples

import net.jozefdransfield.uiregression.UIRegressionTestCase

class SampleUIRegressionTests extends UIRegressionTestCase {

  public void setUp() throws Exception {
    //super.setUp("https://skyid.sky.com", "*firefox");
  }

  protected void tearDown() {
    super.tearDown()
  }

  public void testUntitled() throws Exception {
    navigateTo {
//      selenium.open("/signin");

      println("DOUBLE AWESOME")
    }

  }


}