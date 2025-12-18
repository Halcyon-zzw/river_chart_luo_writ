package ${package.Controller};

<#if customMap?? && customMap.domainPackage??>
import ${customMap.domainPackage}.dto.${entity}DTO;
<#else>
import ${package.Parent}.domain.dto.${entity}DTO;
</#if>
import ${package.Parent}.common.Result;
import ${package.Parent}.exception.DataNotFoundException;
import ${package.Service}.${table.serviceName};
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@Tag(name = "${table.comment!}管理", description = "${table.comment!}相关接口")
@Validated
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>

    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    /**
     * 根据ID获取${table.comment!}
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取${table.comment!}", description = "根据ID获取${table.comment!}详情")
    public Result<${entity}DTO> get${entity}(@PathVariable Long id) {
        ${entity}DTO ${entity?uncap_first}DTO = ${table.serviceName?uncap_first}.getById(id);
        if (${entity?uncap_first}DTO != null) {
            return Result.success(${entity?uncap_first}DTO);
        } else {
            throw new DataNotFoundException("${table.comment!}", id);
        }
    }

    /**
     * 创建${table.comment!}
     */
    @PostMapping("/create")
    @Operation(summary = "创建${table.comment!}", description = "创建新的${table.comment!}")
    public Result<${entity}DTO> create${entity}(@RequestBody @Validated ${entity}DTO ${entity?uncap_first}DTO) {
        ${entity}DTO result = ${table.serviceName?uncap_first}.save(${entity?uncap_first}DTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除${table.comment!}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除${table.comment!}", description = "根据ID删除${table.comment!}")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = ${table.serviceName?uncap_first}.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("${table.comment!}", id);
        }
    }

    /**
     * 根据ID更新${table.comment!}
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新${table.comment!}", description = "根据ID更新${table.comment!}信息")
    public Result<${entity}DTO> updateById(@PathVariable Long id, @RequestBody @Validated ${entity}DTO ${entity?uncap_first}DTO) {
        ${entity}DTO result = ${table.serviceName?uncap_first}.updateById(id, ${entity?uncap_first}DTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("${table.comment!}", id);
        }
    }

}
</#if>