package com.fgwr.jpcorretora.dto;

import com.fgwr.jpcorretora.domain.Imovel;

public class ChecklistDTO {
	
	private String campo;
	private String status;
	
	public ChecklistDTO(Imovel imovel) {
		
		
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
