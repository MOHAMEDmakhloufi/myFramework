package org.fsb.services;

import core.annotations.Autowire;
import core.annotations.Component;
import org.fsb.dao.ClassroomStudentsDao;
import org.fsb.dao.StudentDao;
import org.fsb.entities.ClassroomStudents;
import org.fsb.entities.Student;

import java.util.List;
import java.util.Objects;

@Component("version2")
public class StudentServiceImpl2 implements StudentService {
    @Autowire
    private ClassroomStudentsDao classroomStudentsDao;
    private final StudentDao studentDao;
    @Autowire
    public StudentServiceImpl2(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Student registerStudent(Student student) {
        return studentDao.save(student);
    }

    @Override
    public Student getById(Long id) {
        return studentDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Student Not Found 404 !"));
    }

    @Override
    public List<Student> getAll() {
        return studentDao.findAll();
    }

    @Override
    public List<ClassroomStudents> getStudentClassrooms(Long studentId) {
        return this.classroomStudentsDao.findAll()
                .stream()
                .filter(classroomStudent -> Objects.equals(classroomStudent.getStudentId(), studentId))
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return studentDao.existsById(id);
    }

    @Override
    public void deleteStudentById(Long id) {
        this.studentDao.deleteById(id);
    }

    @Override
    public long countStudents() {
        return this.studentDao.count();
    }
}
