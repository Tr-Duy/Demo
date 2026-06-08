package com.example.demo.repository;

import com.example.demo.domain.entity.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {
    Optional<ScheduleSlot> findBySlotCode(String slotCode);
    List<ScheduleSlot> findByWeekday(Integer weekday);
    boolean existsBySlotCode(String slotCode);
}