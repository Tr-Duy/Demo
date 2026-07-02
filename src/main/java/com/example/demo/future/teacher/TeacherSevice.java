package com.example.demo.future.teacher;

import com.example.demo.future.teacher.dto.TeacherReponse;
import com.example.demo.future.teacher.dto.TeacherRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TeacherSevice {
    //ham lay toan bo danh sach giao vien
    Page<TeacherReponse> findAll(Pageable pageable);

    //ham lay danh sach theo id giao vien
    Optional<TeacherReponse> findById(Long id);

    //ham create
    TeacherReponse create(TeacherRequest req);

    //update
    TeacherReponse update(Long id, TeacherRequest req);

    //delete
    void delete(Long id);
}
