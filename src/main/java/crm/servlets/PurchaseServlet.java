package crm.servlets;

import com.google.gson.JsonObject;
import crm.dbservice.exception.DBException;
import crm.dbservice.bean.DBService;
import crm.utility.JSON_ANSW;
import crm.utility.JSON_ERR;
import crm.utility.PARAMETRS_NAMES;
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
        JsonObject answer = new JsonObject();
        PrintWriter pw = response.getWriter();
        String param = request.getParameter(PARAMETRS_NAMES.PARAMETRS.getTitle());
        if (param.isEmpty()) {
            answer.addProperty(JSON_ERR.ERROR.getTitle(), JSON_ERR.EMPTY_PARAMETRS.getTitle());
            pw.println(answer.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Params params;
        try {
            params = Params.getParams(param);
        } catch (Exception e) {
            answer.addProperty(JSON_ERR.ERROR.getTitle(), e.getMessage());
            pw.println(answer.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            if (!dbService.isExistProduct(params.name)) {
                answer.addProperty(JSON_ERR.ERROR.getTitle(), JSON_ERR.NOT_EXIST_PRODUCT.getTitle());
                pw.println(answer.toString());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            dbService.purchaseProduct(params);
        } catch (DBException e) {
            answer.addProperty(JSON_ERR.ERROR.getTitle(), JSON_ERR.DB_EXCEP.getTitle());
            pw.println(answer.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        answer.addProperty(JSON_ANSW.STATUS.getTitle(), JSON_ANSW.OK.getTitle());
        pw.println(answer.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
