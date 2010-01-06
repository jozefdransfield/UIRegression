import grails.util.BuildSettingsHolder

def seleniumManager

eventAllTestsStart = {
  if (binding.variables.containsKey("functionalTests")) {
    functionalTests << "ui-regression"
  }
}
/*&&  */
eventTestSuiteStart = {String type ->
	if (type == "ui-regression" && BuildSettingsHolder.getSettings().config.uiregression.localSelenium == true) {
		def managerClass = Thread.currentThread().contextClassLoader.loadClass("grails.plugins.selenium.SeleniumManager")
		seleniumManager = managerClass.instance
		seleniumManager.loadConfig()

		event("StatusUpdate", ["starting selenium server"])
		seleniumManager.startServer("${seleniumRcPluginDir}/lib/server/selenium-server.jar")
	}
}

eventTestSuiteEnd = {String type, testSuite ->
  if (type == "ui-regression" && BuildSettingsHolder.getSettings().config.uiregression.localSelenium == true) {
		event("StatusUpdate", ["stopping selenium server"])
		seleniumManager.stopServer()
	}
}
