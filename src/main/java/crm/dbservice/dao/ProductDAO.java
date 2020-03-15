package crm.dbservice.dao;

import crm.dbservice.dataSets.ProductDataSet;
import crm.dbservice.exception.DBException;

import javax.persistence.EntityManager;
import java.util.List;

public class ProductDAO {
    private EntityManager em;


    public ProductDAO(EntityManager em) {
        this.em = em;
    }

    public ProductDataSet getProductDataSet(String name) throws DBException {
        List<ProductDataSet> result = em.createQuery("FROM ProductDataSet where name = :name", ProductDataSet.class)
                .setParameter("name", name)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public void insertProduct(String name) throws DBException {
        em.persist(new ProductDataSet(name));
    }
}
