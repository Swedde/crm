package crm.servlets;

import com.google.gson.JsonObject;
import crm.dbservice.exception.DBException;
import crm.dbservice.bean.DBService;
import crm.utility.*;

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
        JsonObject answer = new JsonObject();
        PrintWriter pw = response.getWriter();
        String param = request.getParameter(PARAMETRS_NAMES.PARAMETRS.getTitle());
        if (param.isEmpty()) {
            answer.addProperty(JSON_ERR.ERROR.getTitle(), JSON_ERR.EMPTY_PARAMETRS.getTitle());
            pw.println(answer.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String name;
        String dateStr;

        try {
            name = Params.getJsonParam(param, JSON_FIELDS.NAME.getTitle());
            dateStr = Params.getJsonParam(param,JSON_FIELDS.DATE.getTitle());
        } catch (Exception e) {
            answer.addProperty(JSON_ERR.ERROR.getTitle(), e.getMessage());
            pw.println(answer.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (name.isEmpty()) {
            answer.addProperty(JSON_ERR.ERROR.getTitle(), JSON_ERR.EMPTY_PRODUCT.getTitle());
            pw.println(answer.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Date date;
        try {
            date = Params.getDateFromString(dateStr);
        } catch (Exception e) {
            answer.addProperty(JSON_ERR.ERROR.getTitle(), e.getMessage());
            pw.println(answer.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            if (!dbService.isExistProduct(name)) {
                answer.addProperty(JSON_ERR.ERROR.getTitle(), JSON_ERR.NOT_EXIST_PRODUCT.getTitle());
                pw.println(answer.toString());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            double report = CRMUtility.getSalesReportDif(dbService, name, date);
            answer.addProperty(JSON_ANSW.SALES_REPORT.getTitle(), report);
            pw.println(answer.toString());
        } catch (DBException e) {
            answer.addProperty(JSON_ERR.ERROR.getTitle(), JSON_ERR.DB_EXCEP.getTitle());
            pw.println(answer.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
