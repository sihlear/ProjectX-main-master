package com.example.application.views.masterdetail;

import java.util.Optional;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;

import com.example.application.views.main.UserData;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.component.textfield.TextField;

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



       /* // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("phone").setAutoWidth(true);
        grid.addColumn("dateOfBirth").setAutoWidth(true);
        grid.addColumn("occupation").setAutoWidth(true);
        TemplateRenderer<SamplePerson> importantRenderer = TemplateRenderer.<SamplePerson>of(
                "<iron-icon hidden='[[!item.important]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.important]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
                .withProperty("important", SamplePerson::isImportant);
        grid.addColumn(importantRenderer).setHeader("Important").setAutoWidth(true);

        grid.setDataProvider(new CrudServiceDataProvider<>(samplePersonService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(SamplePerson.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.samplePerson == null) {
                    this.samplePerson = new SamplePerson();
                }
                binder.writeBean(this.samplePerson);

                samplePersonService.update(this.samplePerson);
                clearForm();
                refreshGrid();
                Notification.show("SamplePerson details stored.");
                UI.getCurrent().navigate(MasterDetailView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the samplePerson details.");
            }
        });*/

    }

    private void addRoutes() {

        Text JP = new Text("Job Post");
        Div top = new Div();
        top.addClickListener(event -> {

            top.getStyle().set("background","orange");
            pageOnDisplay.removeAll();
            pageOnDisplay.add(createJobPost());

            Notification.show(" On click ",1000, Notification.Position.MIDDLE);

        });
        top.add(JP);
        top.getStyle().set("width","100%");


        Text  PR = new Text("Password Reset");
        Div top1 = new Div();
        top1.add(PR);
        top1.getStyle().set("width","100%");


        Text RA = new Text("Recruit Alumni");
        Div top2 = new Div();
        top2.add(RA);
        top2.getStyle().set("width","100%");
        top2.addClickListener(event -> {

            top2.getStyle().set("background","orange");

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
        return  jobPost;
    }

    private VerticalLayout createSkillSearch(){
        VerticalLayout layout = new VerticalLayout();

        Div top = new Div();
        TextField search = new TextField("Skill");
        top.add(search);

        Grid<UserData> eligibleUsers = new Grid();
        SamplePersonService service = new SamplePersonService();
        layout.add(top,eligibleUsers,new Button("Search",e ->{
            eligibleUsers.setItems(service.getEligibleUsers(search.getValue()));
        }));

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
