package com.fgwr.jpcorretora.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fgwr.jpcorretora.domain.Checklist;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;

@Service
public class ImovelService {

	@Autowired
	ImovelRepository repo;

	public Imovel insert(Imovel imv) {
		Imovel imovel = imv;
		repo.save(imovel);
		return imovel;
	}
	
	public Imovel find(Integer id) {

		Optional<Imovel> imovel = repo.findById(id);
		return imovel.orElseThrow(() -> new ObjectNotFoundException("Imovel não encontrado!")); 
		

	}
	
	public List<Checklist> getChecklist(Imovel imv) {
		List<Checklist> checklist = new ArrayList<>();
		List<String> item = new ArrayList<>();
		item.add("Sala");
		item.add("Cozinha");
		item.add("Quarto");
		item.add("Banheiro");
		item.add("Dispensa");
		item.add("Área de Serviço");
		item.add("Garagem");
		item.add("Tomadas");
		item.add("Lâmpadas");
		item.add("Chuveiro");
		item.add("Fechaduras");
		item.add("Chaves");
		item.add("Portão Elétrico");
		item.add("Cerca Elétrica");
		item.add("Controle");
		item.add("Infiltração");
		item.add("Pia");
		item.add("Vaso Sanitário");
		item.add("Box");
		item.add("Ar Condicionado");
		item.add("Móveis Vinculados");
		item.add("Portas");
		item.add("Janelas");
		item.add("Portão");
		item.add("Pintura Interna");
		item.add("Pintura Externa");
		
		List<Boolean> status = new ArrayList<>();
		status.add(imv.isDanoSala());
		status.add(imv.isDanoCozinha());
		status.add(imv.isDanoQuarto());
		status.add(imv.isDanoBanheiro());
		status.add(imv.isDanoDispensa());
		status.add(imv.isDanoAreaServico());
		status.add(imv.isDanoGaragem());
		status.add(imv.isDanoTomadas());
		status.add(imv.isDanoLampadas());
		status.add(imv.isDanoChuveiro());
		status.add(imv.isDanoFechaduras());
		status.add(imv.isDanoChaves());
		status.add(imv.isDanoPortaoEletro());
		status.add(imv.isDanoCercaEletrica());
		status.add(imv.isDanoControle());
		status.add(imv.isDanoInfiltracao());
		status.add(imv.isDanoPia());
		status.add(imv.isDanoVasoSanitario());
		status.add(imv.isDanoBox());
		status.add(imv.isDanoArCondicionado());
		status.add(imv.isDanoMoveisVinculados());
		status.add(imv.isDanoPortas());
		status.add(imv.isDanoJanelas());
		status.add(imv.isDanoPortao());
		status.add(imv.isDanoPinturaInterna());
		status.add(imv.isDanoPinturaExterna());
		
		for (int i=0; i<26; i++ ) {
			String temp;
			if(status.get(i) == true) {
				temp = "sim";
			} else {
				temp = "não";
			}
			Checklist chk = new Checklist(item.get(i), temp);
			checklist.add(chk);
		}
		return checklist;
	} 
	
	

	public List<Imovel> findAll() {
		return repo.findAll();
	}
	
	
	
/*public List<Imovel> findCpfOuCnpj(String cpfOuCnpj) {
		
		List<Imovel> dup = repo.findCpfOuCnpj(cpfOuCnpj);
		if (dup == null) {
			throw new ObjectNotFoundException("Imovels não encontradas!");
		}
		return dup;
	}*/
	
	public Imovel findByContrato(Contrato cont) {
		return repo.findByContrato(cont);
	}



	

	public Imovel findByCliente(String nome) {

		Imovel imovel = repo.findByCliente(nome);
		if (imovel == null) {
			throw new ObjectNotFoundException("Imovels não encontradas!");
		}
		return imovel;
	}
}
