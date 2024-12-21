package dev.stiebo.app.views.empty;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Empty")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.FILE)
@AnonymousAllowed
public class EmptyView extends Composite<VerticalLayout> {

    public EmptyView() {
        var layout = getContent();
        layout.setSizeFull();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        H2 header = new H2("This place intentionally left empty");
        Paragraph paragraph = new Paragraph("It’s a place where you can grow your own UI 🤗");
        layout.add(header, paragraph);
    }

}
