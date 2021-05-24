package com.fgwr.jpcorretora.enums;

public enum TipoMovimento {
	
	RECEITA(1, "Receita (Entrada)"),
	DESPESA(2, "Despesa (Saída)");
	
	private Integer cod;
	private String descricao;
	
	private TipoMovimento(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao () {
		return descricao;
	}
	
	public static TipoMovimento toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (TipoMovimento x : TipoMovimento.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
		
	}
	
	

}
