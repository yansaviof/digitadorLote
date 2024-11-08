package digitador.controller;

import digitador.dto.in.LotesInDTO;
import digitador.dto.out.LotesOutDTO;
import digitador.service.LotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lotes") // Base URL for the lotes endpoints
public class LotesController {

    @Autowired
    private LotesService lotesService;

    @PreAuthorize("hasRole('SELECT')")
    @GetMapping("/")
    public List<LotesInDTO> retornarLotesCompletos() {
        return lotesService.retornarLotesCompletos();
    }

    @PreAuthorize("hasRole('INSERT')")
    @PostMapping
    public ResponseEntity<LotesOutDTO> adicionarLote(@RequestBody LotesInDTO lotesInDTO) {
        LotesOutDTO response = lotesService.adicionarLote(lotesInDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PreAuthorize("hasRole('DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<LotesOutDTO> excluirLote(@PathVariable int id) {
        LotesOutDTO response = lotesService.excluirLote(id);
        if (response.getLoteId() == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<LotesOutDTO> atualizarLote(@PathVariable int id, @RequestBody LotesInDTO lotesInDTO) {
        LotesOutDTO response = lotesService.atualizarLote(id, lotesInDTO);
        if (response.getLoteId() == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('INSERT')")
    @GetMapping("/{id}")
    public ResponseEntity<LotesInDTO> consultarPorId(@PathVariable int id) {
        LotesInDTO response = lotesService.consultarPorId(id);
        if (response.getLotesId() == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }



}
