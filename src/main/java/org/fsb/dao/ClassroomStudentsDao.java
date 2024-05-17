package org.fsb.dao;

import core.annotations.Repository;
import core.orm.CrudRepository;
import org.fsb.entities.Classroom;
import org.fsb.entities.ClassroomStudents;

@Repository
public interface ClassroomStudentsDao extends CrudRepository<ClassroomStudents, Long> {
}
