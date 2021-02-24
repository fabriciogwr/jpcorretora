package com.fgwr.jpcorretora.resources;

import org.springframework.beans.factory.annotation.Autowired;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.dto.ClienteDTO;
import com.fgwr.jpcorretora.dto.ClienteNewDTO;
import com.fgwr.jpcorretora.services.ClienteService;

public class ClienteResource {
	
	@Autowired
	ClienteService cs;
	
	public Cliente find(Integer id) {
		Cliente obj = cs.find(id);
		return obj;
	}
	
	/*public Cliente find(String cpfOuCnpj) {
		Cliente obj = cs.findByCpfOuCnpj(cpfOuCnpj);
		return obj;
	}*/
	
	public void update(ClienteDTO objDto, Integer id) {
		Cliente obj = cs.fromDTO(objDto);
		obj.setId(id);
		obj = cs.update(obj);
		
	}
	
	public Integer insert(ClienteNewDTO objDto) {
		Cliente obj = cs.fromDTO(objDto);
		obj = cs.insert(obj);
		return obj.getId();
	}
	
	

}
