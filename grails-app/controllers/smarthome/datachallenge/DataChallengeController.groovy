package smarthome.datachallenge

import grails.plugin.springsecurity.annotation.Secured
import smarthome.automation.Device

@Secured("isAuthenticated()")
class DataChallengeController {

    DataChallengeService dataChallengeService

    def facebookGroupWidget() {
    }

    def personalData() {
        def user = authenticatedUser
        Device linky = dataChallengeService.getLinky(user)
        [linky: linky]
    }

    def messageWidget() {
        def user = authenticatedUser
        def name
        if (!dataChallengeService.hasData(user)) {
            name = 'no-data'
        }
        Device linky = dataChallengeService.getLinky(user)
        [name: name, linky: linky]
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