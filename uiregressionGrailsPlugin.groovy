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
Brief description of the plugin.
'''
    def documentation = "http://grails.org/UIRegression+Plugin"
}
