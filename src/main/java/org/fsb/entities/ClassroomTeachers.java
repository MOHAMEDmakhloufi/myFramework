package org.fsb.entities;

import core.annotations.Entity;
import core.annotations.GeneratedValue;
import core.annotations.Id;
import core.annotations.SequenceName;
import core.orm.GenerationType;

@Entity
public class ClassroomTeachers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceName("classroomTeachersSeq")
    private Long id;

    private Long classroomId;
    private Long teacherId;
    private String course;

    public ClassroomTeachers(Long id, Long classroomId, Long teacherId, String course) {
        this.id = id;
        this.classroomId = classroomId;
        this.teacherId = teacherId;
        this.course = course;
    }

    public ClassroomTeachers() {
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

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "ClassroomTeachers{" +
                "classroomId=" + classroomId +
                ", teacherId=" + teacherId +
                ", course='" + course + '\'' +
                '}';
    }
}
