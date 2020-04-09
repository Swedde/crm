package crm.server;

import crm.config.ApplicationConfigs;
import crm.config.ServerProperties;
import crm.utility.VFS;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CRMServer {

    public static void main(String[] args) throws Exception {
        (new CRMServer()).startServer();
    }

    public void startServer() throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfigs.class);
        ServerProperties serverData = ctx.getBean(ServerProperties.class);
        ServletContextHandler context = createServletContextHandler(ctx, serverData);
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase(serverData.getProperty("pages_dir"));
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        Server server = new Server(Integer.parseInt(serverData.getProperty("port")));
        server.setHandler(handlers);
        server.start();
        server.join();
    }

    private ServletContextHandler createServletContextHandler(ApplicationContext ctx, ServerProperties serverProperties) throws ClassNotFoundException {
        Pattern fullClassNamePattern = Pattern.compile("/home/swedde/Desktop/JavaProjects/crm/src/main/java/(.*)\\.java");
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        Iterator<File> it = VFS.getIterator("src/main/java/crm/servlets");
        while (it.hasNext()) {
            File file = it.next();
            if (file.isDirectory()) {
                continue;
            }
            Matcher matcher = fullClassNamePattern.matcher(file.getAbsolutePath());
            matcher.find();
            String className = matcher.group(1).replaceAll("/", ".");
            Class cs = Class.forName(className);
            String[] way = className.split("\\.");
            context.addServlet(new ServletHolder((HttpServlet) ctx.getBean(cs)), serverProperties.getProperty(way[way.length - 1]));
        }
//        context.addServlet(new ServletHolder(ctx.getBean(NewProductServlet.class)), "/newproduct");
//        context.addServlet(new ServletHolder(ctx.getBean(PurchaseServlet.class)), "/purchase");
//        context.addServlet(new ServletHolder(ctx.getBean(DemandServlet.class)), "/demand");
//        context.addServlet(new ServletHolder(ctx.getBean(SalesReportServlet.class)), "/salesreport");
        return context;
    }
}
