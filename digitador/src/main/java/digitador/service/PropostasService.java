package digitador.service;

import digitador.domain.LotesDomain;
import digitador.domain.PropostasDomain;
import digitador.domain.enums.EnumStatusPropostas;
import digitador.dto.in.LotesInDTO;
import digitador.dto.in.PropostasInDTO;
import digitador.dto.in.PropostasInDTOSemLote;
import digitador.dto.out.PropostasOutDTO;
import digitador.repository.LotesRepository;
import digitador.repository.PropostasRepository;
import digitador.security.UserPrincipal;
import digitador.user.User;
import digitador.user.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropostasService {

    @Autowired
    private PropostasRepository propostasRepository;
    @Autowired
    private LotesRepository lotesRepository;
    @Autowired
    private UserRepository userRepository;

    public void adicionarPropostasEmLote(Integer loteId, List<PropostasInDTO> propostasInDTOList) {
        User user = getAuthenticatedUser();
        LotesDomain loteDomain = lotesRepository.findById(loteId)
                .orElseThrow(() -> new RuntimeException("Lote não encontrado com ID: " + loteId));
        if (!loteDomain.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Você não tem permissão para adicionar propostas neste lote.");
        }
        for (PropostasInDTO propostaDTO : propostasInDTOList) {
            PropostasDomain proposta = converterDTOParaDominio(propostaDTO);
            proposta.setLoteDomain(loteDomain);
            propostasRepository.save(proposta);
        }
    }

    // Método para retornar todas as propostas de um usuário
    public List<PropostasInDTOSemLote> retornarPropostas() {
        User user = getAuthenticatedUser();
        List<PropostasDomain> propostas = propostasRepository.findAllByLoteDomain_User_UserId(user.getUserId());
        return propostas.stream()
                .map(PropostasInDTOSemLote::new)
                .collect(Collectors.toList());
    }

    // Método para excluir uma proposta se for do mesmo usuário
    public PropostasOutDTO excluirProposta(int id) {
        User authenticatedUser = getAuthenticatedUser();
        Optional<PropostasDomain> optionalProposta = propostasRepository.findById(id);

        if (optionalProposta.isPresent() &&
                optionalProposta.get().getLoteDomain().getUser().getUserId().equals(authenticatedUser.getUserId())) {

            propostasRepository.deleteById(id);
            return PropostasOutDTO.builder()
                    .propostaId(id)  // ID da proposta excluída
                    .build();
        } else {
            return PropostasOutDTO.builder()
                    .propostaId(-1)  // Proposta não encontrada ou não pertence ao usuário
                    .build();
        }
    }

    // Método para atualizar uma proposta
    public PropostasOutDTO atualizarProposta(int id, PropostasInDTO propostasInDTO) {
        User authenticatedUser = getAuthenticatedUser();
        Optional<PropostasDomain> optionalProposta = propostasRepository.findById(id);

        if (optionalProposta.isPresent() &&
                optionalProposta.get().getLoteDomain().getUser().getUserId().equals(authenticatedUser.getUserId())) {

            PropostasDomain proposta = optionalProposta.get();

            // Atualizando os campos da proposta com os dados do DTO
            if (propostasInDTO.getCpf() != null) proposta.setCpf(propostasInDTO.getCpf());
            if (propostasInDTO.getTelefone() != null) proposta.setTelefone(propostasInDTO.getTelefone());
            if (propostasInDTO.getNome() != null) proposta.setNome(propostasInDTO.getNome());
            if (propostasInDTO.getEmail() != null) proposta.setEmail(propostasInDTO.getEmail());
            // Continue com os outros campos conforme necessário

            propostasRepository.save(proposta);
            return PropostasOutDTO.builder()
                    .propostaId(proposta.getPropostaId())
                    .build();
        } else {
            return PropostasOutDTO.builder()
                    .propostaId(-1)  // Proposta não encontrada ou não pertence ao usuário
                    .build();
        }
    }

    // Método para consultar uma proposta por ID
    public PropostasInDTO consultarPropostaPorId(int id) {
        User authenticatedUser = getAuthenticatedUser();
        Optional<PropostasDomain> optionalProposta = propostasRepository.findById(id);

        if (optionalProposta.isPresent() &&
                optionalProposta.get().getLoteDomain().getUser().getUserId().equals(authenticatedUser.getUserId())) {

            PropostasDomain proposta = optionalProposta.get();
            return PropostasInDTO.builder()
                    .propostaId(proposta.getPropostaId())
                    .cpf(proposta.getCpf())
                    .telefone(proposta.getTelefone())
                    .nome(proposta.getNome())
                    .email(proposta.getEmail())
                    .build();
        } else {
            return PropostasInDTO.builder()
                    .propostaId(-1)  // Proposta não encontrada ou não pertence ao usuário
                    .build();
        }
    }

    // Método para atualizar apenas o campo enumStatusPropostas de uma proposta
    public PropostasOutDTO atualizarStatusProposta(int id, EnumStatusPropostas novoStatus) {
        User authenticatedUser = getAuthenticatedUser();
        Optional<PropostasDomain> optionalProposta = propostasRepository.findById(id);

        if (optionalProposta.isPresent() &&
                optionalProposta.get().getLoteDomain().getUser().getUserId().equals(authenticatedUser.getUserId())) {

            PropostasDomain proposta = optionalProposta.get();
            proposta.setEnumStatusPropostas(novoStatus);
            propostasRepository.save(proposta);

            return PropostasOutDTO.builder()
                    .propostaId(proposta.getPropostaId())
                    .build();
        } else {
            return PropostasOutDTO.builder()
                    .propostaId(-1)  // Proposta não encontrada ou não pertence ao usuário
                    .build();
        }
    }


    // Método para atualizar apenas o campo link de uma proposta
    public PropostasOutDTO atualizarLinkProposta(int id, PropostasDomain propostasDomain) {
        User authenticatedUser = getAuthenticatedUser();
        Optional<PropostasDomain> optionalProposta = propostasRepository.findById(id);

        if (optionalProposta.isPresent() &&
                optionalProposta.get().getLoteDomain().getUser().getUserId().equals(authenticatedUser.getUserId())) {

            PropostasDomain proposta = optionalProposta.get();
            proposta.setLinkFormalizacao(propostasDomain.getLinkFormalizacao());
            propostasRepository.save(proposta);

            return PropostasOutDTO.builder()
                    .propostaId(proposta.getPropostaId())
                    .build();
        } else {
            return PropostasOutDTO.builder()
                    .propostaId(-1)  // Proposta não encontrada ou não pertence ao usuário
                    .build();
        }
    }

    public List<PropostasInDTO> lerPlanilha(MultipartFile file, Map<String, String> colunaMapeamento) throws Exception {
        List<PropostasInDTO> propostasList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // Ler o cabeçalho para identificar as colunas e seus índices
        Row headerRow = sheet.getRow(0);
        Map<String, Integer> colunaIndexMap = new HashMap<>();

        // Associar os nomes das colunas ao índice da planilha
        for (Cell cell : headerRow) {
            String headerValue = cell.getStringCellValue().trim();
            if (colunaMapeamento.containsKey(headerValue)) {
                colunaIndexMap.put(colunaMapeamento.get(headerValue), cell.getColumnIndex());
            }
        }

        // Ler as linhas de dados a partir da segunda linha
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                PropostasInDTO proposta = new PropostasInDTO();

                // Preencher os atributos do DTO com base nas colunas mapeadas
                for (Map.Entry<String, Integer> entry : colunaIndexMap.entrySet()) {
                    String atributo = entry.getKey();
                    int colunaIndex = entry.getValue();
                    Cell cell = row.getCell(colunaIndex);

                    if (cell != null) {
                        switch (atributo) {
                            case "propostaId":
                                proposta.setPropostaId((int) cell.getNumericCellValue());
                                break;
                            case "cpf":
                                proposta.setCpf(cell.getStringCellValue());
                                break;
                            case "dataNascimento":
                                if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                                    proposta.setDataNascimento(cell.getLocalDateTimeCellValue().toLocalDate());
                                }
                                break;
                            case "telefone":
                                proposta.setTelefone(cell.getStringCellValue());
                                break;
                            case "nome":
                                proposta.setNome(cell.getStringCellValue());
                                break;
                            case "nacionalidade":
                                proposta.setNacionalidade(cell.getStringCellValue());
                                break;
                            case "naturalidade":
                                proposta.setNaturalidade(cell.getStringCellValue());
                                break;
                            case "estadoCivil":
                                proposta.setEstadoCivil(cell.getStringCellValue());
                                break;
                            case "email":
                                proposta.setEmail(cell.getStringCellValue());
                                break;
                            case "rg":
                                proposta.setRg(cell.getStringCellValue());
                                break;
                            case "emissor":
                                proposta.setEmissor(cell.getStringCellValue());
                                break;
                            case "dataEmissao":
                                if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                                    proposta.setDataEmissao(cell.getLocalDateTimeCellValue().toLocalDate());
                                }
                                break;
                            case "cep":
                                proposta.setCep(cell.getStringCellValue());
                                break;
                            case "numero":
                                proposta.setNumero(cell.getStringCellValue());
                                break;
                            case "nomeMae":
                                proposta.setNomeMae(cell.getStringCellValue());
                                break;
                            case "nomePai":
                                proposta.setNomePai(cell.getStringCellValue());
                                break;
                            case "uf":
                                proposta.setUf(cell.getStringCellValue());
                                break;
                            case "tipoBeneficio":
                                proposta.setTipoBeneficio(cell.getStringCellValue());
                                break;
                            case "recebeCartaoMagnetico":
                                proposta.setRecebeCartaoMagnetico(cell.getBooleanCellValue());
                                break;
                            case "banco":
                                proposta.setBanco((int) cell.getNumericCellValue());
                                break;
                            case "agencia":
                                proposta.setAgencia((int) cell.getNumericCellValue());
                                break;
                            case "digitoAgencia":
                                proposta.setDigitoAgencia((int) cell.getNumericCellValue());
                                break;
                            case "cidadeAgencia":
                                proposta.setCidadeAgencia(cell.getStringCellValue());
                                break;
                            case "conta":
                                proposta.setConta((int) cell.getNumericCellValue());
                                break;
                            case "digitoConta":
                                proposta.setDigitoConta((int) cell.getNumericCellValue());
                                break;
                            case "cpfAgente":
                                proposta.setCpfAgente((int) cell.getNumericCellValue());
                                break;
                            case "nomeAgente":
                                proposta.setNomeAgente(cell.getStringCellValue());
                                break;
                            case "lojaAgente":
                                proposta.setLojaAgente((int) cell.getNumericCellValue());
                                break;
                        }
                    }
                }

                // Adiciona a proposta à lista
                propostasList.add(proposta);
            }
        }

        workbook.close();
        return propostasList;
    }

    // Método para converter DTO para domínio
    private PropostasDomain converterDTOParaDominio(PropostasInDTO propostaInDTO) {
        return PropostasDomain.builder()
                .cpf(propostaInDTO.getCpf())
                .dataNascimento(propostaInDTO.getDataNascimento())
                .ddd(propostaInDTO.getDdd())
                .telefone(propostaInDTO.getTelefone())
                .beneficio(propostaInDTO.getBeneficio())
                .nome(propostaInDTO.getNome())
                .nacionalidade(propostaInDTO.getNacionalidade())
                .naturalidade(propostaInDTO.getNaturalidade())
                .estadoCivil(propostaInDTO.getEstadoCivil())
                .email(propostaInDTO.getEmail())
                .rg(propostaInDTO.getRg())
                .emissor(propostaInDTO.getEmissor())
                .dataEmissao(propostaInDTO.getDataEmissao())
                .cep(propostaInDTO.getCep())
                .numero(propostaInDTO.getNumero())
                .nomeMae(propostaInDTO.getNomeMae())
                .nomePai(propostaInDTO.getNomePai())
                .uf(propostaInDTO.getUf())
                .tipoBeneficio(propostaInDTO.getTipoBeneficio())
                .recebeCartaoMagnetico(propostaInDTO.isRecebeCartaoMagnetico())
                .banco(propostaInDTO.getBanco())
                .agencia(propostaInDTO.getAgencia())
                .digitoAgencia(propostaInDTO.getDigitoAgencia())
                .cidadeAgencia(propostaInDTO.getCidadeAgencia())
                .conta(propostaInDTO.getConta())
                .digitoConta(propostaInDTO.getDigitoConta())
                .cpfAgente(propostaInDTO.getCpfAgente())
                .nomeAgente(propostaInDTO.getNomeAgente())
                .lojaAgente(propostaInDTO.getLojaAgente())
                .build();
    }

    private String getStringCellValue(Cell cell) {
        return cell != null ? cell.getStringCellValue() : null;
    }

    private LocalDate getDateCellValue(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null; // Retorna null se a célula não for do tipo DATE ou estiver vazia
    }

    private Boolean getBooleanCellValue(Cell cell) {
        return cell != null && cell.getCellType() == CellType.BOOLEAN && cell.getBooleanCellValue();
    }


    public User getAuthenticatedUser() {
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