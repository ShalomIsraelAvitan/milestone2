package israela.milestone2;
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
        ArrayList<Photo> photoList = new ArrayList<Photo>();

        photoList = (ArrayList<Photo>)photoRepo.findPhotoByIdOfUser(idOfUser);

        return photoList;
    }

}
