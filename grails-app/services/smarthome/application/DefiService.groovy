/**
 * 
 */
package smarthome.application

import org.springframework.transaction.annotation.Transactional

import smarthome.automation.ChartTypeEnum
import smarthome.automation.Device
import smarthome.automation.House
import smarthome.automation.HouseService
import smarthome.automation.deviceType.Compteur
import smarthome.core.AbstractService
import smarthome.core.QueryUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol
import smarthome.security.User

/**
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DefiService extends AbstractService {

	HouseService houseService


	/**
	 * Recherche des défis d'un admin (les défis qu'il a créé)
	 * 
	 * @param admin
	 * @param pagination
	 * @return
	 */
	List<Defi> listByAdmin(DefiCommand command, Map pagination) {
		return Defi.createCriteria().list(pagination) {
			eq 'user', command.user

			if (command.search) {
				ilike 'libelle', QueryUtils.decorateMatchAll(command.search)
			}

			order 'referenceDebut', 'desc'
		}
	}


	/**
	 * Les défis auxquels participent à user
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 */
	List<Defi> listByUser(DefiCommand command, Map pagination) {
		return Defi.createCriteria().list(pagination) {
			equipes {
				participants {
					eq 'user', command.user
				}
			}
			order 'referenceDebut', 'desc'
		}
	}


	/**
	 * Inscription d'un user à un défi et une équipe
	 * L'équipe est créé et associée au défi et son nom n'est pas trouvée
	 * Le statut du défi est checké pour savoir si toujours ouvert
	 * 
	 * @param user
	 * @param defiId
	 * @param equipeName
	 * 
	 * @return DefiParticipant
	 * 
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DefiParticipant inscription(User user, Long defiId, String equipeName) throws SmartHomeException {
		// vérifie le statut du défi
		Defi defi = Defi.read(defiId)

		if (!defi) {
			throw new SmartHomeException("Le défi n'existe pas !")
		}

		if (!defi.actif) {
			throw new SmartHomeException("Le ${defi.libelle} n'est plus ouvert aux inscriptions !")
		}

		// recherche d'une équipe associée au défi par son nom
		// si elle n'existe pas, elle est créé à la volée pour faciliter la gestion
		// des équipes
		DefiEquipe defiEquipe = DefiEquipe.findByDefiAndLibelle(defi, equipeName)

		if (!defiEquipe) {
			defiEquipe = new DefiEquipe(defi: defi, libelle: equipeName)
			super.save(defiEquipe)
		}

		// Association du user avec l'équipe du défi
		return super.save(new DefiParticipant(user: user, defiEquipe: defiEquipe))
	}


	/**
	 * Graphe comparatif total des périodes pour un user en fonction d'un compteur
	 * 
	 * @param defi
	 * @param user
	 * @param consos données issues de la méthode #loadUserConso
	 * 
	 * @return GoogleChart
	 */
	GoogleChart chartUserTotalPeriode(Defi defi, User user, Map consos) {
		GoogleChart chart = new GoogleChart()

		chart.chartType = ChartTypeEnum.Column.factory

		chart.values = [
			[name: "Total", reference: consos.totalReference, action: consos.totalAction]
		]

		chart.colonnes << new GoogleDataTableCol(label: "Total", property: "name", type: "string")
		chart.colonnes << new GoogleDataTableCol(label: "Référence", property: "reference", type: "number")
		chart.colonnes << new GoogleDataTableCol(label: "Action", property: "action", type: "number")

		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.total, annotation: true]
		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.conso, annotation: true]

		chart.vAxis << [title: 'Consommation (kWh)']

		return chart
	}


	/**
	 * Graphe comparatif des consommations sur les 2 périodes pour un user en fonction d'un compteur
	 * L'abscisse est calculée à partir de la période de référence et affichée sous forme
	 * de n° de jour dans la semaine pour faire coincider les 2 séries/
	 * 
	 * IMPORTANT !!! il faut bien sur que les 2 périodes commencent le même jour
	 * de la semaine (ie lundi) et aient la même durée en jour
	 *
	 * @param defi
	 * @param user
	 * @param consos données issues de la méthode #loadUserConso
	 *
	 * @return GoogleChart
	 */
	GoogleChart chartUserPeriode(Defi defi, User user, Map consos) {
		GoogleChart chart = new GoogleChart()

		chart.chartType = ChartTypeEnum.Column.factory

		// réorganisation avec une liste de N jours correspondant à la période de référence
		// ensuite, les valeurs d'action sont injectée sur ces jours
		// on convertit aussi les consos en kWh
		chart.values = []
		int index = 0

		for (def reference : consos.reference) {
			chart.values << [name: index++, dateValue: reference.dateValue,
				reference: (reference.value / 1000.0).round(1)]
		}

		// injecte les valeurs d'action en tenant compte de "trous"
		// l'index est calculé à partir du jour de départ de la période
		if (consos.action) {
			Date debutAction = consos.action[0].dateValue

			for (def action : consos.action) {
				index = action.dateValue - debutAction

				if (index >= 0 && index < chart.values.size()) {
					chart.values[index].action = (action.value / 1000.0).round(1)
				}
			}
		}

		chart.colonnes << new GoogleDataTableCol(label: "Date", property: "dateValue", type: "datetime")
		chart.colonnes << new GoogleDataTableCol(label: "Référence", property: "reference", type: "number")
		chart.colonnes << new GoogleDataTableCol(label: "Action", property: "action", type: "number")

		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.total, annotation: false]
		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.conso, annotation: true]

		chart.vAxis << [title: 'Consommation (kWh)']

		return chart
	}


	/**
	 * Charge les consos d'un user sur les périodes
	 * La méthode calcul les consos par jour, calcule les totaux sur les 2 périodes
	 * et retourne le tout sous forme de Map.
	 * 
	 * @param defi
	 * @param user
	 * @param compteurType
	 * 
	 * @return Map avec les fields : reference, action, totalReference, totalAction
	 */
	Map loadUserConso(Defi defi, User user, DefiCompteurEnum compteurType) throws SmartHomeException {
		Map result = [:]

		// recherche du compteur avec les consos
		House house = houseService.findDefaultByUser(user)

		if (!house) {
			throw new SmartHomeException("Profil incomplet !")
		}

		Device device = house[compteurType.property]

		if (!device) {
			throw new SmartHomeException("Le compteur ${compteurType} n'est pas renseigné !")
		}

		Compteur compteur = device.newDeviceImpl()

		// charge les consos sur les 2 périodes
		result.reference = compteur.consommationTotalByDay(defi.referenceDebut, defi.referenceFin)
		result.action = compteur.consommationTotalByDay(defi.actionDebut, defi.actionFin)

		// calcul les totaux et diff
		result.totalReference = ((result.reference.sum { it.value } ?: 0.0) / 1000.0).round(1)
		result.totalAction = ((result.action.sum { it.value } ?: 0) / 1000.0).round(1)
		result.totalDiff = result.totalAction - result.totalReference

		return result
	}
}