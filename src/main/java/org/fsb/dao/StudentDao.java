package org.fsb.dao;

import core.annotations.Repository;
import core.orm.CrudRepository;
import org.fsb.entities.Student;

@Repository
public interface StudentDao extends CrudRepository<Student, Long> {
}
