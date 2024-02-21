package israela.milestone2;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoReposiory extends MongoRepository<Photo, ObjectId>{
    
    public List<Photo> findPhotoByIdOfUser(Long id);
}

