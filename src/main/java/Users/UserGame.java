package Users;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Andre on 2015-01-20.
 */
@Entity
public class UserGame implements Serializable{
    @Id
    @Size (max=12)
    private String username;
    @Id
    @Size (max=100)
    private String gameName;
    @Size (max = 100)
    private String hand;

    public UserGame(){}

    public void addUsername(String u)
    {
        username = u;
    }

    public String getUsernames()
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

}
