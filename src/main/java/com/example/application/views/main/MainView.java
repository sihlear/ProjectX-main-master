package com.example.application.views.main;

import com.example.application.data.service.SamplePersonService;
import com.example.application.views.cardlist.Feed;
import com.example.application.views.helloworld.Login;
import com.example.application.views.masterdetail.MasterDetailView;
import com.example.application.views.personform.PersonFormView;
import com.vaadin.componentfactory.model.Message;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.icons.VaadinIcons;
import org.vaadin.addons.autocomplete.generator.SuggestionGenerator;
import org.vaadin.addons.searchbox.SearchBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "Mindworx Alumni", shortName = "Alumni App", enableInstallPrompt = false)
@Theme(themeFolder = "myapp")

public class MainView extends AppLayout {


    private static final Object MESSAGE_LOAD_NUMBER = 2;

    public static class  Post{



        private byte[] image;
        private String name;
        private String Username;
        private String postContent;
        private byte[] media;

        private String date;

        public int getLikes() {
            return this.likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }


        public void setShares(int shares) {
            this.shares = shares;
        }

        public int getNoOfComs() {
            return NoOfComs;
        }

        public void setNoOfComs(int noOfComs) {
            NoOfComs = noOfComs;
        }

        private int likes;
        private int shares;
        private int NoOfComs;


        public String getJsonComments() {
            return JsonComments;
        }
        public int getShares() {
            return shares;
        }
        public void setJsonComments(String jsonComments) {
            JsonComments = jsonComments;
        }

        private String JsonComments;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        private int Id;
        public Map<String, String> getComments() {
            return comments;
        }

        public void setComments(Map<String, String> comments) {
            this.comments = comments;
        }

        private Map<String,String> comments = new HashMap<>();
        public byte[] getImage() {
            return image;
        }

        public void setImage(byte[] image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return Username;
        }

        public void setUsername(String username) {
            Username = username;
        }

        public String getPostContent() {
            return postContent;
        }

        public void setPostContent(String postContent) {
            this.postContent = postContent;
        }

        public byte[] getMedia() {
            return media;
        }

        public void setMedia(byte[] media) {
            this.media = media;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }


        public Post(){
            likes= 0;
            NoOfComs = 0;
            shares = 0;


        }


        public void Liked(Post post) {
            likes +=1;
            SamplePersonService.Like(this);
        }

        public void shared(UserData user)
        {
            shares +=1;
            SamplePersonService.sharePost(this,user);
        }
    }

    private final Tabs menu;
    private H1 viewTitle;

    private  byte[] PostPic;
    public static UserData user = new UserData("User");

    private SamplePersonService personService = new SamplePersonService();

    VaadinSession vaadinSession;


    Dialog dialog = new Dialog();

    Dialog searchResults = new Dialog();
    Grid<UserData> grid = new Grid<>();
    //
    VerticalLayout post1 = new VerticalLayout();
    HorizontalLayout head = new HorizontalLayout();


    public MainView() {

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));


        TextArea TextContent = new TextArea("post");
        TextContent.setWidthFull();
        VerticalLayout vet = new VerticalLayout();

        post1.add(vet);

        HorizontalLayout footer = new HorizontalLayout();


        //upload profile picture
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        Div output = new Div();
        Div cancl = new Div();
        Div ps  = new Div();

        upload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png", "image/gif");
        upload.setUploadButton(new Button("image" ));
        upload.getStyle().set("padding-top","0");
        upload.addSucceededListener(event -> {
            String attachmentName = event.getFileName();

            try {
                // The image can be jpg png or gif, but we store it always as png file in this code
                BufferedImage inputImage = ImageIO.read(buffer.getInputStream(attachmentName));
                ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
                ImageIO.write(inputImage, "png", pngContent);
                PostPic = pngContent.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        upload.addFileRejectedListener(event -> {
            Paragraph component = new Paragraph();
            output.removeAll();
            //showOutput(event.getErrorMessage(), component, output);
        });
        upload.getElement().addEventListener("file-remove", event -> {
            output.removeAll();
        });

        cancl.add(new Button("Cancel",e-> {
            dialog.close();
        }));
        ps.add(new Button("Post",buttonClickEvent -> {

            if(!TextContent.getValue().trim().isEmpty()){

                Post postContent = new Post();

                postContent.setName(user.getFirstName()+" "+user.getLastName());
                postContent.postContent = TextContent.getValue();

                postContent.image = user.getProfile();

                postContent.setMedia(PostPic);
                postContent.Username = user.getEmail();
                postContent.setName(user.getFirstName()+" "+user.getLastName());
                postContent.date = LocalDate.now().toString();
                //if(PostPic != null )postContent.media = PostPic;

                if(personService.CreatePost(postContent)){
                    Notification.show("Posted Successful",1000, Notification.Position.TOP_CENTER);
                    dialog.close();
                };
            }
        }));

        footer.add(cancl,upload,ps);
        vet.add(TextContent,footer);


    }

    private void onCancel(ConfirmDialog.CancelEvent cancelEvent) {
        dialog.close();
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setClassName("sidemenu-header");
        layout.getThemeList().set("dark", true);
        layout.getStyle().set("background-color","#56575b");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        DrawerToggle toggle = new DrawerToggle();
        Icon tog = new Icon(VaadinIcon.LINES);tog.getStyle().set("color","#ff8c44");
        toggle.setIcon(tog);
        layout.add(toggle);
        viewTitle = new H1();
        layout.add(viewTitle);


        TextField searchBar = new TextField();
        searchBar.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchBar.setValueChangeMode(ValueChangeMode.TIMEOUT);
        searchBar.addValueChangeListener(textFieldStringComponentValueChangeEvent ->
        {
            UI.getCurrent().getPage().executeJs("return document.title").then(String.class,
                    pageTitle ->
                    {
                        if(pageTitle.equals("Login") || pageTitle.equals("Sign up") )
                        {

                        }
                        else {
                            UserData data = (UserData) VaadinSession.getCurrent().getAttribute("LoggedInUser");

                            grid = new Grid<>();
                            grid.setItems(suggestUsers(data.getEmail(),searchBar.getValue()));
                            grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_NO_ROW_BORDERS);
                            grid.setSizeFull();
                            grid.addComponentColumn(UserData -> createCard(UserData));
                            VerticalLayout Vt = new VerticalLayout();
                            //Do you guys ave any idea as what makes this thing not to show up?
                            searchResults.add(grid);
                            Vt.setVisible(true);
                            searchResults.setModal(true);
                            searchResults.setDraggable(true);
                            searchResults.open();
                            searchResults.setHeight("500px");
                            searchResults.setResizable(true);

                        }
                    } );
        });


        SearchBox sb = new SearchBox(VaadinIcons.SEARCH, SearchBox.ButtonPosition.RIGHT);
        sb.setSearchMode(SearchBox.SearchMode.DEBOUNCE);
        sb.setSuggestionGenerator(new SuggestionGenerator<UserData>() {
            @Override
            public List<UserData> apply(String s, Integer integer) {
                s = sb.getSearchField().getValue();
                List<UserData> uSERS = new ArrayList<>();
                uSERS = SamplePersonService.getUsers(s);
                return (List<UserData>)uSERS;
            }
        });

        //tabs
        Tab notif = new Tab();
        Icon nt = new Icon(VaadinIcon.BELL);
        nt.setColor("#ff8c44");
        Button b = new Button(nt,e->{});
        b.setClassName("Button");
        Text txt= new Text("notifications");
        notif.add(b,txt);
        notif.getStyle().set("color","#ff8c44");

        //We'll add a div or a layout instead of adding directly to the dialog

        Tab chat = new Tab();
        Icon ct = new Icon(VaadinIcon.CHAT);
        ct.setColor("#ff8c44");
        Button cht = new Button(ct,e->
        {
            UI.getCurrent().navigate("chat");
        }
        );
        chat.add(cht,new Text("chat"));
        chat.getStyle().set("color","#ff8c44");

        Tab settings = new Tab();
        Icon st = new Icon(VaadinIcon.SIGN_OUT);
        st.setColor("#ff8c44");

        settings.add(new Button(st,e->{

            VaadinSession.getCurrent().setAttribute( "LoggedInUser" , null ) ;
            UI.getCurrent().getSession().close();
            Notification.show("Logged out successfully");
            UI.getCurrent().navigate("login");
        }),new Text("sign out"));
        settings.getStyle().set("color","#ff8c44");

        Tab post = new Tab();
        Icon pst = new Icon(VaadinIcon.PLUS_SQUARE_O);
        pst.setColor("#ff8c44");
        post.add(new Button(pst,e->{

            UI.getCurrent().getPage().executeJs("return document.title").then(String.class,
                    pageTitle ->
                    {
                        if(pageTitle.equals("Login") || pageTitle.equals("Sign up") )
                        {

                        }
                        else{
                            user = (UserData) VaadinSession.getCurrent().getAttribute("LoggedInUser");

                        }
                    }
            );
            HorizontalLayout head = new HorizontalLayout();


            Image image = new Image();


            if((user.getProfile() != null)){

                byte[] imageBytes = user.getProfile();
                StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));

                image.setSrc(resource);
                image.getStyle().set("border-radius","50px");
                image.getStyle().set("width","100px");
                image.getStyle().set("height","100px");
                image.getStyle().set("padding-left","0");
                head.add(image);
                H4 h = new H4("Create Post");
                head.add(h);
                h.getStyle().set("padding-top","10%");
            }else {
                head.add(new Avatar());head.add(new H4("Create Post"));
            }
            dialog = new Dialog();
            dialog.add(head);
            head.getStyle().set("padding-left","20%");
            head.getStyle().set("padding-right","20%");

            dialog.add(post1);
            dialog.open();
        }),new Text("post"));

        post.getStyle().set("color","#ff8c44");

        Tab home = new Tab();
        Icon hm = new Icon(VaadinIcon.HOME_O);
        hm.setColor("#ff8c44");
        home.add(new Button(hm,
                e->{
            UI.getCurrent().getPage().executeJs("return document.title").then(String.class,
                        pageTitle -> {
                    if(pageTitle.equals("Login") || pageTitle.equals("Sign up") ){}
                    else{
                        UI.getCurrent().navigate("card-list");}
                    }
                );
        }),
                new Text("home"));
            //UI.getCurrent().navigate("card-list");}),new Text("home")
        home.getStyle().set("color","#ff8c44");

        Tabs tabs = new Tabs(home,chat,notif,post,settings);

        Icon us = new Icon(VaadinIcon.USER);
        us.setColor("#ff8c44");
        Button profile = new Button(us,buttonClickEvent -> {
            UI.getCurrent().navigate("profile-edit");
        });

        profile.addClassName("Avatar");


        //make avatar clickable
        layout.add(searchBar);
        layout.add(tabs);
        layout.add(profile,new Text("profile"));
        layout.getStyle().set("color","#ff8c44");

       /*if(LoggedInUser.getFirstName() == null ){

            layout.add(new H3("Please LogIn"));
        }else {
            layout.add(new H3("loggedIn user "+LoggedInUser.getFirstName()));
        }*/

        layout.setSpacing(true);
        return layout;
    }

    private List<Message> loadMessages() {
        return new ArrayList<>();
    }

    private HorizontalLayout createUserCard(UserData userData) {

        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        if((userData.getProfile() != null)){

            byte[] imageBytes = userData.getProfile();
            StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));

            image.setSrc(resource);
        }

        Span name = new Span(userData.getFirstName()+" "+userData.getLastName());
        Span isOnline = new Span();

        card.add(image,name,isOnline);

        card.addClickListener(

                searchEvent->
                {
                    VaadinSession.getCurrent().setAttribute( "User" , userData ) ;
                    searchResults.close();
                    UI.getCurrent().navigate("user-profile");
                });
        card.setVisible(true);

        return card;

    }

    private HorizontalLayout createCard(UserData userData) {

        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        if((userData.getProfile() != null)){

            byte[] imageBytes = userData.getProfile();
            StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));

            image.setSrc(resource);
            image.getStyle().set("border-radius","50px");
            image.getStyle().set("width","100px");
            image.getStyle().set("height","100px");
            image.getStyle().set("padding-left","0");
        }

        Span name = new Span(userData.getFirstName()+" "+userData.getLastName());
        Span isOnline = new Span();

        card.add(image,name,isOnline);

        card.setVisible(true);
        card.addClickListener(

                searchEvent->
                {
                    VaadinSession.getCurrent().setAttribute( "User" , userData ) ;
                    searchResults.close();
                    UI.getCurrent().navigate("user-profile");
                });
        card.setHeight("250px");

        return card;

    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setClassName("sidemenu-menu");
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/Mindw.png", "Mindworx"));
        logoLayout.add(new H1(""));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        return new Tab[]{createTab("Login", Login.class), createTab("Feed", Feed.class),
                createTab("Admin", MasterDetailView.class), createTab("SignUp", PersonFormView.class)};
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }


    // The suggestion generator method. Returns a list of users.
    private List<UserData> suggestUsers(String username,String text) {
        List<UserData> uSERS = new ArrayList<>();
            uSERS = SamplePersonService.getUsers(text);
        return (List<UserData>)uSERS;
    }

    // Converts a User object into a String to be set as the value of the search field.

    // Converts a User object into an HTML string to be displayed as suggestion item

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private void setItem() {
        getElement().executeJs("debugger; window.localStorage.setItem($0, $1);", "LoggedInUser", "Obaseki Nosa");
    }

    private void retrieveItem() {
        getElement().executeJs("debugger;return window.localStorage.getItem($0);", "LoggedInUser")
                .then(String.class, result -> Notification.show("name is "+ result));
    }
}
