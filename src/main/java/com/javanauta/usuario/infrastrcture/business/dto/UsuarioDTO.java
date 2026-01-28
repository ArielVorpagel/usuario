package com.javanauta.usuario.infrastrcture.business.dto;


import com.javanauta.usuario.infrastrcture.entity.Telefone;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private List<EnderecoDTO> enderecoDTO;
    private List<TelefoneDTO> telefoneDTO;
}
