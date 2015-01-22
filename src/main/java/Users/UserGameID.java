package Users;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by Andre on 2015-01-21.
 */

public class UserGameID implements Serializable{
    private String gameName;
    private String username;


}
