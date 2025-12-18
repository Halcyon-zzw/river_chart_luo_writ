<#if customMap?? && customMap.domainPackage??>
package ${customMap.domainPackage}.req;
<#else>
package ${package.Parent}.domain.req;
</#if>

<#list table.importPackages as pkg>
<#if pkg?contains("java.time") || pkg?contains("java.math") || pkg?contains("java.io")>
import ${pkg};
</#if>
</#list>
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
<#if chainModel>
import lombok.experimental.Accessors;
</#if>
import jakarta.validation.constraints.*;

/**
 * <p>
 * ${table.comment!} Req
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@EqualsAndHashCode(callSuper = false)
<#if chainModel>
@Accessors(chain = true)
</#if>
@Schema(name = "${entity}Req", description = "${table.comment!} Req")
public class ${entity}Req implements Serializable {



}