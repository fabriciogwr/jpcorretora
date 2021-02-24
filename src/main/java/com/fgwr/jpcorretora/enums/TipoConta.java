package com.fgwr.jpcorretora.enums;

public enum TipoConta {

    CORRENTEPF(1, "Conta Corrente - PF"),
    CORRENTEPJ(2, "Conta Corrente - PJ"),
    POUPANCAPF(3, "Conta Poupança - PF"),
    POUPANCAPJ(4, "Conta Poupança - PJ");

    private int cod;
    private String desc;

    private TipoConta(int cod, String desc) {
        this.cod = cod;
        this.desc = desc;
    }

    public int getCod() {
        return cod;
    }

    public String getDesc() {
        return desc;
    }
    
    public static TipoConta toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (TipoConta x : TipoConta.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
		
	}
}