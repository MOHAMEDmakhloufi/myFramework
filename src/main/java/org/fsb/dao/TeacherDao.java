package org.fsb.dao;

import core.annotations.Repository;
import core.orm.CrudRepository;
import org.fsb.entities.Teacher;

@Repository
public interface TeacherDao extends CrudRepository<Teacher, Long> {
}
