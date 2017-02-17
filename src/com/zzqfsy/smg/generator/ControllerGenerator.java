package com.zzqfsy.smg.generator;

import java.util.Map;
import java.util.Set;

import com.zzqfsy.smg.util.DBUtils;
import com.zzqfsy.smg.util.FileUtils;
import com.zzqfsy.smg.util.StringUtils;

public class ControllerGenerator extends BaseGenerator{

	public static void generateController(String tableName) {
		String template = "controller.t";
		String content = generate(template,tableName);
		
		Map<String,String> primaryKeyMap = DBUtils.getPrimaryKey(tableName);
		String primaryKey = primaryKeyMap.get("primaryKey");
		Map<String,String> fieldMap = DBUtils.getFormatedColumnNameTypeMap(tableName);

		content = generateUpdateAssignValue(content, tableName, primaryKey, fieldMap);
		FileUtils.write(content, FileUtils.getPackageDirectory("controller")+StringUtils.firstUpperAndNoPrefix(tableName)+"Controller.java");
	}
	private static String generateUpdateAssignValue(String content, String tableName, String primaryKey, Map<String,String> fieldMap){
		StringBuilder sb = new StringBuilder();
		Set<String> fieldNameSet = fieldMap.keySet();
		for(String name : fieldNameSet){
			if(!StringUtils.format(primaryKey).equals(name)&&!"createTime".equals(name)&&!"createBy".equals(name)&&!"updateTime".equals(name)&&!"updateBy".equals(name)){
				sb.append("\t\t\told").append(StringUtils.firstUpperAndNoPrefix(tableName)).append(".set").append(StringUtils.firstUpperNoFormat(name))
				.append("(").append(StringUtils.formatAndNoPrefix(tableName)).append(".get").append(StringUtils.firstUpperNoFormat(name)).append("());\n");
			}
			
		}
		
		String newContent = content.replaceAll("[$][{]updateAssignValue}", sb.toString()).replaceAll("[$][{]PrimaryKey}", StringUtils.firstUpperAndNoPrefix(primaryKey));
		return newContent;
	}
}
