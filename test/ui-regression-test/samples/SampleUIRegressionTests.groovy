package samples

import net.jozefdransfield.uiregression.UIRegressionTestCase

class SampleUIRegressionTests extends UIRegressionTestCase {

  public void setUp() throws Exception {
    super.setUp("https://alpha.happy-path.com", "*firefox");
  }
  
  public void testSample() throws Exception {

    navigateToAssertScreenShot("testSample") {
       selenium.open("/")  
    }

    

  }


}