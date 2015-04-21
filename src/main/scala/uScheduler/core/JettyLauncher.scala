package uScheduler.core
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler}
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory

//object logger{def info(msg : String) = {println(msg)}}

object JettyLauncher {
  def main(args: Array[String]) {
    val logger = LoggerFactory.getLogger("core.JettyLauncher")
    val reader = new jline.console.ConsoleReader()
    val cwd = System.getProperty("user.dir")
    logger.info("CWD = " + cwd)
    logger.info("Loading org.h2 Driver")
    Class.forName("org.h2.Driver")
    logger.info("Starting embedded H2-Database-Server")
    val h2Server = org.h2.tools.Server.createTcpServer( "-tcpPort", "9092", "-baseDir", "db/", "-ifExists").start

    /*-----------------------------
    println("TESTING H2")
    val conn = java.sql.DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/user-db", "root", "9735FC12BAEBAB0FA2CE222641FBFA68917AB73F008D048E453951C0BD16D927")
    val stmt = conn.createStatement
    val res = stmt.executeQuery("SELECT * FROM users")
    while(res.next) println(res.getString("password"))
    conn.close
    -----------------------------*/

    //  Supported options are
    /*  [-tcpAllowOthers]    Allow other computers to connect - see below
        [-tcpDaemon]    Use a daemon thread
        [-tcpPort <port>]   The port (default: 9092)
        [-tcpSSL]   Use encrypted (SSL) connections
        [-tcpPassword <pwd>]    The password for shutting down a TCP server
        [-baseDir <dir>]    The base directory for H2 databases (all servers)
        [-ifExists] Only existing databases may be opened (all servers)
        [-trace]    Print additional trace information (all servers)
        [-key <from> <to>]  Allows to map a database name to another (all servers)
    */
    logger.info("H2-Database-Server has started")
    
    logger.info("Configuring Jetty")
    val port = if(System.getenv("PORT") != null) System.getenv("PORT").toInt else 8080
    val server = new Server(port)
    val context = new WebAppContext()
    context.setContextPath("/")
    context.setResourceBase("./src/main/webapp/")
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")
    server.setHandler(context)

    logger.info("Starting Jetty")
    server.start
    logger.info("Jetty has started")
    
    var line = ""
    while ({line = reader.readLine();  line != null}) {
        if ((line equalsIgnoreCase "quit")  || (line equalsIgnoreCase "exit") || (line equalsIgnoreCase "stop")) {
            logger.info("Stopping H2-Database-Server")
            h2Server.stop
            logger.info("H2-Database-Server stopped")
            logger.info("Stopping Jetty")
            server.stop
            logger.info("Jetty stopped")
            logger.info("Exiting")
            return
        }
    }
  }
} 