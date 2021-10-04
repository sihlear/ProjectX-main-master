package com.example.application.views.profile;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.main.MainView;
import com.example.application.views.main.UserData;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Route(value = "profile-edit", layout = MainView.class)
@PageTitle("Profile")
public class Profile extends VerticalLayout {



    public static class Divider extends Span {

        public Divider() {
            getStyle().set("background-color", "orange");
            getStyle().set("flex", "0 0 2px");
            getStyle().set("align-self", "stretch");
        }
    }
    private byte[] profilePic ;
    private byte[] cv ;
    private byte[] cert ;
    private TextField firstName = new TextField("First name");

    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email address");
    private IntegerField programeYear = new IntegerField("Programme Year");
    private ComboBox<String> gender = new ComboBox<>("Gender");
    private ComboBox<String> race = new ComboBox<>("Race");
    private TextField progName = new TextField("Programme Name");
    private Button updateProfile;
    private  TextField town = new TextField("Town");
    private  IntegerField phoneNumber = new IntegerField("Contact Number");

    //skills
    private TextArea Skillz = new TextArea("List your Skills");

    //Sections
    private HorizontalLayout personalInfo = new HorizontalLayout();
    private VerticalLayout skills = new VerticalLayout();
    private VerticalLayout cvLayout = new VerticalLayout();
    private VerticalLayout certLayout = new VerticalLayout();
    UserData person ;
    UserData data = (UserData) VaadinSession.getCurrent().getAttribute("LoggedInUser");
    int progress = 4;

    SplitLayout layout = new SplitLayout();

    public Profile(SamplePersonService personService) {


        checkProgress();
        String[] gen = {"Male","Female","Other"};
        String[] rac = {"African","White","Coloured","Asian"};
        gender.setItems(gen);
        race.setItems(rac);
        ProgressBar progressBar = new ProgressBar();

        progressBar.setValue(((float)progress/10));

        add(new Text("Profile Progress"), progressBar);

        AutoFill();
        this.person = new UserData();
        addClassName("profile-view");
        Image pp = new Image();
        
        //upload profile picture
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        Div output = new Div();

        upload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png", "image/gif");
        upload.setUploadButton(new Button("Change profile"));

        upload.addSucceededListener(event -> {
            String attachmentName = event.getFileName();

            try {
                // The image can be jpg png or gif, but we store it always as png file in this code
                BufferedImage inputImage = ImageIO.read(buffer.getInputStream(attachmentName));
                ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
                ImageIO.write(inputImage, "png", pngContent);
                profilePic = pngContent.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        upload.addFileRejectedListener(event -> {
            Paragraph component = new Paragraph();
            output.removeAll();
            showOutput(event.getErrorMessage(), component, output);
        });
        upload.getElement().addEventListener("file-remove", event -> {
            output.removeAll();
        });

        add(upload, output);
        //

        personalInfo.add(new H2("Personal information"),firstName,lastName,email,gender,race,progName,programeYear,phoneNumber,town);
        personalInfo.addClassName("person-form-view");

        Skillz.setPlaceholder("Write here.......");
        skills.add(new H2("Skills"),Skillz);


        // CV Upload

        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Div outputCv = new Div();
        Upload uploadCv = new Upload(memoryBuffer);
        uploadCv.setUploadButton(new Button("Upload"));
        outputCv.add(uploadCv);
        uploadCv.addFinishedListener(e -> {
            InputStream inputStream = memoryBuffer.getInputStream();
            try {
                cv = IOUtils.toByteArray(inputStream);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            // read the contents of the buffered memory
            // from inputStream
        });

        MemoryBuffer memoryBuffer2 = new MemoryBuffer();
        Div outputCert = new Div();
        Upload uploadCert = new Upload(memoryBuffer);
        uploadCert.setUploadButton(new Button("Upload"));
        outputCert.add(uploadCert);
        uploadCert.addFinishedListener(e ->
        {
            InputStream inputStream = memoryBuffer2.getInputStream();
            try {
                cert = IOUtils.toByteArray(inputStream);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            // read the contents of the buffered memory
            // from inputStream
        });

        certLayout.add(new H2(" Upload your latest Certificate"),outputCert);
        cvLayout.add(new H2(" Upload your Resume"),outputCv);
        add(new Divider(),personalInfo,new Divider(),skills,new Divider(),cvLayout, new Divider(),certLayout);
        // Update data

        updateProfile = new Button("Update",buttonClickEvent -> {

            if (!(firstName.getValue().isEmpty() & lastName.getValue().isEmpty() & email.getValue().isEmpty() & progName.getValue().isEmpty()
                    & programeYear.getValue().toString().isEmpty() &phoneNumber.getValue().toString().isEmpty() &town.getValue().isEmpty() &Skillz.getValue().isEmpty() )){

                updateValues(firstName.getValue(),lastName.getValue(),email.getValue(),progName.getValue(),programeYear.getValue(),phoneNumber.getValue(),town.getValue(),Skillz.getValue());

            }
            else{
                Notification.show(" Make sure all the fields are filled in");
            }
            Notification.show("Button clicked");
            if(personService.UpdateProfile(person)){
                person = personService.getUser();
                UI.getCurrent().navigate("card-list");
            }else{
                Notification.show("Failed to update user details");
            }

        });

        add(updateProfile);
        
    }

    private void checkProgress() {

        //i used the wrong variable (person instead of data)
        if(!data.getSkills().isEmpty()){
            //if the user already has skills on their profile, increase the value of the progressbar
            progress+=1;
        }if(!data.getProgName().isEmpty()){
            progress+=1;
        }if(!(data.getProgrammeY()>0)){
            progress+=1;
        }if(data.getContact() > 0){
            progress+=1;
        }if(!data.getTown().isEmpty()){
            progress+=1;
        }
    }

    private void AutoFill() {

        lastName.setValue(lastName.getValue().isEmpty()?data.getLastName():"");
        Skillz.setValue(Skillz.getValue().isEmpty()?data.getSkills():"");
        email.setValue(email.getValue().isEmpty()?data.getEmail():"");
        firstName.setValue(firstName.getValue().isEmpty()?data.getFirstName():"");
        progName.setValue(progName.getValue().isEmpty()?data.getProgName():"");
        gender.setValue(data.getGender());
        programeYear.setValue((programeYear.getValue() == null)?data.getProgrammeY():0);
        town.setValue(town.getValue().isEmpty()?data.getTown():"");
    }

    private void updateValues(String firstName, String lastName, String email, String progName, int programeYear, int phoneNumber, String town,String Skills) {

        //Validation

        //

        person.setProfile(profilePic);
        person.setCv(this.cv);
        person.setCertificate(cert);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setProgName(progName);
        person.setProgrammeY(programeYear);
        person.setTown(town);
        person.setContact(phoneNumber);
        person.setSkills(Skills);


    }

    private Paragraph createComponent(String mimeType, String fileName, InputStream inputStream) {
        return new Paragraph();
    }

    private void showOutput(String errorMessage, Paragraph component, Div output) {
    }
}
