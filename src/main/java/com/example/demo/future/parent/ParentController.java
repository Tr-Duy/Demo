package com.example.demo.future.parent;

import com.example.demo.common.ApiResponse;
import com.example.demo.future.parent.dto.ParentReponse;
import com.example.demo.future.parent.dto.ParentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    //lay toan bo danh sach parent
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ParentReponse>>> getAll(Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(parentService.findAll(pageable),"Get all parent successful"));
    }
    // lay danh sach parent dua vao id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParentReponse>> getById(@PathVariable Long id){
        Optional<ParentReponse> result = parentService.findById(id);
        if(result.isPresent()){
            return ResponseEntity.ok(ApiResponse.success(result.get(),"Get parent successful"));
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    //tao parent
    @PostMapping
    public ResponseEntity<ApiResponse<ParentReponse>> create(@Valid @RequestBody ParentRequest req){
        return ResponseEntity.ok(ApiResponse.success(parentService.create(req)));
    }
    // sua parent
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParentReponse>> update(@PathVariable Long id, @Valid @RequestBody ParentRequest req){
        return ResponseEntity.ok(ApiResponse.success(parentService.update(id,req)));
    }
    // xoa parent
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        parentService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Delete parent successful"));
    }

}


