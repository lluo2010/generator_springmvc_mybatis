<?xml version="1.0" encoding="UTF-8" ?>  
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd"> 
<mapper namespace="${packageName}.dao.${ClassName}Dao">
    <resultMap type="${packageName}.model.${ClassName}" id="${className}Map">
${resultMap}
    </resultMap>

    <select id="getByPrimaryKey" parameterType="${primaryKeyType}" resultMap="${className}Map">
    	${selectSql}
    </select>
    
    <insert id="create" parameterType="${packageName}.model.${ClassName}">
        ${insertPk}
		${insertSql}
    </insert>
    
    <update id="update" parameterType="${packageName}.model.${ClassName}">
		${updateSql}
    </update>
    
    <delete id="delete" parameterType="${primaryKeyType}">
    	${deleteSql}
    </delete>

    <select id="count" parameterType="map" resultType="Integer">
      ${countSql}
    </select>

    <select id="get" parameterType="map" resultMap="${className}Map">
        ${selectOneSql}
    </select>

    <select id="getList" parameterType="map" resultMap="${className}Map">
        ${selectListSql}
    </select>
</mapper>