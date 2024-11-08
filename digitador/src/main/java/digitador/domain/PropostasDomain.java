package digitador.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import digitador.domain.enums.EnumBancos;
import digitador.domain.enums.EnumStatusPropostas;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PropostasDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer propostaId;
    private String cpf;
    private LocalDate dataNascimento;
    private Integer ddd;
    private String telefone;
    private String beneficio;
    private String nome;
    private String nacionalidade;
    private String naturalidade;
    private String estadoCivil;
    private String email;
    private String rg;
    private String emissor;
    private LocalDate dataEmissao;
    private String cep;
    private String numero;
    private String nomeMae;
    private String nomePai;
    private String uf;
    private String tipoBeneficio;
    private boolean recebeCartaoMagnetico;
    private Integer banco;
    private Integer agencia;
    private Integer digitoAgencia;
    private String cidadeAgencia;
    private Integer conta;
    private Integer digitoConta;
    private Integer cpfAgente;
    private String nomeAgente;
    private Integer lojaAgente;
    @ManyToOne
    @JoinColumn(name = "idLote")
    @JsonIgnore
    private LotesDomain loteDomain;
    private EnumStatusPropostas enumStatusPropostas;
    private String linkFormalizacao;

}
