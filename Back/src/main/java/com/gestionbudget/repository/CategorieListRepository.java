package com.gestionbudget.repository;

import com.gestionbudget.model.CategorieList;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

@Repository
public interface CategorieListRepository extends JpaRepository<CategorieList, Long> {
    
}
