package org.dema.lab4.repository;

import org.dema.lab4.entity.HistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<HistoryItem, Long> {

}
