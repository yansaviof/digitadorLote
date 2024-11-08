package digitador.domain.enums;

public enum EnumBancos {
    BANCO_A(1),
    BANCO_B(2),
    BANCO_C(3);

    private final int numero;

    EnumBancos(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public static EnumBancos fromNumero(int numero) {
        for (EnumBancos banco : values()) {
            if (banco.getNumero() == numero) {
                return banco;
            }
        }
        throw new IllegalArgumentException("Número do banco inválido: " + numero);
    }
}
