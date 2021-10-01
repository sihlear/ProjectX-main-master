package com.example.application.views.profile;

import com.example.application.views.main.MainView;
import com.example.application.views.main.UserData;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "user-profile", layout = MainView.class)
@PageTitle("Profile")

public class UserProfile extends VerticalLayout {


    VerticalLayout body = new VerticalLayout();
    UserData user;


    public UserProfile(){

        user = (UserData)VaadinSession.getCurrent().getAttribute("User");



        body.add(new Text("Profile"),new Profile.Divider(),
                new Text(user.getFirstName()+" "+user.getLastName()),
                new Profile.Divider(),new Text("Skills")
                );



        add(body);

    }

}
