package backend.repository;

import backend.model.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SampleEntityRepository extends JpaRepository<SampleEntity, UUID> {
}