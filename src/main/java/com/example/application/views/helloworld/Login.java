package com.example.application.views.helloworld;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.main.MainView;
import com.example.application.views.profile.forgotpass;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;


@Route(value = "login", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Login")
public class Login extends HorizontalLayout {

    private SamplePerson person = new SamplePerson();
    private TextField name;
    private Button login;
    private Button signup;

    public Login(SamplePersonService service) {
        addClassName("hello-world-view");


        //
        RouterLink link = new RouterLink("Forgot Password", forgotpass.class);
        Label em = new Label("Email");
        name = new TextField(em.getText());
        TextField passwordField = new TextField("Password");
        login = new Button("login");
        login.getStyle().set("color","white");


        signup = new Button("sign up",buttonClickEvent -> {
            UI.getCurrent().navigate("person-form");
        });
        //Styling of buttons
        signup.getStyle().set("background","#56575b");
        signup.getStyle().set("color","white");
        signup.getStyle().set("font-family","TAHOMA");
        signup.getStyle().set("font-weight","bold");
        login.getStyle().set("background","#ff8c44");
        login.getStyle().set("font-family","TAHOMA");
        login.getStyle().set("font-weight","bold");

        LoginForm layout = new LoginForm();

        //add(layout);
        add(name,passwordField, login, signup ,link);

        setVerticalComponentAlignment(Alignment.END, name,passwordField, login,signup);



        //when i click the button, it goes to this listener below
        //

        login.addClickListener(action -> {

            if(service.ValidateUser(name.getValue(),passwordField.getValue())){

                Notification.show("Successful check").setDuration(5000);

                UI.getCurrent().navigate("card-list");

            }
            else{
                Notification.show("Invalid credentials").setDuration(5000);
            }
            Notification.show("After check");

        });

    }

    public SamplePerson getPerson() {
        return  this.person;
    }
}
