package digitador.service;

import digitador.domain.LotesDomain;
import digitador.dto.in.LotesInDTO;
import digitador.dto.out.LotesOutDTO;
import digitador.security.UserPrincipal;
import digitador.user.User;
import digitador.repository.LotesRepository;
import digitador.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LotesService {

    @Autowired
    private LotesRepository lotesRepository;

    @Autowired
    private UserRepository userRepository;




    public LotesOutDTO adicionarLote(LotesInDTO lotesInDTO) {
        User user = getAuthenticatedUser();
        LotesDomain novoLote = LotesDomain.builder()
                .enumBancos(lotesInDTO.getEnumBancos())
                .enumStatusLote(lotesInDTO.getEnumStatusLote())
                .usuarioBanco(lotesInDTO.getUsuarioBanco())
                .senhaBanco(lotesInDTO.getSenhaBanco())
                .user(user) // Associa o usuário ao lote
                .build();
        System.out.println("Novo lote a ser salvo: " + novoLote);
        novoLote = lotesRepository.save(novoLote);
        return LotesOutDTO.builder()
                .loteId(novoLote.getLoteId())
                .build();
    }

    public List<LotesInDTO> retornarLotesCompletos() {
        User user = getAuthenticatedUser();
        List<LotesDomain> lotes = lotesRepository.findAllByUser_UserId(user.getUserId());
        return lotes.stream()
                .map(LotesInDTO::new)
                .collect(Collectors.toList());
    }



    public LotesOutDTO excluirLote(int id) {
        User user = getAuthenticatedUser();

        Optional<LotesDomain> optionalLote = lotesRepository.findById(id);
        if (optionalLote.isPresent() && optionalLote.get().getUser().getUserId().equals(user.getUserId())) {
            lotesRepository.deleteById(id);
            return LotesOutDTO.builder()
                    .loteId(id)
                    .build();
        } else {
            return LotesOutDTO.builder()
                    .loteId(-1)
                    .build();
        }
    }

    public LotesOutDTO atualizarLote(int id, LotesInDTO lotesInDTO) {
        User user = getAuthenticatedUser();

        Optional<LotesDomain> optionalLote = lotesRepository.findById(id);
        if (optionalLote.isPresent() && optionalLote.get().getUser().getUserId().equals(user.getUserId())) {
            LotesDomain lote = optionalLote.get();
            if (lotesInDTO.getEnumBancos() != null) lote.setEnumBancos(lotesInDTO.getEnumBancos());
            if (lotesInDTO.getEnumStatusLote() != null) lote.setEnumStatusLote(lotesInDTO.getEnumStatusLote());
            if (lotesInDTO.getUsuarioBanco() != null) lote.setUsuarioBanco(lotesInDTO.getUsuarioBanco());
            if (lotesInDTO.getSenhaBanco() != null) lote.setSenhaBanco(lotesInDTO.getSenhaBanco());
            lotesRepository.save(lote);
            return LotesOutDTO.builder()
                    .loteId(lote.getLoteId())
                    .build();
        } else {
            return LotesOutDTO.builder()
                    .loteId(-1)
                    .build();
        }
    }

    public LotesInDTO consultarPorId(int id) {
        User user = getAuthenticatedUser();

        Optional<LotesDomain> optionalLote = lotesRepository.findById(id);
        if (optionalLote.isPresent() && optionalLote.get().getUser().getUserId().equals(user.getUserId())) {
            LotesDomain lote = optionalLote.get();
            return LotesInDTO.builder()
                    .lotesId(lote.getLoteId())
                    .enumBancos(lote.getEnumBancos())
                    .enumStatusLote(lote.getEnumStatusLote())
                    .usuarioBanco(lote.getUsuarioBanco())
                    .senhaBanco(lote.getSenhaBanco())
                    .build();
        } else {
            return LotesInDTO.builder()
                    .lotesId(-1)
                    .build();
        }
    }



    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userRepository.findById(Long.valueOf(userPrincipal.getUserId()))
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        } else {
            throw new RuntimeException("Usuário não autenticado");
        }
    }


}
