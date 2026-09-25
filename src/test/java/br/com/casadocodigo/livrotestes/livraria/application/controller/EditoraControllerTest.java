package br.com.casadocodigo.livrotestes.livraria.application.controller;

import br.com.casadocodigo.livrotestes.livraria.application.excecoes.CampoInvalidoException;
import br.com.casadocodigo.livrotestes.livraria.application.service.EditoraService;
import br.com.casadocodigo.livrotestes.livraria.domain.entity.Editora;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EditoraControllerTest {

    //    @Autowired
    private final EditoraService editoraService;
    //
//    @Autowired
    private final TestRestTemplate restTemplateTest;

    public EditoraControllerTest(EditoraService editoraService, TestRestTemplate restTemplateTest) {
        this.editoraService = editoraService;
        this.restTemplateTest = restTemplateTest;
    }

    @Test
    public void testSalvarSucesso() throws CampoInvalidoException {
        // Parâmetro de entrada
        Editora editoraTransiente = new Editora();
        editoraTransiente.setNome("FPO Editora");
        editoraTransiente.setDesconto(0.15D);
        editoraTransiente.setCnpj("30.744.209/0001-20");

        // Teste de integração
        this.editoraService.salvar(editoraTransiente);
        List<Editora> editoras = this.restTemplateTest.exchange(
                "/editoras",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Editora>>() {
                }).getBody();

        // Validações
        assertEquals(1, editoras != null ? editoras.size() : 0);
        assertEquals(editoraTransiente.getCnpj(), editoras != null ? editoras.getFirst().getCnpj() : null);
        assertNotNull(editoras != null ? editoras.getFirst().getId() : null);
    }
}
