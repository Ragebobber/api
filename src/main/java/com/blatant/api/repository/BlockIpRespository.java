package com.blatant.api.repository;

import com.blatant.api.entity.BlockIpList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockIpRespository extends JpaRepository<BlockIpList,Long> {
    Optional<BlockIpList> findByIpAddress(String ipAddress);
}
