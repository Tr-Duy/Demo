package com.example.demo.repository;

import com.example.demo.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(
            "SELECT o FROM Course o where o.isActive=1"
    )
// sử dụng JPQL (Java Persistence Query Language)
// Course là tên Entity, không phải tên bảng trong database
// o là bí danh (alias) đại diện cho từng đối tượng Course
// điều kiện where o.isActive = 1 nghĩa là chỉ lấy những Course đang hoạt động

    List<Course> findByIdCourseActive();
// phương thức trả về danh sách các đối tượng Course
// khi gọi hàm này Spring Data JPA sẽ thực thi câu JPQL phía trên
// kết quả là lấy tất cả Course có isActive = 1
}
