package com.example.application.views.profile;

import com.example.application.views.main.MainView;
import com.example.application.views.main.UserData;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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


    public UserProfile(){

        user = (UserData)VaadinSession.getCurrent().getAttribute("User");

        HorizontalLayout head = new HorizontalLayout();
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

        Div sklz = new Div();
        for(String skill: user.getSkills().split(""))
            sklz.add(skill);
        body.add(sklz);
        body.getStyle().set("align-content","center");
        add(body);

    }

}
