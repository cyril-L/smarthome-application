package smarthome.core

import smarthome.automation.Agent
import smarthome.security.User

class File {

    static belongsTo = [user: User]

    Date createdAt
    String sharingId
    String filename
    String mimeType
    byte[] data

    static constraints = {
        sharingId nullable: true
        mimeType nullable: true
        filename blank: false
    }

    static mapping = {
        table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
        user index: "File_User_Idx"
        sharingId index: "File_SharingId_Idx"
    }
}
