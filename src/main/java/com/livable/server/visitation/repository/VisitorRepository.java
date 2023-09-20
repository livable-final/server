package com.livable.server.visitation.repository;

import com.livable.server.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visitor, Long>, VisitorCustomRepository {
}
