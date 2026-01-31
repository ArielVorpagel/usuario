package com.javanauta.usuario.infrastrcture.business;


import com.javanauta.usuario.infrastrcture.business.Converter.UsuarioConverter;
import com.javanauta.usuario.infrastrcture.business.dto.EnderecoDTO;
import com.javanauta.usuario.infrastrcture.business.dto.TelefoneDTO;
import com.javanauta.usuario.infrastrcture.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastrcture.entity.Endereco;
import com.javanauta.usuario.infrastrcture.entity.Telefone;
import com.javanauta.usuario.infrastrcture.entity.Usuario;
import com.javanauta.usuario.infrastrcture.exceptions.ConflictExceptions;
import com.javanauta.usuario.infrastrcture.exceptions.ResourceNotFoundException;
import com.javanauta.usuario.infrastrcture.repository.EnderecoRepository;
import com.javanauta.usuario.infrastrcture.repository.TelefoneRepository;
import com.javanauta.usuario.infrastrcture.repository.UsuarioRepository;
import com.javanauta.usuario.infrastrcture.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        try {
            if (verificaEmailExistente(email)) {
                throw new ConflictExceptions("Email já cadastrado!! ");
            }
        } catch (ConflictExceptions c) {
            throw new ConflictExceptions("Email já cadastrado!! " + c.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado!! " + email)));

        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encontrado!! " + email);
        }
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }



    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO) {
        //Aqui eu salvo o email na vaiavel através do Token
        String email = jwtUtil.extraiEmailToken(token.substring(7));

        //aqui  eu retorno o objeto com todas as informações do usuario através do email
        Usuario usuarioentity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("EMAIL NÃO ENCONTRADO "));

        //Croptografia de senha
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ?
                passwordEncoder.encode(usuarioDTO.getSenha()) : null);

        //aqui eu atualizo a entidade(Usuario) através dos dados recebidos
        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioentity);

        //salvo os dados do usuario no db_usuario, e logo após transformo em DTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }



    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){

        Endereco enderecoEntity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("ID não encontrado" + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, enderecoEntity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO){

        Telefone telefoneEntity = telefoneRepository.findById(idTelefone).orElseThrow(()->
                new ResourceNotFoundException("ID não encontrado " + idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, telefoneEntity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }


    public EnderecoDTO addEndereco(String token, EnderecoDTO enderecoDTO){
        String email = jwtUtil.extraiEmailToken(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("EMAIL não encontrado " + email));

        Endereco endereco = usuarioConverter.paraEnderecoEntity(enderecoDTO, usuario.getId());

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));

    }

    public TelefoneDTO addTelefone(String token, TelefoneDTO telefoneDTO){
        String email = jwtUtil.extraiEmailToken(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("EMAIL não encontrado " + email));

        Telefone telefone = usuarioConverter.parTelefoneEntity(telefoneDTO, usuario.getId());

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }
}