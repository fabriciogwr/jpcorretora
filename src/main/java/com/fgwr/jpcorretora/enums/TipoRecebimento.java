package com.fgwr.jpcorretora.enums;

public enum TipoRecebimento {

    ESPECIE(1, "Espécie"),
    DEPOSITO(2, "Depósito em conta");

    private int cod;
    private String desc;

    private TipoRecebimento(int cod, String desc) {
        this.cod = cod;
        this.desc = desc;
    }

    public int getCod() {
        return cod;
    }

    public String getDesc() {
        return desc;
    }
    
    public static EstadoPagamento toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (EstadoPagamento x : EstadoPagamento.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
		
	}

}