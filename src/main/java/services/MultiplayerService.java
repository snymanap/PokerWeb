package services;

import Repositories.UserRepository;
import Users.Game;
import Users.User;
import Users.UserGame;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andre on 2015-01-20.
 */

@Singleton
public class MultiplayerService {
    @Inject
    private Provider<EntityManager> entityManagerProvider;

    @Inject
    UserRepository userRepository;

    @Transactional
    public boolean gameStore(Game game) {
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(game);
        return true;
    }

    @Transactional
    public boolean usergameStore(UserGame game) {
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(game);
        return true;
    }

    @Transactional
    public void gameUpdate(String _host, String _game) {
        EntityManager entityManager = entityManagerProvider.get();
        Query q = entityManager.createQuery("update Game set host = :Host" + " where gameName = :Game");
        q.setParameter("Host", _host);
        q.setParameter("Game", _game);
        int result = q.executeUpdate();
    }

    @Transactional
    public void gameUpdate2(Game game){
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.merge(game);
    }

    @UnitOfWork
    public List<Game> getAllGames(){
        EntityManager entityManager = entityManagerProvider.get();
        Query q = entityManager.createQuery("SELECT x FROM Game x");
        List<Game> games = (List<Game>) q.getResultList();
        return games;
    }



    @UnitOfWork
    public boolean gameGet(String _game) {

        EntityManager entityManager = entityManagerProvider.get();

        Query q = entityManager.createQuery("SELECT x FROM Game x where x.gameName = :usr");
        q.setParameter("usr", _game);
        List<Game> games = (List<Game>) q.getResultList();

        if (games.size() == 0)
            return false;

        for (int i = 0; i < games.size(); i++)
            System.out.println(games.get(i).getGameName());
        return true;
    }

    @UnitOfWork
    public List<UserGame> getAllUserGames(){
        EntityManager entityManager = entityManagerProvider.get();

        Query q = entityManager.createQuery("select x from UserGame x");

        List<UserGame> l = (List<UserGame>) q.getResultList();

        return l;
    }

    @UnitOfWork
    public List<UserGame> getDistinctAllUserGames(){
        EntityManager entityManager = entityManagerProvider.get();

        Query q = entityManager.createQuery("select distinct UserGame.gameName from UserGame");

        List<UserGame> l = (List<UserGame>) q.getResultList();

        return l;
    }

    @UnitOfWork
    public List<Game> getActiveGames(){
        EntityManager entityManager = entityManagerProvider.get();

        Query q = entityManager.createQuery("select x from Game x where x.active = TRUE");

        List<Game> l = (List<Game>) q.getResultList();

        return l;
    }

    @UnitOfWork
    public List<Game> getInactiveGames(){
        EntityManager entityManager = entityManagerProvider.get();

        Query q = entityManager.createQuery("select x from Game x where x.active = FALSE");

        List<Game> l = (List<Game>) q.getResultList();

        return l;
    }

    @UnitOfWork
    public List<UserGame> getUserGamesByUsername(String u){
        EntityManager entityManager = entityManagerProvider.get();

        Query q = entityManager.createQuery("select x from UserGame x where x.username = :asd");
        q.setParameter("asd", u);
        List<UserGame> l = (List<UserGame>) q.getResultList();

        return l;
    }

    @UnitOfWork
    public List<UserGame> getUserGamesByGameName(String u){
        EntityManager entityManager = entityManagerProvider.get();

        Query q = entityManager.createQuery("select x from UserGame x where x.gameName = :asd");
        q.setParameter("asd", u);
        List<UserGame> l = (List<UserGame>) q.getResultList();

        return l;
    }

    public long allocateWinningsToWinner(User user, long buyInAmount, int numberOfPlayers)
    {
        long potTotal= buyInAmount * (numberOfPlayers -1);
        user.setBalance(user.getBalance() + (potTotal));
        userRepository.merge(user);
        return potTotal;
    }
}
