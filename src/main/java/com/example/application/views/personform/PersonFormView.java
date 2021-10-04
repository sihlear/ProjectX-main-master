package com.example.application.views.personform;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Route(value = "person-form", layout = MainView.class)
@PageTitle("Sign up")

public class PersonFormView extends Div {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email address");
    private IntegerField age = new IntegerField("Age");
    private PasswordField password  = new PasswordField("Password");
    private ComboBox<String> secretQ = new ComboBox<String>("Secrete question for password reset");
    private TextField secretAnswer = new TextField("Answer");
    private ComboBox<String> Gender = new ComboBox<>("Gender");

    private Div linkToPopi = new Div();
    Anchor link = new Anchor("https://popia.co.za/act/", "By Clicking the checkbox here you accept Popia Act found in this link");
    private Checkbox popi = new Checkbox();


    private Button CreateAccount ;

    private Binder<SamplePerson> binder = new Binder(SamplePerson.class);

    public PersonFormView(SamplePersonService personService) {
        addClassName("person-form-view");

        email.setClearButtonVisible(true);
        email.setErrorMessage("Please enter a valid email address");
        String[] questionsList = {"What was your favorite school teacher’s name?",
                                  "What time of the day were you born? (hh:mm)",
                                   "What were the last four digits of your childhood telephone number?",
                                    "What's favourite your appliance brand?"};
        secretQ.setItems(questionsList);
        add(createTitle());
        add(createFormLayout());

        String[] gen = {"Male","Female","Other"};
        Gender.setItems(gen);

        CreateAccount = new Button("Create New Account",buttonClickEvent -> {

            if(passwordIsValid(password.getValue()) & validateEmail(email.getValue()) ){

                if(popi.getValue()) {


                    if (personService.saveUser(firstName.getValue(), lastName.getValue(), email.getValue(), Gender.getValue(), age.getValue(), password.getValue(), secretQ.getValue(), secretAnswer.getValue())) {


                        final String username = "sitlear@gmail.com";
                        final String password = "Zlatan10#";

                        Properties prop = new Properties();
                        prop.put("mail.smtp.host", "smtp.gmail.com");
                        prop.put("mail.smtp.port", "587");
                        prop.put("mail.smtp.auth", "true");
                        prop.put("mail.smtp.starttls.enable", "true"); //TLS

                        Session session = Session.getInstance(prop,
                                new javax.mail.Authenticator() {
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication(username, password);
                                    }
                                });

                        try {

                            Message message = new MimeMessage(session);
                            message.setFrom(new InternetAddress("from@gmail.com"));
                            message.setRecipients(
                                    Message.RecipientType.TO,
                                    InternetAddress.parse("sitlear@gmail.com,pjhfcnrtvrqzhwpnyc@rffff.net,"+email.getValue()+"")
                            );
                            message.setSubject("Testing Gmail TLS");
                            message.setText("Hi,"+firstName.getValue()
                                    + "\n\n You have succesfully signed up Mindworx");

                            Transport.send(message);

                            System.out.println("Done");

                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }


                        UI.getCurrent().navigate("login");

                    }
                }else {
                    Notification.show("Please check the box",1000, Notification.Position.MIDDLE);
                }
            }else{

            }


        });

        add(createButtonLayout());
        binder.bindInstanceFields(this);


    }

    private boolean passwordIsValid(String value) {

        boolean isUpper = false;
        boolean isLower = false;
        boolean hasChar = false;
        boolean hasInt = false;
        boolean haslen = false;

        if(value.length()>=8) {

            haslen= true;
            for (Character c : value.toCharArray()) {

                if(c.toString().matches("(?=.*[A-Z]).*") ){
                    isUpper= true;
                }
                if(c.toString().matches("(?=.*[a-z]).*")){
                  isLower = true;
                }
                if(c.toString().matches("(?=.*[0-9]).*")){
                    hasInt = true;
                }
                if(c.toString().matches("(?=.*[~!@#$%^&*()_-]).*")){
                    hasChar = true;

                }
            }


        }

        if(isUpper& isLower & haslen & hasChar & hasInt){
            return true;
        }
        Notification.show("Your password is too weak it must contain Upper,lower," +
                "(!@#$%^&*),134567890,of length greater than 7",2000, Notification.Position.MIDDLE);
        return false;
    }

    private boolean validateEmail(String email) {

        boolean isValid = false;

        try {

//Create InternetAddress object and validated the email address.

            InternetAddress internetAddress = new InternetAddress(email);

            internetAddress.validate();

            isValid = true;

        } catch (AddressException e) {

            e.printStackTrace();

        }

        return isValid;
    }

    private Component createTitle() {
        return new H3("Sign Up");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Please enter a valid email address");
        linkToPopi.add(popi,link);
        formLayout.add(firstName, lastName,age, Gender,email,password,secretQ,secretAnswer,linkToPopi);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        CreateAccount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(CreateAccount);

        return buttonLayout;
    }

    private static class PhoneNumberField extends CustomField<String> {
        private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        public PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Country");
            countryCode.setPattern("\\+\\d*");
            countryCode.setPreventInvalidInput(true);
            countryCode.setItems("+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setPattern("\\d*");
            number.setPreventInvalidInput(true);
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                String s = countryCode.getValue() + " " + number.getValue();
                return s;
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }

}
