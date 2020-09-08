package com.example.demo.service.sys.impl;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.sys.SysDeptDao;
import com.example.demo.entity.sys.SysDeptDO;
import com.example.demo.service.sys.SysDeptService;
import com.example.demo.util.container.ContainerUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description 系统部门表
 **/
@Service
public class SysDeptServiceImpl implements SysDeptService {

    private SysDeptDao sysDeptDao;

    @Autowired
    public SysDeptServiceImpl(SysDeptDao sysDeptDao) {
        this.sysDeptDao = sysDeptDao;
    }

    @Override
    public ResResult list(SysDeptDO query) throws ServiceException {
        PageHelper.startPage(query);
        List<SysDeptDO> list = sysDeptDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            ResList<SysDeptDO> resList = ResList.page(list, ((Page) list).getTotal());
            return ResResult.success(resList);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

    @Override
    public ResResult getById(Serializable id) {
        SysDeptDO object = sysDeptDao.getById(id);
        if (Objects.nonNull(object)) {
            return ResResult.success(object);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

    @Override
    public ResResult save(SysDeptDO object) throws ServiceException {
        init(object);
        checkNameExists(object);
        return sysDeptDao.save(object) > 0 ? ResResult.success() : ResResult.fail();
    }

    @Override
    public ResResult update(SysDeptDO object) throws ServiceException {
        init(object);
        checkNameExists(object);
        return sysDeptDao.update(object) > 0 ? ResResult.success() : ResResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult deleteByIds(Serializable[] ids) throws ServiceException {
        DelResInfo delResInfo = new DelResInfo();
        for (Serializable id : ids) {
            // 有子级不能删除
            if (!sysDeptDao.isExistsByPidAndName(id, null) && sysDeptDao.deleteById(id) > 0) {
                // 不存在且已删除则将 id 添加到已删除列表
                delResInfo.addDeleted(id);
            } else {
                delResInfo.addNotDelete(id);
            }
        }
        if (ContainerUtil.isNotEmpty(delResInfo.getNotDelete())) {
            return ResResult.response(ResCode.OK, "有系统部门对象删除失败", delResInfo);
        }
        return ResResult.success(delResInfo);
    }

    /**
     * 获取单位树
     *
     * @param pid  父id
     * @param name 部门名称
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult tree(Long pid, String name) throws ServiceException {
        SysDeptDO query = SysDeptDO.builder().pId(pid).name(name).build();
        List<SysDeptDO> list = setChild(query);
        return ContainerUtil.isNotEmpty(list) ? ResResult.success(list) : ResResult.fail(ResCode.NOT_FOUND);
    }


    /**
     * 设置子级
     *
     * @param query 查询对象
     * @return r
     */
    private List<SysDeptDO> setChild(SysDeptDO query) {
        List<SysDeptDO> list = sysDeptDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            list.forEach(sysDept -> {
                query.setPId(sysDept.getId());
                sysDept.setChildList(setChild(query));
            });
        }
        return list;
    }

    /**
     * 同一个部门下面不允许出现重复名称的部门
     *
     * @param object 角色对象
     * @throws ServiceException e
     */
    private void checkNameExists(SysDeptDO object) throws ServiceException {
        String name = object.getName();
        Long pId = object.getPId();
        if (Objects.nonNull(object.getId())) {
            SysDeptDO oldSysDept = sysDeptDao.getById(object.getId());
            if (StringUtils.isNotBlank(name) && !Objects.equals(name, oldSysDept.getName()) && sysDeptDao.isExistsByPidAndName(pId, name)) {
                throw new ServiceException("部门名称已存在");
            }
        } else {
            if (sysDeptDao.isExistsByPidAndName(pId, name)) {
                throw new ServiceException("部门名称已存在");
            }
        }
    }

    /**
     * 初始化
     *
     * @param object 对象
     */
    private void init(SysDeptDO object) {
        if (Objects.isNull(object.getPId())) {
            object.setPId(0L);
        }
        LocalDateTime now = LocalDateTime.now();
        if (Objects.isNull(object.getId())) {
            object.setCreateTime(now);
            object.setStatus(SysDeptDO.STATUS_ABLE);
        }
        object.setUpdateTime(now);
    }
}
