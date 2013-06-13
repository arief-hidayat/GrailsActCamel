package com.hida

class SedaController {
    def springSecurityService
    def index() {
    }
    def send() {
        def msg = params.msg ?: "add params 'msg' for message."
        if(springSecurityService.isLoggedIn()) {
            log.debug("send message to seda ${springSecurityService.authentication.name}.")
            sendMessage("seda:${springSecurityService.authentication.name}", msg )
//            requestMessage
        } else {
            log.error("Please log in")
        }
    }
}
