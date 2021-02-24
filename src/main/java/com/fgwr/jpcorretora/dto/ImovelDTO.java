package com.fgwr.jpcorretora.dto;

import java.io.Serializable;
import java.util.Date;

import com.fgwr.jpcorretora.domain.Imovel;

public class ImovelDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Date dataAngariacao;
	private Integer estadoImovel;
	private String descricao;
	private String corretor;

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
	
	public ImovelDTO() {
		
	}
	
	public ImovelDTO(Imovel obj) {
		id = obj.getId();
		dataAngariacao = obj.getDataAngariacao();
		estadoImovel = obj.getEstadoImovel().getCod();
		descricao = obj.getDescricao();
		corretor = obj.getCorretor();
		danoSala = obj.isDanoSala();
		danoCozinha = obj.isDanoCozinha();
		danoQuarto = obj.isDanoQuarto();
		danoBanheiro = obj.isDanoBanheiro();
		danoDispensa = obj.isDanoDispensa();
		danoAreaServico = obj.isDanoAreaServico();
		danoGaragem = obj.isDanoGaragem();
		danoTomadas = obj.isDanoTomadas();
		danoLampadas = obj.isDanoLampadas();
		danoChuveiro = obj.isDanoChuveiro();
		danoFechaduras = obj.isDanoFechaduras();
		danoChaves = obj.isDanoChaves();
		danoPortaoEletro = obj.isDanoPortaoEletro();
		danoCercaEletrica = obj.isDanoCercaEletrica();
		danoControle = obj.isDanoControle();
		danoInfiltracao = obj.isDanoInfiltracao();
		danoPia = obj.isDanoPia();
		danoVasoSanitario = obj.isDanoVasoSanitario();
		danoBox = obj.isDanoBox();
		danoArCondicionado = obj.isDanoArCondicionado();
		danoMoveisVinculados = obj.isDanoMoveisVinculados();
		danoPortas = obj.isDanoPortas();
		danoJanelas = obj.isDanoJanelas();
		danoPortao = obj.isDanoPortao();
		danoPinturaInterna = obj.isDanoPinturaInterna();
		danoPinturaExterna = obj.isDanoPinturaExterna();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataAngariacao() {
		return dataAngariacao;
	}

	public void setDataAngariacao(Date dataAngariacao) {
		this.dataAngariacao = dataAngariacao;
	}

	public Integer getEstadoImovel() {
		return estadoImovel;
	}

	public void setEstadoImovel(Integer estadoImovel) {
		this.estadoImovel = estadoImovel;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCorretor() {
		return corretor;
	}

	public void setCorretor(String corretor) {
		this.corretor = corretor;
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

	public Date getDataLaudo() {
		return dataLaudo;
	}

	public void setDataLaudo(Date dataLaudo) {
		this.dataLaudo = dataLaudo;
	}
	
	

}
