package crm.dbservice.bean;

import crm.dbservice.dataSets.TradeHistoryDataSet;
import crm.dbservice.exception.DBException;
import crm.utility.Params;

import java.util.Date;
import java.util.List;

public interface DBService {
    boolean isExistProduct(String name) throws DBException;

    void addProduct(String name) throws DBException;

    void purchaseProduct(Params params) throws DBException;

    void demandProduct(Params params) throws DBException;

    long getTotalCountPurchase(String name, Date date) throws DBException;

    long getTotalCountDemand(String name, Date date) throws DBException;

    List<TradeHistoryDataSet> getActivePurchaseReport(String name, Date date) throws DBException;

    List<TradeHistoryDataSet> getActiveDemandReport(String name, Date date) throws DBException;

}
