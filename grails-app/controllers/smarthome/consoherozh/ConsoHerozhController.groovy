package smarthome.consoherozh

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import smarthome.automation.Device
import smarthome.automation.DeviceValueDay
import smarthome.datachallenge.DataChallengeService

import java.text.SimpleDateFormat

@Secured("isAuthenticated()")
class ConsoHerozhController {

    DataChallengeService dataChallengeService

    def dashboard() {
        def user = authenticatedUser
        Device linky = dataChallengeService.getLinky(user)
//        if (!linky) {
//            return redirect(controller: 'dataChallenge', action: 'personalData')
//        }
        def linkyDays
        if (linky) {
            linkyDays = DeviceValueDay.createCriteria().list {
                eq 'device', linky
                // between 'dateValue', dateDebut, dateFin
                eq "name", "basesum"
                order 'dateValue'
            }
            def isoDateFormat = new SimpleDateFormat("yyyy-MM-dd")
            linkyDays = linkyDays.collect { [
                date: isoDateFormat.format(it.dateValue),
                value: it.value
            ] }
        } else {
            linkyDays = []
        }

        render(view: 'dashboard', model: [
                linkyDays: linkyDays as JSON,
                linky: linky
        ])
    }
}
