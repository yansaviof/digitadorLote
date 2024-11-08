package digitador.domain;


import digitador.domain.enums.EnumBancos;
import digitador.domain.enums.EnumStatusLote;
import digitador.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LotesDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loteId;
    private EnumBancos enumBancos;
    private EnumStatusLote enumStatusLote;
    private String usuarioBanco;
    private String senhaBanco;
    private Integer totalPropostas;
    private Integer atualPropostas;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true,mappedBy = "loteDomain", fetch = FetchType.LAZY)
    private List<PropostasDomain> propostasDomains;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PreUpdate
    public void atualizarTotalPropostas() {
        if (propostasDomains != null) {
            this.totalPropostas = propostasDomains.size();
        } else {
            this.totalPropostas = 0;
        }
    }
}
