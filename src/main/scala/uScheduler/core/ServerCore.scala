package uScheduler.core

import org.scalatra._
import scalate.ScalateSupport
import org.apache.shiro.authc._
import org.apache.shiro.SecurityUtils

class ServerCore extends UschedulerStack with FlashMapSupport {

	get("/") {
		redirect("/login")
	}

	get("/login") {	
		contentType="text/html"
		flash("error") = ""
		jade("/views/login.jade", "layout" -> "")
	}
	
	post("/login") {
		contentType="text/html"
		
		try {
			val username = params.getOrElse("username", "")
			val password = params.getOrElse("password", "")
			val subject = SecurityUtils.getSubject()
			val token = new UsernamePasswordToken(username, password)
			subject.login(token)

			flash("success") = "Login erfolgreich."
			redirect("/index")
			
		} catch {
			case uae: UnknownAccountException => {
				flash("error") = "Ungültiger Benutzername."
				jade("/views/login.jade", "layout" -> "")
			}
      
			case ice: IncorrectCredentialsException => {
				flash("error") = "Ungültiges Passwort."
				jade("/views/login.jade", "layout" -> "")
			}
			
			case lae: LockedAccountException => {
				flash("error") = "Account ist gesperrt."
				jade("/views/login.jade", "layout" -> "")
			}
			
			case ae: AuthenticationException => {
				throw ae
			}
		}
	}
	
	get("/logout") {
		val subject = SecurityUtils.getSubject

		if(!subject.isAuthenticated) redirect("/login")

		try {
			subject.logout()
			flash("success") = "Loggout erfolgreich."
		} catch {
			case e: Exception => throw e
		}
	}
	
	get("/index") {
		contentType="text/html"
		if (!SecurityUtils.getSubject().isAuthenticated())
			redirect("/login")
		jade("/views/index.jade", "layout" -> "")
    }

    /*get("/admin-only") {
		contentType="text/html"
		if (!SecurityUtils.getSubject().isAuthenticated())
			redirect("/login")

		if(!SecurityUtils.getSubject().hasRole("admin"))
			halt(401)
			
		jade(...)
    }*/

}