package io.github.XanderGI.infrastructure.transaction;

import io.github.XanderGI.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Supplier;

public class TransactionRunner {

    public <T> T execute(Supplier<T> supplier) {
        Transaction transaction = null;
        try {
            Session session = HibernateUtil.getSession();
            transaction = session.beginTransaction();

            T result = supplier.get();

            transaction.commit();

            return result;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public void execute(VoidAction action) {
        execute(() -> {
            action.perform();
            return null;
        });
    }
}