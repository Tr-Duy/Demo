package com.example.demo.service.impl;

import com.example.demo.common.exception.NotFoundExeception;
import com.example.demo.domain.entity.ScheduleSlot;
import com.example.demo.repository.ScheduleSlotRepository;
import com.example.demo.service.ScheduleSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service            // đánh dấu class này là Spring Service, được quản lý bởi Spring Container
@RequiredArgsConstructor // Lombok tự tạo constructor inject scheduleSlotRepository
public class ScheduleSlotServiceImpl implements ScheduleSlotService {

    private final ScheduleSlotRepository scheduleSlotRepository; // repository thao tác DB bảng schedule_slots

    @Override
    public List<ScheduleSlot> findAll() {
        return scheduleSlotRepository.findAll(); // SELECT * FROM schedule_slots
    }

    @Override
    public Optional<ScheduleSlot> findById(Long id) {
        return scheduleSlotRepository.findById(id); // SELECT * FROM schedule_slots WHERE id = ?
    }

    @Override
    public ScheduleSlot save(ScheduleSlot scheduleSlot) {
        return scheduleSlotRepository.save(scheduleSlot); // INSERT INTO schedule_slots (...)
    }

    @Override
    public void deleteById(Long id) {
        if (!scheduleSlotRepository.existsById(id)) {           // kiểm tra id có tồn tại không
            throw new NotFoundExeception("ScheduleSlot not found: " + id); // nếu không → ném lỗi 404
        }
        scheduleSlotRepository.deleteById(id); // DELETE FROM schedule_slots WHERE id = ?
    }

    @Override
    public Optional<ScheduleSlot> findBySlotCode(String slotCode) {
        return scheduleSlotRepository.findBySlotCode(slotCode); // SELECT * FROM schedule_slots WHERE slot_code = ?
        // VD: tìm slot có mã "SLOT_1" → trả về Optional (có thể null nếu không tìm thấy)
    }

    @Override
    public List<ScheduleSlot> findByWeekday(Integer weekday) {
        return scheduleSlotRepository.findByWeekday(weekday); // SELECT * FROM schedule_slots WHERE weekday = ?
        // VD: weekday = 2 → lấy tất cả slot ngày Thứ 2
    }

    @Override
    public boolean existsBySlotCode(String slotCode) {
        return scheduleSlotRepository.existsBySlotCode(slotCode); // SELECT COUNT(*) > 0 WHERE slot_code = ?
        // dùng khi tạo slot mới để kiểm tra mã đã tồn tại chưa, tránh trùng lặp
    }
}
