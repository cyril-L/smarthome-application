package smarthome.api

import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.Device;
import smarthome.automation.DeviceMetavalue;
import smarthome.automation.DeviceService;
import smarthome.automation.DeviceType;
import smarthome.automation.DeviceValue;
import smarthome.automation.DeviceValueService;
import smarthome.automation.deviceType.Capteur;
import smarthome.core.AbstractService
import smarthome.core.AsynchronousWorkflow;
import smarthome.core.SmartHomeException
import smarthome.security.UserApplication;
import smarthome.security.UserApplicationService;


/**
 * Définition des URLS API : @see UrlMappings
 * @author gregory
 *
 */
class DeviceApiService extends AbstractService {

	UserApplicationService userApplicationService
	DeviceService deviceService
	DeviceValueService deviceValueService
	
	
	/**
	 * Réception de données
	 * 
	 * @param command
	 * @return device
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousWorkflow("deviceApiService.push")
	Device push(PushCommand command, String token) throws SmartHomeException {
		if (!command.datas) {
			throw new SmartHomeException("datas are required !")
		}
		
		UserApplication userApplication = userApplicationService.authenticateToken(token, command.application)
		
		// recherche du device
		Device device = Device.findByMacAndUser(command.name, userApplication.user, [lock: true])
		DeviceMetavalue metavalue
		
		// création d'un nouveau device "à la volée"
		if (!device) {
			device = new Device(user: userApplication.user, mac: command.name, label: command.name,
				deviceType: DeviceType.findByImplClass(Capteur.name))
		}
		
		// init la date du device avec la date la plus récente des datas
		def lastData = command.datas.max { it.timestamp as Long }
		device.dateValue = new Date(lastData.timestamp as Long)
		
		// gestion de la metavalue si demandé avec la valeur la plus récente
		if (command.metaname) {
			metavalue = device.addMetavalue(command.metaname, [trace: true, unite: command.unite,
				label: command.metaname, value: lastData.value.toString()])
		}
		
		// init la value du device avec la value la plus récente des datas si pas de metavalue ou
		// si celle-ci est synchro sur le device (ie main)
		if (!metavalue || metavalue?.main) {
			device.value = lastData.value.toString()
		}
		
		// enregistrement avec les values nécessaires si nouveau device
		deviceService.save(device)
		
		// ajoute toutes les valeurs
		for (def data : command.datas) {
			def convertValue = DeviceValue.parseDoubleValue(data.value.toString())
			
			if (convertValue != null) {
				deviceValueService.save(new DeviceValue(device: device, name: metavalue?.name,
					value: convertValue, dateValue: new Date(data.timestamp as Long)))
			}
		}
		
		return device
	}
}
