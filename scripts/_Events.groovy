def seleniumManager

eventAllTestsStart = {
  if (getBinding().variables.containsKey("functionalTests")) {
    functionalTests << "ui-regression-test"
  }
}

eventTestPhaseStart = {phase ->
  if (phase == 'functional') {
    event("StatusUpdate", ['Starting Selenium RC'])
    def managerClass = Thread.currentThread().contextClassLoader.loadClass("grails.plugins.selenium.SeleniumManager")
    seleniumManager = managerClass.instance
    seleniumManager.loadConfig()

    seleniumManager.startServer("${seleniumRcPluginDir}/lib/server/selenium-server.jar")
  }
}

eventTestPhaseEnd = {phase ->
  if (phase == 'functional') {
    event("StatusUpdate", ['Stopping Selenium RC'])
    seleniumManager.stopServer()
  }
}