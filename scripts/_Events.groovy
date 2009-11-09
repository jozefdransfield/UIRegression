import net.jozefdransfield.uiregression.UIRegressionTestRunner

def webtestAnt = new AntBuilder()

eventAllTestsStart = {
  if (getBinding().variables.containsKey("functionalTests")) {
    functionalTests << "ui-regression-test"
  }
}

eventTestPhaseStart = {phase ->
  if (phase == 'functional') {
    event("StatusUpdate", ['Starting Selenium RC'])
    Ant.java(jar:'selenium/selenium-server-1.0.1/selenium-server.jar', fork:true, spawn: true)

    //testRunner = new UIRegressionTestRunner(testReportsDir, reportFormats)
  }
}

//eventTestSuiteStart = {type ->
//  if (type == "ui-regression-test") {
//    println "----------------------------------------------- Smello fellow"
//  }
//}
//
//eventTestSuiteEnd = {type, suite ->
//    if (type == "ui-regression-test") {
//        println "---------------------------------------------- ${suite.class}"
//    }
//}

eventTestPhaseEnd = {phase ->
  if (phase == 'functional') {
    event("StatusUpdate", ['Stopping Selenium RC'])
    URL url = new URL("http://localhost:4444/selenium-server/driver/?cmd=shutDownSeleniumServer")
    def result = url.openStream().text
    if (result != "OKOK") {
      event("StatusError", ['DANGER DANGER WILL ROBINSON, SELENIUM RC FAILED TO STOP']) 
    }
  }
}