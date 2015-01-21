package Users;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.security.Timestamp;
import java.util.Date;

/**
 * Created by Andre on 2015-01-20.
 */

@Entity
public class Game {
    @Id
    @Size (max = 100)
    private String gameName;



    @Temporal(TemporalType.TIMESTAMP)
    private Date gameDate;

    public Game() {}

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
