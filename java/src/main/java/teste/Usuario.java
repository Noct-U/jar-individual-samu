package teste;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class Usuario {
    private String nome;
    private String senha;

    public Usuario(){}

    public Usuario(String nome, String senha){
        this.nome = nome;
        this.senha = senha;
    }



    public void cadastrar() {
        System.out.print("Insira seu nome de usuário: ");

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void resultadoLogin(Boolean resultado,String usuario){

        if (resultado == true){

            //MENSAGEM BONITINHA
            System.out.println("""
                                *-------------------------*
                                LOGIN REALIZADO COM SUCESSO
                                ***----- BEM VINDO -----***
                                           %s
                                *-------------------------*""".formatted(usuario));
        }
        else {
            System.out.println("HOUVE UM ERRO:\nLogin ou senha inválidos");
        }
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
