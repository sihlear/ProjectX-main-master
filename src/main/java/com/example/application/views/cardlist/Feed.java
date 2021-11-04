package com.example.application.views.cardlist;

import com.example.application.data.service.SamplePersonService;
import com.example.application.views.main.MainView;
import com.example.application.views.main.UserData;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import jdk.jfr.Event;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Hate speech recognition API

@Route(value = "card-list", layout = MainView.class)
@PageTitle("Home")
public class Feed extends Div implements AfterNavigationObserver {


    public static class UserComment {

        private String Username;
        private String CommentText;

        public String getUsername() {
            return Username;
        }

        public void setUsername(String username) {
            Username = username;
        }

        public String getCommentText() {
            return CommentText;
        }

        public void setCommentText(String commentText) {
            CommentText = commentText;
        }


    }

    public Feed() {

        layout.getStyle().set("width","45%");
        person = (UserData) VaadinSession.getCurrent().getAttribute("LoggedInUser");
        addClassName("card-list-view");

        createFeed((List)personService.getAllPost());
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_NO_ROW_BORDERS);

        // we need to call the method on our service class that returns our
        // posts from the database, and find a way to add those posts to our UI
        grid.addComponentColumn(post -> createCard(post));
        FullCalendar fullCalendar = new FullCalendar();
        fullCalendar.getStyle().set("width","100%");
        fullCalendar.setWeekNumbersVisible(false);
        fullCalendar.addEntry(SamplePersonService.getEvents().get(2));
        fullCalendar.setHeightAuto();

        fullCalendar.setColumnHeader(true);


        fullCalendar.addTimeslotsSelectedListener((event) -> {
            // react on the selected timeslot, for instance create a new instance and let the user edit it
            Entry entry = new Entry();

            entry.setStart(fullCalendar.getTimezone().convertToUTC(event.getStartDateTime()));
            entry.setEnd(fullCalendar.getTimezone().convertToUTC(event.getEndDateTime()));
            entry.setAllDay(event.isAllDay());

            List<String> myList = Arrays.asList("orange", "blue", "yellow", "pink");
            Random r = new Random();
            int randomitem = r.nextInt(myList.size());
            entry.setColor(myList.get(randomitem));

            // ... show and editor
        });

        fullCalendar.addDayNumberClickedListener((event)->{


            Dialog ev = new Dialog();
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.add(new Text("Add Event"));

            //top
            HorizontalLayout top =  new HorizontalLayout();
            DateTimePicker start = new DateTimePicker("Start Date :");
            top.add(start);

            //top
            HorizontalLayout end =  new HorizontalLayout();
            DateTimePicker endDate = new DateTimePicker("End Date :");
            end.add(endDate);

            //top
            HorizontalLayout des =  new HorizontalLayout();
            Div description = new Div();
            TextField title = new TextField("Title");
            TextArea desc = new TextArea("Description");
            description.add(desc);
            des.add(description);
            verticalLayout.add(top,end,title,des);

            HorizontalLayout buttons = new HorizontalLayout();
            Button addEvent = new Button("Add", event1 -> {

                Entry entry = new Entry();

                entry.setStart(fullCalendar.getTimezone().convertToUTC(start.getValue()));
                entry.setEnd(fullCalendar.getTimezone().convertToUTC(endDate.getValue()));
                entry.setDescription(desc.getValue());
                entry.setTitle(title.getValue());
                //entry.setAllDay();

                shareEvent(title.getValue(),start.getValue(),desc.getValue(),endDate.getValue(),person);

                List<String> myList = Arrays.asList("orange", "blue", "yellow", "pink");
                Random r = new Random();
                int randomitem = r.nextInt(myList.size());
                entry.setColor(myList.get(randomitem));
                fullCalendar.addEntry(entry);
            });
            addEvent.getStyle().set("background-color","orange");

            Button CancelEvent = new Button("cancel",event1 -> {ev.close();});
            CancelEvent.getStyle().set("background-color","blue");
            buttons.add(addEvent,CancelEvent);
            verticalLayout.add(buttons);
            ev.add(verticalLayout);
            ev.open();

        });

        layout.add(fullCalendar);
        fullCalendar.setHeightAuto();
        fullCalendar.setSizeFull();

        List<Entry> getEvents;

        //load events created
        getEvents = SamplePersonService.getEvents();

        for (Entry ev:
             getEvents) {

            fullCalendar.addEntry(ev);

        }

        HL.add(feed,layout);
        add(HL);

    }

    public static UserData person = new UserData("User");

    //now we have to put all those posts we got from the database to the grid(feed) below
    //but before we do that we need to make sure that the post should have the like,share and click buttons
    Grid<MainView.Post> grid = new Grid<>();

    HorizontalLayout HL = new HorizontalLayout();
    SamplePersonService personService = new SamplePersonService();
    AfterNavigationEvent nav = null;
    VerticalLayout layout =  new VerticalLayout();
    VerticalLayout feed = new VerticalLayout();

    private void shareEvent(String titl,LocalDateTime start,String descr,LocalDateTime endDate,UserData user) {


        List<String> myList = Arrays.asList("https://meet.google.com/xcg-rboq-piy","https://meet.google.com/xba-hshs-zky","https://meet.google.com/cme-ujqd-vpk", "https://meet.google.com/njw-xwsv-kaj", "https://meet.google.com/syk-jtxw-trz", "https://meet.google.com/vra-vgvf-qfn");
        Random r = new Random();
        int randomitem = r.nextInt(myList.size());

        String link = myList.get(randomitem);

        String eventDetails = "Title : "+titl+" \n " +
                "description : "+descr+" "+
                "Date and time : "+start.toString();
        eventDetails.concat("\n here's the link"+link);

        personService.createEvent(titl, person.getEmail(), descr, start.toString(),link.toString(), endDate.toString());

        if(!eventDetails.isEmpty()){

            MainView.Post postContent = new MainView.Post();

            postContent.setName(user.getFirstName()+" "+user.getLastName());
            postContent.postContent = eventDetails;

            postContent.setImage(user.getProfile());

            postContent.setUsername(user.getEmail());
            postContent.setName(user.getFirstName()+" "+user.getLastName());
            postContent.setDate(LocalDate.now().toString());
            //if(PostPic != null )postContent.media = PostPic;

            if(personService.CreatePost(postContent)){

                Notification.show("Event set Successful",1000, Notification.Position.TOP_CENTER);

            };
        }
    }

    public HorizontalLayout createCard(MainView.Post post) {
        // this card represent each and every post's design
        // can do that here

        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        // this is the profile pic of the person who posted the post
        byte[] imageBytes = post.getImage();
        StreamResource resource = new StreamResource("dummyImageName.jpg",() -> new ByteArrayInputStream(imageBytes));
        Image image = new Image();
        image.setSrc(resource);

        //this is the text on our post
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);


        // this is the header that will contain the profile pic and the name of the user
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(post.getName().toUpperCase(Locale.ROOT));
        name.addClassName("name");

        //this should be the date of the post
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
        Span date = new Span(post.getDate());
        date.addClassName("date");
        header.add(name, date);

        //this is the text and check for links

        List<String> links  = new ArrayList<>();
        links.addAll(extractUrls(post.getPostContent()));
        String txt = post.getPostContent();
        Div div = new Div();
        Span postC = new Span(div);

        if(!links.isEmpty()){


            for (int i=0;i< links.size();i++){

                if(txt.contains(links.get(i))){
                    Anchor link = new Anchor(links.get(i),links.get(i));
                    link.getStyle().set("color","blue");

                    div.add(txt.substring(0,txt.indexOf(links.get(i))));
                    div.add(link);
                    div.add(txt.substring(txt.indexOf(links.get(i))).replace(links.get(i),""));
                }
            }
        }else{
            postC.add(txt);
        }
        postC.addClassName("post");

        /*if( containsHateSpeech(txt) > 0.1){
            postC.getStyle().set("background-color","red");
        }*/
        //picture content
        if(!(post.getMedia() == null))
        {
            byte[] imageBytes2 = post.getMedia();// your data source here
            StreamResource resource2 = new StreamResource("ImageName.jpg",
                    () -> new ByteArrayInputStream(imageBytes2));
            Image image2 = new Image(resource2, "dummy image");
            image2.setClassName("postPicture");
            Div postPiC = new Div();

            postPiC.add(image2);
            image2.setSizeFull();
            postC.add(postPiC);
        }

        // we need to add a section for the reactions (like ,comment,share) to our post
        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        //Reactions, so this part is the one that has the user reactions but for now if we click
        // it wont do anything ,so we need to add some e->{}(click events)
        //


        Icon likeIcon = VaadinIcon.HEART.create();// this is the like icon
        likeIcon.addClassName("icon");


        Span likes = new Span(String.valueOf(post.getLikes()));
        likeIcon.addClickListener(iconClickEvent -> {
            post.Liked(post);
            grid.getDataProvider().refreshAll();
        });
        likes.addClassName("likes");

        Icon commentIcon = VaadinIcon.COMMENT.create();//this is the comment
        commentIcon.addClickListener(iconClickEvent ->
                {
                    TextField comt = new TextField();
                    description.add(comt,new Button("comment",e->{

                        if(!comt.getValue().isEmpty())
                        {

                            SamplePersonService.Comment(comt.getValue(),person.getFirstName()+" "+person.getLastName(),post.getId());
                            Notification.show("Commented on the post");
                        }


                        Dialog dialog = new Dialog(new H3("comments"));
                        VerticalLayout comms = new VerticalLayout();

                        //Grid<UserComment> comgrid = new Grid<>();

                        for(UserComment com :SamplePersonService.GetComments(post.getId())){
                            HorizontalLayout row = new HorizontalLayout();
                            row.add(new Text(com.getUsername()+" : " +com.getCommentText()));
                            comms.add(row);

                        }
                        comms.add(new Button("Close comments",event->{
                            dialog.close();
                            grid.getDataProvider().refreshAll();
                        }));
                        dialog.add(comms);
                        dialog.open();
                        comt.clear();

                    }));


                }
        );
        commentIcon.addClassName("icon");
        Span comments = new Span(String.valueOf(SamplePersonService.GetComments(post.getId()).size()));
        comments.addClassName("comments");

        Icon shareIcon = VaadinIcon.CONNECT.create();//and this is the share icon

        shareIcon.addClassName("icon");
        Span shares = new Span(String.valueOf(post.getShares()));
        shares.addClassName("shares");
        shareIcon.addClickListener(iconClickEvent -> {
            post.shared(person);
            grid.getDataProvider().refreshAll();
        });

        Icon delete = VaadinIcon.ELLIPSIS_DOTS_H.create();
        delete.addClickListener(iconClickEvent ->

        {

            if(person.getEmail().equals(post.getUsername())){

                Dialog popUp = new Dialog();
                popUp.open();
                popUp.add(new Button("confirm",e->
                {
                    SamplePersonService.deletePost(post); popUp.close();
                    AfterNavigationEvent nav = null;
                    afterNavigation(nav);
                    grid.getDataProvider().refreshAll();
                }));

            }else {
                Notification.show("You can only delete your Post");
            }

        });

        actions.add(likeIcon, likes, commentIcon, comments, shareIcon, shares);
        if(person.getEmail().equals(post.getUsername())){
            actions.add(delete);
        }

        description.add(header, postC, actions);
        card.add( image, description);



        return card;
    }

    private void createFeed(List<MainView.Post> allPost) {
        for (MainView.Post post:
             allPost) {

            feed.add(createCard(post));

        }
    }

    private Component setSideUserSummary(){

        person =  (UserData)VaadinSession.getCurrent().getAttribute("LoggedInUser");
        VerticalLayout profl = new VerticalLayout();

        byte[] imageBytes = person.getProfile();
        StreamResource resource = new StreamResource("dummyImageName.jpg",() -> new ByteArrayInputStream(imageBytes));
        Image image = new Image();
        image.setSrc(resource);

        profl.setClassName("profile");
        HorizontalLayout top = new HorizontalLayout();
        Span pp = new Span(image);
        H2 title= new H2((person != null &&person.getFirstName() != null)?"Mr "+person.getLastName():"Username") ;
        top.add(pp,title);
        TextArea summary = new TextArea("Summary") ;
        profl.add(top,summary);
        HL.add(profl);

        return profl;
    }

    private static class meeting extends Event implements EventListener {

        protected String  title;
        protected String  description;
        protected String  link;
        protected String startTime;
        protected String endtime;
        private  String hoster;

    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {

       // grid.setItems((List)personService.getAllPost());
    }

    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

}
