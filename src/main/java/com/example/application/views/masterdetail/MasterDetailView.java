package com.example.application.views.masterdetail;

import com.cloudmersive.client.AnalyticsApi;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.cloudmersive.client.model.HateSpeechAnalysisRequest;
import com.cloudmersive.client.model.HateSpeechAnalysisResponse;
import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.cardlist.Feed;
import com.example.application.views.main.MainView;
import com.example.application.views.main.UserData;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Route(value = "Admin", layout = MainView.class)
@PageTitle("Admin")
public class MasterDetailView extends Div implements BeforeEnterObserver {


    private Grid<UserData> grid = new Grid<>(UserData.class, true);
    private final SamplePersonService samplePersonService;
    private final VerticalLayout routes = new VerticalLayout();
    private String selectedUser = "";

    VerticalLayout pageOnDisplay = new VerticalLayout();

    public MasterDetailView(@Autowired SamplePersonService samplePersonService) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.samplePersonService = samplePersonService;
        // Create UI
        HorizontalLayout container = new HorizontalLayout();
        VerticalLayout tabs = new VerticalLayout(); // this one will contain the navigation between views(options for the admin)
        // this will contain the actual view
        addRoutes();
        tabs.add(routes);
        tabs.getStyle().set("width","25%");
        container.add(tabs,pageOnDisplay);

        pageOnDisplay.removeAll();
        pageOnDisplay.add(createJobPost());


        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        //createGridLayout(splitLayout);
        //createEditorLayout(splitLayout);
        add(container);

    }

    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<>();
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

    private double containsHateSpeech(String txt) {

        ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure API key authorization: Apikey
        ApiKeyAuth Apikey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
        Apikey.setApiKey("25bf2682-5daf-42b2-bcc7-1e78409ea010");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //Apikey.setApiKeyPrefix("25bf2682-5daf-42b2-bcc7-1e78409ea010");
        AnalyticsApi apiInstance = new AnalyticsApi();
        HateSpeechAnalysisRequest input = new HateSpeechAnalysisRequest(); // HateSpeechAnalysisRequest | Input hate speech analysis request
        input.setTextToAnalyze(txt);

        try {
            HateSpeechAnalysisResponse result = apiInstance.analyticsHateSpeech(input);
            return result.getHateSpeechScoreResult();
        } catch (ApiException e) {
            Notification.show("Exception when calling AnalyticsApi#analyticsHateSpeech");
            e.printStackTrace();
        }

        return 0;

    }

    private void addRoutes() {

        Text JP = new Text("Job Post");
        Div top = new Div();
        top.addClickListener(event -> {

            top.getStyle().set("background","orange");
            top.getStyle().set("border-radius","5px");
            pageOnDisplay.removeAll();
            pageOnDisplay.add(createJobPost());
        });
        top.add(JP);
        top.getStyle().set("width","100%");


        Text  PR = new Text("Password Reset");
        Div top1 = new Div();
        top1.add(PR);
        top1.addClickListener(event -> {
            top1.getStyle().set("background","orange");
            top1.getStyle().set("border-radius","5px");
            pageOnDisplay.removeAll();
            pageOnDisplay.add(createPasswordResetLayOut());

        });


        top1.getStyle().set("width","100%");


        Text RA = new Text("Recruit Alumni");

        Div top2 = new Div();
        top2.add(RA);

        top2.getStyle().set("width","100%");

        top2.addClickListener(event -> {

            top2.getStyle().set("background","orange");
            top2.getStyle().set("border-radius","5px");
            pageOnDisplay.removeAll();
            pageOnDisplay.add(createSkillSearch());

        });

        Text AN = new Text("Feed Analysis");
        Div top3 = new Div();
        top3.add(AN);
        top3.getStyle().set("width","100%");
        top3.addClickListener(e->{

            top3.getStyle().set("background","orange");
            top3.getStyle().set("border-radius","5px");
            pageOnDisplay.removeAll();
            pageOnDisplay.add(createFeedAnalysis());
        });

        routes.add(top,top1,top2
                ,top3);

        routes.getStyle().set("height","20%");


    }

    private VerticalLayout createFeedAnalysis() {

        VerticalLayout layout = new VerticalLayout();

        SamplePersonService personService = new SamplePersonService();
        return createFeed((List)personService.getAllPost(),layout);
    }

    private VerticalLayout createFeed(List<MainView.Post> allPost, VerticalLayout feed) {
        for (MainView.Post post:
                allPost) {

            feed.add(createCard1(post));

        }
        return feed;
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

        if(containsHateSpeech(txt) > 0.1){
            postC.getStyle().set("background-color","red");
        }
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
        UserData person = (UserData) VaadinSession.getCurrent().getAttribute("LoggedInUser");
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

                        for(Feed.UserComment com :SamplePersonService.GetComments(post.getId())){
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

            if(person.getEmail().equals(post.getUsername()) || person.getIsAdmin().equalsIgnoreCase("yes")){

                Dialog popUp = new Dialog();
                popUp.open();
                popUp.add(new Text("Delete Post"),new Button("confirm",e->
                {
                    SamplePersonService.deletePost(post); popUp.close();
                    AfterNavigationEvent nav = null;
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

    private VerticalLayout createJobPost() {

        VerticalLayout jobPost = new VerticalLayout();

        HorizontalLayout top = new HorizontalLayout();
        top.add(new TextField("Job Title "));

        HorizontalLayout middle = new HorizontalLayout();
        middle.add(new TextArea("Description"));

        HorizontalLayout bottom = new HorizontalLayout();
        bottom.add(new TextArea("Skills"));

        jobPost.add(top,middle,bottom, new Button("Post",e->{
            Notification.show("Job posted",500, Notification.Position.MIDDLE);
        }));

        jobPost.setClassName("card");
        jobPost.getStyle().set("border","2px solid #ff8c44");
        jobPost.getStyle().set("border-radius","10px");
        return  jobPost;
    }

    private VerticalLayout createPasswordResetLayOut() {


        VerticalLayout jobPost = new VerticalLayout();


        SplitLayout splitLayout = new SplitLayout();
        VerticalLayout left = new VerticalLayout();
        left.add(new Text("User requests "));
        Grid<String> users = new Grid<>();
        users.setItems(SamplePersonService.getUsersRequest());

        users.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        users.addComponentColumn(string -> createDiv(string));
        users.setEnabled(true);


        left.add(users);

        VerticalLayout right = new VerticalLayout();
        right.add(new Text("Reset"));

        splitLayout.addToPrimary(left);
        splitLayout.addToSecondary(right);

        right.add(new Button("reset",event -> {



            if(selectedUser == null){

                Notification.show("Please select the user to reset password for",1000,
                        Notification.Position.MIDDLE);
            }else{
                sendPass(selectedUser);
            }

        }));

        jobPost.add(splitLayout);
        splitLayout.getStyle().set("width","100%");
        jobPost.setClassName("card");
        jobPost.getStyle().set("border","2px solid #ff8c44");
        jobPost.getStyle().set("border-radius","10px");
        return  jobPost;
    }

    private VerticalLayout createSkillSearch(){
        VerticalLayout layout = new VerticalLayout();

        Div top = new Div();
        TextField search = new TextField("Skill");
        top.add(search);

        SamplePersonService service = new SamplePersonService();
        layout.add(top,new Button("Search",e ->{


            //Add users to the layout
            VerticalLayout list = new VerticalLayout();
            Grid<UserData> grid1 = new Grid<>();
            //grid1.setItems(service.getEligibleUsers(search.getValue()));
            grid.setHeight("100%");
            grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_NO_ROW_BORDERS);
            for (UserData user1: service.getEligibleUsers(search.getValue())) {
                Notification.show("User is -> "+user1.getFirstName()+" Skill Set is "+user1.getSkills(),1000, Notification.Position.MIDDLE);

                list.add(createCard(user1));
            }
            layout.add(list);
            layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        }));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setVisible(true);
        layout.getStyle().set("border","2px solid #ff8c44");
        layout.getStyle().set("border-radius","10px");

        return layout;
    }
    private HorizontalLayout createCard(UserData userData) {

        HorizontalLayout layout = new HorizontalLayout();
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");
        layout.getStyle().set("background","#56575b");
        layout.getStyle().set("width","100%");

        Image image = new Image();
        if((userData.getProfile() != null)){

            byte[] imageBytes = userData.getProfile();
            StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));

            image.setSrc(resource);
            image.setClassName("Avatar");
        }

        image.getStyle().set("height","50%");
        image.getStyle().set("border-radius","50%");
        Span name = new Span(userData.getFirstName()+" "+userData.getLastName());
        Span isOnline = new Span();

        card.add(image,name,isOnline);

        card.setVisible(true);

        HorizontalLayout buttons = new HorizontalLayout();

        Button viewProfile = new Button("View Profile",event -> {
            VaadinSession.getCurrent().setAttribute( "User" , userData ) ;

            UI.getCurrent().navigate("user-profile");
        }
        );

        buttons.getStyle().set("padding-top","40%");
        buttons.add(viewProfile);
        layout.add(card,buttons);
        layout.setFlexGrow(1, card);
        layout.setVerticalComponentAlignment(FlexComponent.Alignment.END,buttons);


        layout.getStyle().set("border-radius","5px #FF7F00");
        layout.getStyle().set("padding","10%");
        layout.getStyle().set("height","20px");
        buttons.getStyle().set("padding","5%");
        viewProfile.getStyle().set("padding-bottom","5%");
        viewProfile.getStyle().set("border","2px solid #ffffff");

        layout.getStyle().set("border","2px solid #FF7F00");
        return layout;

    }
    public HorizontalLayout createCard1(MainView.Post post) {
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

        if( containsHateSpeech(txt) > 0.1){
            postC.getStyle().set("background-color","red");
        }
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
        UserData person = (UserData) VaadinSession.getCurrent().getAttribute("LoggedInUser");
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

                        for(Feed.UserComment com :SamplePersonService.GetComments(post.getId())){
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


    private Div createDiv(String name){

        Div div = new Div();
        div.add(name);
        div.getStyle().set("align-content","center");
        div.getStyle().set("border-radius","5px");

        div.addClickListener(event -> {
            selectedUser = name;
        });
        return div;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String selected = "";
        Optional<Integer> samplePersonId = event.getRouteParameters().getInteger(selected);
        if (samplePersonId.isPresent()) {
            Optional<SamplePerson> samplePersonFromBackend = samplePersonService.get(samplePersonId.get());
            if (samplePersonFromBackend.isPresent()) {
            } else {
                Notification.show(
                        String.format("The requested samplePerson was not found, ID = %d", samplePersonId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterDetailView.class);
            }
        }
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void sendPass(String username){

        UserData userData = SamplePersonService.getUser(username);

        String password = "Ton#john44";

        Twilio.init("AC84be4410c97275d8e715d4e2ba2f867c", "0faf4aec27de1711579e00da1eb11c72");

        String pass = shuffle(password);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+27"+userData.getContact()),
                        new com.twilio.type.PhoneNumber("+13192545806"),
                        "Hi " +userData.getFirstName()+
                                ".\n Your  temporary password is"+pass)
                .create();
        if(SamplePersonService.resetPassword(pass,username)){
            Notification.show("Pass word reset succesfully \n " +
                    "The user will be recieving the new pasword via sms",3000, Notification.Position.MIDDLE);
        }else {
            Notification.show("Password rest failled please try again");
        }

    }

    public String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }
}
