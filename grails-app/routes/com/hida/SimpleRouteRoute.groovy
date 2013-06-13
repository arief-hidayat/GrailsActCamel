package com.hida



import org.apache.camel.builder.RouteBuilder

class SimpleRouteRoute extends RouteBuilder {
	def grailsApplication
    @Override
    void configure() {
		def config = grailsApplication?.config
        log.debug "configure.. simple route"

        // example:
//        from('seda:admin').to('stream:out').policy('admin')
//        from('seda:user').filter { it.in.body.contains("LOG::")}.to('bean:loggingService?method=logThis').policy('user')
        from('seda:user').policy('user').to('bean:loggingService?method=logThis')

    }
}
