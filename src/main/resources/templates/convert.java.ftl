package ${package.Parent}.convert;

import ${package.Entity}.${entity};
import ${package.Parent}.dto.${entity}DTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 转换接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Mapper(componentModel = "spring")
public interface ${entity}Convert {

    ${entity}Convert INSTANCE = Mappers.getMapper(${entity}Convert.class);

    /**
     * ${entity} 转 ${entity}DTO
     *
     * @param ${entity?uncap_first} ${table.comment!}实体
     * @return ${table.comment!}DTO
     */
    ${entity}DTO to${entity}DTO(${entity} ${entity?uncap_first});

    /**
     * ${entity}DTO 转 ${entity}
     *
     * @param ${entity?uncap_first}DTO ${table.comment!}DTO
     * @return ${table.comment!}实体
     */
    ${entity} to${entity}(${entity}DTO ${entity?uncap_first}DTO);

    /**
     * ${entity} 列表转 ${entity}DTO 列表
     *
     * @param ${entity?uncap_first}List ${table.comment!}实体列表
     * @return ${table.comment!}DTO列表
     */
    List<${entity}DTO> to${entity}DTOList(List<${entity}> ${entity?uncap_first}List);

    /**
     * ${entity}DTO 列表转 ${entity} 列表
     *
     * @param ${entity?uncap_first}DTOList ${table.comment!}DTO列表
     * @return ${table.comment!}实体列表
     */
    List<${entity}> to${entity}List(List<${entity}DTO> ${entity?uncap_first}DTOList);

}