package crm.dbservice.exception;

import org.hibernate.HibernateException;

public class DBException extends HibernateException {
    public DBException(String message) {
        super(message);
    }
}
