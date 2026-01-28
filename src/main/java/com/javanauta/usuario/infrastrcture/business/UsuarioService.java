package com.javanauta.usuario.infrastrcture.business;


import com.javanauta.usuario.infrastrcture.business.Converter.UsuarioConverter;
import com.javanauta.usuario.infrastrcture.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastrcture.entity.Usuario;
import com.javanauta.usuario.infrastrcture.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

   public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
       Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
       return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
   }
}
