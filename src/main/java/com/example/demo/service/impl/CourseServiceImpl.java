package com.example.demo.service.impl;

import com.example.demo.common.exception.NotFoundExeception;
import com.example.demo.domain.entity.Course;
import com.example.demo.dto.course.CourseResponse;
import com.example.demo.dto.course.CourseUpsertRequest;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
// đánh dấu đây là một Service Bean để Spring quản lý

@RequiredArgsConstructor
// Lombok tự động tạo Constructor chứa các biến final bên dưới
// tương đương:
// public CourseServiceImpl(CourseRepository courseRepository, ModelMapper mapper) { ... }

public class CourseServiceImpl implements CourseService {
    // lớp triển khai (implements) các phương thức được khai báo trong CourseService

    private final CourseRepository courseRepository;
    // dùng để thao tác với database (CRUD bảng Course)

    private final ModelMapper mapper;
    // dùng để chuyển đổi giữa Entity <-> DTO

    @Override
    public List<CourseResponse> findAll() {

        return courseRepository.findAll()
                // lấy tất cả Course từ database

                .stream()
                // chuyển List<Course> thành Stream<Course>

                .map(c -> mapper.map(c, CourseResponse.class))
                // với mỗi Course c
                // chuyển Course -> CourseResponse

                .toList();
        // gom lại thành List<CourseResponse>
    }

    @Override
    public Optional<CourseResponse> findById(Long id) {

        return courseRepository.findById(id)
                // tìm Course theo id
                // trả về Optional<Course>

                .map(c -> mapper.map(c, CourseResponse.class));
        // nếu tìm thấy thì chuyển Course -> CourseResponse
        // kết quả cuối cùng là Optional<CourseResponse>
    }

    @Override
    public CourseResponse save(CourseUpsertRequest req) {

        Course course = mapper.map(req, Course.class);
        // chuyển dữ liệu từ request -> entity Course

        return mapper.map(
                courseRepository.save(course),
                CourseResponse.class
        );
        // lưu Course xuống database

        // save() trả về entity sau khi lưu

        // chuyển entity vừa lưu thành CourseResponse
        // rồi trả về cho client
    }

    @Override
    public CourseResponse update(Long id, CourseUpsertRequest req) {

        Course existing = courseRepository.findById(id)

                .orElseThrow(
                        () -> new NotFoundExeception(
                                "Course not found: " + id
                        )
                );
        // tìm Course theo id

        // nếu không tồn tại
        // ném ra Exception:
        // Course not found: 1

        mapper.map(req, existing);
        // copy dữ liệu từ request sang object existing

        // ví dụ:
        // req.name = "Java Core"
        // existing.name sẽ được cập nhật thành "Java Core"

        return mapper.map(
                courseRepository.save(existing),
                CourseResponse.class
        );
        // lưu dữ liệu đã cập nhật xuống database

        // chuyển kết quả thành CourseResponse

        // trả về cho client
    }

    @Override
    public void delete(Long id) {

        if (!courseRepository.existsById(id)) {
            // kiểm tra Course có tồn tại hay không

            throw new NotFoundExeception(
                    "Course not found: " + id
            );
            // nếu không tồn tại thì báo lỗi
        }

        courseRepository.deleteById(id);
        // xóa Course theo id
    }
}