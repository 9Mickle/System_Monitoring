package com.epam.system_monitoring.repository;

import com.epam.system_monitoring.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    Optional<Module> findByTitle(String title);

    //Сделал так, потому что модуль удалялся только после второго вызова метода delete().
    @Modifying
    @Query(nativeQuery = true, value = "Delete From module where id = ?1")
    void deleteById(Long id);
}
