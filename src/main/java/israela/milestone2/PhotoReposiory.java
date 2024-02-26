package israela.milestone2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vaadin.flow.component.html.Image;

@Repository
public interface PhotoReposiory extends MongoRepository<Photo, ObjectId>{
    
    public ArrayList<Photo> findByIdOfUser(Long idOfUser);
    public boolean deleteByIdOfUser(Long id);
    //public ArrayList<Image> findImgoByIdOfUser(Long id);
    
 }

