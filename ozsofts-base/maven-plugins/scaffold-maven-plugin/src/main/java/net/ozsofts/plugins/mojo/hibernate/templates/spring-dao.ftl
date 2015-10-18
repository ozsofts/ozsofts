<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">

<#list entityList as entity>
	<bean id="${entity.entityPrefix}Dao" class="${entity.basePackage}.dao.impl.${entity.entityName}DaoImpl">
		<property name="sessionFactory" ref="${sessionFactory!'sessionFactory'}" />
	</bean>
</#list>

</beans>