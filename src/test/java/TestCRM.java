import crm.config.ApplicationConfigs;
import crm.dbservice.bean.DBService;
import crm.servlets.SalesReportServlet;
import crm.utility.JSON_FIELDS;
import crm.utility.Params;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class TestCRM {
    private DBService dbService;

    @Before
    public void initialization() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfigs.class);
        dbService = ctx.getBean(DBService.class);
        dbService.addProduct("green");
        dbService.addProduct("white");
        dbService.purchaseProduct(new Params("green", 3, 10, new Date(1577836800L)));
        dbService.purchaseProduct(new Params("green", 2, 10, new Date(1577923200L)));
        dbService.demandProduct(new Params("green", 2, 20, new Date(1577836800L)));
        dbService.demandProduct(new Params("green", 3, 30, new Date(1577923200L)));
    }

    @Test
    public void testGetJson() throws Exception {
        Assert.assertEquals("green", Params.getJsonParam("{name:green}", JSON_FIELDS.NAME.getTitle()));
        try {
            Params.getJsonParam("{nam:green}", JSON_FIELDS.NAME.getTitle());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Incorrect query", e.getMessage());
        }
    }

    @Test
    public void testIsExistProduct() throws Exception {
        Assert.assertTrue(dbService.isExistProduct("green"));
        Assert.assertTrue(dbService.isExistProduct("white"));
        Assert.assertFalse(dbService.isExistProduct(""));
        Assert.assertFalse(dbService.isExistProduct("blue"));
    }

    @Test
    public void testGetTotalCount() throws Exception {
        Assert.assertEquals(3, dbService.getTotalCountPurchase("green", new Date(1577836800L)));
        Assert.assertEquals(2, dbService.getTotalCountDemand("green", new Date(1577836800L)));
        Assert.assertEquals(5, dbService.getTotalCountPurchase("green", new Date(1577923200L)));
        Assert.assertEquals(5, dbService.getTotalCountDemand("green", new Date(1577923200L)));
    }

    @Test
    public void testTotalDiff() throws Exception {
        SalesReportServlet servlet = new SalesReportServlet(dbService);
        Assert.assertEquals(20, servlet.getSalesReportDif(dbService, "green", new Date(1577836800L)), 0.01);
        Assert.assertEquals(80, servlet.getSalesReportDif(dbService, "green", new Date(1577923200L)), 0.01);
    }
}
