package Repositories;

import Users.User;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 * Created by Charl on 2015-01-27.
 */
@Singleton
public class UserRepository extends  BaseRepository<User> {

    public boolean userStore(User user) {
        persist(user);
        return true;

    }

    @UnitOfWork
    public List<User> getAllUsers(){
        EntityManager entityManager = getEntityManager();
        String out = "";
        Query q = entityManager.createQuery("SELECT x FROM User x");

        List<User> users = (List<User>) q.getResultList();

        return users;
    }

    @UnitOfWork
    public List<User> getUsersByName(String name){
        EntityManager entityManager = getEntityManager();
        Query q = entityManager.createQuery("SELECT x FROM User x WHERE x.username = :username");
        q.setParameter("username", name);
        List<User> users = (List<User>) q.getResultList();
        return users;
    }

    @UnitOfWork
    public Optional<User> getUserByName(String name){
        EntityManager entityManager = getEntityManager();
        Query q = entityManager.createQuery("SELECT x FROM User x WHERE x.username = :username");
        q.setParameter("username", name);
        return getSingleResult(q);
    }

    @UnitOfWork
    public boolean userGet(String _username) {

        EntityManager entityManager = getEntityManager();

        Query q = entityManager.createQuery("SELECT x FROM User x where x.username = :usr");
        q.setParameter("usr", _username);
        List<User> users = (List<User>) q.getResultList();
        if (users.size() == 0)
            return false;

        for (int i = 0; i < users.size(); i++)
            System.out.println(users.get(i).getUsername());
        return true;
    }
}
