class uiregressionGrailsPlugin {
    def version = "0.1"
    def grailsVersion = "1.1.1 > *"
    def dependsOn = ["seleniumRc": "0.1.1 > *"]

    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Jozef Dransfield"
    def authorEmail = "Jozef.Dransfield@me.com"
    def title = "Plugin summary/headline"
    def description = '''\\
UIRegression plugin adds a new type of functional test, which uses selenium to navigate to a page and screenshot it.
This screenshot is compared with a previous reference screenshot, to test for ui regressions.
'''
    def documentation = "http://grails.org/UIRegression+Plugin"
}
