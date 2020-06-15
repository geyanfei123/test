package com.xieke.test.tyqxcms.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xieke.test.tyqxcms.dto.PermissionInfo;
import com.xieke.test.tyqxcms.entity.Permission;
import java.util.List;

/**
 * <p>
 * 系统权限表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<PermissionInfo> allPermissionInfo();

}
