package crm.servlets;

import com.google.gson.JsonObject;
import crm.dbservice.dataSets.TradeHistoryDataSet;
import crm.dbservice.exception.DBException;
import crm.dbservice.bean.DBService;
import crm.utility.JSON_ANSW;
import crm.utility.JSON_ERR;
import crm.utility.JSON_FIELDS;
import crm.utility.PARAMETRS_NAMES;
import crm.utility.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@Component
public class SalesReportServlet extends HttpServlet {
    private DBService dbService;

    public SalesReportServlet(@Autowired DBService dbService) {
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
            double report = getSalesReportDif(dbService, name, date);
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

    public double getSalesReportDif(DBService dbService, String name, Date date) throws DBException {
        List<TradeHistoryDataSet> purchase = dbService.getActivePurchaseReport(name, date);
        List<TradeHistoryDataSet> demand = dbService.getActiveDemandReport(name, date);
        if (demand == null) {
            return 0;
        }
        int demandCount = demand.stream().map(el -> el.getNums()).reduce(0, (x, y) -> x + y);
        int i = 0;
        int j = 0;
        double report = 0;

        while (demandCount > 0) {
            TradeHistoryDataSet pur = purchase.get(i);
            TradeHistoryDataSet dem = demand.get(j);
            if (pur.getNums() >= dem.getNums()) {
                report += dem.getNums() * dem.getPrice() - pur.getPrice() * dem.getNums();
                pur.setNums(pur.getNums() - dem.getNums());
                dem.setNums(0);
                j++;
            } else {
                report += pur.getNums() * dem.getPrice() - pur.getPrice() * pur.getNums();
                dem.setNums(dem.getNums() - pur.getNums());
                pur.setNums(0);
                i++;
            }
            demandCount = demand.stream().map(el -> el.getNums()).reduce(0, (x, y) -> x + y);
        }
        return report;
    }
}
