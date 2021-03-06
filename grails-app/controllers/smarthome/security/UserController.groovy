package smarthome.security

import smarthome.security.UserService;
import grails.plugin.springsecurity.annotation.Secured;
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum
import smarthome.security.RegistrationCode;
import smarthome.security.User;


/**
 * Controller gestion utilisateur
 * 
 * @author gregory
 *
 */
@Secured("hasRole('ROLE_ADMIN')")
class UserController extends AbstractController {

	UserService userService


	/**
	 * Retourne la liste des utilisateurs
	 * 
	 * @return
	 */
	@NavigableAction(label = "Utilisateurs", navigation = NavigationEnum.configuration, header = "Administrateur")
	def users(UserCommand command) {
		def users = userService.search(command, this.getPagination([:]))
		def recordsTotal = users.totalCount

		// users est accessible depuis le model avec la variable user[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond users, model: [recordsTotal: recordsTotal, command: command]
	}
	
	
	/**
	 * Edition d'un utilisateur
	 * 
	 * @param user
	 * @return
	 */
	def edit(User user) {
		def editUser = parseFlashCommand("user", user)
		def userRoles = editUser.getAuthorities()
		def registrations = RegistrationCode.where({
			username == editUser.username
		}).list(sort: 'dateCreated', order: 'desc')
		
		def model = [user: editUser, roles: Role.list(), userRoles: userRoles, 
			registration: registrations ? registrations[0] : null]
		
		render(view: 'user', model: model)
	}


	/**
	 * Création d'un utilisateur
	 *
	 * @param user
	 * @return
	 */
	def create() {
		def editUser = parseFlashCommand("user", new User(lastActivation: new Date(), enabled: false))
		render(view: 'user', model: [user: editUser, roles: Role.list(), userRoles: []])
	}


	/**
	 * Enregistrement d'un utilisateur existant avec toutes ses associations
	 * 
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = "user")
	def saveEdit(User user) {
		checkErrors(this, user)

		userService.save(user, true)
		redirect(action: 'users')
	}


	/**
	 * Enregistrement d'un nouvel utilisateur avec toutes ses associations
	 * 
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = "user")
	def saveCreate(User user) {
		userService.save(user, true)
		redirect(action: 'users')
	}
	
	
	/**
	 * Authentification rapide vers un autre utilisateur
	 *
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "users", modelName = "user")
	def switchUser(User user) {
		// nettoie la session
		// TODO 
		redirect(uri: "/j_spring_security_switch_user", params: [j_username: user.username])
	}
	
	
	/**
	 * Revenir à la session normale
	 *
	 * @return
	 */
	@Secured("isAuthenticated()")
	def exitSwitchUser() {
		// nettoie la session
		// TODO 
		redirect(uri: "/j_spring_security_exit_user")
	}
	
}
