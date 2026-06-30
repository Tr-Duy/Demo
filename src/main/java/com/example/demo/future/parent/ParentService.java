package com.example.demo.future.parent;

import com.example.demo.future.parent.dto.ParentReponse;
import com.example.demo.future.parent.dto.ParentRequest;

import java.util.List;
import java.util.Optional;

public interface ParentService {
    //ham lay toan bo danh sach Parent
    List<ParentReponse> findAll();

    //ham tim kiem theo id
    Optional<ParentReponse> findById(Long id);

    //ham tao
    ParentReponse create(ParentRequest req);

    //ham sua
    ParentReponse update(Long id, ParentRequest req);

    //ham xoa
    void delete(Long id);
}
