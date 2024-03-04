package israela.milestone2;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

//import py4j.GatewayServer;
//from py4j.java_gateway import JavaGateway;

@Route(value = "/upload", layout = AppMainLayout.class)
public class UploadPhotoPage extends VerticalLayout{
    private PhotoServise photoService;
    private Upload singleFileUpload; //UI component (file upload)
    private Photo uploadPhoto;
    private String photoID;

    private String strOfOutpotPhyton;
    private byte[] photoFileContend; 
    

    
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
       

        System.out.println("UploadPhotoPage=======>>\n");;
        add(new H1("Photo Upload"));
        add(singleFileUpload);
        

        setSizeFull();
        setAlignItems(Alignment.CENTER);

    }

    private void remove(String attribute) {

        Long id = Long.parseLong(attribute);
        try {
            
             boolean b = photoService.removPhotoById(this.photoID);
             System.out.println("b = "+b);
             if(b==true){
                Notification.show("remove Succeeded",5000, Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                this.photoID = null;
             }
            
        } catch (Exception e) {
            Notification.show("Remove Failed",5000, Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        
        
    }

    private void sendToCNN(){
        if(this.photoID==null)
        {
            Notification.show("You must upload a photo before sending for evaluation",10000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_WARNING);
            return;
        }

        String strPredictions;
        Photo photo = photoService.getPhotoById(this.photoID);
        System.out.println("sendToNN==>>"+photo.getName());
        byte[] photoFileContend =  photo.getContend();
        System.out.println("========================>>"+photoFileContend.toString());

        StreamResource resource = new StreamResource("stam.jpg", new InputStreamFactory() 
        {
            public java.io.InputStream createInputStream() 
            {
                return new ByteArrayInputStream(photoFileContend);
            };
        });
       
        Image image = new Image(resource, photo.getName());
        try {
            OutputStream out = new FileOutputStream("C:\\Users\\user\\Desktop\\savePhoto\\"+photoID+".jpg");
            out.write(photoFileContend);
            out.flush();
            out.close();
            System.out.println("YESSSSSSSSSSSSS");
        } catch (Exception e) {
            System.out.println("OutputStream =====>>"+e.toString());
        }

        String pathPython = "C:\\Users\\user\\Documents\\VSProj\\milestone2\\src\\main\\java\\israela\\milestone2\\CNN.py";
        //String pathImage = "C:\\Users\\user\\Desktop\\savePhoto\\"+photoID+".png";
        String pathImage = "C:\\Users\\user\\Desktop\\savePhoto\\"+photoID+".jpg";
        //String pathImage ="C:\\Users\\user\\Desktop\\savePhoto\\00c5774bc9883453a565f949e4b1e19b.jpg";

        String [] cmd = new String[3];
        cmd[0] = "python";
        cmd[1] = pathPython;
        cmd[2] = pathImage;

        
        strPredictions ="";
        Runtime r = Runtime.getRuntime();
        System.out.println("Runtime==>>");
        try {
            Process p = r.exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((strOfOutpotPhyton=in.readLine()) != null){
                //Notification.show(strOfOutpotPhyton,10000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                System.out.println("java = "+strOfOutpotPhyton);
                strPredictions = strOfOutpotPhyton;
            }
        } catch (Exception e) {
            System.out.println("sendToNN ERROR  Process p = r.exec(cmd);===>>"+e.toString());
        }

        Notification.show(strPredictions, 5000, Position.BOTTOM_START);

        try {
            double p = Double.parseDouble((String)strPredictions.toString());
            System.out.println("double = "+p);
            if(p>50)
            {
                //Notification.show("Realism", 5000, Position.BOTTOM_START);
                strPredictions = "Realism";
            }
            else{
                //Notification.show("Abstract", 5000, Position.BOTTOM_START);
                strPredictions = "Abstract";
            }

            add(new H2("The model classified your image into a category: "+strPredictions));
            if(p>50)
            {
                add(new H2("with an accuracy of: "+p));
            }
            else{
                double p2 = 100-p;
                add(new H2("with an accuracy of: "+p2));
            }

            boolean b =photoService.setClassification(photo, strPredictions);
                if(b==true){
                    System.out.println("secsses");
                    remove(this.photoID);
                    photoService.addPhoto(photo, photo.getIdOfUser());
                }
                else
                    System.out.println("not work");
            
          
           

        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Cnut convert");
            System.out.println(strPredictions);
            }
        // Long longPredictions = Long.parseLong(strOfOutpotPhyton);
        // if(longPredictions >0.5)
        // {
        //     Notification.show("Predicted class is Rializem",10000,Position.TOP_CENTER);
           
        // }
        // else{
        //     Notification.show("Predicted class is Abstract",10000,Position.TOP_CENTER);
        // }
        
    }
    
    private static void saveAsPNG(BufferedImage image, String savePath) {
        System.out.println(savePath);
        try {
            // Save the BufferedImage as a PNG file
            String format = "png";
            ImageIO.write(image, format, new File(savePath));
        } catch (IOException e) {
            System.out.println("saveAsPNG ERORR==>>"+e.toString());
            e.printStackTrace();
        }
    }

    
    private static BufferedImage toBufferedImage(Image image) {
        try {
                // Create a BufferedImage with the same dimensions as the Image
            BufferedImage bufferedImage = new BufferedImage(
                Integer.parseInt(image.getWidth()),
                Integer.parseInt(image.getHeight()),

                BufferedImage.TYPE_INT_RGB
                
        );
        
        return bufferedImage;
            
        } catch (Exception e) {
            System.out.println("toBufferedImage ERROR==>>"+e.toString()+"\n");
            return null;
        }
        
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
                photoFileContend =  memoryBuffer.getInputStream().readAllBytes();

                
                //showPhotoOnPage(photoFileContend, uploadPhoto);
                uploadPhoto = new Photo(event.getFileName(), "stam tmuna...", photoFileContend);
                Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
                this.photoID = photoService.addPhoto(uploadPhoto, idUser);
                add(new Button("Send to Evaluation", e -> sendToCNN()));
                add(new Button("Remove Photo", e -> remove((String)VaadinSession.getCurrent().getSession().getAttribute("userId"))));

                //Notification.show("Photo Upload to DB Succeeded!", 5000, Position.TOP_CENTER);
               
                //showPhotoOnPage(photoFileContend, uploadPhoto);
                try{
                ArrayList<Photo> list = photoService.getPhotoByUserId(idUser);
                System.out.println("*****************************");
                System.out.println("Size of photoUser = "+list.size());
                System.out.println("*****************************\n");
                showPhotoOnPage(list.get(list.size()-1).getContend(), uploadPhoto);
                

                
                }
                catch (Exception e){
                    System.out.println("error of photoService====>>\n");
                }
            } catch (Exception e) {

                
                System.out.println("ERROR=======>>creatPhotoUpload\n");
                this.photoID = null;
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
