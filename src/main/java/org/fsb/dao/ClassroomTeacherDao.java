package org.fsb.dao;

import core.annotations.Repository;
import core.orm.CrudRepository;
import org.fsb.entities.Classroom;
import org.fsb.entities.ClassroomTeachers;

@Repository
public interface ClassroomTeacherDao extends CrudRepository<ClassroomTeachers, Long> {
}
