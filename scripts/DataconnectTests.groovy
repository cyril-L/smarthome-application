//includeTargets << grailsScript("_GrailsInit")
//includeTargets << grailsScript("_GrailsBootstrap")
//includeTargets << grailsScript("_GrailsClasspath")
//
//target(main: "An example script that calls a service") {
//	depends(bootstrap)
//
//	def dataConnectService = appCtx.getBean("dataConnectServiceNotExisting") // look up the service
//	//someService.runReportOrSomething() // invoke a method on the service
//}
//
//setDefaultTarget(main)


import grails.util.Environment
import org.springframework.beans.factory.annotation.Autowired
import smarthome.api.DataConnectService
import smarthome.automation.datasource.DataConnectDataSourceProvider

import static grails.util.Metadata.current as metaInfo
 
header 'Application Status'
row 'App version', metaInfo['app.version']
row 'Grails version', metaInfo['app.grails.version']
row 'Groovy version', GroovySystem.version
row 'JVM version', System.getProperty('java.version')
row 'Reloading active', Environment.reloadingAgentEnabled
row 'Controllers', grailsApplication.controllerClasses.size()
row 'Domains', grailsApplication.domainClasses.size()
row 'Services', grailsApplication.serviceClasses.size()
row 'Tag Libraries', grailsApplication.tagLibClasses.size()
println()
 
header 'Installed Plugins'
ctx.getBean('pluginManager').allPlugins.each { plugin ->
	row plugin.name, plugin.version
}
 
void row(final String label, final value) {
	println label.padRight(18) + ' : ' + value.toString().padLeft(8)
}
 
void header(final String title) {
	final int length = 29
	println '-' * length
	println title.center(length)
	println '-' * length
}

import smarthome.api.DataConnectApi
import smarthome.security.User
import smarthome.automation.NotificationAccount
import smarthome.automation.NotificationAccountSender
import org.codehaus.groovy.grails.web.json.JSONElement
import smarthome.core.SmartHomeException

def dataConnectService = ctx.dataConnectService

User user = User.findById(415);

NotificationAccountSender accountSender = NotificationAccountSender.findByLibelle(grailsApplication.config.enedis.appName)

NotificationAccount notificationAccount = NotificationAccount.createCriteria().get {
	eq 'user', user
	eq 'notificationAccountSender', accountSender
}

notificationAccount.configToJson()

dataConnectService.refresh_token(notificationAccount)

List<JSONElement> out = dataConnectService.consumptionMaxPower(notificationAccount)

print(out)

//if (!notificationAccount.jsonConfig.access_token) {
//	throw new SmartHomeException("access_token is required !")
//}
//
//if (!notificationAccount.jsonConfig.usage_point_id) {
//	throw new SmartHomeException("usage_point_id is required !")
//}
//
////def bookClass = grailsApplication.classLoader.loadClass("DataConnectApi")
////DataConnectApi dataConnectApi = bookClass.newInstance()
//
//DataConnectApi dataConnectApi = new DataConnectApi()
//dataConnectApi.grailsApplication = grailsApplication
//
//Date start = new Date(120, 7, 15)
//Date end = new Date(120, 7, 20)
//
//println(start)
//
//List<JSONElement> datapoints = dataConnectApi.consumption_load_curve(
//	start, end,
//	notificationAccount.jsonConfig.usage_point_id,
//	notificationAccount.jsonConfig.access_token)
//
////List<JSONElement> datapoints = dataConnectApi.daily_consumption(
////		start, end,
////		notificationAccount.jsonConfig.usage_point_id,
////		notificationAccount.jsonConfig.access_token)
//
////List<JSONElement> datapoints = dataConnectApi.daily_consumption(
////		start, end,
////		notificationAccount.jsonConfig.usage_point_id,
////		notificationAccount.jsonConfig.access_token)
//
//println(datapoints)

// DataConnectApi.daily_consumption(Date start, Date end, String usagePointId, String token)