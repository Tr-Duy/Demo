package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.course.CourseResponse;
import com.example.demo.dto.course.CourseUpsertRequest;
import com.example.demo.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
// đánh dấu đây là REST Controller
// các hàm trong class sẽ xử lý HTTP Request và trả về JSON

@RequestMapping("/api/course")
// URL gốc của Controller là:
// /api/course

@RequiredArgsConstructor
// Lombok tự động tạo constructor chứa các biến final

public class CourseController {

    private final CourseService courseService;
    // inject Service để xử lý nghiệp vụ Course

    @GetMapping
    // xử lý request GET /api/course

    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    // chỉ ADMIN hoặc ACADEMIC_STAFF mới được gọi API này

    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        courseService.findAll()
                )
        );
        // gọi service lấy toàn bộ Course

        // dữ liệu trả về dạng:
        /*
        {
            "success": true,
            "data": [
                {
                    "id": 1,
                    "name": "Java"
                },
                {
                    "id": 2,
                    "name": "Spring Boot"
                }
            ]
        }
        */
    }

    @GetMapping("/{id}")
    // xử lý request:
    // GET /api/course/1

    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    // chỉ ADMIN hoặc ACADEMIC_STAFF được phép xem

    public ResponseEntity<ApiResponse<CourseResponse>> getById(
            @PathVariable Long id
    ) {
        // lấy id từ URL

        Optional<CourseResponse> result =
                courseService.findById(id);
        // tìm Course theo id

        if (result.isPresent()) {
            // nếu tìm thấy

            return ResponseEntity.ok(
                    ApiResponse.success(
                            result.get()
                    )
            );
            // trả về dữ liệu Course
        }

        return ResponseEntity.notFound().build();
        // nếu không tìm thấy trả về HTTP 404
    }

    @PostMapping
    // xử lý request POST /api/course

    @PreAuthorize("hasRole('ADMIN')")
    // chỉ ADMIN được tạo Course mới

    public ResponseEntity<ApiResponse<CourseResponse>> create(

            @Valid
            // kích hoạt validate dữ liệu

            @RequestBody
            // lấy dữ liệu JSON từ body request

            CourseUpsertRequest req
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        courseService.save(req)
                )
        );
        // gọi service lưu Course mới

        /*
        Request:

        {
            "name":"Java Core",
            "description":"..."
        }

        Response:

        {
            "success": true,
            "data": {
                "id":1,
                "name":"Java Core"
            }
        }
        */
    }

    @PutMapping("/{id}")
    // xử lý request:
    // PUT /api/course/1

    @PreAuthorize("hasRole('ADMIN')")
    // chỉ ADMIN được cập nhật Course

    public ResponseEntity<ApiResponse<CourseResponse>> update(

            @PathVariable Long id,
            // lấy id từ URL

            @Valid
            @RequestBody
            CourseUpsertRequest req
            // dữ liệu cập nhật từ body
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        courseService.update(id, req)
                )
        );
        // gọi service cập nhật Course theo id

        /*
        PUT /api/course/1

        {
            "name":"Spring Boot"
        }
        */
    }

    @DeleteMapping("/{id}")
    // xử lý request:
    // DELETE /api/course/1

    @PreAuthorize("hasRole('ADMIN')")
    // chỉ ADMIN được phép xóa

    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id
    ) {

        courseService.delete(id);
        // gọi service xóa Course

        return ResponseEntity.ok(
                ApiResponse.successMessage(
                        "Deleted successfully"
                )
        );
        // trả về thông báo thành công

        /*
        {
            "success": true,
            "message": "Deleted successfully"
        }
        */
    }
}
