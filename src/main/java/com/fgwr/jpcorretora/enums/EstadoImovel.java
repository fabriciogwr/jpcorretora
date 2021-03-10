package com.fgwr.jpcorretora.enums;

public enum EstadoImovel {
	
	NOVO(1, "Imóvel Novo"),
	USADO(2, "Imóvel Usado");
	
	private Integer cod;
	private String descricao;
	
	private EstadoImovel(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao () {
		return descricao;
	}
	
	public static EstadoImovel toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (EstadoImovel x : EstadoImovel.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
		
	}
	
	public static EstadoImovel valueOfDescricao(String desc) {
	    for (EstadoImovel estadoImovel: values()) {
	        if (estadoImovel.descricao.equals(desc)) {
	            return estadoImovel;
	        }
	    }
	    return null;
	}

}
