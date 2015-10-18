package ${entity.basePackage}.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobirit.mtcore.hibernate.dao.BaseDao;
import com.mobirit.mtcore.hibernate.manager.BaseManagerImpl;
import ${entity.basePackage}.entity.${entity.entityName};
import ${entity.basePackage}.manager.${entity.entityName}Manager;
import ${entity.basePackage}.dao.${entity.entityName}Dao;

@Service("${entity.entityPrefix}Manager")
@Transactional
public class ${entity.entityName}ManagerImpl extends
		BaseManagerImpl<${entity.entityName}, Long> 
		implements ${entity.entityName}Manager {
	
	private ${entity.entityName}Dao ${entity.entityPrefix}Dao;
	
	@Autowired
	public void set${entity.entityName}Dao(${entity.entityName}Dao ${entity.entityPrefix}Dao) {
		super.setDao(${entity.entityPrefix}Dao);
		
		this.${entity.entityPrefix}Dao = ${entity.entityPrefix}Dao;
	}
}
