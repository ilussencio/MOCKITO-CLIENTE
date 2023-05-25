package com.iftm.client.service;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.repositories.ClientRepository;
import com.iftm.client.services.ClientService;
import com.iftm.client.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ClientServiceTestIntegracao {

    @Autowired
    private ClientService servico;

    @DisplayName("Testar se o método deleteById apaga um registro e não retorna outras informações")
    @Test
    public void testarApagarPorIdTemSucessoComIdExistente() {
        long idExistente = 1;
        Assertions.assertDoesNotThrow(() -> {
            servico.delete(idExistente);
        });
    }

    @DisplayName("Testar se o método deleteById retorna exception para idInexistente")
    @Test
    public void testarApagarPorIdGeraExceptionComIdInexistente() {
        //cenário
        long idNaoExistente = 100;

        assertThrows(ResourceNotFoundException.class, () -> servico.delete(idNaoExistente));

    }

    @Test
    @DisplayName("testar método findByIncomeGreaterThan retorna a página com clientes corretos")
    public void testarBuscaPorSalarioMaiorQueRetornaElementosEsperados() {
        //cenário de teste
        double entrada = 4800.00;
        int paginaApresentada = 1;
        int linhasPorPagina = 2;
        String ordemOrdenacao = "ASC";
        String campoOrdenacao = "income";
        Client clienteSete = new Client(7L, "Jose Saramago", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0);
        Client clienteQuatro = new Client(4L, "Carolina Maria de Jesus", "10419244771", 7500.0, Instant.parse("1996-12-23T07:00:00Z"), 0);

        PageRequest pagina = PageRequest.of(paginaApresentada, linhasPorPagina,
                Sort.Direction.valueOf(ordemOrdenacao), campoOrdenacao);

        //configurar o Mock
        List<Client> lista = new ArrayList<>();
        lista.add(clienteSete);
        lista.add(clienteQuatro);
        Page<Client> paginaEsperada = new PageImpl<>(lista, pagina, 1);

    }


    @DisplayName("(EXECICIO 01) Testar se o método deleteById apaga um registro e não retorna outras informações")
    @Test
    public void testarApagarPorIdRetornaVazio() {
        long idExistente = 1;
        long idNaoExistente = 100;

        Assertions.assertDoesNotThrow(() -> servico.delete(idExistente));


        assertThrows(ResourceNotFoundException.class,
                () -> servico.delete(idNaoExistente));
    }

    @DisplayName("(EXERCICIO 02) Testar o metodo findAllPaged")
    @Test
    public void testarFindAllPaged(){
        //cenário de teste
        int paginaApresentada = 1;
        int linhasPorPagina = 2;
        String ordemOrdenacao = "ASC";
        String campoOrdenacao = "name";
        Client clienteQuatro = new Client(4L, "Carolina Maria de Jesus", "10419244771", 7500.0, Instant.parse("1996-12-23T07:00:00Z"), 0);
        Client clienteDez = new Client(10L, "Chimamanda Adichie", "10114274861", 1500.0, Instant.parse("1956-09-23T07:00:00Z"), 0);

        PageRequest pagina = PageRequest.of(paginaApresentada, linhasPorPagina,
                Sort.Direction.valueOf(ordemOrdenacao), campoOrdenacao);

        //configurar o Mock
        List<Client> lista = new ArrayList<>();
        lista.add(clienteQuatro);
        lista.add(clienteDez);
        Page<Client> paginaEsperada = new PageImpl<>(lista, pagina, 1);

        Page<ClientDTO> page = servico.findAllPaged(pagina);
        assertThat(page).isNotEmpty();
        assertThat(page.getNumberOfElements()).isEqualTo(2);
        assertThat(page.toList().get(0).toEntity()).isEqualTo(clienteQuatro);
        assertThat(page.toList().get(1).toEntity()).isEqualTo(clienteDez);
    }

    @DisplayName("(EXERCICIO 03) Testar o metodo findByIncome")
    @Test
    public void testarFindByIncomeRetornaPaginaComClientesDoIncomeInformado() {
        // Cenário de teste
        double income = 4800.00;
        int paginaApresentada = 1;
        int linhasPorPagina = 2;
        String ordemOrdenacao = "ASC";
        String campoOrdenacao = "name";
        Client clienteQuatro = new Client(4L, "Carolina Maria de Jesus", "10419244771", 4800.00, Instant.parse("1996-12-23T07:00:00Z"), 0);
        Client clienteDez = new Client(10L, "Chimamanda Adichie", "10114274861", 4800.00, Instant.parse("1956-09-23T07:00:00Z"), 0);

        PageRequest pagina = PageRequest.of(paginaApresentada, linhasPorPagina,
                Sort.Direction.valueOf(ordemOrdenacao), campoOrdenacao);

        // Configurar o mock
        List<Client> lista = new ArrayList<>();
        lista.add(clienteQuatro);
        lista.add(clienteDez);
        Page<Client> paginaEsperada = new PageImpl<>(lista, pagina, 1);

        // Executar o método a ser testado
        Page<ClientDTO> page = servico.findByIncome(pagina, income);

        // Verificar os resultados
        assertThat(page).isNotEmpty();
        assertThat(page.getNumberOfElements()).isEqualTo(2);
        assertThat(page.toList().get(0).toEntity()).isEqualTo(clienteQuatro);
        assertThat(page.toList().get(1).toEntity()).isEqualTo(clienteDez);

    }

    @DisplayName("(EXERCICIO 04) Testar se o método findById")
    @Test
    public void testarFindByIdRetornaClientDTOQuandoIdExistirELevantaResourceNotFoundExceptionQuandoIdNaoExistir() {
        // Cenário de teste
        Long idExistente = 1L;
        Long idNaoExistente = 999L;
        String nome = "João";
        String cpf = "12345678900";
        Double income = 4800.00;
        Instant dataNascimento = Instant.parse("1990-01-01T00:00:00Z");
        Integer children = 0;
        Client clienteExistente = new Client(idExistente, nome, cpf, income, dataNascimento, children);


        // Executar o método a ser testado
        ClientDTO clientDTOExistente = servico.findById(idExistente);
        assertThrows(ResourceNotFoundException.class, () -> servico.findById(idNaoExistente));

        // Verificar os resultados
        assertThat(clientDTOExistente).isNotNull();
        assertThat(clientDTOExistente.getId()).isEqualTo(idExistente);
        assertThat(clientDTOExistente.getName()).isEqualTo(nome);
        assertThat(clientDTOExistente.getCpf()).isEqualTo(cpf);
        assertThat(clientDTOExistente.getIncome()).isEqualTo(income);
        assertThat(clientDTOExistente.getBirthDate()).isEqualTo(dataNascimento);
        assertThat(clientDTOExistente.getChildren()).isEqualTo(children);

    }
}
