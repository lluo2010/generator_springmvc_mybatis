package ${packageName}.service;

import java.util.List;
import java.util.Map;

import ${packageName}.model.${ClassName};

public interface ${ClassName}Service {
	public ${ClassName} getByPrimaryKey(${primaryKeyType} ${primaryKey});
	public void create(${ClassName} ${className});
	public void update(${ClassName} ${className});
	public void delete(${primaryKeyType} ${primaryKey});
	public int count(Map<String,Object> params);
	public ${ClassName} get(Map<String,Object> params);
    public List<${ClassName}> getList(Map<String,Object> params);
}