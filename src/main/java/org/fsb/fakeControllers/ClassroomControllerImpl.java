package org.fsb.fakeControllers;

import core.annotations.Autowire;
import core.annotations.Component;
import core.annotations.FakeController;
import core.annotations.Qualifier;
import org.fsb.entities.*;
import org.fsb.model.ClassroomRecord;
import org.fsb.services.ClassroomService;
import org.fsb.services.StudentService;
import org.fsb.services.TeacherService;

import java.util.List;

@FakeController
@Component
public class ClassroomControllerImpl {

    @Autowire
    @Qualifier("version2")
    private StudentService studentService;


    private TeacherService teacherService;
    private ClassroomService classroomService;
    @Autowire
    public ClassroomControllerImpl(TeacherService teacherService) {
        this.teacherService = teacherService;
    }
    @Autowire
    public void setClassroomService(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    public void testCrudApplication() {
        // Create three students
        Student student1 = new Student(0L, "mariam");
        Student student2 = new Student(0L, "mohamed");
        Student student3 = new Student(0L, "bacem");

        // Register students
        student1 = studentService.registerStudent(student1);
        System.out.println("Registered Student 1: " + student1);
        student2 = studentService.registerStudent(student2);
        System.out.println("Registered Student 2: " + student2);
        student3 = studentService.registerStudent(student3);
        System.out.println("Registered Student 3: " + student3);

        // Create two teachers
        Teacher teacher1 = new Teacher(0L, "fedi");
        Teacher teacher2 = new Teacher(0L, "tarak");

        // Register teachers
        teacher1 = teacherService.registerTeacher(teacher1);
        System.out.println("Registered Teacher 1: " + teacher1);
        teacher2 = teacherService.registerTeacher(teacher2);
        System.out.println("Registered Teacher 2: " + teacher2);

        // Create two classrooms
        Classroom classroom1 = new Classroom(0L, "CI1");
        Classroom classroom2 = new Classroom(0L, "CI2");

        // Register classrooms
        classroom1 = classroomService.registerClassroom(classroom1);
        System.out.println("Registered Classroom 1: " + classroom1);
        classroom2 = classroomService.registerClassroom(classroom2);
        System.out.println("Registered Classroom 2: " + classroom2);

        // Add teachers to classrooms
        ClassroomTeachers classroomTeacher1 = new ClassroomTeachers(0L, classroom1.getId(), teacher1.getId(), "Math");
        ClassroomTeachers classroomTeacher2 = new ClassroomTeachers(0L, classroom2.getId(), teacher2.getId(), "Physics");
        classroomService.addTeacherToClassroom(classroomTeacher1);
        System.out.println("Added Teacher 1 to Classroom 1: " + classroomTeacher1);
        classroomService.addTeacherToClassroom(classroomTeacher2);
        System.out.println("Added Teacher 2 to Classroom 2: " + classroomTeacher2);

        // Add students to classrooms
        ClassroomStudents classroomStudent1 = new ClassroomStudents(0L, classroom1.getId(), student1.getId());
        ClassroomStudents classroomStudent2 = new ClassroomStudents(0L, classroom1.getId(), student2.getId());
        ClassroomStudents classroomStudent3 = new ClassroomStudents(0L, classroom2.getId(), student3.getId());
        classroomService.addStudentToClassroom(classroomStudent1);
        System.out.println("Added Student 1 to Classroom 1: " + classroomStudent1);
        classroomService.addStudentToClassroom(classroomStudent2);
        System.out.println("Added Student 2 to Classroom 1: " + classroomStudent2);
        classroomService.addStudentToClassroom(classroomStudent3);
        System.out.println("Added Student 3 to Classroom 2: " + classroomStudent3);

        // Retrieve classrooms by ID
        Classroom classroomById1 = classroomService.getClassroomById(classroom1.getId());
        System.out.println("Retrieved Classroom 1: " + classroomById1);
        Classroom classroomById2 = classroomService.getClassroomById(classroom2.getId());
        System.out.println("Retrieved Classroom 2: " + classroomById2);

        // Retrieve full classrooms by ID
        ClassroomRecord fullClassroomById1 = classroomService.getFullClassroomById(classroom1.getId());
        System.out.println("Retrieved Full Classroom 1: " + fullClassroomById1);
        ClassroomRecord fullClassroomById2 = classroomService.getFullClassroomById(classroom2.getId());
        System.out.println("Retrieved Full Classroom 2: " + fullClassroomById2);

        // Retrieve all classrooms
        List<Classroom> allClassrooms = classroomService.getAll();
        System.out.println("All Classrooms: " + allClassrooms);

        // Retrieve all full classrooms
        List<ClassroomRecord> allFullClassrooms = classroomService.getFullClassrooms();
        System.out.println("All Full Classrooms: " + allFullClassrooms);

        // Check existence by ID
        boolean existsById1 = classroomService.existsById(classroom1.getId());
        System.out.println("Does Classroom 1 exist? " + existsById1);
        boolean existsById2 = classroomService.existsById(classroom2.getId());
        System.out.println("Does Classroom 2 exist? " + existsById2);

        // We comment this section to check the database has the data or not

        /*
        // Delete classrooms by ID
        classroomService.deleteClassroomById(classroom1.getId());
        System.out.println("Deleted Classroom 1 with ID: " + classroom1.getId());
        classroomService.deleteClassroomById(classroom2.getId());
        System.out.println("Deleted Classroom 2 with ID: " + classroom2.getId());

        // Count classrooms
        long countClassrooms = classroomService.countClassrooms();
        System.out.println("Total Classrooms: " + countClassrooms);

        // Delete students and teachers (if needed)
        studentService.deleteStudentById(student1.getId());
        System.out.println("Deleted Student 1 with ID: " + student1.getId());
        studentService.deleteStudentById(student2.getId());
        System.out.println("Deleted Student 2 with ID: " + student2.getId());
        studentService.deleteStudentById(student3.getId());
        System.out.println("Deleted Student 3 with ID: " + student3.getId());
        teacherService.deleteTeacherById(teacher1.getId());
        System.out.println("Deleted Teacher 1 with ID: " + teacher1.getId());
        teacherService.deleteTeacherById(teacher2.getId());
        System.out.println("Deleted Teacher 2 with ID: " + teacher2.getId());

         */
    }



}
