package dev.stiebo.app.views.LibraryView;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.stiebo.app.dtos.DocumentDto;
import dev.stiebo.app.dtos.UploadFileInputStreamResource;
import dev.stiebo.app.services.RagChatService;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@PageTitle("Library")
@Route("library")
@Menu(order = 1, icon = LineAwesomeIconUrl.FILE)
@RolesAllowed("USER")
public class LibraryView extends Composite<VerticalLayout> {

    private final RagChatService service;
    private final Grid<DocumentDto> grid;
    private TextField docNameFilter;
    private GridListDataView<DocumentDto> dataView;
    private List<DocumentDto> documentDtoList;

    public LibraryView(RagChatService service) {
        this.service = service;

        VerticalLayout layout = getContent();
        layout.setSizeFull();

        HorizontalLayout topRow = new HorizontalLayout();
        docNameFilter = new TextField();
        docNameFilter.setPlaceholder("Search documents");
        docNameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        Button resetBtn = new Button("Reset", event -> {
            docNameFilter.clear();
        });
        Button addNewBtn = new Button("Add new");

        Popover popover = new Popover();
        popover.setTarget(addNewBtn);
        popover.setPosition(PopoverPosition.BOTTOM);
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        popover.add(upload);

        topRow.add(docNameFilter, resetBtn, addNewBtn);

        grid = new Grid<>();
        grid.setWidthFull();
        grid.addColumn(DocumentDto::documentName).setHeader("Document name");
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, documentDto) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> deleteDocument(documentDto));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage");
        loadDataView();
        upload.addSucceededListener(e -> {
            service.addDocument(new UploadFileInputStreamResource(buffer.getInputStream(), e.getFileName()));
            loadDataView();
        });

        layout.add(topRow, grid);
    }

    private void deleteDocument(DocumentDto documentDto) {
        service.deleteDocument(documentDto);
        loadDataView();
    }

    private void loadDataView() {
        dataView = grid.setItems(service.listDocuments());
        docNameFilter.addValueChangeListener(e -> dataView.refreshAll());
        dataView.addFilter(documentDto -> {
            String filter = docNameFilter.getValue().trim();
            if (filter.isEmpty()) {
                return true;
            }
            return documentDto.documentName().toLowerCase().contains(filter.toLowerCase());
        });

    }

}
