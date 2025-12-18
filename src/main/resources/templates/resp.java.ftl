<#if customMap?? && customMap.domainPackage??>
package ${customMap.domainPackage}.resp;
<#else>
package ${package.Parent}.domain.resp;
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
 * ${table.comment!} Resp
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
@Schema(name = "${entity}Resp", description = "${table.comment!} Resp")
public class ${entity}Resp implements Serializable {

    private static final long serialVersionUID = 1L;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>

<#--    <#if field.comment!?length gt 0>-->
<#--    /**-->
<#--     * ${field.comment}-->
<#--     */-->
<#--    <#else>-->
<#--    /**-->
<#--     * ${field.propertyName}-->
<#--     */-->
<#--    </#if>-->
    @Schema(description = "${field.comment!}"<#if (field.keyFlag!false) == true>, example = "1"</#if>)
    <#-- 根据字段名称添加特定验证 -->
    <#if field.propertyName == "username">
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 64, message = "用户名长度必须在3-64个字符之间")
    <#elseif field.propertyName == "email">
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    <#elseif field.propertyName == "phone" || field.propertyName == "mobile">
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    <#elseif field.propertyName == "realName" || field.propertyName == "name">
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    <#-- 对于其他字段，暂时不添加必填验证，可以根据实际需要手动调整 -->
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

}