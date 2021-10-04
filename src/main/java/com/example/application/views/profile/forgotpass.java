package com.example.application.views.profile;

import com.example.application.data.service.SamplePersonService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "forgot-password", layout = MainView.class)
@PageTitle("Reset Password")

public class forgotpass extends Div {


    private String Username;
    private String SecretAnswer;
    private String Question;

    public forgotpass(SamplePersonService personService){
        addClassName("hello-world-view");
        TextField username = new TextField("Enter Your email");

        TextArea Ques = new TextArea("Question");
        TextField answer = new TextField("Answer");

        TextField newPass = new TextField("Enter new password");
        TextField confirmPass = new TextField("Confirm new password");

        VerticalLayout main = new VerticalLayout();
        add(main);
        HorizontalLayout body = new HorizontalLayout();
        main.add(body);
        body.add(username);

        VerticalLayout buttonLayout = new VerticalLayout();

        Button ok = new Button("Enter", buttonClickEvent -> {

            if(!username.getValue().isEmpty() && Ques.getValue().isEmpty()){

                Ques.setValue(ForgotPassCheck(personService,username.getValue()));
                body.add(Ques,answer);
                Ques.setReadOnly(true);

            }else if(!Ques.getValue().isEmpty() && newPass.getValue().isEmpty()){

                //Notification.show("Checking if the answer matches");

                if((!SecretAnswer.isEmpty()) && SecretAnswer.toLowerCase().equals(answer.getValue().toLowerCase()) ){

                    body.add(newPass,confirmPass);
                }

            }else if(newPass.getValue().equals(confirmPass.getValue()) && !newPass.isEmpty()){

               if( personService.resetPassword(newPass.getValue(), username.getValue())){
                    Notification.show("Password reset Successful");
                    UI.getCurrent().navigate("login");
               };

            }else
                Notification.show("There is a blank field or passwords don't match");
        });

        buttonLayout.add(ok);
        add(buttonLayout);




    }

    public String ForgotPassCheck(SamplePersonService personService, String Username){

       Question = personService.getSecretAnswer(Username).get(0);
       SecretAnswer = personService.getSecretAnswer(Username).get(1);
       return Question;


    }
}
