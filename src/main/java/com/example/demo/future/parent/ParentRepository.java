package com.example.demo.future.parent;

import com.example.demo.future.parent.domain.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent,Long> {
}
