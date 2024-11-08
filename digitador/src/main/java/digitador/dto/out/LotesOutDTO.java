package digitador.dto.out;

import digitador.domain.PropostasDomain;
import digitador.domain.enums.EnumBancos;
import digitador.domain.enums.EnumStatusLote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotesOutDTO {

    private Integer loteId;

}
