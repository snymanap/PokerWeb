package Repositories;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
//import java.security.Provider;

/**
 * Created by Andre on 2015-01-20.
 */
@Singleton
public class BaseRepository<T> {
    private Provider<EntityManager> entityManagerProvider;

    @Transactional
    public void persist(T entity){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("prod_unit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();


        entityManager.persist(entity);
    }
}
