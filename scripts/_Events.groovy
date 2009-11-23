eventAllTestsStart = {
  if (getBinding().variables.containsKey("functionalTests")) {
    functionalTests << "ui-regression-test"
  }
}

eventTestPhaseStart = { phase ->
  if (phase == 'functional') {
    event("StatusUpdate", ['Starting Selenium RC'])
    Ant.java(jar:'selenium/selenium-server-1.0.2-SNAPSHOT-standalone.jar', fork:true, spawn: true)
  }
}

eventTestPhaseEnd = { phase ->
  if (phase == 'functional') {
    event("StatusUpdate", ['Stopping Selenium RC'])
    URL url = new URL("http://localhost:4444/selenium-server/driver/?cmd=shutDownSeleniumServer")
    def result = url.openStream().text
    if (result != "OKOK") {
      event("StatusError", ['DANGER DANGER WILL ROBINSON, SELENIUM RC FAILED TO STOP']) 
    }
  }
}