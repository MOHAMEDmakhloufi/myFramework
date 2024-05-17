package org.fsb.services;

import org.fsb.entities.ClassroomStudents;
import org.fsb.entities.Student;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student registerStudent(Student student);
    Student getById(Long id);
    List<Student> getAll();
    List<ClassroomStudents> getStudentClassrooms(Long studentId);
    boolean existsById(Long id);
    void deleteStudentById(Long id);
    long countStudents();
}
