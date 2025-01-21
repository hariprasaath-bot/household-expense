package in.house.financial.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@Document(collection = "accepted_status_master")
public class AcceptedStatusMaster {

    @Id
    private String id;

   @Field(name = "accepted_status_master_name")
    private String acceptedStatusMasterName;

}
