package teste;

import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.janelas.JanelaGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.mysql.cj.x.protobuf.MysqlxResultset;
import dao.NoctuDao;
import oshi.SystemInfo;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestePrototipo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Scanner inText = new Scanner(System.in);

        SystemInfo si = new SystemInfo();
        NoctuDao dao = new NoctuDao();
        Processador processador = new Processador();
        Memoria memoria = new Memoria();
        DiscoGrupo grupoDeDiscos = new DiscoGrupo();
        List<Disco> discos = grupoDeDiscos.getDiscos();
        List<Volume> volumes = grupoDeDiscos.getVolumes();
        JanelaGrupo grupoDeJanelas = new JanelaGrupo(si);

        // FORMATAR DATA E HORA NO FORMATO MYSQL
        DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        Integer opcaoEscolhida = -1;
        List<Usuario> usuarios = new ArrayList<>();
        do {
            System.out.println("""
                                        
                    SISTEMA DE MONITORAMENTO NOCT.U
                    1) Cadastro
                    2) Login         
                    0) Sair""");
            opcaoEscolhida = in.nextInt();

            switch (opcaoEscolhida) {
                case 1:
                    System.out.print("Digite seu nome de usuário: ");
                    String usuario = inText.nextLine();

                    System.out.print("Digite sua nova senha: ");
                    String senha = inText.nextLine();

                    System.out.print("Digite sua senha novamente: ");
                    String senhaConfirmacao = inText.nextLine();

                    if (senha.equals(senhaConfirmacao)) {
                        Usuario user = new Usuario(usuario, senha);
                        usuarios.add(user);
                        System.out.println("Usuário criado com sucesso!");
                    } else {
                        System.out.println("As senhas não coincidem");
                    }
                    break;
                case 2:
                    System.out.print("Digite seu usuário: ");
                    String usuarioLogin = inText.nextLine();

                    System.out.print("Digite sua senha: ");
                    String senhaLogin = inText.nextLine();

                    Usuario userLogin = new Usuario(usuarioLogin, senhaLogin);

                    Boolean exists = false;
                    for (Usuario u : usuarios) {
                        if (u.getNome().equals(userLogin.getNome()) && u.getSenha().equals(userLogin.getSenha())) {
                            exists = true;
                        }
                    }
                    if (exists) {
                        System.out.println("""
                                Logando...
                                Criando ficha técnica""");

                        // ADICIONANDO A FICHA TECNICA (INSERINDO HARDWARE NO BANCO)
                        Hardware hardwareCPU = new Hardware(processador.getNome(), 100.0, 1);
                        Hardware hardwareMemoria = new Hardware("RAM", memoria.getTotal().doubleValue(), 2);

                        dao.adicionarHardware(hardwareCPU);
                        dao.adicionarHardware(hardwareMemoria);

                        Hardware hardwareDisco = new Hardware();
                        Double capacidadeDisco = 0.0;
                        for (Volume v : volumes) {
                            hardwareDisco = new Hardware(v.getNome(), v.getTotal().doubleValue(), 3);
                            dao.adicionarHardware(hardwareDisco);
                            Double aux = hardwareDisco.getCapacidade() / 1024 / 1024 / 1024;
                            capacidadeDisco = aux;
                        }
                        hardwareDisco.getCapacidade();
                        Hardware hardwareJanelas = new Hardware("Janelas", grupoDeJanelas.getTotalJanelasVisiveis().doubleValue(), 4);
                        dao.adicionarHardware(hardwareJanelas);

                        Componente componenteCPU = new Componente(1, 1, "123dsa45");
                        Componente componenteRAM = new Componente(1, 2, "78asd9s1");
                        Componente componenteDisco = new Componente(1, 3, "7avsd9s0");
                        Componente componenteJanela = new Componente(1, 4, "dasd546a");

                        dao.adicionarComponente(componenteCPU);
                        dao.adicionarComponente(componenteRAM);
                        dao.adicionarComponente(componenteDisco);
                        dao.adicionarComponente(componenteJanela);

                        Double capacidadeMemoria = hardwareMemoria.getCapacidade() / 1024 / 1024 / 1024;
                        Double capacidadeJanela = grupoDeJanelas.getTotalJanelas().doubleValue();

                        System.out.println("""
                                -------------------------
                                      Ficha técnica
                                -------------------------
                                CPU
                                Nome: %s
                                Capacidade: %.1f%%
                                                               
                                Memória RAM
                                Total: %.1fGB
                                                                
                                Disco
                                Volume total: %.2fGB
                                                                
                                Janelas
                                Janelas abertas: %.0f
                                                                
                                DADOS GERADOS COM SUCESSO!""".formatted(hardwareCPU.getNome(), hardwareCPU.getCapacidade(), capacidadeMemoria, capacidadeDisco, capacidadeJanela));

                        System.out.println("Voltando para a pagina inicial...");
                    } else {
                        System.out.println("Usuario e/ou senha inválidos");
                    }
                    break;
            }
        } while (opcaoEscolhida != 0);
        System.out.println("Até a próxima!");
    }
}