package ${package.Service};

<#if customMap?? && customMap.domainPackage??>
import ${customMap.domainPackage}.dto.${entity}DTO;
<#else>
import ${package.Parent}.domain.dto.${entity}DTO;
</#if>

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
<#if customMap?? && customMap.projectName??>
 * @project ${customMap.projectName}
</#if>
<#if customMap?? && customMap.companyName??>
 * @company ${customMap.companyName}
</#if>
<#if customMap?? && customMap.copyright??>
 * ${customMap.copyright}
</#if>
 */
<#if kotlin>
interface ${table.serviceName}
<#else>
public interface ${table.serviceName} {

    /**
     * 根据ID获取${table.comment!}
     *
     * @param id 主键ID
     * @return ${table.comment!}DTO对象
     */
    ${entity}DTO getById(Long id);

    /**
     * 保存${table.comment!}
     *
     * @param ${entity?uncap_first}DTO ${table.comment!}DTO对象
     * @return ${table.comment!}DTO对象
     */
    ${entity}DTO save(${entity}DTO ${entity?uncap_first}DTO);

    /**
     * 根据ID删除${table.comment!}
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 根据ID更新${table.comment!}
     *
     * @param id 主键ID
     * @param ${entity?uncap_first}DTO ${table.comment!}DTO对象
     * @return ${table.comment!}DTO对象
     */
    ${entity}DTO updateById(Long id, ${entity}DTO ${entity?uncap_first}DTO);

}
</#if>