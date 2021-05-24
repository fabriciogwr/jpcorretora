package com.fgwr.jpcorretora.domain;

import java.util.Date;

public class ReciboAvulso {
	
	private String pagador;
	private String cpfPagador;
	private String recebedor;
	private String cpfRecebedor;
	private String referenteA;
	private Double valorPago;
	private Despesa despesa;
	private Receita receita;
	private Date dataPgto;
	
	public ReciboAvulso() {
		
	}

	public ReciboAvulso(String pagador, String cpfPagador, String recebedor, String cpfRecebedor, String referenteA, Double valorPago, Despesa despesa,
			Date dataPgto) {
		super();
		this.pagador = pagador;
		this.cpfPagador = cpfPagador;
		this.recebedor = recebedor;
		this.cpfRecebedor = cpfRecebedor;
		this.referenteA = referenteA;
		this.valorPago = valorPago;
		this.despesa = despesa;
		this.dataPgto = dataPgto;
	}
	
	public ReciboAvulso(String pagador, String cpfPagador, String recebedor, String cpfRecebedor, String referenteA, Double valorPago, Receita receita,
			Date dataPgto) {
		super();
		this.pagador = pagador;
		this.cpfPagador = cpfPagador;
		this.recebedor = recebedor;
		this.cpfRecebedor = cpfRecebedor;
		this.referenteA = referenteA;
		this.valorPago = valorPago;
		this.receita = receita;
		this.dataPgto = dataPgto;
	}

	public String getPagador() {
		return pagador;
	}

	public void setPagador(String pagador) {
		this.pagador = pagador;
	}

	public String getCpfPagador() {
		return cpfPagador;
	}

	public void setCpfPagador(String cpfPagador) {
		this.cpfPagador = cpfPagador;
	}

	public String getCpfRecebedor() {
		return cpfRecebedor;
	}

	public void setCpfRecebedor(String cpfRecebedor) {
		this.cpfRecebedor = cpfRecebedor;
	}

	public String getRecebedor() {
		return recebedor;
	}

	public void setRecebedor(String recebedor) {
		this.recebedor = recebedor;
	}

	public String getReferenteA() {
		return referenteA;
	}

	public void setReferenteA(String referenteA) {
		this.referenteA = referenteA;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}
	
	public Receita getReceita() {
		return receita;
	}

	public void setReceita(Receita receita) {
		this.receita = receita;
	}

	public Date getDataPgto() {
		return dataPgto;
	}

	public void setDataPgto(Date dataPgto) {
		this.dataPgto = dataPgto;
	}
	


}
