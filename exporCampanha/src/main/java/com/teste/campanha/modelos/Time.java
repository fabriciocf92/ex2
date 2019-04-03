package com.teste.campanha.modelos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
public class Time {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonSerialize
	@JsonDeserialize
	private Long id;
	
	@Column(columnDefinition = "text", unique = true)
	private String nome;
	
	@OneToMany(mappedBy = "time")
	private List<Campanha> campanhas = new ArrayList<>();
	
	@OneToMany(mappedBy = "time")
	private List<Cliente> clientes = new ArrayList<>();
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
