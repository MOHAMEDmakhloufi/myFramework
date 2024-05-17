package org.fsb.dao;

import core.annotations.Repository;
import core.orm.CrudRepository;
import org.fsb.entities.Classroom;
import org.fsb.entities.Student;

@Repository
public interface ClassroomDao extends CrudRepository<Classroom, Long> {
}
