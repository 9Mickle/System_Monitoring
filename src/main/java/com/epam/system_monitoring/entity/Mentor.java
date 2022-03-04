package com.epam.system_monitoring.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "mentor")
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column(unique = true)
    private String username;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mentor", cascade = CascadeType.PERSIST)
    private List<Student> students;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reporter", cascade = CascadeType.ALL)
    private List<Module> modules;

    @PreRemove
    public void deleteMentor() {
        this.getStudents().forEach(student -> student.setMentor(null));
    }
}
