package com.epam.system_monitoring.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column(unique = true)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignee", cascade = CascadeType.PERSIST)
    private List<Module> modules;

    @PreRemove
    public void deleteStudent() {
        this.setMentor(null);
        this.getModules().forEach(module -> module.setAssignee(null));
    }
}
