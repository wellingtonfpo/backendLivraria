package br.com.casadocodigo.livrotestes.livraria.application.service;

import br.com.casadocodigo.livrotestes.livraria.application.excecoes.EstoqueLivroException;
import br.com.casadocodigo.livrotestes.livraria.domain.entity.Editora;
import br.com.casadocodigo.livrotestes.livraria.domain.entity.Livro;
import br.com.casadocodigo.livrotestes.livraria.domain.repository.JpaLivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.com.casadocodigo.livrotestes.livraria.application.util.MessagensUtil.getMensagem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

    @Mock
    private EditoraService editoraService;

    @Mock
    private JpaLivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;


    @Test
    public void testAtualizarLivroComDesconto() {

        Editora editora = new Editora();
        editora.setNome("Editora Teste");
        editora.setId(1L);
        editora.setDesconto(0.1D);

        Livro livro = new Livro();
        livro.setIsbn("9785934588347");
        livro.setPreco(79.9D);
        livro.setEditora(editora);

        when(this.editoraService.obterDesconto(editora)).thenReturn(0.1D);

        when(this.livroRepository.save(livro)).thenReturn(livro);

        Livro resultado = this.livroService.atualizar(livro);

        verify(this.editoraService).obterDesconto(editora);
        verify(this.livroRepository).save(livro);

        double valorComDesconto = livro.getPreco() * editora.getDesconto();

        assertEquals(valorComDesconto, resultado.getPrecoDesconto());

    }

    @Test
    public void testExcluirComErro() {
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setIsbn("9785934588347");
        livro.setQuantidade(10);

        when(this.livroRepository.getReferenceById(livro.getId())).thenReturn(livro);

        EstoqueLivroException erro = assertThrows(EstoqueLivroException.class,
                () -> this.livroService.excluir(livro.getId()));

        verify(this.livroRepository).getReferenceById(livro.getId());

        assertEquals(erro.getMessage(), getMensagem("livro.estoque.erro"));
    }

}
