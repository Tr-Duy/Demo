package com.example.demo.future.parent;

import com.example.demo.common.exception.NotFoundException;
import com.example.demo.future.parent.domain.Parent;
import com.example.demo.future.parent.dto.ParentReponse;
import com.example.demo.future.parent.dto.ParentRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final ModelMapper mapper;

    //ham lay toan bo danh sach Parent
    @Override
    public Page<ParentReponse> findAll(Pageable pageable){
        return parentRepository.findAll(pageable).map(p -> mapper.map(p,ParentReponse.class));
    }
    //ham tim kiem theo id
    @Override
    public Optional<ParentReponse> findById(Long id){
        return parentRepository.findById(id).map(p ->mapper.map(p, ParentReponse.class));
    }
    //ham tao
    @Override
    public ParentReponse create(ParentRequest req){
        Parent parent = mapper.map(req, Parent.class);
        return mapper.map(parentRepository.save(parent), ParentReponse.class);
    }
    //ham sua
    @Override
    public ParentReponse update(Long id, ParentRequest req){
        Parent existing = parentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Parent not found: " + id));
        mapper.map(req,existing);
        return mapper.map(parentRepository.save(existing),ParentReponse.class);
    }
    //ham xoa
    @Override
    public void delete(Long id){
        if(!parentRepository.existsById(id)){
            throw new NotFoundException("Parent not found: " + id);
        }
        parentRepository.deleteById(id);
    }
}
