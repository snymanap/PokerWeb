package services;

import Repositories.UserRepository;
import Users.User;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import controllers.ApplicationController;
import ninja.Result;
import ninja.Results;
import ninja.Router;
import ninja.jpa.UnitOfWork;

import javax.persistence.EntityManager;
import javax.persistence.Query;
//import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Andre on 2015-01-16.
 */

@Singleton
public class RegisterService {
    @Inject
    UserRepository userRepository;

    public static String output() {
        return "Register to poker service";

    }

    public static boolean createUser(String name, String pass) {
        User u = new User(name, pass);
        for (User user : userList) {
            if (user.getUsername().compareTo(name) == 0)
                return false;

        }

        userList.add(u);
        return true;
    }

    public static List<User> getUsers() {
        return userList;
    }

    private static List<User> userList = new ArrayList<>();

    public boolean registerUser(User user) {
        return userRepository.userStore(user);
    }

    public void updateUser(User user) {
        userRepository.merge(user);
    }

    public boolean userExists(String username)
    {
        return  userRepository.userGet(username);
    }

    public Optional<User> getUserByName(String username)
    {
       return userRepository.getUserByName(username);


    }
}
