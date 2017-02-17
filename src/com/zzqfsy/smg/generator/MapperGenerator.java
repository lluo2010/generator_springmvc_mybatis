package com.zzqfsy.smg.generator;

import java.util.List;
import java.util.Map;

import com.zzqfsy.smg.util.DBUtils;
import com.zzqfsy.smg.util.FileUtils;
import com.zzqfsy.smg.util.StringUtils;

public class MapperGenerator extends BaseGenerator{

    public static void generateMapper(String tableName) {
        String template = "mapper.t";
        List<String> list = DBUtils.getColumnNameList(tableName);
        Map<String,String> map = DBUtils.getPrimaryKey(tableName);
        String primaryKey = map.get("primaryKey");
        String primaryKeyType = map.get("primaryKeyType");
        String content = generate(template,tableName);

        content = generateResultMap(content,list, primaryKey);
        content = generateSelectSql(content, tableName, primaryKey);
        content = generateInsertSql(content, tableName, list);
        content = generateUpdateSql(content, tableName, primaryKey, list);
        content = generateDeleteSql(content, tableName, primaryKey);
        content = generateInsertPk(content, tableName, primaryKey, primaryKeyType);
        content = generateCountSql(content, tableName, primaryKey);
        content = generateSelectOneSql(content, tableName, primaryKey);
        content = generateSelectListSql(content, tableName, primaryKey);
        FileUtils.write(content, FileUtils.getPackageDirectory("mapperXml")+StringUtils.firstUpperAndNoPrefix(tableName)+"DaoMapper.xml");
    }

    private static String generateResultMap(String content,List<String> columnList, String primaryKey){
        StringBuilder sb = new StringBuilder();
        for(String col : columnList){
            String field = StringUtils.format(col);
            sb.append("\t\t<result property=\"").append(field).append("\" column=\"").append(col).append("\"/>\n");
        }
        String newContent = content.replaceAll("[$][{]resultMap}", sb.toString());
        return newContent;
    }

    private static String generateSelectSql(String content, String tableName, String primaryKey){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(tableName).append(" WHERE is_deleted = 0 AND ")
                .append(primaryKey).append(" = #{").append(StringUtils.format(primaryKey)).append("}");
        String newContent = content.replaceAll("[$][{]selectSql}", sb.toString());
        return newContent;
    }
    private static String generateInsertSql(String content, String tableName, List<String> columnList){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableName).append("\n");
        sb.append("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">").append("\n");
        for(int i=0;i<columnList.size();i++){
            String col = columnList.get(i);
            String field = StringUtils.format(col);
            sb.append("\t\t\t").append("<if test=\"" + field + " != null\">");
            if(i==(columnList.size()-1)){
                sb.append(col).append(",");
            }else{
                sb.append(col).append(",");
            }
            sb.append("</if>").append("\n");
        }
        sb.append("\t\t</trim>").append("\n");
        sb.append("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">").append("\n");
        for(int i=0;i<columnList.size();i++){
            String col = columnList.get(i);
            String field = StringUtils.format(col);
            sb.append("\t\t\t").append("<if test=\"" + field + " != null\">");
            if(i==(columnList.size()-1)){
                if ("isDeleted".equals(field)){
                    sb.append("0");
                }else {
                    sb.append("#{").append(field).append("}");
                }
            }else{
                String dbType = DBUtils.getDatabaseType();
                if("addTime".equals(field) || "modifyTime".equals(field)){
                    if("mysql".equals(dbType)){
                        sb.append("now(),");
                    }else if("oracle".equals(dbType)){
                        sb.append("SYSDATE,");
                    }
                }else if ("isDeleted".equals(field)){
                    sb.append("0,");
                }else{
                    sb.append("#{").append(field).append("},");
                }

            }
            sb.append("</if>").append("\n");
        }
        sb.append("\t\t</trim>");
        String newContent = content.replaceAll("[$][{]insertSql}", sb.toString());
        return newContent;
    }
    private static String generateUpdateSql(String content, String tableName, String primaryKey, List<String> columnList){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(tableName).append("\n");
        sb.append("\t\t<set>\n");
        for(int i=0;i<columnList.size();i++){
            String col = columnList.get(i);
            String field = StringUtils.format(col);
            if(!primaryKey.equals(col)&&!col.contains("create_time")&&!col.contains("create_by") &&
                !col.equals("add_time") && !col.equals("add_user_id")&& !col.equals("is_deleted")){
                sb.append("\t\t\t").append("<if test=\"" + field + " != null\">");
                if(i==(columnList.size()-1)){
                    if (!"addTime".equals(field) && !"addUserId".equals(field) && !"isDeleted".equals(field)){
                        sb.append(col).append(" = #{").append(field).append("},");
                    }
                }else{
                    String dbType = DBUtils.getDatabaseType();
                    if("modifyTime".equals(field)){
                        if("mysql".equals(dbType)){
                            sb.append(col).append(" = now(),");
                        }else if("oracle".equals(dbType)){
                            sb.append(col).append(" = SYSDATE,");
                        }
                    } else{
                        sb.append(col).append(" = #{").append(field).append("},");
                    }
                }
                sb.append("</if>").append("\n");
            }
        }
        sb.append("\t\t</set>\n");
        sb.append("\t\tWHERE ").append(primaryKey).append(" = #{").append(StringUtils.format(primaryKey)).append("}\n");
        if (columnList.contains("is_deleted"))
          sb.append("\t\t\tAND is_deleted = 0");
        String newContent = content.replaceAll("[$][{]updateSql}", sb.toString());
        return newContent;
    }
    private static String generateDeleteSql(String content, String tableName, String primaryKey){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(tableName).append(" SET is_deleted = 1\n")
          .append("\t\tWHERE ").append(primaryKey).append(" = #{").append(StringUtils.format(primaryKey)).append("}");
        String newContent = content.replaceAll("[$][{]deleteSql}", sb.toString());
        return newContent;
    }
    private static String generateInsertPk(String content, String tableName, String primaryKey, String primaryKeyType){
        StringBuilder sb = new StringBuilder();
        String dbType = DBUtils.getDatabaseType();
        if("mysql".equals(dbType)){
            sb.append("<selectKey resultType=\"").append(primaryKeyType).append("\" order=\"AFTER\" keyProperty=\"").append(StringUtils.format(primaryKey)).append("\" >\n");
            sb.append("\t\t\tSELECT LAST_INSERT_ID()\n");
            sb.append("\t\t</selectKey>");
        }else if("oracle".equals(dbType)){
            sb.append("<selectKey resultType=\"").append(primaryKeyType).append("\" order=\"BEFORE\" keyProperty=\"").append(StringUtils.format(primaryKey)).append("\" >\n");
            sb.append("\t\t\tSELECT S_").append(tableName).append(".NEXTVAL FROM DUAL\n");
            sb.append("\t\t</selectKey>");
        }
        String newContent = content.replaceAll("[$][{]insertPk}", sb.toString());
        return newContent;
    }

    private static String generateCountSql(String content, String tableName, String primaryKey){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(id) FROM ").append(tableName).append(" WHERE 1 = 1 AND is_deleted = 0").append("\n");
        sb.append("\t\t").append("<if test=\"" + primaryKey + " != null\">").append("\n");
        sb.append("\t\t\t").append("AND ").append(primaryKey).append(" = #{").append(primaryKey).append("}\n");
        sb.append("\t\t").append("</if>").append("\n");
        String newContent = content.replaceAll("[$][{]countSql}", sb.toString());
        return newContent;
    }

    private static String generateSelectOneSql(String content, String tableName, String primaryKey){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(tableName).append(" WHERE 1 = 1 AND is_deleted = 0 ").append("\n");
        sb.append("\t\t").append("<if test=\"" + primaryKey + " != null\">").append("\n");
        sb.append("\t\t\t").append("AND ").append(primaryKey).append(" = #{").append(primaryKey).append("}\n");
        sb.append("\t\t").append("</if>").append("\n");
        sb.append("\t\t").append("limit 0, 1");
        String newContent = content.replaceAll("[$][{]selectOneSql}", sb.toString());
        return newContent;
    }

    private static String generateSelectListSql(String content, String tableName, String primaryKey){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(tableName).append(" WHERE 1 = 1 AND is_deleted = 0 ").append("\n");
        sb.append("\t\t").append("<if test=\"" + primaryKey + " != null\">").append("\n");
        sb.append("\t\t\t").append("AND ").append(primaryKey).append(" = #{").append(primaryKey).append("}\n");
        sb.append("\t\t").append("</if>");
        String newContent = content.replaceAll("[$][{]selectListSql}", sb.toString());
        return newContent;
    }
}
