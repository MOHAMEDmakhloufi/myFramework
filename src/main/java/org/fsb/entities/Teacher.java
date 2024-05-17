package org.fsb.entities;

import core.annotations.Entity;
import core.annotations.GeneratedValue;
import core.annotations.Id;
import core.annotations.SequenceName;
import core.orm.GenerationType;

@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceName("teacherSeq")
    private Long id;

    private String name;

    public Teacher(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Teacher() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
