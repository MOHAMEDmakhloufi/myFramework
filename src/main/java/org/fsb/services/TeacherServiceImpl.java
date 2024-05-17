package org.fsb.services;

import core.annotations.Autowire;
import core.annotations.Component;
import org.fsb.dao.ClassroomTeacherDao;
import org.fsb.dao.TeacherDao;
import org.fsb.entities.ClassroomTeachers;
import org.fsb.entities.Teacher;

import java.util.List;
import java.util.Objects;
@Component
public class TeacherServiceImpl implements TeacherService {
    private final ClassroomTeacherDao classroomTeacherDao;
    private final TeacherDao teacherDao;

    @Autowire
    public TeacherServiceImpl(ClassroomTeacherDao classroomTeacherDao, TeacherDao teacherDao) {
        this.classroomTeacherDao = classroomTeacherDao;
        this.teacherDao = teacherDao;
    }




    @Override
    public Teacher registerTeacher(Teacher teacher) {
        return teacherDao.save(teacher);
    }

    @Override
    public Teacher getById(Long id) {
        return teacherDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher Not Found 404 !"));
    }

    @Override
    public List<Teacher> getAll() {
        return teacherDao.findAll();
    }

    @Override
    public List<ClassroomTeachers> getTeacherClassrooms(Long teacherId) {
        return classroomTeacherDao.findAll()
                .stream()
                .filter(classroomTeacher -> Objects.equals(classroomTeacher.getTeacherId(), teacherId))
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return teacherDao.existsById(id);
    }

    @Override
    public void deleteTeacherById(Long id) {
        this.teacherDao.deleteById(id);
    }

    @Override
    public long countTeachers() {
        return teacherDao.count();
    }
}
