package com.hida

class LoggingService {

    def logThis(fooBarText) {
        log.info "Got text: ${ fooBarText }"
    }
}
