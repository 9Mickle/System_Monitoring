package com.epam.system_monitoring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;

    //Если убрать mappedBy, то будет таблица course_modules
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "course", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Module> modules;

    @PreRemove
    public void deleteCourse() {
        this.getModules().forEach(module -> module.setCourse(null));
    }
}
