package smarthome.automation.deviceType.catalogue

import smarthome.automation.ChartTypeEnum;

/**
 * Un contact sec de type on / off
 * 0 = off, 1 = on
 * Aucune action particulière car capteur
 * 
 * @author gregory
 *
 */
class ContactSec extends AbstractDeviceType {
	/**
	 * Retourne le type de graphique par défaut
	 *
	 * @return
	 */
	ChartTypeEnum defaultChartType() {
		ChartTypeEnum.Scatter
	}
}
