package net.jozefdransfield.uiregression

import com.thoughtworks.selenium.SeleneseTestCase

public class UIRegressionTestCase extends SeleneseTestCase {

  public void navigateTo(Closure closure) {

    closure.call()

    //take screen shot and compare with recorded screenshot
    //selenium.captureEntirePageScreenshot("/Users/jozefdransfield/Desktop/image.png", "")
  }

}