package digitador.dto.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import digitador.domain.LotesDomain;
import digitador.domain.PropostasDomain;
import digitador.domain.enums.EnumStatusPropostas;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropostasInDTOSemLote {
    private Integer propostaId;
    private String cpf;  // CPF do proponente
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento; // Data de nascimento do proponente
    private Integer ddd; // DDD do telefone
    private String telefone; // Telefone do proponente
    private String beneficio; // Tipo de benefício
    private String nome; // Nome do proponente
    private String nacionalidade; // Nacionalidade do proponente
    private String naturalidade; // Naturalidade do proponente
    private String estadoCivil; // Estado civil do proponente
    private String email; // E-mail do proponente
    private String rg; // Registro Geral (RG) do proponente
    private String emissor; // Emissor do RG
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataEmissao; // Data de emissão do RG
    private String cep; // CEP do endereço do proponente
    private String numero; // Número do endereço
    private String nomeMae; // Nome da mãe do proponente
    private String nomePai; // Nome do pai do proponente
    private String uf; // Unidade Federativa
    private String tipoBeneficio; // Tipo do benefício
    private boolean recebeCartaoMagnetico; // Indica se recebe cartão magnético
    private Integer banco; // Código do banco
    private Integer agencia; // Número da agência
    private Integer digitoAgencia; // Dígito da agência
    private String cidadeAgencia; // Cidade da agência
    private Integer conta; // Número da conta
    private Integer digitoConta; // Dígito da conta
    private Integer cpfAgente; // CPF do agente
    private String nomeAgente; // Nome do agente
    private Integer lojaAgente; // Loja do agente
    private EnumStatusPropostas enumStatusPropostas;
    private String linkFormalizacao;

    public PropostasInDTOSemLote(PropostasDomain proposta) {
        this.propostaId = proposta.getPropostaId();
        this.cpf = proposta.getCpf();
        this.dataNascimento = proposta.getDataNascimento();
        this.ddd = proposta.getDdd();
        this.telefone = proposta.getTelefone();
        this.beneficio = proposta.getBeneficio();
        this.nome = proposta.getNome();
        this.nacionalidade = proposta.getNacionalidade();
        this.naturalidade = proposta.getNaturalidade();
        this.estadoCivil = proposta.getEstadoCivil();
        this.email = proposta.getEmail();
        this.rg = proposta.getRg();
        this.emissor = proposta.getEmissor();
        this.dataEmissao = proposta.getDataEmissao();
        this.cep = proposta.getCep();
        this.numero = proposta.getNumero();
        this.nomeMae = proposta.getNomeMae();
        this.nomePai = proposta.getNomePai();
        this.uf = proposta.getUf();
        this.tipoBeneficio = proposta.getTipoBeneficio();
        this.recebeCartaoMagnetico = proposta.isRecebeCartaoMagnetico();
        this.banco = proposta.getBanco();
        this.agencia = proposta.getAgencia();
        this.digitoAgencia = proposta.getDigitoAgencia();
        this.cidadeAgencia = proposta.getCidadeAgencia();
        this.conta = proposta.getConta();
        this.digitoConta = proposta.getDigitoConta();
        this.cpfAgente = proposta.getCpfAgente();
        this.nomeAgente = String.valueOf(proposta.getNomeAgente());
        this.lojaAgente = proposta.getLojaAgente();
        this.enumStatusPropostas = proposta.getEnumStatusPropostas();
        this.linkFormalizacao = proposta.getLinkFormalizacao();
    }

    public static List<PropostasInDTOSemLote> convert(List<PropostasDomain> propostas) {
        return propostas.stream().map(PropostasInDTOSemLote::new).collect(Collectors.toList());
    }
}
