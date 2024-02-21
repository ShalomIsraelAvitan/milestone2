package israela.milestone2;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;


@Route(value = "/Upload", layout = AppMainLayout.class)
public class UploadPhotoPage extends VerticalLayout{
    private PhotoServise photoService;
    private Upload singleFileUpload; //UI component (file upload)
    private Photo uploadPhoto;

    public  UploadPhotoPage(PhotoServise photoService) {
        this.photoService = photoService;
        if (!isUserAuthorized())
        {
            System.out.println("-------- User NOT Authorized - can't use! --------");
            //Notification.show("You need to login or register first",5000,Position.TOP_CENTER);
            UI.getCurrent().getPage().setLocation("/"); // Redirect to login page (HomePage).
            return;
        }
        creatPhotoUpload();
         
        try{
        showPhotoGallery2();}
        catch(Exception e){
            System.out.println("Error========>showPhotoGallery\n");
        }

        
        add(new H1("Photo Gallery"));
        add(singleFileUpload);
        add(new Button("OK", event -> sendToNN()));

        setSizeFull();
        setAlignItems(Alignment.CENTER);

    }

    private void sendToNN() {
        
    }


    private void creatPhotoUpload() {
        /* Example for MemoryBuffer */
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        singleFileUpload = new Upload(memoryBuffer);
        singleFileUpload.setAcceptedFileTypes("image/*");
        //singleFileUpload.setMaxFileSize(0);//הגבלה של הגודל

        singleFileUpload.addSucceededListener(event -> {
            Notification.show("Photo Upload to Server Succeeded!", 5000, Position.TOP_CENTER);

            // Get information about the uploaded file
            //InputStream fileData = memoryBuffer.getInputStream();
            //String fileName = event.getFileName();
            //long contentLength = event.getContentLength();
            //String mimeType = event.getMIMEType();

            // Do something with the file data
            // processFile(fileData, fileName, contentLength, mimeType);

            System.out.println("File name: "+event.getFileName());
            System.out.println("File size: "+event.getContentLength());
            System.out.println("File type: "+event.getMIMEType());

           

            try {
                byte[] photoFileContend =  memoryBuffer.getInputStream().readAllBytes();
                Photo uploadPhoto = new Photo(event.getFileName(), "stam tmuna...", photoFileContend);
                Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
                photoService.addPhoto(uploadPhoto, idUser);
                Notification.show("Photo Upload to DB Succeeded!", 5000, Position.TOP_CENTER);
                
            } catch (Exception e) {
                
                System.out.println("ERROR=======>>creatPhotoUpload\n");
            }
           
            
        });
    }
    

    private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        return (userName == null) ? false : true;
    }

    private void showPhotoGallery() throws IOException {
        //TO_DO: get all photos frome DB
        ArrayList<Photo> photoList = new ArrayList<>();
        Long id =Long.parseLong(UI.getCurrent().getId().get());
        photoList = photoService.getPhotoById(id);

        for(int i = 0; i<photoList.size(); i++)
        {
            File file = new File("photo");
            Files.copy(new ByteArrayInputStream(photoList.get(i).getContend()),file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Image image = new Image("photo","image");
            add(image);
        }


    }

    private void showPhotoGallery2() {
        Long idUser = Long.parseLong((String) VaadinSession.getCurrent().getSession().getAttribute("userId"));
    
        ArrayList<Photo> photoArr = photoService.getPhotoById(idUser);
    
        for (int i = 0; i < photoArr.size(); i++) {
            if(photoArr.isEmpty())
            {
                System.err.println("the photoArr=========>>\n");
                break;
            }
            byte[] imageData = photoArr.get(i).getContend();
            System.out.println("Length of imageData: " + imageData.length);
            StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(imageData));
            Image image = new Image(resource, "Alt Text");
            add(image);
        }
    }


}
