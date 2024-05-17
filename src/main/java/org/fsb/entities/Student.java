package org.fsb.entities;

import core.annotations.Entity;
import core.annotations.GeneratedValue;
import core.annotations.Id;
import core.annotations.SequenceName;
import core.orm.GenerationType;

@Entity
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceName("studentSeq")
    private Long id;

    private String name;

    public Student(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Student() {
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
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
