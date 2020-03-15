package crm.servlets;

import crm.dbservice.exception.DBException;
import crm.dbservice.bean.DBService;
import crm.utility.Params;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PurchaseServlet extends HttpServlet {
    private DBService dbService;

    public PurchaseServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        String param = request.getParameter("params");
        if (param.isEmpty()) {
            pw.println("Empty query");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
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
            dbService.purchaseProduct(params);
        } catch (DBException e) {
            pw.println("Data base fail");
            return;
        }
        pw.println("OK");
    }
}
