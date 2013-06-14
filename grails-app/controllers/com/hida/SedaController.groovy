package com.hida

import org.springframework.security.core.context.SecurityContextHolder

class SedaController {
    def springSecurityService
    def index() {
    }
    def send() {
        def msg = params.msg ?: "add params 'msg' for message."
        if(springSecurityService.isLoggedIn()) {
            log.debug("send message to seda ${springSecurityService.authentication.name}. ${SecurityContextHolder.context.authentication}")
            sendMessageWithAuth("seda:${springSecurityService.authentication.name}", msg, SecurityContextHolder.context.authentication )
        } else {
            sendMessage("seda:anyuser", msg )
        }
    }
}
