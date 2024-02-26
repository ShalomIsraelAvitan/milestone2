package israela.milestone2;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "photos")
public class Photo {
    
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private byte[] contend; //photo file contend (pixels data)
    private String classification;
    private Long idOfUser;

    public Photo()
    {
        
    }
    public Photo(String name, String description, byte[] contend) {
        this.name = name;
        this.description = description;
        this.dateTime = LocalDateTime.now();
        this.contend = contend;
        this.classification = "Null classification";
        this.idOfUser = null;
    }
    public Photo(String name, String description, LocalDateTime dateTime, byte[] contend, Long idUser) {
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.contend = contend;
        this.idOfUser = idUser;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public byte[] getContend() {
        return contend;
    }
    public void setContend(byte[] contend) {
        this.contend = contend;
    }

    public void setClassification(String classification)
    {
        this.classification = classification;
    }

    public String getClassification()
    {
        return this.classification;
    }
    public Long getIdOfUser() {
        return idOfUser;

    }
    public void setIdOfUser(Long idUser) {
        this.idOfUser = idUser;
    }
    @Override
    public String toString() {
        return "Photo {name=" + name + ", description=" + description + ", dateTime=" + dateTime + ", contend="
                + Arrays.toString(contend) + ", classification=" + classification + ", idOfUser=" + idOfUser + "}";
    }
    
}
