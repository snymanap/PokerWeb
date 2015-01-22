/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import Users.Game;
import Users.User;
import Users.UserGame;
import cards.Hand;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import ninja.Result;
import ninja.Results;

import ninja.session.Session;

import com.google.inject.Singleton;
import org.eclipse.jetty.security.LoginService;
import services.MultiplayerService;
import services.PokerService;
import services.RegisterService;
import services.loginService;

import java.lang.Object;
import java.util.Date;
import java.util.List;
import javax.swing.Popup;

import ninja.Context;

import javax.swing.*;


@Singleton
public class ApplicationController {

    @Inject private PokerService pokerService;
   @Inject private RegisterService registerService;
    @Inject private loginService _loginService;
    @Inject private Session session;
    @Inject private MultiplayerService multiplayerService;



    public Result multiplayer(){
        Result result = Results.html();

        /*Game game = new Game();
        game.setGameDate(new Date());
        game.setGameName("lekkergame");

        User user = new User();
        user.setPassword("123");
        user.setUsername("Arno");
        pokerService.createDeck();

        UserGame userGame = new UserGame();
        userGame.setUsername(user.getUsername());
        userGame.setGameName(game.getGameName());
        userGame.setHand("3♠,4♠,5♠,6♠,7♠");

        user.addGame(game);
        userGame.setUser(user);
        game.addUser(user);
        userGame.setGame(game);

        multiplayerService.gameStore(game);

        multiplayerService.usergameStore(userGame);
        List<User> users = registerService.getAllUsers();*/



        pokerService.createDeck();
        result.render("handDeal1", pokerService.test());
        result.render("handDeal2", pokerService.test());
        result.render("handDeal3", pokerService.test());
        result.render("handDeal4", pokerService.test());
        result.render("handDeal5", pokerService.test());
        /*String u1 = "";
        String u2 = "";
        String u3 = "";
        String u4 = "";
        String u5 = "";
        result.render("user1", users.get(0));
        result.render("user2", users.get(1));
        result.render("user3", users.get(2));
        result.render("user4", users.get(3));
        result.render("user5", users.get(4));*/
        result.render("winning", pokerService.evalHands());
        return result;
    }

    public Result history(){
        return Results.html();
    }

    public Result index(Context context) {

        Result result = Results.html();
        String names = "";
        //boolean hello = registerService.userGet();
        if (context.getParameter("usernameReg") == null && context.getParameter("passwordReg") == null)
        {
            String name = context.getParameter("username");
            String pass = context.getParameter("password");
            if (!registerService.userGet(name)) {
                result = Results.redirect("/register");
                System.out.println("already");
                return result;
            }
            names = name;
            //session.put("username", names);
            context.getSession().put("username", names);

        }
        else {
            String name = context.getParameter("usernameReg");
            String pass = context.getParameter("passwordReg");
            User u = new User(name,pass);
            if (!registerService.userStore(u))
            {
                result = Results.redirect("/register");
                System.out.println("No user found");
                return result;
            }
            names = name;

            result = Results.redirect("/register");
            return result;
        }
        session = context.getSession();
        c = context;
        logged = names;

        pokerService.createDeck();
        List<Hand> hand = pokerService.getHandList();



        result.render("register", "Hello " + context.getSession().get("username"));
        result.render("name", pokerService.test());

        result.render("card1", pokerService.convert(pokerService.getHand().get(0)));
        result.render("card2", pokerService.convert(pokerService.getHand().get(1)));
        result.render("card3", pokerService.convert(pokerService.getHand().get(2)));
        result.render("card4", pokerService.convert(pokerService.getHand().get(3)));
        result.render("card5", pokerService.convert(pokerService.getHand().get(4)));
        return result;

    }



    private String logged = "";
    Context c = null;
    public Result login()
    {
        Result result = Results.html();
        /*if (c != null && c.getSession().get("username") != null
                && logged.
                compareTo("") != 0
                && c.getSession().
                get("username").
                compareTo(logged) ==0) {
            System.out.println("HEKEKEKEKEK");
            return result.redirect("/index");
        }*/

        //result.render("login", loginService.output());
        return result;
    }

    public Result register()
    {
        registerService.getAllUsers();
        Result result = Results.html();
        result = login();
        return result;
    }

    public Result logout(Context context)
    {
        Result result = Results.redirect("/login");
        c.getSession().clear();
        session = null;
        context.getSession().clear();
        return result;
    }
    
    public static class SimplePojo {

        public String content;
        
    }
}
