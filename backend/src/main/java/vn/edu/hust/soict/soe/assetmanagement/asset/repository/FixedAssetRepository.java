package vn.edu.hust.soict.soe.assetmanagement.asset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.hust.soict.soe.assetmanagement.asset.entity.FixedAsset;

import java.util.Optional;
import java.util.UUID;

/**
 * Database access for {@link FixedAsset} rows in the {@code assets} table.
 * Supports CRUD plus specification-based filtering for list and report queries.
 */
@Repository
public interface FixedAssetRepository extends JpaRepository<FixedAsset, UUID>, JpaSpecificationExecutor<FixedAsset> {
    Optional<FixedAsset> findByAssetCode(String assetCode);
    boolean existsByAssetCode(String assetCode);
}
