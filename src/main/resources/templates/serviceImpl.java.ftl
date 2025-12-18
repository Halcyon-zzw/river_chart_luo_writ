package ${package.ServiceImpl};

import ${package.Entity}.${entity};
<#if customMap?? && customMap.domainPackage??>
import ${customMap.domainPackage}.dto.${entity}DTO;
import ${customMap.domainPackage}.convert.${entity}Convert;
<#else>
import ${package.Parent}.domain.dto.${entity}DTO;
import ${package.Parent}.domain.convert.${entity}Convert;
</#if>
import ${package.Parent}.dao.${entity}Dao;
import ${package.Service}.${table.serviceName};
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
<#if customMap?? && customMap.enableLog?? && customMap.enableLog>
import lombok.extern.slf4j.Slf4j;
</#if>

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if customMap?? && customMap.enableLog?? && customMap.enableLog>
@Slf4j
</#if>
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
<#if customMap?? && customMap.enableLog?? && customMap.enableLog>
        log.info("根据ID获取${table.comment!}: {}", id);
</#if>
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