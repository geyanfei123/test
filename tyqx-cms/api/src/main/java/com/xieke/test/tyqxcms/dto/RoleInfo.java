package com.xieke.test.tyqxcms.dto;

import com.xieke.test.tyqxcms.entity.Permission;
import com.xieke.test.tyqxcms.entity.Role;

import java.io.Serializable;
import java.util.List;

public class RoleInfo extends Role implements Serializable {

    private List<Permission> permissions;

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}