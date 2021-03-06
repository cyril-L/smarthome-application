package smarthome.automation

import java.io.Serializable;
import java.util.Date;

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;
import groovy.time.TimeCategory;

/**
 * Les valeurs aggrégées sur une journée
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceValueDay implements Serializable {
	Device device
	Double value
	Date dateValue
	String name 
	
	
	static belongsTo = [device: Device]
	
	
    static constraints = {
		
    }
	
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		dateValue index: "DeviceValueDay_Idx"
		device index: "DeviceValueDay_Idx"
		name index: "DeviceValueDay_Idx", length: 64
		version false
	}
	
	
	static List values(Device device, Date dateDebut, Date dateFin, String metaName = null) {
		return DeviceValueDay.createCriteria().list {
			eq 'device', device
			between 'dateValue', dateDebut, dateFin
			
			if (metaName) {
				or {
					for (String token : metaName.split(",")) {
						eq "name", token
					}
				}
			}
			
			order 'dateValue', 'name'
		}
	}
}
