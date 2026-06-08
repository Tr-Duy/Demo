package com.example.demo.service;

import com.example.demo.domain.entity.ScheduleSlot;

import java.util.List;
import java.util.Optional;

public interface ScheduleSlotService {

    List<ScheduleSlot> findAll();                                    // lấy toàn bộ slot

    Optional<ScheduleSlot> findById(Long id);                       // tìm slot theo id

    ScheduleSlot save(ScheduleSlot scheduleSlot);                   // tạo mới slot

    void deleteById(Long id);                                        // xóa slot theo id

    Optional<ScheduleSlot> findBySlotCode(String slotCode);         // tìm slot theo mã slot (VD: "SLOT_1")

    List<ScheduleSlot> findByWeekday(Integer weekday);              // lấy danh sách slot theo ngày trong tuần (1=Thứ 2, 7=Chủ nhật)

    boolean existsBySlotCode(String slotCode);                      // kiểm tra slot có tồn tại theo mã không (dùng khi tạo mới tránh trùng)
}
