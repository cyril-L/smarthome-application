package smarthome.datachallenge

import grails.plugin.springsecurity.annotation.Secured
import smarthome.automation.Device
import smarthome.automation.NotificationAccount
import smarthome.automation.deviceType.Linky

@Secured("isAuthenticated()")
class DataChallengeController {

    DataChallengeService dataChallengeService

    def facebookGroupWidget() {
    }

    def messageWidget() {
        def user = authenticatedUser
        Linky linky = dataChallengeService.getLinky(user)
        [linky: linky]
    }

    def personalData() {
        def user = authenticatedUser
        Linky linky = dataChallengeService.getLinky(user)
        [linky: linky]
    }

    def dashboard() {
        def user = authenticatedUser
        Linky linky = dataChallengeService.getLinky(user)
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