package crm.server;

import crm.beanfactory.BeanFactory;
import crm.dbservice.bean.DBService;
import crm.servlets.DemandServlet;
import crm.servlets.NewProductServlet;
import crm.servlets.PurchaseServlet;
import crm.servlets.SalesReportServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class CRMServer {

    public static void main(String[] args) throws Exception {
        (new CRMServer()).startServer();
    }

    public void startServer() throws Exception {
        DBService dbService = BeanFactory.getDBService("CRM");
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new NewProductServlet(dbService)), "/newproduct");
        context.addServlet(new ServletHolder(new PurchaseServlet(dbService)), "/purchase");
        context.addServlet(new ServletHolder(new DemandServlet(dbService)), "/demand");
        context.addServlet(new ServletHolder(new SalesReportServlet(dbService)), "/salesreport");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(8080);
        server.setHandler(handlers);
        server.start();
        server.join();
    }
}
