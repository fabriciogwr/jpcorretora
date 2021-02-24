package com.fgwr.jpcorretora.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.services.DuplicataService;

public class DuplicataResource {
	
	@Autowired
	DuplicataService ds;
	
	public Duplicata find(Integer id) {
		Duplicata obj = ds.find(id);
		return obj;
	}
	
	/*public List<Duplicata> find(String cpfOuCnpj) {
		List<Duplicata> obj = ds.findCpfOuCnpj(cpfOuCnpj);
		return obj;
	}*/
	
/*	public void update(Duplicata dup, Integer id) {
		Duplicata obj = ds.fromDTO(objDto);
		obj.setId(id);
		obj = ds.update(obj);
		
	} */
	
	

}
