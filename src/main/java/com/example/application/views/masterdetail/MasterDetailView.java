package com.example.application.views.masterdetail;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.main.MainView;
import com.example.application.views.main.UserData;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@Route(value = "Admin", layout = MainView.class)
@PageTitle("Admin")
public class MasterDetailView extends Div implements BeforeEnterObserver {

    private final String SAMPLEPERSON_ID = "samplePersonID";

    private Grid<UserData> grid = new Grid<>(UserData.class, true);

    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextField phone;
    private TextField occupation;
    private Checkbox important;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<SamplePerson> binder;

    private SamplePerson samplePerson;

    private SamplePersonService samplePersonService;
    private VerticalLayout routes = new VerticalLayout();

    VerticalLayout pageOnDisplay = new VerticalLayout();
    public MasterDetailView(@Autowired SamplePersonService samplePersonService) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.samplePersonService = samplePersonService;
        // Create UI
        HorizontalLayout container = new HorizontalLayout();
        VerticalLayout tabs = new VerticalLayout(); // this one will contain the navigation between views(options for the admin)
        ;// this will contain the actual view
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

                    UI.getCurrent().navigate("user-profile");
                });
        card.setVisible(true);

        return card;

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

        Text AN = new Text("Analytics");
        Div top3 = new Div();
        top3.add(AN);
        top3.getStyle().set("width","100%");
        routes.add(top,top1,top2
                ,top3);

        routes.getStyle().set("height","20%");


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
        UserData selectedUser = null;

        SplitLayout splitLayout = new SplitLayout();
        VerticalLayout left = new VerticalLayout();
        left.add(new Text("User requests "));
        Grid<UserData> users = new Grid<>();
        left.add(users);

        VerticalLayout right = new VerticalLayout();
        right.add(new Text("Reset"));

        splitLayout.addToPrimary(left);
        splitLayout.addToSecondary(right);
        right.add(new Button("reset",event -> {

            if(selectedUser == null){

                Notification.show("Please select the user to reset password for",1000,
                        Notification.Position.MIDDLE);
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


        layout.getStyle().set("border-radius","1px #FF7F00");
        layout.getStyle().set("padding","10%");
        layout.getStyle().set("height","20px");
        buttons.getStyle().set("padding","5%");
        viewProfile.getStyle().set("padding-bottom","5%");
        viewProfile.getStyle().set("border","2px solid #ffffff");

        layout.getStyle().set("border","2px solid #FF7F00");
        return layout;

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> samplePersonId = event.getRouteParameters().getInteger(SAMPLEPERSON_ID);
        if (samplePersonId.isPresent()) {
            Optional<SamplePerson> samplePersonFromBackend = samplePersonService.get(samplePersonId.get());
            if (samplePersonFromBackend.isPresent()) {
                populateForm(samplePersonFromBackend.get());
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

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");
        phone = new TextField("Phone");

        occupation = new TextField("Occupation");
        important = new Checkbox("Important");
        important.getStyle().set("padding-top", "var(--lumo-space-m)");
        Component[] fields = new Component[]{firstName, lastName, email, phone , occupation, important};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(SamplePerson value) {
        this.samplePerson = value;
        binder.readBean(this.samplePerson);

    }
}
