package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Parent}.dto.${entity}DTO;
import ${package.Parent}.convert.${entity}Convert;
import ${package.Parent}.dao.${entity}Dao;
import ${package.Service}.${table.serviceName};
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} implements ${table.serviceName} {

    @Autowired
    private ${entity}Dao ${entity?uncap_first}Dao;

    @Autowired
    private ${entity}Convert ${entity?uncap_first}Convert;

    @Override
    public ${entity}DTO getById(Long id) {
        ${entity} ${entity?uncap_first} = ${entity?uncap_first}Dao.getById(id);
        return ${entity?uncap_first} != null ? ${entity?uncap_first}Convert.to${entity}DTO(${entity?uncap_first}) : null;
    }

    @Override
    public ${entity}DTO save(${entity}DTO ${entity?uncap_first}DTO) {
        ${entity} ${entity?uncap_first} = ${entity?uncap_first}Convert.to${entity}(${entity?uncap_first}DTO);
        boolean success = ${entity?uncap_first}Dao.save(${entity?uncap_first});
        return success ? ${entity?uncap_first}Convert.to${entity}DTO(${entity?uncap_first}) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return ${entity?uncap_first}Dao.removeById(id);
    }

    @Override
    public ${entity}DTO updateById(Long id, ${entity}DTO ${entity?uncap_first}DTO) {
        ${entity?uncap_first}DTO.setId(id);
        ${entity} ${entity?uncap_first} = ${entity?uncap_first}Convert.to${entity}(${entity?uncap_first}DTO);
        boolean success = ${entity?uncap_first}Dao.updateById(${entity?uncap_first});
        return success ? ${entity?uncap_first}Convert.to${entity}DTO(${entity?uncap_first}) : null;
    }

}
</#if>