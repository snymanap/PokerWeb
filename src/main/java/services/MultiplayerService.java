package services;

import Users.Game;
import Users.User;
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
    @Transactional
    public boolean gameStore(Game game) {
        EntityManager entityManager = entityManagerProvider.get();

        entityManager.persist(game);
        return true;

    }

    @UnitOfWork
    public void getAllGames(){
        EntityManager entityManager = entityManagerProvider.get();
        String out = "";
        Query q = entityManager.createQuery("SELECT x FROM Game x");

        List<Game> games = (List<Game>) q.getResultList();
        if (games != null)
            for (int i = 0; i < games.size(); i++)
                System.out.println(games.get(i).getGameName());
        //return "No users";
    }

    @UnitOfWork
    public boolean gameGet(String _game) {

        EntityManager entityManager = entityManagerProvider.get();

        Query q = entityManager.createQuery("SELECT x FROM Game x where x.gameName = :usr");
        q.setParameter("usr", _game);
        List<Game> games = (List<Game>) q.getResultList();
        //System.out.println("JLKJLLKJLKJLKJLKJLKJlkjLKJLJL" + users.size());
        if (games.size() == 0)
            return false;

        for (int i = 0; i < games.size(); i++)
            System.out.println(games.get(i).getGameName());
        return true;
    }
}
