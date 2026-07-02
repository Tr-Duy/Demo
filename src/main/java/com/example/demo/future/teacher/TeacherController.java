package com.example.demo.future.teacher;

import com.example.demo.common.ApiResponse;
import com.example.demo.future.parent.dto.ParentReponse;
import com.example.demo.future.teacher.dto.TeacherReponse;
import com.example.demo.future.teacher.dto.TeacherRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherSeviceImpl teacherSevice;

    //lay toan bo danh sach teacher
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TeacherReponse>>> getAll(Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(teacherSevice.findAll(pageable),"Get all parent successful"));
    }
    @GetMapping("{/id}")
    public ResponseEntity<ApiResponse<TeacherReponse>> getById(@PathVariable Long id) {
        Optional<TeacherReponse> result = teacherSevice.findById(id);
        if(result.isPresent()){
            return ResponseEntity.ok(ApiResponse.success(result.get(),"Get teacher successful"));
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    //tao teacher
    @PostMapping()
    public ResponseEntity<ApiResponse<TeacherReponse>> create(@Valid @RequestBody TeacherRequest req){
        return ResponseEntity.ok(ApiResponse.success(teacherSevice.create(req)));
    }
    //update teacher
    public ResponseEntity<ApiResponse<TeacherReponse>> update(@PathVariable Long id, @Valid @RequestBody TeacherRequest req){
        return ResponseEntity.ok(ApiResponse.success(teacherSevice.update(id,req)));
    }
    //delete
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        teacherSevice.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Delete teacher successful"));
    }
}
