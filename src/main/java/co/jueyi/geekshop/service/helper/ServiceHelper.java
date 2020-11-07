package co.jueyi.geekshop.service.helper;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.exception.EntityNotFoundException;
import co.jueyi.geekshop.types.common.ListOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;

/**
 * Created on Nov, 2020 by @author bobo
 */
public abstract class ServiceHelper {
    private static String ENTITY_MAPPER_SUFFIX = "EntityMapper";
    private static String COLUMN_DELETED_AT = "deleted_at";
    private static String COLUMN_ID = "id";

    public static <T> T getEntityOrThrow(BaseMapper<T> mapper, Long id) {
        T entity = mapper.selectById(id);
        if (entity == null) {
            handleNullEntity(mapper, id);
        }
        return entity;
    }

    public static <T> T getEntityWithSoftDeleteOrThrow(BaseMapper<T> mapper, Long id) {
        QueryWrapper<T> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        queryWrapper.isNull(COLUMN_DELETED_AT);
        T entity = mapper.selectOne(queryWrapper);
        if (entity == null) {
            handleNullEntity(mapper, id);
        }
        return entity;
    }

    private static<T> T handleNullEntity(BaseMapper<T> mapper, Long id) {
        Type type = mapper.getClass().getGenericInterfaces()[0];
        String fullClassName = type.getTypeName();
        String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        if (simpleClassName.endsWith(ENTITY_MAPPER_SUFFIX)) {
            simpleClassName = simpleClassName.replace(ENTITY_MAPPER_SUFFIX, "");
        }
        throw new EntityNotFoundException(simpleClassName, id);
    }

    public static Pair getListOptions(ListOptions options) {
        int currentPage = Constant.DEFAULT_CURRENT_PAGE;
        if (options != null && options.getCurrentPage() != null) {
            currentPage = options.getCurrentPage();
        }
        int pageSize = Constant.DEFAULT_PAGE_SIZE;
        if (options != null && options.getPageSize() != null) {
            pageSize = options.getPageSize();
        }
        return Pair.of(currentPage, pageSize);
    }
}
