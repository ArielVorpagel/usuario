package com.javanauta.usuario.infrastrcture.repository;

import com.javanauta.usuario.infrastrcture.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
