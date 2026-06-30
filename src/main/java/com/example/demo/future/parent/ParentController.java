package com.example.demo.future.parent;

import com.example.demo.common.ApiResponse;
import com.example.demo.future.parent.dto.ParentReponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    //lay toan bo danh sach parent
    public ResponseEntity<ApiResponse<List<ParentReponse>>> getAll(){
        return ResponseEntity.ok(ApiResponse.success(parentService.findAll(),"Get all parent successful"));
    }
    // lay danh sach parent dua vao id

    //tao parent

    // sua parent

    // xoa parent

}
