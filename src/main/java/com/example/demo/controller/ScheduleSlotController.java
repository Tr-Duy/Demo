package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.exception.NotFoundExeception;
import com.example.demo.domain.entity.ScheduleSlot;
import com.example.demo.dto.scheduleslot.ScheduleSlotResponse;
import com.example.demo.dto.scheduleslot.ScheduleSlotUpsertRequest;
import com.example.demo.service.ScheduleSlotService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController                             // đánh dấu đây là REST Controller, mọi method trả về JSON
@RequiredArgsConstructor                    // Lombok tự tạo constructor inject các biến final
@RequestMapping("/api/schedule-slots")      // URL gốc: /api/schedule-slots
public class ScheduleSlotController {

    private final ScheduleSlotService scheduleSlotService; // xử lý nghiệp vụ ScheduleSlot
    private final ModelMapper mapper;                       // chuyển đổi giữa Entity ↔ DTO

    @GetMapping                                                         // GET /api/schedule-slots
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")              // chỉ ADMIN hoặc ACADEMIC_STAFF được xem
    public ResponseEntity<ApiResponse<List<ScheduleSlotResponse>>> findAll() {
        List<ScheduleSlotResponse> list = scheduleSlotService.findAll().stream() // lấy toàn bộ slot từ DB
                .map(s -> mapper.map(s, ScheduleSlotResponse.class))             // convert từng Entity → DTO
                .toList();
        return ResponseEntity.ok(ApiResponse.success(list));                     // trả về 200 kèm danh sách
    }

    @GetMapping("/{id}")                                                // GET /api/schedule-slots/{id}
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")              // chỉ ADMIN hoặc ACADEMIC_STAFF được xem
    public ResponseEntity<ApiResponse<ScheduleSlotResponse>> findById(@PathVariable Long id) { // lấy id từ URL
        ScheduleSlotResponse res = scheduleSlotService.findById(id)     // tìm slot theo id
                .map(s -> mapper.map(s, ScheduleSlotResponse.class))    // nếu có → convert Entity → DTO
                .orElseThrow(() -> new NotFoundExeception("Schedule slot not found: " + id)); // không có → ném 404
        return ResponseEntity.ok(ApiResponse.success(res));             // trả về 200 kèm dữ liệu
    }

    @GetMapping("/weekday/{weekday}")                                   // GET /api/schedule-slots/weekday/2
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")              // chỉ ADMIN hoặc ACADEMIC_STAFF được xem
    public ResponseEntity<ApiResponse<List<ScheduleSlotResponse>>> findByWeekday(
            @PathVariable Integer weekday) {                            // lấy weekday từ URL (2=Thứ 2, 7=Chủ nhật)
        List<ScheduleSlotResponse> list = scheduleSlotService.findByWeekday(weekday) // lấy slot theo ngày trong tuần
                .stream()
                .map(s -> mapper.map(s, ScheduleSlotResponse.class))    // convert từng Entity → DTO
                .toList();
        return ResponseEntity.ok(ApiResponse.success(list));            // trả về 200 kèm danh sách
    }

    @GetMapping("/slot-code/{slotCode}")                                // GET /api/schedule-slots/slot-code/SLOT_1
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")              // chỉ ADMIN hoặc ACADEMIC_STAFF được xem
    public ResponseEntity<ApiResponse<ScheduleSlotResponse>> findBySlotCode(
            @PathVariable String slotCode) {                            // lấy slotCode từ URL
        ScheduleSlotResponse res = scheduleSlotService.findBySlotCode(slotCode) // tìm slot theo mã
                .map(s -> mapper.map(s, ScheduleSlotResponse.class))    // nếu có → convert Entity → DTO
                .orElseThrow(() -> new NotFoundExeception("Schedule slot not found: " + slotCode)); // không có → 404
        return ResponseEntity.ok(ApiResponse.success(res));             // trả về 200 kèm dữ liệu
    }

    @PostMapping                                                        // POST /api/schedule-slots
    @PreAuthorize("hasRole('ADMIN')")                                   // chỉ ADMIN được tạo mới
    public ResponseEntity<ApiResponse<ScheduleSlotResponse>> save(
            @Valid @RequestBody ScheduleSlotUpsertRequest req) {        // @Valid validate dữ liệu, @RequestBody đọc JSON từ body
        ScheduleSlot entity = mapper.map(req, ScheduleSlot.class);      // convert DTO → Entity để lưu DB
        ScheduleSlot saved = scheduleSlotService.save(entity);          // INSERT vào DB
        return ResponseEntity.ok(ApiResponse.success(mapper.map(saved, ScheduleSlotResponse.class))); // trả về slot vừa tạo
    }

    @PutMapping("/{id}")                                                // PUT /api/schedule-slots/{id}
    @PreAuthorize("hasRole('ADMIN')")                                   // chỉ ADMIN được cập nhật
    public ResponseEntity<ApiResponse<ScheduleSlotResponse>> update(
            @PathVariable Long id,                                      // lấy id từ URL
            @Valid @RequestBody ScheduleSlotUpsertRequest req) {        // dữ liệu cập nhật từ body
        ScheduleSlot entity = scheduleSlotService.findById(id)          // tìm slot cần cập nhật
                .orElseThrow(() -> new NotFoundExeception("Schedule slot not found: " + id)); // không tìm thấy → 404
        mapper.map(req, entity);                                        // ghi đè dữ liệu mới lên entity cũ
        ScheduleSlot saved = scheduleSlotService.save(entity);          // UPDATE vào DB
        return ResponseEntity.ok(ApiResponse.success(mapper.map(saved, ScheduleSlotResponse.class))); // trả về slot sau khi cập nhật
    }

    @DeleteMapping("/{id}")                                             // DELETE /api/schedule-slots/{id}
    @PreAuthorize("hasRole('ADMIN')")                                   // chỉ ADMIN được xóa
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { // lấy id từ URL
        scheduleSlotService.deleteById(id);                             // xóa slot theo id, ném 404 nếu không tồn tại
        return ResponseEntity.ok(ApiResponse.successMessage("Deleted successfully")); // trả về thông báo thành công
    }
}
