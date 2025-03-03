package in.house.financial.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UploadProgress {
    private String currentProgress;
    private Integer status;
}
