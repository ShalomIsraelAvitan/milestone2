package israela.milestone2;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "/gallery", layout = AppMainLayout.class)
public class PhotoGalleryPage extends VerticalLayout{

    private PhotoServise photoServise;

    public PhotoGalleryPage(PhotoServise photoServise)
    {
        this.photoServise = photoServise;
        if (!isUserAuthorized())
        {
            System.out.println("-------- User NOT Authorized - can't use! --------");
            UI.getCurrent().getPage().setLocation("/"); // Redirect to login page (HomePage).
            return;
        }
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        add(new H1("Welcome "+userName.toUpperCase()+" To Your Photo Gallery"));
        showAllPhotoUser();

        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }
    private void showAllPhotoUser() {
         
        Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
        ArrayList<Photo> list = photoServise.getPhotoById(idUser);

        if(list.size()==0)
        {
            add(new H2("There are no images to display"));
            return; 
        }
        for(int i =0; i<list.size(); i++)
        {
            showPhotoOnPage(list.get(i).getContend(), list.get(i));
        }
            
    }
    private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        return (userName == null) ? false : true;
    }
    private void showPhotoOnPage(byte[] photoFileContend, Photo photo) {

        StreamResource resource = new StreamResource("stam.jpg", new InputStreamFactory() 
        {
            public java.io.InputStream createInputStream()
            {
                return new ByteArrayInputStream(photoFileContend);
            };
        });
       
        Image image = new Image(resource, photo.getName());
        image.setHeight("200px");
        image.setWidth("200px");
        add("Name: "+photo.getName()+"\n");
        add("\nClassification: "+photo.getClassification()+"\n");
        add(image);
  
    }
}
