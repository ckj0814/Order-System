package com.ruyuan.eshop.address.controller;

import com.ruyuan.eshop.address.dao.ProvinceDAO;
import com.ruyuan.eshop.address.domain.entity.ProvinceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private ProvinceDAO provinceDAO;

    @GetMapping("/listProvinces")
    public List<ProvinceDO> listProvinces() {
        List<ProvinceDO> provinceDOList = provinceDAO.list();
        return provinceDOList;
    }

}