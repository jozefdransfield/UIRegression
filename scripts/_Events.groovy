def seleniumManager

eventAllTestsStart = {
  if (getBinding().variables.containsKey("functionalTests")) {
    functionalTests << "ui-regression-test"
  }
}

eventTestSuiteStart = {String type ->
	if (type == "ui-regression-test") {
		def managerClass = Thread.currentThread().contextClassLoader.loadClass("grails.plugins.selenium.SeleniumManager")
		seleniumManager = managerClass.instance
		seleniumManager.loadConfig()

		event("StatusUpdate", ["starting selenium server"])
		seleniumManager.startServer("${seleniumRcPluginDir}/lib/server/selenium-server.jar")
	}
}

eventTestSuiteEnd = {String type, testSuite ->
	if (type == "ui-regression-test") {
		event("StatusUpdate", ["stopping selenium server"])
		seleniumManager.stopServer()
	}
}
