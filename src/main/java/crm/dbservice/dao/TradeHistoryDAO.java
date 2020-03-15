package crm.dbservice.dao;

import crm.dbservice.dataSets.TradeHistoryDataSet;
import crm.dbservice.exception.DBException;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class TradeHistoryDAO {
    private EntityManager em;

    public TradeHistoryDAO(EntityManager em) {
        this.em = em;
    }

    public void insert(TradeHistoryDataSet dataSet) throws DBException {
        em.persist(dataSet);
    }

    public List<TradeHistoryDataSet> getReport(String name, Date date, int nums, boolean income) throws DBException {
        List<TradeHistoryDataSet> res = em.createQuery("FROM TradeHistoryDataSet " +
                "WHERE name = :name AND income = :income AND date <= :date AND nums > :nums",
                TradeHistoryDataSet.class)
                .setParameter("name", name)
                .setParameter("income", income)
                .setParameter("date", date)
                .setParameter("nums", nums)
                .getResultList();
        if (res.isEmpty() || res.get(0) == null) {
            return null;
        } else {
            return res;
        }
    }
}
