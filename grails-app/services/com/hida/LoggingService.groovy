package com.hida

class LoggingService {
    def logUser(fooBarText) {
        log.info "Got text from user: ${ fooBarText }"
    }
    def logAnonym(text) {
        log.info "Got text from anonym: ${ text }"
    }
}
