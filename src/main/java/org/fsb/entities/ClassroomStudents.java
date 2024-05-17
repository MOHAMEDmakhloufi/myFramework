package org.fsb.entities;

import core.annotations.Entity;
import core.annotations.GeneratedValue;
import core.annotations.Id;
import core.annotations.SequenceName;
import core.orm.GenerationType;

@Entity
public class ClassroomStudents {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceName("classroomStudentsSeq")
    private Long id;

    private Long classroomId;
    private Long studentId;

    public ClassroomStudents(Long id, Long classroomId, Long studentId) {
        this.id = id;
        this.classroomId = classroomId;
        this.studentId = studentId;
    }

    public ClassroomStudents() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "ClassroomStudents{" +
                "id=" + id +
                ", classroomId=" + classroomId +
                ", studentId=" + studentId +
                '}';
    }
}
