package israela.milestone2;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;


@Route(value = "/upload", layout = AppMainLayout.class)
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
        //initUploaderImage();
        /*  
        try{
        System.err.println("staet showPhotoGallery2=====>\n");
        showPhotoGallery2();}
        catch(Exception e){
            System.out.println("Error========>showPhotoGallery\n");
        }*/

        System.out.println("UploadPhotoPage=======>>\n");;
        add(new H1("Photo Upload"));
        add(singleFileUpload);
        add(new Button("Send to CNN", event -> sendToNN()));
        add(new Button("Remove Photo", event -> remove((String)VaadinSession.getCurrent().getSession().getAttribute("userId"))));

        setSizeFull();
        setAlignItems(Alignment.CENTER);

    }

    private void remove(String attribute) {

        Long id = Long.parseLong(attribute);
        try {
             boolean b = photoService.removPhotoOfUserId(id);
             System.out.println("b = "+b);
             if(b==true){
                Notification.show("remove Succeeded",5000, Position.TOP_CENTER);
             }
            
        } catch (Exception e) {
            Notification.show("Remove Failed",5000, Position.TOP_CENTER);
        }
        
        
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
            System.out.println("");
        
            try {
                byte[] photoFileContend =  memoryBuffer.getInputStream().readAllBytes();
                //showPhotoOnPage(photoFileContend, uploadPhoto);
                uploadPhoto = new Photo(event.getFileName(), "stam tmuna...", photoFileContend);
                Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
                photoService.addPhoto(uploadPhoto, idUser);

                Notification.show("Photo Upload to DB Succeeded!", 5000, Position.TOP_CENTER);
               
                //showPhotoOnPage(photoFileContend, uploadPhoto);
                try{
                ArrayList<Photo> list = photoService.getPhotoById(idUser);
                System.out.println("***********************************\n");
                System.out.println(list.size());
                System.out.println("*****************************\n");
                showPhotoOnPage(list.get(list.size()-1).getContend(), uploadPhoto);
                }
                catch (Exception e){
                    System.out.println("error of photoService====>>\n");
                }
            } catch (Exception e) {

                
                System.out.println("ERROR=======>>creatPhotoUpload\n");
            }
           
            
        });
    }
    
    private void showPhotoOnPage(byte[] photoFileContend, Photo uploadPhoto) {

        StreamResource resource = new StreamResource("stam.jpg", new InputStreamFactory() 
        {
            public java.io.InputStream createInputStream() 
            {
                return new ByteArrayInputStream(photoFileContend);
            };
        });
       
        Image image = new Image(resource, uploadPhoto.getName());
        image.setHeight("200px");
        image.setWidth("200px");
        add(image);
  
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
        System.err.println("size = "+ photoArr.size()+"\n");
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
    /* 
    private void initUploaderImage() {
    MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    singleFileUpload = new Upload(buffer);
    singleFileUpload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png", "image/gif");

    singleFileUpload.addSucceededListener(event -> {
        String attachmentName = event.getFileName();
        try {
            // The image can be jpg png or gif, but we store it always as png file in this example
            BufferedImage inputImage = ImageIO.read(buffer.getInputStream(attachmentName));
            ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
            ImageIO.write(inputImage, "png", pngContent);
            //saveProfilePicture(pngContent.toByteArray());
            Image img =  showImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    add(singleFileUpload);
}*/

/* 
    private Image showImage() {
        System.err.println("showImage=====>>\n");
        Long id =Long.parseLong(UI.getCurrent().getId().get());
        Image image = photoService.getImg(id);
        Image image2 = new Image(image.getSrc(), "img");
        System.err.println(image.getText());
        image.setHeight("100%");
        HasComponents imageContainer;
        // imageContainer.removeAll();
        // imageContainer.add(image);
        return image2;
    }*/


}
