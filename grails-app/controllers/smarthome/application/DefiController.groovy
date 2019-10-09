package smarthome.application



import org.springframework.security.access.annotation.Secured

import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum


@Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMIN_GRAND_DEFI')")
class DefiController extends AbstractController {

	private static final String COMMAND_NAME = 'defi'

	DefiService defiService

	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Défis", navigation = NavigationEnum.configuration, header = "Compte")
	def defis(DefiCommand command) {
		command.user = authenticatedUser // spring security plugin
		def defis = defiService.listByAdmin(command, this.getPagination([:]))
		def recordsTotal = defis.totalCount

		// defis est accessible depuis le model avec la variable defi[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond defis, model: [recordsTotal: recordsTotal, command: command]
	}


	/**
	 * Edition
	 *
	 * @param defi
	 * @return
	 */
	def edit(Defi defi) {
		def editDefi = parseFlashCommand(COMMAND_NAME, defi)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editDefi]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]

		// TODO Compléter le model
		// model.toto = 'toto'

		// on remplit avec les infos du user
		model << userModel

		return model
	}


	/**
	 * Enregistrement modification
	 *
	 * @param defi
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DefiController.COMMAND_NAME)
	def save(Defi defi) {
		defi.user = authenticatedUser // spring security plugin
		defi.validate()

		checkErrors(this, defi)
		defiService.save(defi)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Suppression
	 *
	 * @param defi
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "defis")
	def delete(Defi defi) {
		defiService.delete(defi)
		redirect(action: COMMAND_NAME + 's')
	}
}