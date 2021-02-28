package com.fgwr.jpcorretora.enums;

public enum Banco {
	
	BANCODOBRASIL(1, "Banco do Brasil"),
	BANCOINTERSA(77, "Banco Inter S.A."),
	CAIXAECONOMICAFEDERAL(104, "Caixa Econômica Federal");
	
	
	private Integer cod;
	private String descricao;
	
	
	private Banco(Integer cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}


	public Integer getCod() {
		return cod;
	}
	
	public String getFullCod() {
		String codtemp = "";
		if (cod <100 && cod >= 10) {
			codtemp = "0" + cod.toString();
		} else if (cod <10) {
			codtemp = "00" + cod.toString();
			
		} else {
			codtemp = cod.toString();
		}
		return codtemp;
		
	}


	public void setCod(Integer cod) {
		this.cod = cod;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static Banco toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (Banco x : Banco.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
		
	}
	
	public static Banco valueOfDescricao(String desc) {
	    for (Banco banco: values()) {
	        if (banco.descricao.equals(desc)) {
	            return banco;
	        }
	    }
	    return null;
	}
}
