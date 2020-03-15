import crm.beanfactory.BeanFactory;
import crm.dbservice.bean.DBService;
import crm.utility.CRMUtility;
import crm.utility.JSON_FIELDS;
import crm.utility.Params;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class TestCRM {
    private DBService dbService;

    @Before
    public void initialization() {
        dbService = BeanFactory.getDBService("CRM");
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
        Assert.assertEquals(20, CRMUtility.getSalesReportDif(dbService, "green", new Date(1577836800L)));
        Assert.assertEquals(80, CRMUtility.getSalesReportDif(dbService, "green", new Date(1577923200L)));
    }
}
