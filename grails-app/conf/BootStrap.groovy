import com.hida.Role
import com.hida.User
import com.hida.UserRole
import org.apache.commons.lang.StringUtils

class BootStrap {
    def springSecurityService
    def sessionFactory
    def init = { servletContext ->
        createDemoUserAndRole("password")
    }
    def createDemoUserAndRole(String defaultPassword) {
        String password = springSecurityService.encodePassword(defaultPassword)
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
                UserRole.create(user, role, true)
            }
        }
    }
    def destroy = {
    }
}
