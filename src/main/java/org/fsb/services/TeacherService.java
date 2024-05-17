package org.fsb.services;

import org.fsb.entities.ClassroomStudents;
import org.fsb.entities.ClassroomTeachers;
import org.fsb.entities.Teacher;

import java.util.List;

public interface TeacherService {

    Teacher registerTeacher(Teacher teacher);
    Teacher getById(Long id);
    List<Teacher> getAll();
    List<ClassroomTeachers> getTeacherClassrooms(Long teacherId);
    boolean existsById(Long id);
    void deleteTeacherById(Long id);
    long countTeachers();
}
