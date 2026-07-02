package com.example.demo.future.parent;

import com.example.demo.future.parent.dto.ParentReponse;
import com.example.demo.future.parent.dto.ParentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ParentService {
    //ham lay toan bo danh sach Parent
    Page<ParentReponse> findAll(Pageable pageable);

    //ham tim kiem theo id
    Optional<ParentReponse> findById(Long id);

    //ham tao
    ParentReponse create(ParentRequest req);

    //ham sua
    ParentReponse update(Long id, ParentRequest req);

    //ham xoa
    void delete(Long id);
}
