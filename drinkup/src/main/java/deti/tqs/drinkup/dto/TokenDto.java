package deti.tqs.drinkup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private Long id;

    private String token;

    private String errorMessage;

    public boolean isEmpty() {
        return token.isEmpty();
    }
}
