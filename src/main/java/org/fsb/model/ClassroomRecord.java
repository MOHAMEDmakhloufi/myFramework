package org.fsb.model;

import org.fsb.entities.Classroom;
import org.fsb.entities.ClassroomStudents;
import org.fsb.entities.ClassroomTeachers;
import org.fsb.entities.Student;

import java.util.List;

public record ClassroomRecord(Long classroomId,
                              String classroomName,
                              List<ClassroomTeachers> classroomTeachers,
                              List<Student> students) {

}
