package dev.stiebo.app.views.ChatView;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.stiebo.app.services.RagChatService;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.firitin.components.messagelist.MarkdownMessage;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Chat")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.FILE)
@RolesAllowed("USER")
public class ChatView extends Composite<VerticalLayout> {

    private final RagChatService service;

    public ChatView(RagChatService service) {
        this.service = service;

        VerticalLayout layout = getContent();
        layout.setSizeFull();

        VerticalLayout messageList = new VerticalLayout();
        Scroller scroller = new Scroller(messageList);
        scroller.setWidthFull();

        HorizontalLayout inputLayout = new HorizontalLayout();
        MessageInput messageInput = new MessageInput();
        inputLayout.addAndExpand(messageInput);

        messageInput.addSubmitListener(e -> {
            String prompt = e.getValue();
            MarkdownMessage promptMessage = new MarkdownMessage(prompt, "You");
            MarkdownMessage answerMessage = new MarkdownMessage("Ai");
            messageList.add(promptMessage, answerMessage);
            service.chat(prompt).subscribe(answerMessage::appendMarkdownAsync);
            scroller.scrollToBottom();
        });

        layout.addAndExpand(scroller);
        layout.add(inputLayout);
    }
}
