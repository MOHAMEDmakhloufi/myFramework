package org.fsb.services;

import org.fsb.entities.Classroom;
import org.fsb.entities.ClassroomStudents;
import org.fsb.entities.ClassroomTeachers;
import org.fsb.model.ClassroomRecord;

import java.util.List;

public interface ClassroomService {

    Classroom registerClassroom(Classroom classroom);
    ClassroomTeachers addTeacherToClassroom(ClassroomTeachers classroomTeacher);
    ClassroomStudents addStudentToClassroom(ClassroomStudents classroomStudent);
    Classroom getClassroomById(Long id);
    ClassroomRecord getFullClassroomById(Long id);
    List<Classroom> getAll();
    List<ClassroomRecord> getFullClassrooms();
    boolean existsById(Long id);
    void deleteClassroomById(Long id);
    long countClassrooms();
}
