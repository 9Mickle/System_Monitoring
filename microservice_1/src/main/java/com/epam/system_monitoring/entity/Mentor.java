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
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mentor", cascade = CascadeType.PERSIST)
    private List<Student> students;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reporter", cascade = CascadeType.PERSIST)
    private List<Module> modules;

    @PreRemove
    public void deleteMentor() {
        this.getStudents().forEach(student -> student.setMentor(null));
        this.getModules().forEach(module -> module.setReporter(null));
    }
}
