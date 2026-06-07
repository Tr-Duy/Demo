package com.example.demo.service;

import com.example.demo.dto.course.CourseResponse;
import com.example.demo.dto.course.CourseUpsertRequest;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<CourseResponse> findAll();

    Optional<CourseResponse> findById(Long id);

    CourseResponse save(CourseUpsertRequest req);

    CourseResponse update(Long id, CourseUpsertRequest req);

    void delete(Long id);
}
