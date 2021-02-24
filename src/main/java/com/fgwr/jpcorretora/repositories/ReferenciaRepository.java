package com.fgwr.jpcorretora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fgwr.jpcorretora.domain.Referencia;

@Repository
public interface ReferenciaRepository extends JpaRepository <Referencia, Integer> {

}
