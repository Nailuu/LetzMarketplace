package lu.letzmarketplace.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultipleErrorsResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private Map<String, String> errors;
    private String path;
}
