package crm.utility;

import crm.dbservice.bean.DBService;
import crm.dbservice.dataSets.TradeHistoryDataSet;
import crm.dbservice.exception.DBException;

import java.util.Date;
import java.util.List;

public class CRMUtility {
    public static long getSalesReportDif(DBService dbService, String name, Date date) throws DBException {
        List<TradeHistoryDataSet> purchase = dbService.getActivePurchaseReport(name, date);
        List<TradeHistoryDataSet> demand = dbService.getActiveDemandReport(name, date);
        int demandCount = demand.stream().map(el -> el.getNums()).reduce(0, (x, y) -> x + y);
        int i = 0;
        int j = 0;
        long report = 0;

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
