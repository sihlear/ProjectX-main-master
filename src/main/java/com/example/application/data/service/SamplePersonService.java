package com.example.application.data.service;

import com.example.application.data.entity.SamplePerson;
import com.example.application.views.cardlist.Feed;
import com.example.application.views.main.MainView;
import com.example.application.views.main.UserData;
import com.vaadin.componentfactory.model.Message;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import org.vaadin.stefan.fullcalendar.Entry;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class SamplePersonService extends CrudService<SamplePerson, Integer> {


    public static List<UserData> getUsersChat(String usr)
    {
        List<UserData> users = new ArrayList<>();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from mindworxdb.users where  not email = "+"'"+usr+"'" );

            //get all users online
            while (rs.next()) {

                UserData person = new UserData();

                person.setEmail(rs.getString(1));
                person.setLastName(!rs.getString(7).toString().equals(null)? rs.getString(7):"");
                person.setFirstName(!rs.getString(8).toString().equals(null)? rs.getString(8):"");

                //get profile picture !(post.getMedia()== null)?
                byte[] decodedBytes = !(rs.getBlob(13) == null)?
                        (rs.getBlob(13).getBytes(1, (int) rs.getBlob(13).length())):null;
                person.setProfile(decodedBytes);

                byte[] decodedBytes1 = !(rs.getBlob(14) == null)?
                        (rs.getBlob(14).getBytes(1, (int) rs.getBlob(14).length())):null;
                person.setCv(decodedBytes1);

                person.setSkills(rs.getString(15));
                users.add(person);

            }

            con.close();

        } catch (SQLException throwables) {
            Notification.show("Exception is :" + throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static List<UserData> getUsers(String name)
    {
        List<UserData> users = new ArrayList<>();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from mindworxdb.users where NameOfUser like "+"'"+name+"%'" );

            //get all users online
            while (rs.next()) {

                UserData person = new UserData();

                person.setEmail(rs.getString(1));
                person.setLastName(!rs.getString(7).toString().equals(null)? rs.getString(7):"");
                person.setFirstName(!rs.getString(8).toString().equals(null)? rs.getString(8):"");

                //get profile picture !(post.getMedia()== null)?
                byte[] decodedBytes = !(rs.getBlob(13) == null)?
                        (rs.getBlob(13).getBytes(1, (int) rs.getBlob(13).length())):null;
                person.setProfile(decodedBytes);

                byte[] decodedBytes1 = !(rs.getBlob(14) == null)?
                        (rs.getBlob(14).getBytes(1, (int) rs.getBlob(14).length())):null;
                person.setCv(decodedBytes1);

                person.setSkills(rs.getString(15).toString());
                users.add(person);

            }

            con.close();

        } catch (SQLException throwables) {
            Notification.show("Exception is :" + throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static List<UserData> getEligibleUsers(String skillname)
    {
        List<UserData> users = new ArrayList<>();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from mindworxdb.users where Skills Like "+"'%"+skillname+"%'" );

            //get all users online
            while (rs.next()) {

                UserData person = new UserData();

                person.setEmail(rs.getString(1));
                person.setLastName(!rs.getString(7).toString().equals(null)? rs.getString(7):"");
                person.setFirstName(!rs.getString(8).toString().equals(null)? rs.getString(8):"");

                //get profile picture !(post.getMedia()== null)?
                byte[] decodedBytes = !(rs.getBlob(13) == null)?
                        (rs.getBlob(13).getBytes(1, (int) rs.getBlob(13).length())):null;
                person.setProfile(decodedBytes);

                byte[] decodedBytes1 = !(rs.getBlob(14) == null)?
                        (rs.getBlob(14).getBytes(1, (int) rs.getBlob(14).length())):null;
                person.setCv(decodedBytes1);

                person.setSkills(rs.getString(15).toString());
                users.add(person);

            }

            con.close();

        } catch (SQLException throwables) {
            Notification.show("Exception is :" + throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void Like(MainView.Post post) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

            String query = " update  mindworxdb.post set likes = ? where post_id = "+post.getId();

            PreparedStatement preparedStmt = con.prepareStatement(query);

            preparedStmt.setInt(1,post.getLikes());

            preparedStmt.execute();
            con.close();

            Notification.show("Post liked Successful",5999, Notification.Position.MIDDLE);

        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("Class not found exception");
        }


    }

    public static void isOnline(String email) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

            String query = "update  mindworxdb.users set isOffline = ? where email = '"+email+"'";

            PreparedStatement preparedStmt = con.prepareStatement(query);

            preparedStmt.setString(1,"Yes");

            preparedStmt.execute();
            con.close();

        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("Class not found exception");
        }


    }


    public static void Comment(String text, String name, int id) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

            String query = " insert into mindworxdb.comment (CommentText,post_id,nameOfUser)"
                    + " values (?,?, ?)";

            PreparedStatement preparedStmt = con.prepareStatement(query);

            preparedStmt.setString(1,text);
            preparedStmt.setString(3,name);
            preparedStmt.setInt(2,id);


            preparedStmt.execute();
            con.close();

        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());// this should help us with the error
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("error "+e.getMessage());
        }

    }

    public static List<Feed.UserComment> GetComments(int id) {

        ArrayList<Feed.UserComment> comments = new ArrayList<>();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from mindworxdb.comment where post_id ="+id);

            //assign values to the person object to have data of the currently loggin user

            while (rs.next()) {

                Feed.UserComment com = new Feed.UserComment();
                // here we're getting the post's date
                com.setUsername(rs.getString(4));
                com.setCommentText(rs.getString(2));
                comments.add(com);

                // here we're getting the picture posted if it's not null

            }

            con.close();

        } catch (SQLException throwables) {
            Notification.show("Exception is :" + throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return comments;

    }

    public static void sharePost(MainView.Post post, UserData user) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

            String query = " insert into mindworxdb.post (postPic, datePosted, userPosted,postContent,nameOfUser,picOfUser)"
                    + " values (?,?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = con.prepareStatement(query);

            preparedStmt.setBlob (1,!(post.getMedia()== null)?new SerialBlob(post.getMedia()):null);
            preparedStmt.setString(2, post.getDate());
            preparedStmt.setString   (3, user.getEmail());
            preparedStmt.setString(4, post.getPostContent());
            preparedStmt.setString(5, user.getFirstName()+" "+user.getLastName());

            preparedStmt.setBlob(6 ,new SerialBlob(user.getProfile()));

            preparedStmt.execute();
            con.close();
            share(post);
            getAllPost();

            Notification.show("Post Shared successfully");
        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());// this should help us with the error
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("error "+e.getMessage());
        }
    }

    private static void share(MainView.Post post) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

            String query = " update  mindworxdb.post set shares = ? where post_id = "+post.getId();

            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1,post.getShares());

            preparedStmt.execute();
            con.close();

            Notification.show("Post liked Successful",5999, Notification.Position.MIDDLE);




        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("Class not found exception");
        }

    }

    public static List<MainView.Post> getAllPost() {
        ArrayList<MainView.Post> Posts = new ArrayList<MainView.Post>();

        try {



            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from mindworxdb.post ORDER BY post_id DESC");

//asign values to the person object to have data of the currently loggin user

            while (rs.next()) {

                MainView.Post post = new MainView.Post();
                // here we're getting the post's date
                post.setId(rs.getInt(1));
                post.setDate(rs.getString(2));

                post.setUsername(rs.getString(3));// User who posted the current post
                post.setPostContent(rs.getString(4));//content of the post (text part)
                post.setName(rs.getString(7));

                // here we're getting the picture posted if it's not null

                if (!(rs.getBlob(5) == (null))) {
                    byte[] decodedBytes = rs.getBlob(5).getBytes(1, (int) rs.getBlob(5).length());

                    post.setMedia(decodedBytes);
                }


                post.setJsonComments(rs.getString(6));
                //user posted profile pic

                if (!(rs.getBlob(8) == (null))) {
                    byte[] decodedBytes = rs.getBlob(8).getBytes(1, (int) rs.getBlob(8).length());

                    post.setImage(decodedBytes);
                }

                post.setLikes(rs.getInt(9));
                post.setNoOfComs(rs.getInt(10));
                post.setShares(rs.getInt(11));
                Posts.add(post);

            }

            con.close();

        } catch (SQLException throwables) {
            Notification.show("Exception is :" + throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Posts;

    }

    public static void deletePost(MainView.Post post) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

            String query = "delete from mindworxdb.post where post_id = "+post.getId();

            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.execute();
            con.close();

            Notification.show("Post deleted Successful",5999, Notification.Position.MIDDLE);

        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("Class not found exception");
        }
    }

    public static void sendMessage(String s1,String sender,String s) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

            String query = " insert into mindworxdb.message (Chat,message,messageDate,sender)"
                    + " values (?,?, ?,?)";

            PreparedStatement preparedStmt = con.prepareStatement(query);

            preparedStmt.setString(1,s1);
            preparedStmt.setString(3, LocalDateTime.now().toString());
            preparedStmt.setString(2,s);
            preparedStmt.setString(4,sender);


            preparedStmt.execute();
            con.close();

        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());// this should help us with the error
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("error "+e.getMessage());
        }

    }

    public static List<Message> loadMessages(String email, String email1,String name, String name2) {
        List<Message> messages = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";
            //creating connection

            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from mindworxdb.message  where Chat =" + "'" + email+email1 + "'"+" or Chat ="+ "'" + email1+email + "'");

           //assign values to the person object to have data of the currently login user

            while (rs.next()) {

                Message QA = new Message(rs.getString(3),(rs.getString(6).equals(email))?name:name2,
                        (rs.getString(6) .equals(email) )?name:name2,
                        (rs.getString(6).equals(email))?true:false);

                messages.add(QA);

            }

            con.close();
            return messages;
        } catch (SQLException throwables) {
            Notification.show("Exception is :" + throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public static void passwordResetRequest(String username)  {



        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con = DriverManager.getConnection(url);

            // delete from mindworxdb.post where post_id =
            String query = " insert into mindworxdb.passwordreset (username)"
                    + " values (?)";

            PreparedStatement preparedStmt = con.prepareStatement(query);

            preparedStmt.setString(1,username);
            preparedStmt.execute();
            con.close();
        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());// this should help us with the error
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("error "+e.getMessage());
        }

    }

    public static List<String> getUsersRequest() {

        List<String> users = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from mindworxdb.passwordreset");

            while(rs.next()){

                users.add(rs.getString(2));

            }
            con.close();
        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());// this should help us with the error
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("error "+e.getMessage());
        }
        return users;
    }

    public static UserData getUser(String username) {

        UserData User = new UserData("user");
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

            Statement stmt=con.createStatement();
            ResultSet rs =stmt.executeQuery("select * from mindworxdb.users  where email ="+"'"+username+"'");

//asign values to the person object to have data of the currently loggin user

            while(rs.next()){


                User.setEmail(rs.getString(1));
                User.setPassword(rs.getString(2));

                // validate password and get user data

                User.setProgrammeY(rs.getInt(4));
                User.setProgName(!rs.getString(6).toString().equals(null)? rs.getString(6):"");
                User.setLastName(!rs.getString(7).toString().equals(null)? rs.getString(7):"");
                User.setFirstName(!rs.getString(8).toString().equals(null)? rs.getString(8):"");
                User.setAge(rs.getInt(9));



                User.setSkills(rs.getString(15).toString().equals(null)? rs.getString(15).toString():"");
                User.setContact(rs.getInt(16));
                User.setTown(rs.getString(17).toString().equals(null)? rs.getString(17).toString():"");

                VaadinSession.getCurrent().setAttribute( "User" , User );


                return User;



            }
            con.close();

        } catch (SQLException throwables) {
            Notification.show("Exception is :"+throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return User;

    }

    public static void updatePass(String username, String pass) {
    }


    public boolean CreatePost(MainView.Post post){


        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

           // delete from mindworxdb.post where post_id =
            String query = " insert into mindworxdb.post (postPic, datePosted, userPosted,postContent,nameOfUser,picOfUser)"
                    + " values (?,?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = con.prepareStatement(query);

            preparedStmt.setBlob (1,!(post.getMedia()== null)?new SerialBlob(post.getMedia()):null);
            preparedStmt.setString(2, post.getDate());
            preparedStmt.setString   (3, post.getUsername());
            preparedStmt.setString(4, post.getPostContent());
            preparedStmt.setString(5, post.getName());

            preparedStmt.setBlob(6 ,new SerialBlob(post.getImage()));

            preparedStmt.execute();
            con.close();


            return true;


        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());// this should help us with the error
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("error "+e.getMessage());
        }


        return false;
    }

    public static List<Entry> getEvents(){
        List<Entry> events = new ArrayList<>();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from mindworxdb.webinar" );

            //get all entries online
            while (rs.next()) {
//1346
                Entry event = new Entry();
                event.setTitle(rs.getString(1));
                event.setDescription(rs.getString(3));
                LocalDateTime dateTime = getDate(rs.getString(5));
                event.setStart(dateTime);
                event.setEnd(getDate(rs.getString(7)));

                List<String> myList = Arrays.asList("orange", "blue", "yellow", "pink");
                Random r = new Random();
                int randomitem = r.nextInt(myList.size());
                event.setColor(myList.get(randomitem));
                events.add(event);
            }

            con.close();

        } catch (SQLException throwables) {
            Notification.show("Exception is :" + throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return events;
    }

    static LocalDateTime getDate(String time){

        int year ,DOM ,hour,minute,mon;


        year = Integer.parseInt(time.substring(0,4));
        DOM = Integer.parseInt(time.substring(8,10));
        hour = Integer.parseInt(time.substring(11,13));
        minute = Integer.parseInt(time.substring(14,16));
        mon = Integer.parseInt(time.substring(5,7));

        LocalDateTime localDateTime ;

        switch (mon){
            case 0 :
                localDateTime = LocalDateTime.of(year, Month.JANUARY, DOM, hour, minute);
            case 1 :
                localDateTime = LocalDateTime.of(year, Month.FEBRUARY, DOM, hour, minute);
            case 2 :
                localDateTime = LocalDateTime.of(year, Month.MARCH, DOM, hour, minute);
            case 3 :
                localDateTime = LocalDateTime.of(year, Month.APRIL, DOM, hour, minute);
            case 4 :
                localDateTime = LocalDateTime.of(year, Month.MAY, DOM, hour, minute);
            case 5 :
                localDateTime = LocalDateTime.of(year, Month.JUNE, DOM, hour, minute);
            case 6 :
                localDateTime = LocalDateTime.of(year, Month.JULY, DOM, hour, minute);
            case 7 :
                localDateTime = LocalDateTime.of(year, Month.AUGUST, DOM, hour, minute);
            case 8 :
                localDateTime = LocalDateTime.of(year, Month.SEPTEMBER, DOM, hour, minute);
            case 9 :
                localDateTime = LocalDateTime.of(year, Month.OCTOBER, DOM, hour, minute);
            case 10 :
                localDateTime = LocalDateTime.of(year, Month.NOVEMBER, DOM, hour, minute);
            case 11 :
                localDateTime = LocalDateTime.of(year, Month.DECEMBER, DOM, hour, minute);

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mon);
        }

        return localDateTime;

    }


    public boolean saveUser(String fname, String lname, String email, String gender, int age, String password,String secretQ, String secretAnswer) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);
            String query = " insert into users (NameOfUser, Surname, age, gender,email, UserPassword,secretQuestion,secretAnswer)"
                    + " values (?,?, ?, ?, ?, ? ,?,?)";

            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, fname);
            preparedStmt.setString(2,lname);
            preparedStmt.setInt(3, age);
            preparedStmt.setString   (4, gender);
            preparedStmt.setString(5, email);
            preparedStmt.setString    (6, password);
            preparedStmt.setString(7,secretQ);
            preparedStmt.setString(8,secretAnswer);



            preparedStmt.execute();
            con.close();

            Notification.show("Query Sent");

            return true;



        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;


    }

    public List<String> getSecretAnswer(String username) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";
            //creating connection

            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select secretQuestion,secretAnswer from mindworxdb.users  where email =" + "'" + username + "'");

//assign values to the person object to have data of the currently login user

            while (rs.next()) {

                List<String> QA = new ArrayList<>();
                QA.add(rs.getString(1));
                QA.add(rs.getString(2));
                return QA;


            }
            con.close();

        } catch (SQLException throwables) {
            Notification.show("Exception is :" + throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;


    }

    public static boolean resetPassword(String newPass, String username) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

            Statement stmt=con.createStatement();

            String query = "update mindworxdb.users set UserPassword = ? where email = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, newPass);
            preparedStmt.setString(2, username);


            preparedStmt.execute();
            con.close();

            Notification.show("Query Sent");

            return true;
            /*if(true){

                return true;


            }
            else{
                Notification.show("Password entered is "+password );
                return false;
            }*/


        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected JpaRepository<SamplePerson, Integer> getRepository() {
        return null;
    }

    public UserData getUser() {
        return new UserData();
    }

    public boolean ValidateUser(String username, String password){

        UserData LoggedInUser = new UserData("user");
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);

            Statement stmt=con.createStatement();
            ResultSet rs =stmt.executeQuery("select * from mindworxdb.users  where email ="+"'"+username+"'");

//asign values to the person object to have data of the currently loggin user

            while(rs.next()){


                LoggedInUser.setEmail(rs.getString(1));
                LoggedInUser.setPassword(rs.getString(2));

                // validate password and get user data

                if(LoggedInUser.getPassword().equals(password)){
                    LoggedInUser.setProgrammeY(rs.getInt(4));
                    LoggedInUser.setProgName(!rs.getString(6).toString().equals(null)? rs.getString(6):"");
                    LoggedInUser.setLastName(!rs.getString(7).toString().equals(null)? rs.getString(7):"");
                    LoggedInUser.setFirstName(!rs.getString(8).toString().equals(null)? rs.getString(8):"");
                    LoggedInUser.setAge(rs.getInt(9));

                    LoggedInUser.setGender(!rs.getString(10).toString().equals(null)? rs.getString(10).toString():"");
                    LoggedInUser.setSecurityQ(!rs.getString(11).toString().equals(null)? rs.getString(11).toString():"");
                    LoggedInUser.setAnswer(!rs.getString(12).toString().equals(null)? rs.getString(12).toString():"");


                    byte[] decodedBytes = rs.getBlob(13).getBytes(1,(int)rs.getBlob(13).length());
                    LoggedInUser.setProfile(decodedBytes);

                    LoggedInUser.setSkills(rs.getString(15).toString().equals(null)? rs.getString(15).toString():"");
                    LoggedInUser.setContact(rs.getInt(16));
                    LoggedInUser.setTown(rs.getString(17).toString().equals(null)? rs.getString(17).toString():"");

                    isOnline(LoggedInUser.getEmail());

                    VaadinSession.getCurrent().setAttribute( "LoggedInUser" , LoggedInUser ) ;
                      return true;


                }
                else{
                    Notification.show("Incorrect Password");
                    return false;
                }
            }
            con.close();

        } catch (SQLException throwables) {
            Notification.show("Exception is :"+throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return true;

    }

    public  boolean UpdateProfile(UserData user){

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection

            Connection con= DriverManager.getConnection(url);
            Notification.show("Query execution");

            String query = "update  mindworxdb.users set NameOfUser=?, Surname = ?, email=?,Contact=?,Skills=? ,Town=?,programmeName=?,programmeYear=?, profilePicture=?, CV=? ,Certificate =? where email = ?";

            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, user.getFirstName());
            preparedStmt.setString(2,user.getLastName());
            preparedStmt.setString(3, user.getEmail());
            preparedStmt.setInt(4, user.getContact());
            preparedStmt.setString(5, user.getSkills());
            preparedStmt.setString    (6, user.getTown());
            preparedStmt.setString(7, user.getProgName());
            preparedStmt.setInt(8,user.getProgrammeY());
            preparedStmt.setBlob(9,new SerialBlob(user.getProfile()));
            preparedStmt.setBlob(10,new SerialBlob(user.getCv()));
            preparedStmt.setString(11,user.getEmail());
            preparedStmt.setBlob(12,new SerialBlob(user.getCertificate()));


            preparedStmt.execute();
            con.close();

            Notification.show(" Profile Update Successful");

            return true;



        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("Class not found exception");
        }

        return false;


    }

    public void createEvent(String title,
              String hoster,
              String descr ,
              String startdate,
              String link,
              String enddateTime)
    {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mindworxdb?user=root&password=Root_Password&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true&failOverReadOnly=false";

            //creating connection
            Connection con= DriverManager.getConnection(url);
            //
            String query = "insert into mindworxdb.webinar (title, hoster, descr,startdate,link,enddateTime)"
                    + " values (?,?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = con.prepareStatement(query);

            preparedStmt.setString(1, title);
            preparedStmt.setString(2, hoster);
            preparedStmt.setString(3, descr);
            preparedStmt.setString(4, startdate);
            preparedStmt.setString(5, link);
            preparedStmt.setString(6, enddateTime);

            preparedStmt.execute();
            con.close();

        } catch (SQLException throwables) {
            Notification.show("Got an exception!");
            Notification.show(throwables.getMessage());// this should help us with the error
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Notification.show("error "+e.getMessage());
        }

    }
}


