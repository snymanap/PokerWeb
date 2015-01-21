package Users;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * Created by Andre on 2015-01-20.
 */

@Entity
public class Game {
    @Id
    @Size (max = 100)
    private String gameName;

    public Game() {}

    public void setGameName(String _name)
    {
        this.gameName = _name;
    }

    public String getGameName()
    {
        return gameName;
    }

}
