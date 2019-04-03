package com.teste.campanha.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teste.campanha.modelos.Cliente;

@Repository
public interface RepositorioCliente extends JpaRepository<Cliente, Long>{

}
