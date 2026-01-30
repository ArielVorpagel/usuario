package com.javanauta.usuario.infrastrcture.business;


import com.javanauta.usuario.infrastrcture.business.Converter.UsuarioConverter;
import com.javanauta.usuario.infrastrcture.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastrcture.entity.Usuario;
import com.javanauta.usuario.infrastrcture.exceptions.ConflictExceptions;
import com.javanauta.usuario.infrastrcture.exceptions.ResourceNotFoundException;
import com.javanauta.usuario.infrastrcture.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

   public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
       emailExiste(usuarioDTO.getEmail());
       usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
       Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
       return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
   }
    public void emailExiste(String email){
        try {
            if (verificaEmailExistente(email)){
                throw new ConflictExceptions("Email já cadastrado!! ");
            }
        }
        catch (ConflictExceptions c){
            throw new ConflictExceptions("Email já cadastrado!! " + c.getCause());
        }
    }
    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email não encontrado!! " + email));
    }
    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }
}
