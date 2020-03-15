package crm.dbservice.bean;

import crm.dbservice.dao.ProductDAO;
import crm.dbservice.dao.TradeHistoryDAO;
import crm.dbservice.dataSets.ProductDataSet;
import crm.dbservice.dataSets.TradeHistoryDataSet;
import crm.dbservice.exception.DBException;
import crm.utility.Params;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class DBServiceImp implements DBService {

    private final EntityManagerFactory entityManagerFactory;

    public DBServiceImp(String persistenceUnit) {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
    }

    @Override
    public boolean isExistProduct(String name) throws DBException {
        EntityManager em = entityManagerFactory.createEntityManager();
        ProductDAO dao = new ProductDAO(em);
        ProductDataSet dataSet = dao.getProductDataSet(name);
        boolean result = dataSet != null;
        em.close();
        return result;
    }

    @Override
    public void addProduct(String name) throws DBException {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        ProductDAO dao = new ProductDAO(em);
        dao.insertProduct(name);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void purchaseProduct(Params params) throws DBException {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        TradeHistoryDAO dao = new TradeHistoryDAO(em);
        dao.insert(new TradeHistoryDataSet(params.name, params.nums, params.price, params.date, true));
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void demandProduct(Params params) throws DBException {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        TradeHistoryDAO dao = new TradeHistoryDAO(em);
        dao.insert(new TradeHistoryDataSet(params.name, params.nums, params.price, params.date, false));
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public long getTotalCountPurchase(String name, Date date) throws DBException {
        List<TradeHistoryDataSet> list = getActivePurchaseReport(name, date);
        if (list == null) {
            return 0;
        }
        return list.stream().map(el -> el.getNums()).reduce(0, (x, y) -> x + y);
    }

    @Override
    public long getTotalCountDemand(String name, Date date) throws DBException {
        List<TradeHistoryDataSet> list = getActiveDemandReport(name, date);
        if (list == null) {
            return 0;
        }
        return list.stream().map(el -> el.getNums()).reduce(0, (x, y) -> x + y);
    }

    @Override
    public List<TradeHistoryDataSet> getActivePurchaseReport(String name, Date date) throws DBException {
        return getActiveReport(name, date, true);
    }

    @Override
    public List<TradeHistoryDataSet> getActiveDemandReport(String name, Date date) throws DBException {
        return getActiveReport(name, date, false);
    }

    private List<TradeHistoryDataSet> getActiveReport(String name, Date date, boolean income) throws DBException {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        TradeHistoryDAO dao = new TradeHistoryDAO(em);
        List<TradeHistoryDataSet> report = dao.getReport(name, date, 0, income);
        em.getTransaction().commit();
        em.close();
        return report;
    }
}
