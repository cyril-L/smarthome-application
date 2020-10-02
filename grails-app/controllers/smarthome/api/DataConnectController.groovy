package smarthome.api

import org.springframework.security.access.annotation.Secured

import grails.converters.JSON
import smarthome.automation.NotificationAccount
import smarthome.automation.NotificationAccountSender
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.security.User

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Secured("isAuthenticated()")
class DataConnectController extends AbstractController {

	DataConnectService dataConnectService


	/**
	 * URL de redirection après validation du consentement pour récupérer les
	 * params code et usage_point_id
	 * 
	 * @return
	 */
	@Secured("permitAll()")
	@ExceptionNavigationHandler(controllerName = "notificationAccount", actionName = "notificationAccounts")
	def redirect(String code, String usage_point_id, String state) {
		User user = authenticatedUser // spring security plugin
		dataConnectService.authorization_code(user, code, usage_point_id)

		log.info "Redirect Consentement from ${state}"
		String info = "Votre consentement sur le service Enedis DataConnect est validé avec succès !"


		if (state == "grand-defi") {
			log.info "Redirect DataConnect Autorisation to BMHS GrandDefi"
			// la demande de consentement vient de l'application spéciale "Grand Défi"
			// et la redirection doit être renvoyée vers cette application
			redirect(url: 'https://www.jdevops.com/granddefi/compteur/compteur', params: [info: info])
		} else {
			setInfo(info)
			redirect(controller: 'dataChallenge', action: 'dashboard')
		}
	}


	/**
	 * Consentement DataConnect
	 *
	 * @return
	 */
	def dataconnect() {
		render(view: '/compteur/dataconnect')
	}


	/**
	 * Demande consentement depuis service
	 * 
	 * 
	 * @return
	 */
	def authorize() {
		redirect(url: dataConnectService.authorize_uri())
	}


	/**
	 *
	 * @return
	 */
	@ExceptionNavigationHandler(controllerName = "notificationAccount", actionName = "notificationAccounts")
	def refresh_token() {
		User user = authenticatedUser // spring security plugin
		def tokens = dataConnectService.refresh_token(user)
		render(contentType: "application/json") {
			tokens
		}
	}


	/**
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(controllerName = "notificationAccount", actionName = "notificationAccounts")
	def consumption_load_curve() {
		User user = authenticatedUser // spring security plugin
		def datapoints = dataConnectService.consumptionLoadCurve(user)
		render(contentType: "application/json") {
			datapoints
		}
	}


	/**
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(controllerName = "notificationAccount", actionName = "notificationAccounts")
	def daily_consumption() {
		User user = authenticatedUser // spring security plugin
		def datapoints = dataConnectService.dailyConsumption(user)
		render(contentType: "application/json") {
			datapoints
		}
	}


	/**
	 *
	 * @return
	 */
	@ExceptionNavigationHandler(controllerName = "notificationAccount", actionName = "notificationAccounts")
	def consumption_max_power() {
		User user = authenticatedUser // spring security plugin
		def datapoints = dataConnectService.consumptionMaxPower(user)
		render(contentType: "application/json") {
			datapoints
		}
	}

	def widget() {
		def user = authenticatedUser
		NotificationAccountSender accountSender = NotificationAccountSender.findByLibelle(grailsApplication.config.enedis.appName)
		NotificationAccount notificationAccount = NotificationAccount.createCriteria().get {
			eq 'user', user
			eq 'notificationAccountSender', accountSender
		}
		if (notificationAccount) {
			notificationAccount.configToJson()
		}
		[notificationAccount: notificationAccount]
//		def house = houseService.findDefaultByUser(user)
//		render(template: '/deviceType/teleInformation/widget', model: [house: house])
	}
}
