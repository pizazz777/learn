package com.example.demo.configuration;

import com.example.demo.annotation.log.Action;
import com.example.demo.dao.sys.SysResourceDao;
import com.example.demo.entity.sys.PermissionGenerateInfo;
import com.example.demo.entity.sys.SysResourceDO;
import com.example.demo.util.clazz.ClassUtil;
import com.example.demo.util.container.ContainerUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/03/08
 * @description 项目启动时, 扫描controller层生成权限资源符到数据库
 */
@Component
@Slf4j
public class PermissionConfig implements ApplicationRunner {

    private static final String SCAN_PACKAGE_PATH = "com.example.demo.controller";

    private SysResourceDao sysResourceDao;

    @Autowired
    public PermissionConfig(SysResourceDao sysResourceDao) {
        this.sysResourceDao = sysResourceDao;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        try {
            scanControllerPermission();
        } catch (IOException | ClassNotFoundException e) {
            log.error("项目启动生成权限资源错误!");
            e.printStackTrace();
        }
    }


    /**
     * 扫描controller层方法的权限注解
     *
     * @throws IOException            e
     * @throws ClassNotFoundException e
     */
    private void scanControllerPermission() throws IOException, ClassNotFoundException {
        List<Class<?>> classList = ClassUtil.getClasses(SCAN_PACKAGE_PATH);
        if (ContainerUtil.isEmpty(classList)) {
            return;
        }
        List<PermissionGenerateInfo> list = Lists.newArrayList();
        classList.forEach(clz -> {
            // 反射获取全部方法
            Method[] methods = clz.getDeclaredMethods();
            if (ContainerUtil.isNotEmpty(methods)) {
                for (Method method : methods) {
                    // 反射获取方法上面的注解
                    Annotation[] annotations = method.getDeclaredAnnotations();
                    if (ContainerUtil.isEmpty(annotations)) {
                        return;
                    }
                    String[] permissions = null;
                    String description = null;
                    for (Annotation annotation : annotations) {
                        // 权限注解
                        if (annotation instanceof RequiresPermissions) {
                            RequiresPermissions requiresPermissions = (RequiresPermissions) annotation;
                            permissions = requiresPermissions.value();
                        }
                        // 自定义注解
                        if (annotation instanceof Action) {
                            Action action = (Action) annotation;
                            description = action.desc();
                        }
                    }
                    if (ContainerUtil.isNotEmpty(permissions)) {
                        for (String permission : permissions) {
                            PermissionGenerateInfo info = PermissionGenerateInfo.builder()
                                    .permission(permission)
                                    .description(description)
                                    .sysType(permission.toLowerCase().startsWith("sys") ? 1 : 2)
                                    .build();
                            list.add(info);
                        }
                    }
                }
            }
        });
        // 保存权限资源符
        savePermission(list);
    }


    /**
     * 保存新的权限资源符
     *
     * @param list 权限资源符集合
     */
    private void savePermission(List<PermissionGenerateInfo> list) {
        if (ContainerUtil.isNotEmpty(list)) {
            List<String> oldPermissionList = sysResourceDao.listAllResource();
            list = list.stream().filter(resource -> !oldPermissionList.contains(resource.getPermission())).collect(Collectors.toList());
            LocalDateTime now = LocalDateTime.now();
            for (PermissionGenerateInfo resource : list) {
                SysResourceDO sysResource = SysResourceDO.builder()
                        .permission(resource.getPermission())
                        .description(resource.getDescription())
                        .type(2)
                        .status(1)
                        .sysType(resource.getSysType())
                        .createTime(now)
                        .updateTime(now)
                        .build();
                sysResourceDao.save(sysResource);
            }
        }
    }

}
