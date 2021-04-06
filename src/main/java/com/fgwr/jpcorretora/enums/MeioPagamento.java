package com.fgwr.jpcorretora.enums;

public enum MeioPagamento {
	ESPECIE(1, "Espécie"),
	PIX(2, "Pix"),
	DOC(3, "Transferência DOC"),
	TED(4, "Transferência TED"),
	CHEQUE(5, "Cheque");

	private Integer cod;
	private String descricao;
	
	MeioPagamento(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static MeioPagamento toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (MeioPagamento x : MeioPagamento.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
		
	}
	
	public static MeioPagamento valueOfDescricao(String desc) {
	    for (MeioPagamento meioPagamento: values()) {
	        if (meioPagamento.descricao.equals(desc)) {
	            return meioPagamento;
	        }
	    }
	    return null;
	}

}


