package com.fgwr.jpcorretora.dto;

import java.util.Date;

public class ImovelChecklistDTO {

	private boolean control=false;
	private boolean danoSala;
	private boolean danoCozinha;
	private boolean danoQuarto;
	private boolean danoBanheiro;
	private boolean danoDispensa;
	private boolean danoAreaServico;
	private boolean danoGaragem;
	private boolean danoTomadas;
	private boolean danoLampadas;
	private boolean danoChuveiro;
	private boolean danoFechaduras;
	private boolean danoChaves;
	private boolean danoPortaoEletro;
	private boolean danoCercaEletrica;
	private boolean danoControle;
	private boolean danoInfiltracao;
	private boolean danoPia;
	private boolean danoVasoSanitario;
	private boolean danoBox;
	private boolean danoArCondicionado;
	private boolean danoMoveisVinculados;
	private boolean danoPortas;
	private boolean danoJanelas;
	private boolean danoPortao;
	private boolean danoPinturaInterna;
	private boolean danoPinturaExterna;
	private String obs;
	
	private Date dataLaudo;
	
	public ImovelChecklistDTO() {
		
	}

	public ImovelChecklistDTO(boolean danoSala, boolean danoCozinha, boolean danoQuarto, boolean danoBanheiro,
			boolean danoDispensa, boolean danoAreaServico, boolean danoGaragem, boolean danoTomadas,
			boolean danoLampadas, boolean danoChuveiro, boolean danoFechaduras, boolean danoChaves,
			boolean danoPortaoEletro, boolean danoCercaEletrica, boolean danoControle, boolean danoInfiltracao,
			boolean danoPia, boolean danoVasoSanitario, boolean danoBox, boolean danoArCondicionado,
			boolean danoMoveisVinculados, boolean danoPortas, boolean danoJanelas, boolean danoPortao,
			boolean danoPinturaInterna, boolean danoPinturaExterna, String obs) {
		super();
		this.danoSala = danoSala;
		this.danoCozinha = danoCozinha;
		this.danoQuarto = danoQuarto;
		this.danoBanheiro = danoBanheiro;
		this.danoDispensa = danoDispensa;
		this.danoAreaServico = danoAreaServico;
		this.danoGaragem = danoGaragem;
		this.danoTomadas = danoTomadas;
		this.danoLampadas = danoLampadas;
		this.danoChuveiro = danoChuveiro;
		this.danoFechaduras = danoFechaduras;
		this.danoChaves = danoChaves;
		this.danoPortaoEletro = danoPortaoEletro;
		this.danoCercaEletrica = danoCercaEletrica;
		this.danoControle = danoControle;
		this.danoInfiltracao = danoInfiltracao;
		this.danoPia = danoPia;
		this.danoVasoSanitario = danoVasoSanitario;
		this.danoBox = danoBox;
		this.danoArCondicionado = danoArCondicionado;
		this.danoMoveisVinculados = danoMoveisVinculados;
		this.danoPortas = danoPortas;
		this.danoJanelas = danoJanelas;
		this.danoPortao = danoPortao;
		this.danoPinturaInterna = danoPinturaInterna;
		this.danoPinturaExterna = danoPinturaExterna;
		this.obs = obs;
	}



	public boolean isDanoSala() {
		return danoSala;
	}

	public void setDanoSala(boolean danoSala) {
		this.danoSala = danoSala;
	}

	public boolean isDanoCozinha() {
		return danoCozinha;
	}

	public void setDanoCozinha(boolean danoCozinha) {
		this.danoCozinha = danoCozinha;
	}

	public boolean isDanoQuarto() {
		return danoQuarto;
	}

	public void setDanoQuarto(boolean danoQuarto) {
		this.danoQuarto = danoQuarto;
	}

	public boolean isDanoBanheiro() {
		return danoBanheiro;
	}

	public void setDanoBanheiro(boolean danoBanheiro) {
		this.danoBanheiro = danoBanheiro;
	}

	public boolean isDanoDispensa() {
		return danoDispensa;
	}

	public void setDanoDispensa(boolean danoDispensa) {
		this.danoDispensa = danoDispensa;
	}

	public boolean isDanoAreaServico() {
		return danoAreaServico;
	}

	public void setDanoAreaServico(boolean danoAreaServico) {
		this.danoAreaServico = danoAreaServico;
	}

	public boolean isDanoGaragem() {
		return danoGaragem;
	}

	public void setDanoGaragem(boolean danoGaragem) {
		this.danoGaragem = danoGaragem;
	}

	public boolean isDanoTomadas() {
		return danoTomadas;
	}

	public void setDanoTomadas(boolean danoTomadas) {
		this.danoTomadas = danoTomadas;
	}

	public boolean isDanoLampadas() {
		return danoLampadas;
	}

	public void setDanoLampadas(boolean danoLampadas) {
		this.danoLampadas = danoLampadas;
	}

	public boolean isDanoChuveiro() {
		return danoChuveiro;
	}

	public void setDanoChuveiro(boolean danoChuveiro) {
		this.danoChuveiro = danoChuveiro;
	}

	public boolean isDanoFechaduras() {
		return danoFechaduras;
	}

	public void setDanoFechaduras(boolean danoFechaduras) {
		this.danoFechaduras = danoFechaduras;
	}

	public boolean isDanoChaves() {
		return danoChaves;
	}

	public void setDanoChaves(boolean danoChaves) {
		this.danoChaves = danoChaves;
	}

	public boolean isDanoPortaoEletro() {
		return danoPortaoEletro;
	}

	public void setDanoPortaoEletro(boolean danoPortaoEletro) {
		this.danoPortaoEletro = danoPortaoEletro;
	}

	public boolean isDanoCercaEletrica() {
		return danoCercaEletrica;
	}

	public void setDanoCercaEletrica(boolean danoCercaEletrica) {
		this.danoCercaEletrica = danoCercaEletrica;
	}

	public boolean isDanoControle() {
		return danoControle;
	}

	public void setDanoControle(boolean danoControle) {
		this.danoControle = danoControle;
	}

	public boolean isDanoInfiltracao() {
		return danoInfiltracao;
	}

	public void setDanoInfiltracao(boolean danoInfiltracao) {
		this.danoInfiltracao = danoInfiltracao;
	}

	public boolean isDanoPia() {
		return danoPia;
	}

	public void setDanoPia(boolean danoPia) {
		this.danoPia = danoPia;
	}

	public boolean isDanoVasoSanitario() {
		return danoVasoSanitario;
	}

	public void setDanoVasoSanitario(boolean danoVasoSanitario) {
		this.danoVasoSanitario = danoVasoSanitario;
	}

	public boolean isDanoBox() {
		return danoBox;
	}

	public void setDanoBox(boolean danoBox) {
		this.danoBox = danoBox;
	}

	public boolean isDanoArCondicionado() {
		return danoArCondicionado;
	}

	public void setDanoArCondicionado(boolean danoArCondicionado) {
		this.danoArCondicionado = danoArCondicionado;
	}

	public boolean isDanoMoveisVinculados() {
		return danoMoveisVinculados;
	}

	public void setDanoMoveisVinculados(boolean danoMoveisVinculados) {
		this.danoMoveisVinculados = danoMoveisVinculados;
	}

	public boolean isDanoPortas() {
		return danoPortas;
	}

	public void setDanoPortas(boolean danoPortas) {
		this.danoPortas = danoPortas;
	}

	public boolean isDanoJanelas() {
		return danoJanelas;
	}

	public void setDanoJanelas(boolean danoJanelas) {
		this.danoJanelas = danoJanelas;
	}

	public boolean isDanoPortao() {
		return danoPortao;
	}

	public void setDanoPortao(boolean danoPortao) {
		this.danoPortao = danoPortao;
	}

	public boolean isDanoPinturaInterna() {
		return danoPinturaInterna;
	}

	public void setDanoPinturaInterna(boolean danoPinturaInterna) {
		this.danoPinturaInterna = danoPinturaInterna;
	}

	public boolean isDanoPinturaExterna() {
		return danoPinturaExterna;
	}

	public void setDanoPinturaExterna(boolean danoPinturaExterna) {
		this.danoPinturaExterna = danoPinturaExterna;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public boolean isControl() {
		return control;
	}

	public void setControl(boolean control) {
		this.control = control;
	}

	public Date getDataLaudo() {
		return dataLaudo;
	}

	public void setDataLaudo(Date dataLaudo) {
		this.dataLaudo = dataLaudo;
	}

}
