package com.fgwr.jpcorretora.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.domain.Categoria;
import com.fgwr.jpcorretora.enums.TipoMovimento;
import com.fgwr.jpcorretora.repositories.CategoriaRepository;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.repositories.ReciboRepository;

@Service
public class PopularBanco {

	@Autowired
	ImovelRepository imovelRepository;

	@Autowired
	DadosBancariosRepository dadosBancariosRepository;

	@Autowired
	ContratoRepository contratoRepository;

	@Autowired
	DuplicataRepository duplicataRepository;

	@Autowired
	ReciboRepository reciboRepository;

	@Autowired
	CategoriaRepository cr;

	@Autowired
	ClienteService cs;

	@Autowired
	DuplicataService ds;

	@Autowired
	ImovelService is;

	@Transactional
	public void popularBanco() {

		List<Categoria> cats = cr.findAll();
		List<Categoria> catsCreate = new ArrayList<>();
		int createCL = 1;
		int createCV = 1;
		int createVI = 1;
		int createCC = 1;
		int createAV = 1;

		Categoria categoria1 = new Categoria(1, TipoMovimento.RECEITA, "Comissão Locação",
				"Comissão de mensalidade de contrato de locação");
		Categoria categoria2 = new Categoria(2, TipoMovimento.RECEITA, "Comissão Venda", "Comissão de venda de imóvel");
		Categoria categoria3 = new Categoria(3, TipoMovimento.RECEITA, "Vistoria de Imóvel",
				"Serviço de Vistoria em Imóvel");
		Categoria categoria4 = new Categoria(4, TipoMovimento.RECEITA, "Avaliação de Imóvel",
				"Serviço de Avaliação de imóvel");
		Categoria categoria5 = new Categoria(5, TipoMovimento.RECEITA, "Confecção de Contrato",
				"Serviço de Confecção de Contratos Avulsos");

		for (Categoria categoria : cats) {
			if (categoria.equals(categoria1)) {
				createCL = 0;
			}
			if (categoria.equals(categoria2)) {
				createCV = 0;
			}
			if (categoria.equals(categoria3)) {
				createCC = 0;
			}
			if (categoria.equals(categoria4)) {
				createVI = 0;
			}
			if (categoria.equals(categoria5)) {
				createAV = 0;
			}
		}

		if (createCL == 1) {
			catsCreate.add(categoria1);
		}
		if (createCV == 1) {
			catsCreate.add(categoria2);
		}
		if (createVI == 1) {
			catsCreate.add(categoria3);
		}
		if (createAV == 1) {
			catsCreate.add(categoria4);
		}
		if (createCC == 1) {
			catsCreate.add(categoria5);
		}
		cr.saveAll(catsCreate);

	}
}
