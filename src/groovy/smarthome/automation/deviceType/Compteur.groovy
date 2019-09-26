package smarthome.automation.deviceType

import java.util.Date
import java.util.List
import java.util.Map

import smarthome.automation.ChartViewEnum
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceTypeProvider
import smarthome.automation.DeviceTypeProviderPrix
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueMonth
import smarthome.automation.HouseConso
import smarthome.automation.SaisieIndexCommand
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol

/**
 * Compteur
 * 
 * @author gregory
 *
 */
class Compteur extends AbstractDeviceType {

	private static final String AGGREGATE_METRIC_NAME = "sum"
	private static final String META_METRIC_NAME = "conso"
	private static final String DEFAULT_CONTRAT = "BASE"

	protected DeviceTypeProvider fournisseurCache
	protected String contratCache


	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.chartDataTemplate()
	 */
	@Override
	def chartDataTemplate() {
		'/deviceType/compteur/compteurChartDatas'
	}



	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#values()
	 */
	@Override
	List values(DeviceChartCommand command) throws SmartHomeException {
		def values = []

		// pour chaque vue, il n'y a qu'une seule métrique à charger
		// la metavalue "META_METRIC_NAME" avec la conso horaire entre chaque prise de mesure
		// pour les vues agrégées, la fonction de calcul repart des index (métrique principale
		// sans nom) pour recalculer les consos entre 2 index de la même période.
		// sans ces 2 vues, il n'y a donc qu'une métrique appelée "AGGREGATE_METRIC_NAME"

		if (command.viewMode == ChartViewEnum.day) {
			values = DeviceValue.values(command.device, command.dateDebut(), command.dateFin(),
					META_METRIC_NAME)
		} else if (command.viewMode == ChartViewEnum.month) {
			values = DeviceValueDay.values(command.device, command.dateDebut(), command.dateFin(),
					AGGREGATE_METRIC_NAME)
		} else if (command.viewMode == ChartViewEnum.year) {
			values = DeviceValueMonth.values(command.device, command.dateDebut(), command.dateFin(),
					AGGREGATE_METRIC_NAME)
		}

		return values
	}


	/**
	 * important de surcharger cette boite car le graphe teleinfo a son propre builder de chart
	 * sinon le graphe sera créé une 1ere fois dans la méthode parent
	 *
	 * @see smarthome.automation.deviceType.AbstractDeviceType#googleChart(smarthome.automation.DeviceChartCommand, java.util.List)
	 */
	@Override
	GoogleChart googleChart(DeviceChartCommand command, List values) {
		GoogleChart chart = new GoogleChart()
		command.device.extrasToJson()
		chart.values = values

		chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "dateValue")
		chart.colonnes << new GoogleDataTableCol(label: "Index", property: "value", type: "number")

		// Convertit les valeurs si besoin
		def coefConversion = command.device.metadata('coefConversion')

		if (coefConversion?.value) {
			chart.values.each {
				it.value = it.value * coefConversion.value.toDouble()
			}
		}

		return chart
	}


	/**
	 * Construction d'un graphe avec les tarifs
	 *
	 * @param command
	 * @param values
	 * @return
	 */
	GoogleChart googleChartTarif(DeviceChartCommand command, def values) {
		GoogleChart chart = new GoogleChart()

		return chart
	}


	/**
	 * Charge les prix pour une année donnée et les renvoit indexés dans une map en
	 * fonction de l'option tarifaire
	 *
	 * @param annee
	 * @return
	 */
	final Map listTarifAnnee(int annee) {
		if (tarifCache != null) {
			return tarifCache
		}

		tarifCache = [:]
		DeviceTypeProvider provider = getFournisseur()

		if (provider) {
			String contrat = getContrat()

			if (contrat) {
				DeviceTypeProviderPrix.findAllByDeviceTypeProviderAndContratAndAnnee(provider, contrat, annee).each {
					tarifCache.put(it.period, it.prixUnitaire)
				}
			}
		}

		return tarifCache
	}


	/**
	 * Calcul d'un tarif pour une période donnée
	 *
	 * @param period
	 * @param quantite
	 * @param annee
	 * @return
	 */
	Double calculTarif(String period, double quantite, int annee) {
		Double prixUnitaire = listTarifAnnee(annee)?.get(period.toUpperCase())

		if (prixUnitaire != null) {
			return ((prixUnitaire * quantite) as Double).round(2)
		}

		return null
	}


	/**
	 * Retourne le fournisseur du contrat
	 *
	 * @return
	 */
	DeviceTypeProvider getFournisseur() {
		if (fournisseurCache != null) {
			return fournisseurCache
		}

		// cherche le fournisseur dans les metadonnées
		String libelle = device.metadata("fournisseur")?.value

		if (libelle) {
			fournisseurCache = DeviceTypeProvider.findByLibelle(libelle)
			return fournisseurCache
		}

		return null
	}


	/**
	 * Nom du contrat
	 */
	String getContrat() {
		if (contratCache != null) {
			return contratCache
		}

		String optionTarifaire = getOptTarif()

		if (optionTarifaire) {
			String intensiteSouscrite = getPuissanceSous()

			if (intensiteSouscrite) {
				contratCache = "${optionTarifaire}_${intensiteSouscrite}".toUpperCase() // ex : HC_60
			} else {
				contratCache = "${optionTarifaire}".toUpperCase() // ex : HC_60
			}
		} else {
			contratCache = DEFAULT_CONTRAT
		}

		return contratCache
	}


	/**
	 * Option tarifaire
	 *
	 * @return
	 */
	String getOptTarif() {
		return device.metavalue("opttarif")?.value // base, hc, ...
	}


	/**
	 * Puissance du contrat
	 * 
	 * @return
	 */
	String getPuissanceSous() {
		return device.metavalue("isousc")?.value // 60A, 45A, ...
	}


	/**
	 * Unité pour les widgets (peut être différent)
	 *
	 * @return
	 */
	String defaultUnite() {
		return device.unite
	}


	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueDay(java.util.Date)
	 */
	@Override
	List aggregateValueDay(Date dateReference) {
		// calcule la conso du jour en repartant des index
		def values = DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('day', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			(max(deviceValue.value) - min(deviceValue.value)) as ${AGGREGATE_METRIC_NAME})
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name is null
			GROUP BY deviceValue.name, date_trunc('day', deviceValue.dateValue)""", [device: device,
					dateDebut: DateUtils.firstTimeInDay(dateReference), dateFin: DateUtils.lastTimeInDay(dateReference)])

		return values
	}



	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueMonth(java.util.Date)
	 */
	@Override
	List aggregateValueMonth(Date dateReference) {
		// calcule la conso du mois en repartant des index
		def values = DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			(max(deviceValue.value) - min(deviceValue.value)) as ${AGGREGATE_METRIC_NAME})
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name is null
			GROUP BY deviceValue.name, date_trunc('month', deviceValue.dateValue)""", [device: device,
					dateDebut: DateUtils.firstDayInMonth(dateReference), dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference))])

		return values
	}


	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.prepateMetaValuesForSave()
	 */
	@Override
	def prepareMetaValuesForSave(def datas) {
		Date dateInf = device.dateValue - 1

		// si le device n'existe pas encore, il n'y a donc pas d'anciennes valeurs
		// pour calculer la dernière conso
		if (device.id) {
			// les consos intermédiaires sont désormais calculées par l'agent (à cause du offline)
			// ce test sert à calculer la conso si pas envoyée par un ancien agent
			if (!datas.metavalues?.conso) {
				device.addMetavalue(META_METRIC_NAME, [value: "0", label: "Période consommation", trace: true])

				// récupère la dernière valeur principale (le dernier index)
				def lastIndex = DeviceValue.lastValueInPeriod(device, dateInf, device.dateValue)

				if (lastIndex) {
					def conso = device.value.toLong() - lastIndex.value.toLong()
					device.addMetavalue(META_METRIC_NAME, [value: conso.toString()])
				}
			}
		}
	}


	/**
	 * Les consos du jour en map indexé par le type d'heure (HC, HP, BASE, etc.)
	 *
	 * @return
	 */
	Map consosJour(Date currentDate = null) {
		def consos = [optTarif: getOptTarif()]
		currentDate = currentDate ?: new Date()
		def currentYear = currentDate[Calendar.YEAR]
		Date firstHour = DateUtils.firstTimeInDay(currentDate)
		Date lastHour = DateUtils.lastTimeInDay(currentDate)

		if (consos.optTarif in ['HC', 'EJP']) {
			consos.hchp = ((DeviceValue.values(device, firstHour, lastHour, 'hpinst').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.hchc = ((DeviceValue.values(device, firstHour, lastHour, 'hcinst').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = (consos.hchp + consos.hchc as Double).round(1)

			consos.tarifHP = calculTarif(consos.optTarif == 'HC' ? 'HP' : 'PM', consos.hchp, currentYear)
			consos.tarifHC = calculTarif(consos.optTarif == 'HC' ? 'HC' : 'HN', consos.hchc, currentYear)
			consos.tarifTotal = (consos.tarifHP != null || consos.tarifHC != null) ? (consos.tarifHP ?: 0.0) + (consos.tarifHC ?: 0.0) : null
		} else {
			consos.base = ((DeviceValue.values(device, firstHour, lastHour, 'baseinst').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = consos.base

			consos.tarifBASE = calculTarif('BASE', consos.base, currentYear)
			consos.tarifTotal = consos.tarifBASE
		}

		return consos
	}


	/**
	 * Consommation moyenne par jour sur une période
	 *
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	Double consoMoyenneJour(Date dateStart, Date dateEnd) {
		Double consoMoyenne
		def consos = [optTarif: getOptTarif()]
		dateStart = dateStart.clearTime()
		dateEnd = dateEnd.clearTime()
		int duree = (dateEnd - dateStart) + 1

		if (duree) {
			if (consos.optTarif in ['HC', 'EJP']) {
				def hchp = ((DeviceValueDay.values(device, dateStart, dateEnd, 'hchpsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
				def hchc = ((DeviceValueDay.values(device, dateStart, dateEnd, 'hchcsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
				consoMoyenne = ((hchp + hchc) / duree as Double).round(1)
			} else {
				def base = ((DeviceValueDay.values(device, dateStart, dateEnd, 'basesum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
				consoMoyenne = (base / duree as Double).round(1)
			}
		}

		return consoMoyenne
	}


	/**
	 * Les consos du mois en map indexé par le type d'heure (HC, HP, BASE, etc.)
	 *
	 * @return
	 */
	Map consosMois(Date currentDate = null) {
		def consos = [optTarif: getOptTarif()]
		currentDate = currentDate ?: new Date()
		def currentYear = currentDate[Calendar.YEAR]
		Date firstDayMonth = DateUtils.firstDayInMonth(currentDate)
		Date lastDayMonth = DateUtils.lastDayInMonth(currentDate)

		if (consos.optTarif in ['HC', 'EJP']) {
			consos.hchp = ((DeviceValueDay.values(device, firstDayMonth, lastDayMonth, 'hchpsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.hchc = ((DeviceValueDay.values(device, firstDayMonth, lastDayMonth, 'hchcsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = (consos.hchp + consos.hchc as Double).round(1)

			consos.tarifHP = calculTarif(consos.optTarif == 'HC' ? 'HP' : 'PM', consos.hchp, currentYear)
			consos.tarifHC = calculTarif(consos.optTarif == 'HC' ? 'HC' : 'HN', consos.hchc, currentYear)
			consos.tarifTotal = (consos.tarifHP != null || consos.tarifHC != null) ? (consos.tarifHP ?: 0.0) + (consos.tarifHC ?: 0.0) : null
		} else {
			consos.base = ((DeviceValueDay.values(device, firstDayMonth, lastDayMonth, 'basesum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = consos.base

			consos.tarifBASE = calculTarif('BASE', consos.base, currentYear)
			consos.tarifTotal = consos.tarifBASE
		}

		return consos
	}


	/**
	 * Les consos du mois en map indexé par le type d'heure (HC, HP, BASE, etc.)
	 *
	 * @return
	 */
	Map consosAnnee(Date currentDate = null) {
		def consos = [optTarif: getOptTarif()]
		currentDate = currentDate ?: new Date()
		def currentYear = currentDate[Calendar.YEAR]
		Date firstDayYear = DateUtils.firstDayInYear(currentDate)
		Date lastDayYear = DateUtils.lastDayInYear(currentDate)

		if (consos.optTarif in ['HC', 'EJP']) {
			consos.hchp = ((DeviceValueMonth.values(device, firstDayYear, lastDayYear, 'hchpsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.hchc = ((DeviceValueMonth.values(device, firstDayYear, lastDayYear, 'hchcsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = (consos.hchp + consos.hchc as Double).round(1)

			consos.tarifHP = calculTarif(consos.optTarif == 'HC' ? 'HP' : 'PM', consos.hchp, currentYear)
			consos.tarifHC = calculTarif(consos.optTarif == 'HC' ? 'HC' : 'HN', consos.hchc, currentYear)
			consos.tarifTotal = (consos.tarifHP != null || consos.tarifHC != null) ? (consos.tarifHP ?: 0.0) + (consos.tarifHC ?: 0.0) : null
		} else {
			consos.base = ((DeviceValueMonth.values(device, firstDayYear, lastDayYear, 'basesum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = consos.base

			consos.tarifBASE = calculTarif('BASE', consos.base, currentYear)
			consos.tarifTotal = consos.tarifBASE
		}

		return consos
	}


	/**
	 * Parse une saisie d'index par un user pour mettre à jour l'objet device associé
	 * 
	 * @param command
	 * @throws SmartHomeException
	 */
	void parseIndex(SaisieIndexCommand command) throws SmartHomeException {

	}

}
