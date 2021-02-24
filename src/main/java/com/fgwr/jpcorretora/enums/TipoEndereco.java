package com.fgwr.jpcorretora.enums;

public enum TipoEndereco {

    ENDERECORESIDENCIAL(1, "Endereço Residencial"),
    ENDERECOCOMERCIAL(2, "Endereço Comercial"),
    ENDERECOCORRESP(3, "Endereço de Correspondência"),
    IMOVELLOCACAO(4, "Imóvel para locação");

    private int cod;
    private String desc;

    private TipoEndereco(int cod, String desc) {
        this.cod = cod;
        this.desc = desc;
    }

    public int getCod() {
        return cod;
    }

    public String getDesc() {
        return desc;
    }
    
    public static TipoEndereco toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (TipoEndereco x : TipoEndereco.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
		
	}
}