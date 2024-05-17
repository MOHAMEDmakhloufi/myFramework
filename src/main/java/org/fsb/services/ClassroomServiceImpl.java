package org.fsb.services;

import core.annotations.Autowire;
import core.annotations.Component;
import core.annotations.Qualifier;
import org.fsb.dao.ClassroomDao;
import org.fsb.dao.ClassroomStudentsDao;
import org.fsb.dao.ClassroomTeacherDao;
import org.fsb.entities.Classroom;
import org.fsb.entities.ClassroomStudents;
import org.fsb.entities.ClassroomTeachers;
import org.fsb.entities.Student;
import org.fsb.model.ClassroomRecord;

import java.util.List;
import java.util.Objects;
@Component
public class ClassroomServiceImpl implements ClassroomService {
    @Autowire
    private ClassroomDao classroomDao;
    @Autowire
    private ClassroomTeacherDao classroomTeacherDao;
    @Autowire
    private ClassroomStudentsDao classroomStudentsDao;
    @Autowire
    @Qualifier("version1")
    private StudentService studentService;
    @Override
    public Classroom registerClassroom(Classroom classroom) {
        return classroomDao.save(classroom);
    }

    @Override
    public ClassroomTeachers addTeacherToClassroom(ClassroomTeachers classroomTeacher) {
        return classroomTeacherDao.save(classroomTeacher);
    }

    @Override
    public ClassroomStudents addStudentToClassroom(ClassroomStudents classroomStudent) {
        return classroomStudentsDao.save(classroomStudent);
    }

    @Override
    public Classroom getClassroomById(Long id) {
        return classroomDao.findById(id)
                .orElseThrow(()-> new RuntimeException("classroom not found 404!"));
    }

    @Override
    public ClassroomRecord getFullClassroomById(Long id) {
        Classroom classroom = classroomDao.findById(id)
                .orElseThrow(() -> new RuntimeException("classroom not found 404!"));
        List<Student> classroomStudents1 = classroomStudentsDao.findAll().stream()
                .filter(classroomStudent -> Objects.equals(classroomStudent.getClassroomId(), id))
                .map(classroomStudents -> studentService.getById(classroomStudents.getStudentId()))
                .toList();
        List<ClassroomTeachers> classroomTeachers = classroomTeacherDao.findAll().stream()
                .filter(classroomTeacher -> Objects.equals(classroomTeacher.getClassroomId(), id))
                .toList();

        return new ClassroomRecord(id, classroom.getName(), classroomTeachers, classroomStudents1);
    }

    @Override
    public List<Classroom> getAll() {
        return classroomDao.findAll();
    }

    @Override
    public List<ClassroomRecord> getFullClassrooms() {
        return classroomDao.findAll().stream()
                .map(classroom -> {
                    List<Student> classroomStudents1 = classroomStudentsDao.findAll().stream()
                            .filter(classroomStudent -> Objects.equals(classroomStudent.getClassroomId(), classroom.getId()))
                            .map(classroomStudents -> studentService.getById(classroomStudents.getStudentId()))
                            .toList();
                    List<ClassroomTeachers> classroomTeachers = classroomTeacherDao.findAll().stream()
                            .filter(classroomTeacher -> Objects.equals(classroomTeacher.getClassroomId(), classroom.getId()))
                            .toList();

                    return new ClassroomRecord(classroom.getId(), classroom.getName(), classroomTeachers, classroomStudents1);
                })
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return classroomDao.existsById(id);
    }

    @Override
    public void deleteClassroomById(Long id) {
        classroomStudentsDao.findAll().stream()
                .filter(classroomStudent -> Objects.equals(classroomStudent.getClassroomId(), id))
                .forEach(classroomStudents -> {
                    classroomStudentsDao.deleteById(classroomStudents.getId());
                });
        classroomTeacherDao.findAll().stream()
                .filter(classroomTeacher -> Objects.equals(classroomTeacher.getClassroomId(), id))
                .forEach(classroomTeacher -> {
                    classroomTeacherDao.deleteById(classroomTeacher.getId());
                });

        classroomDao.deleteById(id);
    }

    @Override
    public long countClassrooms() {
        return classroomDao.count();
    }
}
