package digitador.controller;

import digitador.domain.LotesDomain;
import digitador.domain.PropostasDomain;
import digitador.domain.enums.EnumStatusPropostas;
import digitador.dto.in.PropostasInDTO;
import digitador.dto.in.PropostasInDTOSemLote;
import digitador.dto.out.PropostasOutDTO;
import digitador.repository.LotesRepository;
import digitador.service.PropostasService;
import digitador.user.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/propostas")
public class PropostasController {
    @Autowired
    private PropostasService propostasService;
    @Autowired
    private LotesRepository lotesRepository;


    @PreAuthorize("hasRole('INSERT')")
    @PostMapping("/adicionar-lote/{loteId}")
    public ResponseEntity<Void> adicionarLote(
            @PathVariable Integer loteId,
            @RequestParam("file") MultipartFile file,
            @RequestParam Map<String, String> colunaMapeamento) throws Exception {

        // Chama o serviço passando o mapeamento e a planilha
        List<PropostasInDTO> propostasInDTOList = propostasService.lerPlanilha(file, colunaMapeamento);

        // Adiciona as propostas ao lote
        propostasService.adicionarPropostasEmLote(loteId, propostasInDTOList);
        return ResponseEntity.ok().build();
    }



    // Endpoint para retornar todas as propostas do usuário autenticado
    @GetMapping("/")
    public ResponseEntity<List<PropostasInDTOSemLote>> retornarPropostas() {
        List<PropostasInDTOSemLote> propostas = propostasService.retornarPropostas();
        return ResponseEntity.ok(propostas);
    }



    // Endpoint para consultar uma proposta por ID
    @GetMapping("/{id}")
    public ResponseEntity<PropostasInDTO> consultarPropostaPorId(@PathVariable int id) {
        PropostasInDTO proposta = propostasService.consultarPropostaPorId(id);
        if (proposta.getPropostaId() == -1) {
            return ResponseEntity.notFound().build();  // Proposta não encontrada ou não pertence ao usuário
        }
        return ResponseEntity.ok(proposta);
    }

    // Endpoint para excluir uma proposta
    @DeleteMapping("/{id}")
    public ResponseEntity<PropostasOutDTO> excluirProposta(@PathVariable int id) {
        PropostasOutDTO resultado = propostasService.excluirProposta(id);
        if (resultado.getPropostaId() == -1) {
            return ResponseEntity.notFound().build();  // Proposta não encontrada ou não pertence ao usuário
        }
        return ResponseEntity.ok(resultado);
    }

    // Endpoint para atualizar uma proposta
    @PutMapping("/{id}")
    public ResponseEntity<PropostasOutDTO> atualizarProposta(
            @PathVariable int id,
            @RequestBody PropostasInDTO propostasInDTO) {
        PropostasOutDTO resultado = propostasService.atualizarProposta(id, propostasInDTO);
        if (resultado.getPropostaId() == -1) {
            return ResponseEntity.notFound().build();  // Proposta não encontrada ou não pertence ao usuário
        }
        return ResponseEntity.ok(resultado);
    }

    // Endpoint para atualizar apenas o campo enumStatusPropostas de uma proposta
    @PutMapping("/{id}/status/{novoStatus}")
    public ResponseEntity<PropostasOutDTO> atualizarStatusProposta(
            @PathVariable int id,
            @PathVariable String novoStatus) {
        EnumStatusPropostas status = EnumStatusPropostas.fromCodigo(Integer.parseInt(novoStatus));

        PropostasOutDTO resultado = propostasService.atualizarStatusProposta(id, status);
        if (resultado.getPropostaId() == -1) {
            return ResponseEntity.notFound().build();  // Proposta não encontrada ou não pertence ao usuário
        }
        return ResponseEntity.ok(resultado);
    }

    // Endpoint para atualizar apenas o campo link de uma proposta
    @PutMapping("/{id}/link")
    public ResponseEntity<PropostasOutDTO> atualizarLinkProposta(
            @PathVariable int id,
            @RequestBody PropostasDomain novoLink) {
        PropostasOutDTO resultado = propostasService.atualizarLinkProposta(id, novoLink);
        if (resultado.getPropostaId() == -1) {
            return ResponseEntity.notFound().build();  // Proposta não encontrada ou não pertence ao usuário
        }
        return ResponseEntity.ok(resultado);
    }
}
