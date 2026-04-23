package learning.com.br.screenMatch.repository;

import learning.com.br.screenMatch.models.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Serie,Long> {
}
