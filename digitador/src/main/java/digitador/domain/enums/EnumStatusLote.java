package digitador.domain.enums;

public enum EnumStatusLote {
    Finalizado(1, "Finalizado"),
    Pendente(2, "Pendente"),
    Aguardando_inicar(3, "Aguardando Iniciar"),
    Iniciado(4, "Iniciado");



    private final int codigo;
    private final String descricao;

    EnumStatusLote(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumStatusLote fromCodigo(int codigo) {
        for (EnumStatusLote status : EnumStatusLote.values()) {
            if (status.getCodigo() == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
