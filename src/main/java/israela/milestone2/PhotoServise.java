package israela.milestone2;

import java.awt.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class PhotoServise {
    private PhotoReposiory photoRepo;


    public PhotoServise(PhotoReposiory photoRepo) {
        this.photoRepo = photoRepo;
    }


    public void addPhoto(Photo photo, Long idUser)
    {
        photo.setIdOfUser(idUser);
        try {
            photoRepo.insert(photo);
            
        } catch (Exception e) {
            
            System.err.println("Error==========>>addPhoto\n");
        }
        
    }

    public ArrayList<Photo> getPhotoById(Long idOfUser)
    {

        ArrayList<Photo> photoList = new ArrayList<>();
        try {
            System.out.println("start getPhotoById=======>>\n");
            try {
                //photoList.addAll(photoRepo.findByIdOfUser(idOfUser));
                photoList = photoRepo.findByIdOfUser(idOfUser);
                
            } catch (Exception e) {
               
                System.out.println("Not work\n"+e.toString());
            }
            
            //photoList.addAll( photoRepo.findByIdOfUser(idOfUser));
           
            
            
            System.out.println("size= "+photoList.size());
            System.out.println("Yes===>>getPhotoById");
        
            return photoList;
        } catch (Exception e) {
            
            System.out.println("ERROR in getPhotoById=====>>>\n");
            return photoList;
        }
        
        
        
    }


    public boolean removPhotoOfUserId(Long id) {

       boolean b =  photoRepo.deleteByIdOfUser(id);
        return b;
    }

    /*public Image generateImage(User user) {
    
    Long id = user.getId();
    StreamResource sr = new StreamResource("user", () ->  {
        User attached = userRepo.findUserById(id);
        ArrayList<Photo> arryPhoto = photoRepo.findPhotoByIdOfUser(id);
        return new ByteArrayInputStream(arryPhoto.get(0).getContend());
    });
    sr.setContentType("image/png");
    Image image = new Image(sr, "profile-picture");
    return image;
}*/

    /* 
    public Image getImg(Long id) {
        ArrayList<Image> arrayImg = photoRepo.findImgoByIdOfUser(id);
        System.err.println("size = "+arrayImg.size()+"\n");
        return arrayImg.get(0);
    }*/

}
