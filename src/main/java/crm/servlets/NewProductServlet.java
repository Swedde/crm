package crm.servlets;

import crm.dbservice.exception.DBException;
import crm.dbservice.bean.DBService;
import crm.utility.JSON_FIELDS;
import crm.utility.Params;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class NewProductServlet extends HttpServlet {
    private DBService dbService;

    public NewProductServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        String param = request.getParameter("params");
        if (param.isEmpty()) {
            pw.println("Empty query");
            return;
        }

        String name;
        try {
            name = Params.getJsonParam(param, JSON_FIELDS.NAME.getTitle());
        } catch (Exception e) {
            pw.println(e.getMessage());
            return;
        }

        if (name.isEmpty()) {
            pw.println("Empty product name");
            return;
        }

        try {
            if (dbService.isExistProduct(name)) {
                pw.println("Product already exist");
                return;
            }
            dbService.addProduct(name);
            pw.println("OK");
        } catch (DBException e) {
            pw.println("Data base fail");
        }
    }
}
