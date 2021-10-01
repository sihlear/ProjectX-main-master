package com.example.application.views.cardlist;

import com.example.application.data.service.SamplePersonService;
import com.example.application.views.main.MainView;
import com.example.application.views.main.UserData;
import com.vaadin.componentfactory.Chat;
import com.vaadin.componentfactory.model.Message;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Route(value = "chat", layout = MainView.class)
@PageTitle("Chat")
public class Social extends Div implements AfterNavigationObserver {

    VerticalLayout users = new VerticalLayout();
    VerticalLayout userChat = new VerticalLayout();
    Grid<UserData> userDataGrid = new Grid<>();
    SamplePersonService personService = new SamplePersonService();

    UserData userData1 = (UserData) VaadinSession.getCurrent().getAttribute("LoggedInUser");


    Social(){

        addClassName("card-list-view");

        listUsers(personService.getUsersChat(userData1.getEmail()));

        add(users);
        userChat.setVisible(true);
    }

    private void listUsers(List<UserData> usersChat) {

        for (UserData user:
             usersChat) {

            users.add(createCard(user));

        }
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

        Span name = new Span(userData.getFirstName()+" "+userData.getLastName());
        Span isOnline = new Span();

        card.add(image,name,isOnline);

        card.addClickListener(

            ChatEvent->{


                Chat chat2 = new Chat();
                chat2.setMessages(SamplePersonService.loadMessages(userData1.getEmail(),
                        userData.getEmail(),
                        userData1.getFirstName()+" "+userData1.getLastName(),
                        userData.getFirstName()+" "+userData.getLastName()));
                chat2.setDebouncePeriod(200);
                chat2.setLazyLoadTriggerOffset(2500);
                chat2.scrollToBottom();

                //Hi Ronaldo


                byte[] imageBytes2 = userData1.getProfile();// your data source here
                StreamResource resource2 = new StreamResource("ImageName.jpg",
                        () -> new ByteArrayInputStream(imageBytes2));
                Image image2 = new Image(resource2, "dummy image");

                String path = image2.getSrc();

                chat2.addChatNewMessageListener(event -> {

                            if (event.getMessage().isEmpty()) {

                            } else {
                                event.getSource().addNewMessage(
                                        new Message(event.getMessage(),
                                                image2.toString(),
                                                userData1.getFirstName() + " " + userData1.getLastName(),
                                                true));
                                event.getSource().clearInput();
                                event.getSource().scrollToBottom();
                                SamplePersonService.sendMessage(userData.getEmail() + userData1.getEmail(),
                                        userData1.getEmail(),
                                        event.getMessage().toString());
                            }
                        }
                );


                chat2.addLazyLoadTriggerEvent(ev -> {
                    List<Message> list = loadMessages();
                    try {

                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {}
                    chat2.setLoading(false);
                    chat2.addMessagesToTop(list);
                });

                Div loading = new Div();
                loading.setText("Loading...");
                loading.getElement().setAttribute("style", "text-align: center;");
                chat2.setLoadingIndicator(loading);


                Dialog pop = new Dialog();
                pop.setWidth("40%");
                pop.setModal(true);
                pop.add(chat2);
                pop.open();



        });
        card.setVisible(true);

        HorizontalLayout buttons = new HorizontalLayout();

        Button viewProfile = new Button("View Profile",event -> {
            VaadinSession.getCurrent().setAttribute( "User" , userData ) ;

            UI.getCurrent().navigate("user-profile");
        }
        );
        Button  chat = new Button("Chat",ChatEvent->{


            Chat chat2 = new Chat();
            chat2.setMessages(SamplePersonService.loadMessages(userData1.getEmail(),
                    userData.getEmail(),
                    userData1.getFirstName()+" "+userData1.getLastName(),
                    userData.getFirstName()+" "+userData.getLastName()));
            chat2.setDebouncePeriod(200);
            chat2.setLazyLoadTriggerOffset(2500);
            chat2.scrollToBottom();

            //Hi Ronaldo


            byte[] imageBytes2 = userData1.getProfile();// your data source here
            StreamResource resource2 = new StreamResource("ImageName.jpg",
                    () -> new ByteArrayInputStream(imageBytes2));
            Image image2 = new Image(resource2, "dummy image");
            image.getStyle().set("border-radius","50%");
            image.getStyle().set("height", "var(--lumo-size-m)");
            image.getStyle().set("width", "var(--lumo-size-m)");
            String path = image2.getSrc();

            chat2.addChatNewMessageListener(event -> {

                        if (event.getMessage().isEmpty()) {

                        } else {
                            event.getSource().addNewMessage(
                                    new Message(event.getMessage(),
                                            image2.toString(),
                                            userData1.getFirstName() + " " + userData1.getLastName(),
                                            true));
                            event.getSource().clearInput();
                            event.getSource().scrollToBottom();
                            SamplePersonService.sendMessage(userData.getEmail() + userData1.getEmail(),
                                    userData1.getEmail(),
                                    event.getMessage().toString());
                        }
                    }
            );


            chat2.addLazyLoadTriggerEvent(ev -> {
                List<Message> list = loadMessages();
                try {

                    Thread.sleep(1000);
                } catch (InterruptedException e1) {}
                chat2.setLoading(false);
                chat2.addMessagesToTop(list);
            });

            Div loading = new Div();
            loading.setText("Loading...");
            loading.getElement().setAttribute("style", "text-align: center;");
            chat2.setLoadingIndicator(loading);


            Dialog pop = new Dialog();
            pop.setWidth("40%");
            HorizontalLayout head = new HorizontalLayout();
            head.add(image);head.add(name);
            head.getStyle().set("background","#56575b");
            head.getStyle().set("border-radius","2px");
            pop.add(head);
            pop.setModal(true);
            pop.add(chat2);
            pop.open();



        }
        );
        buttons.add(viewProfile,chat);
        layout.add(card,buttons);
        layout.setFlexGrow(1, card);
        layout.setVerticalComponentAlignment(FlexComponent.Alignment.END,
                buttons);

        layout.getStyle().set("border-radius","1px #FF7F00");
        buttons.getStyle().set("padding","5%");
        chat.getStyle().set("padding-bottom","5%");
        viewProfile.getStyle().set("padding-bottom","5%");
        chat.getStyle().set("border","2px solid #ffffff");
        viewProfile.getStyle().set("border","2px solid #ffffff");



        return layout;

    }


    private List<Message> loadMessages() {

        List<Message> messages = new ArrayList<>();


        return messages;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        SamplePersonService personService = new SamplePersonService();
        userDataGrid.setItems((List)personService.getUsersChat(userData1.getEmail()));
    }

}
