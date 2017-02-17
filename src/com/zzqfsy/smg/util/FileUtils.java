package com.zzqfsy.smg.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {


	public static String getTemplate(String template){
		String templatePath = FileUtils.class.getResource("").getPath().replace("util", "template");
		File file = new File(templatePath+template);
		String str = read(file);
		return str;
	}

	public static String read(File file) {
		StringBuffer res = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			int i = 0;
			while ((line = reader.readLine()) != null) {
				if (i != 0) {
					res.append('\n');
				}
				res.append(line);
				i++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	public static boolean write(String content, String path) {
		try {
			File file = new File(path);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void createPackageDirectory(){
		String location = PropertiesUtils.getLocation();
		String mainLocation = "";
		String mapperLocation = "";
		String testLocation = "";
		String project = PropertiesUtils.getProject();
		if(project!=null && !"".equals(project)){
			mainLocation = location + "/src/main/java";
			mapperLocation = location + "/src/main/resources";
			testLocation = location + "/src/test/java";
		}
		
		String packageDir = "/" + PropertiesUtils.getPackage().replaceAll("[.]", "/");
		String daoDir = mainLocation + packageDir +"/dao";
		String mapperDir = mapperLocation + "/mapper";
		String serviceDir = mainLocation + packageDir +"/service";
		String serviceImplDir = mainLocation + packageDir +"/service/impl";
		String controllerDir = mainLocation + packageDir +"/controller";
		String modelDir = mainLocation + packageDir +"/model";
		String constDir = mainLocation + packageDir +"/constant";
		String utilDir = mainLocation + packageDir +"/util";
		String testDir = testLocation + packageDir +"/test";
		String jspDir = null;
		if(project!=null && !"".equals(project)){
			jspDir = PropertiesUtils.getLocation()+"/WebContent/WEB-INF/jsp";
		}else{
			jspDir = mainLocation + "/jsp";
		}
		
		if(project!=null && !"".equals(project)){
			mkdir(constDir); 
			mkdir(utilDir); 
		}
		
		String layers = PropertiesUtils.getLayers();
		if(layers.contains("controller")){
			mkdir(controllerDir); 
		}if(layers.contains("dao")){
			mkdir(daoDir); 
		}if(layers.contains("mapper")){
			mkdir(mapperDir); 
		}if(layers.contains("service")){
			mkdir(serviceDir); 
			mkdir(serviceImplDir); 
		}if(layers.contains("model")){
			mkdir(modelDir); 
		}if(layers.contains("jsp")){
			mkdir(jspDir); 
		}if(layers.contains("test")){
			mkdir(testDir); 
		}
	}

	private static void mkdir(String dir) {
		File file;
		file = new File(dir); 
		if(!file.exists()){ 
			file.mkdirs();
		}
	}
	public static String getPackageDirectory(String name){
		String location = PropertiesUtils.getLocation();
		String packageDir = "/" + PropertiesUtils.getPackage().replaceAll("[.]", "/");
		String project = PropertiesUtils.getProject();
		String directory = null;
		if(project!=null && !"".equals(project) && !name.equals("test") && !name.equals("mapperXml")){
			directory = location + "/src/main/java" + packageDir +"/"+ name +"/";
		} else if (name.equals("mapperXml")) {
			directory = location + "/src/main/resources" +"/mapper/";
		} else if (name.equals("test")) {
			directory = location + "/src/test/java" + packageDir +"/"+ name +"/";
		} else{
			directory = location + packageDir +"/"+ name +"/";
		}
		return directory;
	}
	
	public static void unZip(String path)   
    {  
		int buffer = 1024;  
        int count = -1;  
        String savePath = path+ "/";  
  
        try   
        {  
            BufferedOutputStream bos = null;  
            ZipEntry entry = null;  
            FileInputStream fis = new FileInputStream("generator-framework.zip");   
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));  
              
            while((entry = zis.getNextEntry()) != null)   
            {  
                byte data[] = new byte[buffer];   
  
                String temp = entry.getName();  
                 
                temp = savePath + temp;  
                File file = new File(temp);
                if(entry.isDirectory()){
                	file.mkdirs();
                }else{
                	file.createNewFile();  
                	FileOutputStream fos = new FileOutputStream(file);  
                    bos = new BufferedOutputStream(fos, buffer);  
                    while((count = zis.read(data, 0, buffer)) != -1)   
                    {  
                        bos.write(data, 0, count);  
                    }  
                    bos.flush();  
                    bos.close();  
                }
            }  
  
            zis.close();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}
