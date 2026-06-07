package vn.edu.hust.soict.soe.assetmanagement.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.soict.soe.assetmanagement.stock.entity.StorageLocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Database access for warehouse/storage locations linked to managing units.
 */
@Repository
public interface StorageLocationRepository extends JpaRepository<StorageLocation, UUID> {
    Optional<StorageLocation> findByCode(String code);
    List<StorageLocation> findByIsActiveTrue();
}
