package com.fgwr.jpcorretora.enums;

public enum EstadoCivil {
	
	SOLTEIRO(1, "Solteiro(a)"),
	CASADO(2, "Casado(a)"),
	DIVORCIADO(3, "Divorciado(a)"),
	VIUVO(4, "Viúvo(a)"),
	SEPARADOJUDICIALMENTE(5, "Separado(a) Judicialmente"),
	UNIAOESTAVEL(6, "União Estável/Convivente (Não Judicial)");
	
	private Integer cod;
	private String descricao;
	
	private EstadoCivil(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao () {
		return descricao;
	}
	
	public static EstadoCivil toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (EstadoCivil x : EstadoCivil.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
		
	}
	
	public static EstadoCivil valueOfDescricao(String desc) {
	    for (EstadoCivil estadoCivil: values()) {
	        if (estadoCivil.descricao.equals(desc)) {
	            return estadoCivil;
	        }
	    }
	    return null;
	}

}
