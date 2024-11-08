package digitador.domain.enums;

public enum EnumStatusPropostas {
    Pendente(1, "Pendente"),
    Digitado(2, "Digitado"),
    Erro(3, "Erro");


    private final int codigo;
    private final String descricao;

    EnumStatusPropostas(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumStatusPropostas fromCodigo(int codigo) {
        for (EnumStatusPropostas status : EnumStatusPropostas.values()) {
            if (status.getCodigo() == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
