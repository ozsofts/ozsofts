<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">

<#list entityList as entity>
	<bean id="${entity.entityPrefix}Manager" class="${entity.basePackage}.manager.impl.${entity.entityName}ManagerImpl">
		<property name="${entity.entityPrefix}Dao" ref="${entity.entityPrefix}Dao" />
	</bean>
</#list>

</beans>
