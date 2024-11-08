package digitador.repository;

import digitador.domain.LotesDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LotesRepository extends JpaRepository<LotesDomain, Integer>{

    List<LotesDomain> findAllByUser_UserId(Integer userId);




}
