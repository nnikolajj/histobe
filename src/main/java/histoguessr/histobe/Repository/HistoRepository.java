package histoguessr.histobe.Repository;

import histoguessr.histobe.Entity.HistoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoRepository extends JpaRepository<HistoEntity, Long> {

}
