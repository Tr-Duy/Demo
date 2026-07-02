package com.example.demo.future.teacher;


import com.example.demo.common.exception.NotFoundException;
import com.example.demo.future.teacher.domain.Teacher;
import com.example.demo.future.teacher.dto.TeacherReponse;
import com.example.demo.future.teacher.dto.TeacherRequest;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class TeacherSeviceImpl implements TeacherSevice {
    private final TeacherRepository teacherRepository;
    private final ModelMapper mapper;

    //ham lay toan bo danh sach giao vien
    @Override
    public Page<TeacherReponse> findAll(Pageable pageable){
        return teacherRepository.findAll(pageable).map(t -> mapper.map(t, TeacherReponse.class));
    }
    //ham lay danh sach theo id giao vien
    @Override
    public Optional<TeacherReponse> findById(Long id){
        return teacherRepository.findById(id).map(t -> mapper.map(t, TeacherReponse.class));
    }
    //ham create
    @Override
    public TeacherReponse create(TeacherRequest req){
        Teacher teacher = mapper.map(req, Teacher.class);
        return mapper.map(teacherRepository.save(teacher), TeacherReponse.class);
    }
    //update
    @Override
    public TeacherReponse update(Long id, TeacherRequest req){
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + id));
         mapper.map(req,existing);
        return mapper.map(teacherRepository.save(existing), TeacherReponse.class);
    }
    //delete
    @Override
    public void delete(Long id){
        if(!teacherRepository.existsById(id)){
            throw new NotFoundException("Teacher not found: " + id);
        }
        teacherRepository.deleteById(id);
    }
}
