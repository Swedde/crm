package crm.servlets;

import crm.dbservice.exception.DBException;
import crm.dbservice.bean.DBService;
import crm.utility.Params;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DemandServlet extends HttpServlet {
    private DBService dbService;

    public DemandServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        String param = request.getParameter("params");
        if (param.isEmpty()) {
            pw.println("Empty query");
            return;
        }

        Params params;

        try {
            params = Params.getParams(param);
        } catch (Exception e) {
            pw.println(e.getMessage());
            return;
        }

        try {
            if (!dbService.isExistProduct(params.name)) {
                pw.println("Product not exist");
                return;
            }
            if (dbService.getTotalCountPurchase(params.name, params.date)
                    - dbService.getTotalCountDemand(params.name, params.date) - params.nums < 0) {
                pw.println("Not enough product");
                return;
            }
            dbService.demandProduct(params);
            pw.println("OK");
        } catch (DBException e) {
            pw.println("Data base fail");
        }
    }
}
