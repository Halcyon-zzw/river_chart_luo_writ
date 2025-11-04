package ${package.Parent}.dao;

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
    * ${table.comment!} Dao类
    * </p>
*
* @author ${author}
* @since ${date}
*/
@Repository
public class ${entity}Dao extends ServiceImpl<${entity}Mapper, ${entity}> {

    @Autowired
    private ${table.mapperName} ${table.mapperName?uncap_first};

}