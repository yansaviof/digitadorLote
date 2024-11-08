package digitador.repository;

import digitador.domain.LotesDomain;
import digitador.domain.PropostasDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostasRepository extends JpaRepository<PropostasDomain, Integer>{

    List<PropostasDomain> findAllByLoteDomain_User_UserId(Integer userId);

}
