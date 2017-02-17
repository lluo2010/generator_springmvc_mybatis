package com.zzqfsy.smg.generator;

import java.util.List;

import com.zzqfsy.smg.util.FileUtils;
import com.zzqfsy.smg.util.PropertiesUtils;


public class ProjectGenerator extends BaseGenerator{

	public static void generateProject(String project,List<String> tableList) {
		try {
			FileUtils.unZip(PropertiesUtils.getLocation());
			//generate constant
			ConstGenerator.generateConst();
			//generate config files in Web-Inf
			ConfigGenerator.generateConfig(tableList);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}
