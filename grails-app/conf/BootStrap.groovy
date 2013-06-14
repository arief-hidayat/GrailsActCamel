import com.hida.Role
import com.hida.User
import com.hida.UserRole
import org.apache.commons.lang.StringUtils

class BootStrap {
    def springSecurityService
    def sessionFactory
    def grailsApplication
    def init = { servletContext ->
        println "XXX security manager: ${grailsApplication.mainContext.getBean('accessDecisionManager') != null } ... ${grailsApplication.mainContext.getBean('authenticationManager') != null}"
//        println "XXX camelAuth : ${grailsApplication.mainContext.getBean('camelUser') } ... ${grailsApplication.mainContext.getBean('camelAdmin')}"
        createDemoUserAndRole("password")
        println "password:  ${springSecurityService.encodePassword('password')}"
    }
    def createDemoUserAndRole(String password) {
        User.list().each {
            UserRole.removeAll it
        }
        sessionFactory.currentSession.flush()
        sessionFactory.currentSession.clear()
        User.list().each { it.delete(flush : true)}
        Role.list().each { it.delete(flush : true)}

        ['USER', 'ADMIN'].each {
            new Role(authority : "ROLE_$it").save(flush : true)
        }
        Role.list(sort: 'authority').each {  Role role ->
            def name = role.authority?.replace("ROLE_", "").replace("_", " ").toLowerCase()
            def user = User.findWhere([username : name])
            if(user) {
                if(!StringUtils.equals(password, user.password)) {
                    user.password = password
                    user.save(flush : true)
                }
                if(!UserRole.get(user.id, role.id)) {
                    UserRole.create user, role, true
                }
            } else {
                user = new User(username: name, enabled: true, password: password)
                user.save(flush : true)
                log.debug "Create user role ${user} ${role}"
                sessionFactory.currentSession.flush()
                sessionFactory.currentSession.clear()
                user = User.get(user.id)
                role = Role.get(role.id)
                UserRole.create(user, role, true)
            }
        }
    }
    def destroy = {
    }
}
