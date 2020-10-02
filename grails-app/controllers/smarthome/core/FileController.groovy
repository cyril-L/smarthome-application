package smarthome.core

import org.springframework.security.access.annotation.Secured

@Secured("isAuthenticated()")
class FileController {

    def index() { }

    def saveEdit(File file) {
        checkErrors(this, file)
        if (!file.save()) {
            throw new SmartHomeException("Erreur enregistrement fichier")
        }
        render contentType: "text/json", text: '{"sharingId":"plop"}'
    }
}
