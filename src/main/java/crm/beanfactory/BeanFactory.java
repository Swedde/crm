package crm.beanfactory;

import crm.dbservice.bean.DBService;
import crm.dbservice.bean.DBServiceImp;

public class BeanFactory {
    public static DBService getDBService(String persistenceUnit) {
        return new DBServiceImp(persistenceUnit);
    }
}
