package smarthome.datachallenge

import grails.plugin.springsecurity.annotation.Secured
import smarthome.automation.Device

@Secured("isAuthenticated()")
class DataChallengeController {

    DataChallengeService dataChallengeService

    def facebookGroupWidget() {
    }

    def dashboard() {
        def user = authenticatedUser
        Device linky = dataChallengeService.getLinky(user)
        if (!linky) {
            return redirect(controller: 'dataChallenge', action: 'personalData')
        }
        def widgetUsers = dataChallengeService.getWidgets(user)
        render(view: '/tableauBord/tableauBord', model: [
                user          : user,
                widgetUsers   : widgetUsers,
                defaultWidgets: dataChallengeService.DEFAULT_WIDGETS,
                secUser       : user])

    }
}