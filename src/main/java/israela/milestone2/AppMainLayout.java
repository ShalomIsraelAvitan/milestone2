package israela.milestone2;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

public class AppMainLayout extends AppLayout
{
    public AppMainLayout()
    {
        createHeader();
    }

    private void createHeader()
    {
        H3 logo = new H3("MyApp");
        logo.getStyle().setColor("blue");
        //logo.getStyle().setColor("#");

        RouterLink linkHome = new RouterLink("Home", HomePage.class);
        RouterLink linkUpload = new RouterLink("Upload", UploadPhotoPage.class);
        Span last = new Span("");
        //Button btnLogin = new Button("Login", e->login());
        Button btnLogout = new Button("Logout", e->logout());
        btnLogout.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR); // RED button

        HorizontalLayout header  = new HorizontalLayout();
        header.add(logo,linkHome,linkUpload,last,btnLogout);
        header.getStyle();
        header.setWidthFull();
        header.setAlignItems(Alignment.BASELINE);
        header.setPadding(true);//רווחים מסביב לכפתור
        header.expand(last);
        addToNavbar(header);
        
    }
    private void login()
    {
        //Notification.show("login",1000,Position.BOTTOM_START);
        UI.getCurrent().navigate(LoginPage.class);//צד הלקוח יקפוץ לדף לוגאין
        
    }
    private void logout()
   {
      // Invalidate Session (delete the user-session-id and all its attributes)
      VaadinSession.getCurrent().getSession().invalidate();

      // Reload this page with new user-session-id
      UI.getCurrent().getPage().reload();
   }
}
