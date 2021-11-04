package com.example.application.views.profile;

import com.example.application.views.main.MainView;
import com.example.application.views.main.UserData;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import java.io.ByteArrayInputStream;

@Route(value = "user-profile", layout = MainView.class)
@PageTitle("Profile")

public class UserProfile extends VerticalLayout {


    VerticalLayout body = new VerticalLayout();
    UserData user;
    UserData data = (UserData) VaadinSession.getCurrent().getAttribute("LoggedInUser");
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email address");
    private IntegerField programeYear = new IntegerField("Programme Year");
    private Text gender = new Text("Gender");
    private Text race = new Text("Race");
    private TextField progName = new TextField("Programme Name");
    private Button updateProfile;
    private  TextField town = new TextField("Town");
    private  IntegerField phoneNumber = new IntegerField("Contact Number");
    //skills
    private TextArea Skillz = new TextArea("List your Skills");
    //Sections
    private HorizontalLayout personalInfo = new HorizontalLayout();
    private VerticalLayout skills = new VerticalLayout();



    public UserProfile(){
        AutoFill();
        for (Component component:
             body.getChildren().toList()) {
            component.onEnabledStateChanged(true);

        }
        user = (UserData)VaadinSession.getCurrent().getAttribute("User");

        Div picture = new Div();
        byte[] imageBytes = user.getProfile();
        StreamResource resource = new StreamResource("dummyImageName.jpg",() -> new ByteArrayInputStream(imageBytes));
        Image image = new Image();
        image.setSrc(resource);
        picture.add(image);

        picture.getStyle().set("border-radius","50%");
        body.add(picture);
        body.add(new Text("Profile"),new Profile.Divider(),
                new Text(user.getFirstName()+" "+user.getLastName()),
                new Profile.Divider(),new Text("Skills")
                );

        HorizontalLayout sklz = new HorizontalLayout();

        for(String skill: user.getSkills().split("\n")) {
            Notification.show("Skill is " + skill);
            Div skil = new Div();

            skil.add(skill);

            skil.getStyle().set("background","orange");
            skil.getStyle().set("padding","auto");



            skil.getStyle().set("border-radius","5px");
            sklz.add(skil);
        }

        body.add(sklz);
        sklz.getStyle().set("padding","auto");
        sklz.getThemeList().add("spacing-s");
        body.getStyle().set("align-content","center");
        add(body);

        addClassName("profile-view");

        Div output = new Div();

        add(output);
        //

        personalInfo.add(new H2("Personal information"),firstName,lastName,email,progName,programeYear,phoneNumber,town);
        personalInfo.addClassName("person-form-view");

        skills.add(new H2("Skills"),Skillz);


        // CV Upload

        add(new Profile.Divider(),personalInfo,new Profile.Divider(),new Profile.Divider());
        // Update data
    }
    private void AutoFill() {

        lastName.setValue(lastName.getValue().isEmpty()?data.getLastName():"");
        email.setValue(email.getValue().isEmpty()?data.getEmail():"");
        firstName.setValue(firstName.getValue().isEmpty()?data.getFirstName():"");
        progName.setValue(progName.getValue().isEmpty()?data.getProgName():"");
        programeYear.setValue((programeYear.getValue() == null)?data.getProgrammeY():0);
        town.setValue(town.getValue().isEmpty()?data.getTown():"");
    }
}
