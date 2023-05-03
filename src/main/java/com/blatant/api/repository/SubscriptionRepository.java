package com.blatant.api.repository;

import com.blatant.api.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    List<Subscription> findByUserId(Long id);

    /**
     *
     * @param date - Date now
     * @param isActive - Sub is active
     * @return - Array with active Subs
     */
    List<Subscription> findAllByExpirationDateBeforeAndIsActive(Date date,Boolean isActive);
}
