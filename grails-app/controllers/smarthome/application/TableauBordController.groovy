package smarthome.application

import smarthome.automation.HouseService;
import smarthome.core.AbstractController
import smarthome.core.Widget;
import smarthome.core.WidgetService
import smarthome.core.WidgetUser
import smarthome.datachallenge.DataChallengeService;
import smarthome.security.User;
import smarthome.security.UserFriend;
import smarthome.security.UserFriendService;
import grails.plugin.springsecurity.annotation.Secured


/**
 * Tableau de bord central
 * 
 * @author gregory
 *
 */
@Secured("isAuthenticated()")
class TableauBordController extends AbstractController {

	UserFriendService userFriendService
	WidgetService widgetService
	HouseService houseService

	DataChallengeService dataChallengeService

	/**
	 * 
	 * @return
	 */
	def index() {
		/*def user = authenticatedUser
		// FIXME(cyril) Ugly way to set force some widgets
		def defaultWidgets = [
				[name: "Ma conso", col: 0, row: 0],
				[name: "Facebook group", col: 1, row: 0],
				[name: "Enedis Data Connect", col: 0, row: 1],
		]
		defaultWidgets.each {
			Widget widget = Widget.findByLibelle(it.name)
			if (!widget) {
				throw new RuntimeException("No default widget named $it.name")
			}
			WidgetUser widgetUser = widgetService.addWidgetUser(widget, user.id)
			widgetService.moveWidgetUser(widgetUser, it.row, it.col)
		}
		def widgetUsers = widgetService.findAllByUserId(principal.id)*/
		def user = authenticatedUser
		def widgetUsers = dataChallengeService.getWidgets(user)
		render(view: 'tableauBord', model: [
				user: user,
				widgetUsers: widgetUsers,
				defaultWidgets: dataChallengeService.DEFAULT_WIDGETS,
				secUser: user])
	}
	
	
	/**
	 * Tableau de bord publique d'un ami
	 * 
	 * @param friend
	 * @return
	 */
	def tableauBordFriend(User friend) {
		def user = authenticatedUser
		userFriendService.assertFriend(user, friend)
		
		//  on s'assure que tous les nouveax objets sont bien associés
		def house = houseService.findDefaultByUser(friend)
		houseService.shareHouse(house, user)
		
		render(view: 'tableauBordFriend', model: [user: friend, house: house, viewOnly: true,
			secUser: user])
	}
}
