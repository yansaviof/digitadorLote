package digitador.dto.in;

import digitador.domain.LotesDomain;
import digitador.domain.enums.EnumBancos;
import digitador.domain.enums.EnumStatusLote;
import digitador.domain.PropostasDomain;
import digitador.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotesInDTO {
    private Integer lotesId;
    private EnumBancos enumBancos;
    private EnumStatusLote enumStatusLote;
    private Integer totalPropostas;
    private Integer atualPropostas;
    private String usuarioBanco;
    private String senhaBanco;
    private List<PropostasDomain> propostasDomains;

    public LotesInDTO(LotesDomain lotesDomain) {
        this.lotesId = lotesDomain.getLoteId();
        this.enumBancos = lotesDomain.getEnumBancos();
        this.enumStatusLote = lotesDomain.getEnumStatusLote();
        this.usuarioBanco = lotesDomain.getUsuarioBanco();
        this.senhaBanco = lotesDomain.getSenhaBanco();
        this.propostasDomains = lotesDomain.getPropostasDomains();
        this.totalPropostas = (lotesDomain.getPropostasDomains() != null) ? lotesDomain.getPropostasDomains().size() : 0;
        this.atualPropostas = (lotesDomain.getPropostasDomains() != null)
                ? (int) lotesDomain.getPropostasDomains().stream()
                .filter(proposta -> proposta.getEnumStatusPropostas() != null)
                .count()
                :0;
    }

    public static List<LotesInDTO> convert(List<LotesDomain> lotesDomains) {
        return lotesDomains.stream()
                .map(LotesInDTO::new) // Chama o construtor que aceita LotesDomain
                .collect(Collectors.toList());
    }
}
