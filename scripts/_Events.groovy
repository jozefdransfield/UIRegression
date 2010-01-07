import grails.util.BuildSettingsHolder

includeTargets << new File("$seleniumRcPluginDir/scripts/_Selenium.groovy")

eventAllTestsStart = {
  loadSeleniumConfig()
  if (binding.variables.containsKey("functionalTests")) {
    functionalTests << "ui-regression"
  }
}
eventTestSuiteStart = {String type ->
  if (type =~ /ui-regression/ && BuildSettingsHolder.getSettings().config.uiregression.localSelenium == true) {
    startSeleniumServer()
  }
}

eventTestSuiteEnd = {String type ->
  if (type =~ /ui-regression/ && BuildSettingsHolder.getSettings().config.uiregression.localSelenium == true) {
    stopSeleniumServer()
  }
}
