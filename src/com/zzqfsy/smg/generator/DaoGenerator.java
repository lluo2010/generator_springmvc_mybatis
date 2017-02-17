package com.zzqfsy.smg.generator;

import com.zzqfsy.smg.util.FileUtils;
import com.zzqfsy.smg.util.StringUtils;

public class DaoGenerator extends BaseGenerator{

	public static void generateDao(String tableName) {
		String template = "dao.t";
		String content = generate(template,tableName);
		FileUtils.write(content, FileUtils.getPackageDirectory("dao")+StringUtils.firstUpperAndNoPrefix(tableName)+"Dao.java");
	}
}
