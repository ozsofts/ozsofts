package ${entity.basePackage}.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import ${entity.basePackage}.entity.base.Base${entity.entityName};

@SuppressWarnings("serial")
@Entity
@Table(name = "t_...")
public class ${entity.entityName} extends Base${entity.entityName} {
}
