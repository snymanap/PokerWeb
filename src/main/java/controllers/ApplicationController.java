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
import cards.Card;
import cards.Hand;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import ninja.Result;
import ninja.Results;

import ninja.params.PathParam;
import ninja.session.Session;

import com.google.inject.Singleton;
import org.eclipse.jetty.security.LoginService;
import org.jsoup.Jsoup;
import services.MultiplayerService;
import services.PokerService;
import services.RegisterService;
import services.loginService;

import java.lang.Object;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.Popup;

import ninja.Context;

import javax.swing.text.Document;
import javax.swing.text.html.*;

import javax.swing.*;
import javax.swing.text.html.HTML;


@Singleton
public class ApplicationController {

    @Inject private PokerService pokerService;
   @Inject private RegisterService registerService;
    @Inject private loginService _loginService;
    @Inject private Session session;
    @Inject private MultiplayerService multiplayerService;


    public Result lobby(@PathParam("game") String id){
        List<UserGame> userGames = multiplayerService.getAllUserGames();
        List<Game> gameList = multiplayerService.getAllGames();
        String out = "";
        String yes = "";
        out += "<table>";
        for (Game game : gameList) {
            for (UserGame u : userGames) {
                if (u.getGameName().compareTo(id) == 0 && game.getGameName().compareTo(u.getGameName()) == 0) {
                    out += "<h2>";
                    out += u.getGameName();
                    out += "</h2><br>";
                    out += "<tr><td>";
                    out += u.getUsername();
                    out += "</td></tr>";
                }
            }
        }
        out += "</table>";
        out += "<br><a href = '/start/";
        out += id;
        out += "'>Deal</a>";
        Result result = Results.html();
        result.render("output", out);
        return result;
    }


    public Result hostGame(Context context){
        List<User> userList = registerService.getUsersByName(context.getSession().get("username"));

        Game game = new Game();
        game.setActive(true);
        game.setGameName(context.getParameter("gamename"));
        game.setHost(context.getSession().get("username"));
        game.setGameDate(new Date());
        multiplayerService.gameStore(game);

        UserGame userGame = new UserGame();
        userGame.setHand(pokerService.dealHand().toString());
        userGame.setUsername(game.getHost());
        userGame.setUser(userList.get(0));
        userGame.setGameName(game.getGameName());
        userGame.setGame(game);
        multiplayerService.usergameStore(userGame);
        String out = "/currentGames/";
        out += context.getSession().get("username");
        //out += context.getParameter("username");
        return Results.redirect(out);
    }


    public Result startGame(@PathParam("game") String id){
        List<UserGame> userGames = multiplayerService.getAllUserGames();
        //System.out.println("LEWE1");
        for (UserGame u : userGames){
            if (u.getGameName().compareTo(id) == 0){
                u.getGame().setActive(false);
                multiplayerService.gameUpdate2(u.getGame());
                //System.out.println("LEWE2");
            }
        }
        String out = "/game/";
        out += id;
        return Results.redirect(out);
    }


    public Result currentGames(@PathParam("user") String id){
        Result result = Results.html();
        String g = "";
        String out = "";
        out += "<h2>Games waiting</h2>";
        List<UserGame> userGames = new ArrayList<>();
        List<Game> activeGames = multiplayerService.getActiveGames();
        List<UserGame> p = multiplayerService.getAllUserGames();
        for (UserGame u : p){
            for (Game g2 : activeGames){
                if (u.getGameName().compareTo(g2.getGameName()) == 0){
                    userGames.add(u);
                }
            }
        }

        List<UserGame> userGamesList = multiplayerService.getUserGamesByUsername(id);
        for (UserGame userGame1 : userGamesList){
            String pie = "";
            if (userGame1.getGame().getActive() == true)
            {
                pie = userGame1.getGameName();
                out += "<h1>";
                out += pie;
                out += "</h1><br><table>";
            }

            for (UserGame userGame : userGames){

                if (userGame.getGameName().compareTo(userGame1.getGameName()) == 0 && userGame.getGame().getActive() == true){

                    out += "<tr><td>";
                    out += userGame.getUsername();
                    out += "</td></tr>";

                }
            }
            if (userGame1.getGame().getActive() == true) out += "<tr><td>Waiting for players to join</td></tr></table>";
            if (userGame1.getGame().getHost().compareTo(id) == 0 && userGame1.getGame().getActive() == true){
                out += "<br><a href='/lobby/";
                out += userGame1.getGameName();
                /*out += "/";
                out += userGame1.getUsername();*/
                out += "'>Go to lobby</a>";
            }

        }



        List<UserGame> distinctU = multiplayerService.getAllUserGames();
        List<UserGame> currentGames = multiplayerService.getUserGamesByUsername(id);
        List<Game> gameList = multiplayerService.getAllGames();
        String out2 = "";
        out2 += "<h2>Join games</h2> <table>";

            for (UserGame current : distinctU) {
                for (Game userGame : gameList) {
                    if (current.getGameName().compareTo(userGame.getGameName()) == 0 && current.getUsername().compareTo(id) != 0) {
                        out2 += "<tr><td><a href='/joinGame/";

                        out2 += userGame.getGameName();
                        out2 += "/";
                        out2 += id;

                        out2 += "'>Join game</a></td><td>";
                        out2 += userGame.getGameName();
                        out2 += "</td></tr>";
                    }
                }
            }


        out2 += "</table>";




        result.render("output", out);
        result.render("joingames", out2);
        return result;



    }

    public Result joinGame(@PathParam("game") String id, @PathParam("user") String id2){
        List<User> userList = registerService.getUsersByName(id2);
        UserGame userGame = new UserGame();
        List<UserGame> gameList = multiplayerService.getUserGamesByGameName(id);
        Game game = gameList.get(0).getGame();
        userGame.setGame(game);
        userGame.setGameName(game.getGameName());
        userGame.setUser(userList.get(0));
        userGame.setHand(pokerService.dealHand().toString());
        userGame.setUsername(userList.get(0).getUsername());
        multiplayerService.usergameStore(userGame);
        String res = "/lobby/";
        res += id;

        return Results.redirect(res);
    }



    public Result multiplayer(Context context){
        Result result = Results.html();

        /*Game game = new Game();
        game.setGameDate(new Date());
        game.setGameName("lekkergame");
        game.setActive(true);
        game.setHost("Arno");

        List<User> users = registerService.getAllUsers();

        multiplayerService.gameStore(game);
        pokerService.createDeck();

        User user = new User();
        user.setPassword("123");
        user.setUsername("Arno");




            UserGame userGame = new UserGame();
            userGame.setUsername(user.getUsername());
            userGame.setGameName(game.getGameName());
            userGame.setHand(pokerService.dealHand().toString());

            user.addGame(game);
            userGame.setUser(user);
            game.addUser(user);
            userGame.setGame(game);

        multiplayerService.usergameStore(userGame);



        User user2 = new User();
        user2.setPassword("123");
        user2.setUsername("Chris");

        UserGame userGame2 = new UserGame();
        userGame2.setUsername(user2.getUsername());
        userGame2.setGameName(game.getGameName());
        userGame2.setHand(pokerService.dealHand().toString());

        user2.addGame(game);
        userGame2.setUser(user2);
        game.addUser(user2);
        userGame2.setGame(game);


        multiplayerService.usergameStore(userGame2);

        user2 = new User();
        user2.setPassword("123");
        user2.setUsername("Andre");

        userGame2 = new UserGame();
        userGame2.setUsername(user2.getUsername());
        userGame2.setGameName(game.getGameName());
        userGame2.setHand(pokerService.dealHand().toString());

        user2.addGame(game);
        userGame2.setUser(user2);
        game.addUser(user2);
        userGame2.setGame(game);


        multiplayerService.usergameStore(userGame2);

        user2 = new User();
        user2.setPassword("123");
        user2.setUsername("Dihan");

        userGame2 = new UserGame();
        userGame2.setUsername(user2.getUsername());
        userGame2.setGameName(game.getGameName());
        userGame2.setHand(pokerService.dealHand().toString());

        user2.addGame(game);
        userGame2.setUser(user2);
        game.addUser(user2);
        userGame2.setGame(game);


        multiplayerService.usergameStore(userGame2);

        user2 = new User();
        user2.setPassword("123");
        user2.setUsername("Stefan");

        userGame2 = new UserGame();
        userGame2.setUsername(user2.getUsername());
        userGame2.setGameName(game.getGameName());
        userGame2.setHand(pokerService.dealHand().toString());

        user2.addGame(game);
        userGame2.setUser(user2);
        game.addUser(user2);
        userGame2.setGame(game);


        multiplayerService.usergameStore(userGame2);





        Game game2 = new Game();
        game2.setGameName("weg");
        game2.setGameDate(new Date());
        game2.setActive(true);
        game2.setHost("Hardu");
        multiplayerService.gameStore(game2);

        user2 = new User();
        user2.setPassword("123");
        user2.setUsername("Hardu");

        userGame2 = new UserGame();
        userGame2.setUsername(user2.getUsername());
        userGame2.setGameName(game2.getGameName());
        userGame2.setHand(pokerService.dealHand().toString());

        user2.addGame(game2);
        userGame2.setUser(user2);
        game2.addUser(user2);
        userGame2.setGame(game2);


        multiplayerService.usergameStore(userGame2);


        user2.setUsername("Arno");
        user2.setPassword("123");
        userGame2 = new UserGame();
        userGame2.setUsername(user2.getUsername());
        userGame2.setGameName(game2.getGameName());
        userGame2.setHand(pokerService.dealHand().toString());

        user2.addGame(game2);
        userGame2.setUser(user2);
        game2.addUser(user2);
        userGame2.setGame(game2);


        multiplayerService.usergameStore(userGame2);*/









        //List<UserGame> ugames = multiplayerService.getAllUserGames();

        List<UserGame> ugames = multiplayerService.getUserGamesByUsername(context.getParameter("username"));
        String o = "";






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



    public Result gameResult(@PathParam("gameName") String id){
        Result result = Results.html();
        result.render("output", "lewe");
        result.render("game", "game not found");

        List<UserGame> u = multiplayerService.getAllUserGames();
        UserGame pie = null;
        boolean found = false;
        for (UserGame userGame : u)
        {
            if (userGame.getGameName().compareTo(id) == 0)
                pie = userGame;

        }

        if (pie == null)
            return result;
        result = Results.html();
        String out = "";
        String c = "";
        List<UserGame> hello = multiplayerService.getUserGamesByGameName(id);
        //System.out.println("HIIIIIIIIIIIIIIIIIIIIII " + hello.get(0).getHand());
        for (UserGame userGame : hello){
            String uig = userGame.getHand();
            uig = uig.replace("(","");
            uig = uig.replace(")","");
            String[] ar = uig.split(",");
            out += "<tr><td>";
            out += userGame.getUsername();
            out += "</td><td>";
            c = pokerService.convert(new Card(ar[0]));
            out += "<img src='/assets/card-images/" + c + ".png' height = '100' width='50' id='c1' style = 'position:relative;'>";
            c = pokerService.convert(new Card(ar[1]));
            out += "<img src='/assets/card-images/" + c + ".png' height = '100' width='50' id='c1' style = 'position:relative;'>";
            c = pokerService.convert(new Card(ar[2]));
            out += "<img src='/assets/card-images/" + c + ".png' height = '100' width='50' id='c1' style = 'position:relative;'>";
            c = pokerService.convert(new Card(ar[3]));
            out += "<img src='/assets/card-images/" + c + ".png' height = '100' width='50' id='c1' style = 'position:relative;'>";
            c = pokerService.convert(new Card(ar[4]));
            out += "<img src='/assets/card-images/" + c + ".png' height = '100' width='50' id='c1' style = 'position:relative;'>";
            out += "</td><td>";

            out += pokerService.evaluate(new Hand(ar[0], ar[1], ar[2],ar[3], ar[4]));
            out += "</td></tr>";

        }

        result.render("output", out);
        result.render("game",id);
        return result;

    }



    public Result history(){
        List<Game> activeGames = multiplayerService.getInactiveGames();
        List<UserGame> us = multiplayerService.getAllUserGames();
        List<UserGame> u = new ArrayList<>();
        for (UserGame userGame : us){
            for (Game game : activeGames){
                if (userGame.getGameName().compareTo(game.getGameName()) == 0){
                    u.add(userGame);
                }
            }
        }

        //List<UserGame> u = multiplayerService.getDistinctAllUserGames();
        String out = "";
    //out += "<tr>";
        String c="";
        for (UserGame userGame : u)
        {
            String uig = userGame.getHand();
            uig = uig.replace("(","");
            uig = uig.replace(")","");
            String[] ar = uig.split(",");
            out += "<tr><td>";
            out += "<a href = '/game/";
            out += userGame.getGameName();
            out += "'>";
            out += userGame.getGameName();
            out += "</a>";
            out += "</td><td>";
            out += userGame.getUsername();
            c = pokerService.convert(new Card(ar[0]));
            out += "<img src='/assets/card-images/" + c + ".png' height = '100' width='50' id='c1' style = 'position:relative;'>";
            c = pokerService.convert(new Card(ar[1]));
            out += "<img src='/assets/card-images/" + c + ".png' height = '100' width='50' id='c1' style = 'position:relative;'>";
            c = pokerService.convert(new Card(ar[2]));
            out += "<img src='/assets/card-images/" + c + ".png' height = '100' width='50' id='c1' style = 'position:relative;'>";
            c = pokerService.convert(new Card(ar[3]));
            out += "<img src='/assets/card-images/" + c + ".png' height = '100' width='50' id='c1' style = 'position:relative;'>";
            c = pokerService.convert(new Card(ar[4]));
            out += "<img src='/assets/card-images/" + c + ".png' height = '100' width='50' id='c1' style = 'position:relative;'>";
            out += "</td><td>";
            out += userGame.getGame().getGameDate().toString();
            out += "</td></tr>";
        }
        Result result = Results.html();

        result.render("stuff", out);


        return result;
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


        String out = "<a href='/currentGames/";
        out += context.getSession().get("username");
        out += "' class = 'btn btn-success'>View current games</a>";
        result.render("output", out);


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
