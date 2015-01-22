package Users;

import cards.Hand;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Andre on 2015-01-20.
 */

@Entity
@IdClass(UserGameID.class)
public class UserGame implements Serializable{
    @Id
    @Size(max = 12)
    private String username;

    @Id
    @Size(max = 100)
    private String gameName;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "gameName", referencedColumnName = "gameName", insertable = false, updatable = false)
    private Game game;


    private String hand;

    public UserGame(){}

    public void setUsername(String u)
    {
        username = u;
    }

    public String getUsername()
    {
        return username;
    }

    public void setGameName(String s)
    {
        gameName = s;
    }

    public String getGameName()
    {
        return gameName;
    }

    public void setHand(String h)
    {
        this.hand = h;
    }

    public String getHand()
    {
        return hand;
    }

    public void setUser(User u){
        user = u;
    }

    public void setGame(Game g){
        game = g;
    }



}
