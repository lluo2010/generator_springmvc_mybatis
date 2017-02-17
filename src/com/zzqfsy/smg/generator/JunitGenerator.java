package com.zzqfsy.smg.generator;

import com.zzqfsy.smg.util.FileUtils;
import com.zzqfsy.smg.util.PropertiesUtils;
import com.zzqfsy.smg.util.StringUtils;


public class JunitGenerator extends BaseGenerator{

	public static void generateJunit(String tableName) {
		generateBaseJunit(tableName);
		generateDaoTest(tableName);
	}
	private static void generateDaoTest(String tableName) {
		String template = "dao-test.t";
		String content = generate(template,tableName);
		FileUtils.write(content, FileUtils.getPackageDirectory("test")+StringUtils.firstUpperAndNoPrefix(tableName)+"DaoTest.java");
	}
	private static void generateBaseJunit(String tableName) {
		String template = "base-junit.t";
		String content = generate(template,tableName);
		String project = PropertiesUtils.getProject();
		if(project!=null && !"".equals(project)){
			FileUtils.write(content, FileUtils.getPackageDirectory("test")+"BaseJunit.java");
		}
		
	}
}
