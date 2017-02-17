package ${packageName}.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${packageName}.dao.${ClassName}Dao;
import ${packageName}.model.${ClassName};
import ${packageName}.service.${ClassName}Service;

@Service
public class ${ClassName}ServiceImpl implements ${ClassName}Service {
    @Autowired
	private ${ClassName}Dao ${className}Dao;

	public ${ClassName} getByPrimaryKey(${primaryKeyType} ${primaryKey}){
		return ${className}Dao.getByPrimaryKey(${primaryKey});
	}
	public void create(${ClassName} ${className}){
		${className}Dao.create(${className});
	}
	public void update(${ClassName} ${className}){
		${className}Dao.update(${className});
	}
	public void delete(${primaryKeyType} ${primaryKey}){
		${className}Dao.delete(${primaryKey});
	}
	public int count(Map<String,Object> params){
		return ${className}Dao.count(params);
	}
	public ${ClassName} get(Map<String,Object> params){
	    return ${className}Dao.get(params);
	}
	public List<${ClassName}> getList(Map<String,Object> params){
		return ${className}Dao.getList(params);
	}
}
