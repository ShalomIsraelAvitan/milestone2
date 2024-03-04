package israela.milestone2;

import java.util.Date;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "/home",layout = AppMainLayout.class)
@PageTitle("Home")
public class HomePage extends VerticalLayout{

    private PhotoServise photoService;
    private UserServise userServise;
    private Upload singleFileUpload; //UI component (file upload)
    private Photo uploadPhoto;

    private String sessionId, userName;

    private static final String CHAT_IMAGE_URL = "https://www.smorescience.com/wp-content/uploads/2023/08/Featured-Images-50.jpg";
    public  HomePage(PhotoServise photoService) {
    setAlignItems(Alignment.CENTER);
    if (!isUserAuthorized())
        {
            System.out.println("-------- User NOT Authorized - can't use! --------");
            //Notification.show("You need to login or register first",5000,Position.TOP_CENTER);
            UI.getCurrent().getPage().setLocation("/"); // Redirect to login page (HomePage).
            return;
        }

    add(new H2("Home Page"));

      //Image image = new Image("images/israel.jpeg", "יחד ננצח");
      //image.setWidth("600px");

      // Get from Session the user-session-id & 'username' attribute 
      sessionId = VaadinSession.getCurrent().getSession().getId();
      userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

      // if no 'username' attribute, this is a Guest.
      String welcomeMsg = "Welcome Guest!";
      if (userName != null)
         welcomeMsg = "Welcome " + userName.toUpperCase();

      // create image for chat page   
      Image imgLogo = new Image(CHAT_IMAGE_URL, "Home image");
      imgLogo.setHeight("250px");


      HorizontalLayout helloPanel = new HorizontalLayout();
      helloPanel.setAlignItems(Alignment.BASELINE);
      //TextField fieldName = new TextField("Your Name");
      String str = "On this website, you can upload photos of your paintings and check whether the painting is a realism or abstract painting";
      String str2 = "In order to upload your drawings, you will have to click on the upload button located in the navigation bar on the top left.";

      
      // Button btnSignUp = new Button("sign up", event -> signUp());
      // Button btnLogin = new Button("Log in", event -> logIn());
      //helloPanel.add(fieldName, btnSayHello);

      //helloPanel.add(btnLogin,btnSignUp);
      helloPanel.add(new H3(str));
      
      add(new Text(new Date() + ""));
      //add(imgLogo);
      add(new H1(welcomeMsg));
      //add(new H3("( SessionID: " + sessionId + " )"));
      add( helloPanel);
      add(new H3(str2));

      // set all components in the Center of page
      setSizeFull();
      setAlignItems(Alignment.CENTER);
   }
   private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        return (userName == null) ? false : true;
    }
   
}
