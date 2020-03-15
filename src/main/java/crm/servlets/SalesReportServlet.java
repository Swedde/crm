package crm.servlets;

import crm.dbservice.exception.DBException;
import crm.dbservice.bean.DBService;
import crm.utility.CRMUtility;
import crm.utility.JSON_FIELDS;
import crm.utility.Params;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class SalesReportServlet extends HttpServlet {
    private DBService dbService;

    public SalesReportServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        String param = request.getParameter("params");
        if (param.isEmpty()) {
            pw.println("Empty query");
            return;
        }

        String name;
        String dateStr;
        Date date;

        try {
            name = Params.getJsonParam(param, JSON_FIELDS.NAME.getTitle());
            dateStr = Params.getJsonParam(param,JSON_FIELDS.DATE.getTitle());
        } catch (Exception e) {
            pw.println(e.getMessage());
            return;
        }

        if (name.isEmpty()) {
            pw.println("Empty product name");
            return;
        }

        try {
            date = Params.getDateFromString(dateStr);
        } catch (Exception e) {
            pw.println(e.getMessage());
            return;
        }

        try {
            if (!dbService.isExistProduct(name)) {
                pw.println("Product not exist");
                return;
            }
            long report = CRMUtility.getSalesReportDif(dbService, name, date);
            pw.println("Income: " + report);
        } catch (DBException e) {
            pw.println("Data base fail");
        }
    }
}
