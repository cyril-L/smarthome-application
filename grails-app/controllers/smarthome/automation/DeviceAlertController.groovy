package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;


@Secured("isAuthenticated()")
class DeviceAlertController extends AbstractController {

    private static final String COMMAND_NAME = 'deviceAlert'
	
	DeviceAlertService deviceAlertService
	EventService eventService
	
	
	
	/**
	 * Recherche multi-critère des alertes
	 * 
	 * @param command
	 * @return
	 */
	def deviceAlerts(DeviceAlertCommand command) {
		command.userId = principal.id
		def alerts = deviceAlertService.search(command, this.getPagination([:]))
		def recordsTotal = alerts.totalCount
		
		// alerts est accessible depuis le model avec la variable deviceAlert[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond alerts, model: [recordsTotal: recordsTotal, command: command]
    }
	
	
	/**
	 * Ajout d'une config alerte
	 * 
	 * @param device
	 * @return
	 */
	def addLevelAlert(Device device) {
		deviceAlertService.addLevelAlert(device)
		render(template: 'deviceLevelAlertTable', model: [levelAlerts: device.levelAlerts,
			deviceEvents: eventService.listByUser(null, principal.id, [:])])
	}
	
	
	/**
	 * Suppression config alerte
	 * 
	 * @param device
	 * @param status
	 * @return
	 */
	def deleteLevelAlert(Device device, int status) {
		deviceAlertService.deleteLevelAlert(device, status)
		render(template: 'deviceLevelAlertTable', model: [levelAlerts: device.levelAlerts,
			deviceEvents: eventService.listByUser(null, principal.id, [:])])
	}
	
	
	/**
	 * Gestion de la marque "viewed"
	 * 
	 * @param alert
	 * @return
	 */
	def markViewed(DeviceAlert alert) {
		deviceAlertService.markViewed(alert)
		render(template: 'deviceAlertStatusLozenge', model: [alert: alert])
	}
	
	
	/**
	 * Suppression d'une alerte
	 * 
	 * @param alert
	 * @return
	 */
	def delete(DeviceAlert alert) {
		deviceAlertService.delete(alert)
		nop()
	}
}
