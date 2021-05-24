package com.fgwr.jpcorretora.enums;

public enum Meses {
	
	JANEIRO(0, "Janeiro"),
	FEVEREIRO(1, "Fevereiro"),
	MARCO(2, "Março"),
	ABRIL(3, "Abril"),
	MAIO(4, "Maio"),
	JUNHO(5, "Junho"),
	JULHO(6, "Julho"),
	AGOSTO(7, "Agosto"),
	SETEMBRO(8, "Setembro"),
	OUTUBRO(9, "Outubro"),
	NOVEMBRO(10, "Novembro"),
	DEZEMBRO(11, "Dezembro");
	
	private Integer cod;
	private String mes;
	
	private Meses(int cod, String mes) {
		this.cod = cod;
		this.mes = mes;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao () {
		return mes;
	}
	
	public static Meses toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (Meses x : Meses.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
		
	}
	
	public static Meses valueOfDescricao(String mes) {
	    for (Meses meses: values()) {
	        if (meses.mes.equals(mes)) {
	            return meses;
	        }
	    }
	    return null;
	}

}
