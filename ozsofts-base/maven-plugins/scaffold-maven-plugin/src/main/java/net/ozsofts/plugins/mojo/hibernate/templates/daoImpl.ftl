package ${entity.basePackage}.dao.impl;

import org.springframework.stereotype.Repository;

import com.mobirit.mtcore.hibernate.dao.BaseDaoImpl;
import ${entity.basePackage}.dao.${entity.entityName}Dao;
import ${entity.basePackage}.entity.${entity.entityName};

@Repository("${entity.entityPrefix}Dao")
public class ${entity.entityName}DaoImpl extends BaseDaoImpl<${entity.entityName}, Long> implements ${entity.entityName}Dao {
}
