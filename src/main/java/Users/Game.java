package Users;

import Repositories.BaseRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
//import com.google.inject.persist.jpa.JpaPersistService;
import org.hibernate.annotations.Cascade;
import services.MultiplayerService;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andre on 2015-01-20.
 */

@Entity
public class Game extends BaseRepository{
    @Id
    @Size (max = 100)
    private String gameName;

    @OneToMany(mappedBy = "game")
    private List<UserGame> users = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date gameDate;

    public Game() {}

    public void addUser(User user){
        UserGame userGame = new UserGame();
        userGame.setGameName(this.gameName);
        userGame.setUsername(user.getUsername());
        userGame.setGame(this);
        userGame.setUser(user);
        //userGame.setHand("3♠,4♠,5♠,6♠,7♠");
        this.users.add(userGame);
        //persist(userGame);


        //persist(userGame);
        //return userGame;


    }

    public void setGameName(String _name)
    {
        this.gameName = _name;
    }

    public String getGameName()
    {
        return gameName;
    }

    public void setGameDate(Date t) { gameDate = t; }

    public Date getGameDate(){
        return this.gameDate;
    }

}
