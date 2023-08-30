package com.ruyuan.eshop.address;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.address.api.AddressApi;
import com.ruyuan.eshop.address.domain.dto.AddressDTO;
import com.ruyuan.eshop.address.domain.query.AddressQuery;
import com.ruyuan.eshop.common.core.JsonResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = AddressApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AddressApiTest {

    @DubboReference(version = "1.0.0")
    private AddressApi addressApi;

    @Test
    public void testQueryAddress1() throws Exception {
        AddressQuery query = new AddressQuery();
        query.setProvince("北京");
        query.setProvinceCode("110000");
        JsonResult<AddressDTO> jsonResult = addressApi.queryAddress(query);

        System.out.println(JSONObject.toJSONString(jsonResult));
    }

    @Test
    public void testQueryAddress2() throws Exception {
        AddressQuery query = new AddressQuery();
        query.setProvince("北京");
        query.setProvinceCode("110000");
        query.setCity("北京市");
        query.setCityCode("110100");
        JsonResult<AddressDTO> jsonResult = addressApi.queryAddress(query);

        System.out.println(JSONObject.toJSONString(jsonResult));
    }

    @Test
    public void testQueryAddress3() throws Exception {
        AddressQuery query = new AddressQuery();
        query.setProvince("北京");
        query.setProvinceCode("110000");
        query.setCity("北京市");
        query.setCityCode("110100");
        query.setArea("东城区");
        query.setAreaCode("110101");
        JsonResult<AddressDTO> jsonResult = addressApi.queryAddress(query);

        System.out.println(JSONObject.toJSONString(jsonResult));
    }

    @Test
    public void testQueryAddress4() throws Exception {
        AddressQuery query = new AddressQuery();
        query.setProvince("北京");
        query.setProvinceCode("110000");
        query.setCity("北京市");
        query.setCityCode("110100");
        query.setArea("东城区");
        query.setAreaCode("110101");
        query.setStreet("东华门街道");
        query.setStreetCode("110101001");
        JsonResult<AddressDTO> jsonResult = addressApi.queryAddress(query);

        System.out.println(JSONObject.toJSONString(jsonResult));
    }

    @Test
    public void testQueryAddress5() throws Exception {
        AddressQuery query = new AddressQuery();
        query.setProvinceCode("110000");
        query.setCityCode("110100");
        query.setAreaCode("110105");
        query.setStreetCode("110101007");
        JsonResult<AddressDTO> jsonResult = addressApi.queryAddress(query);

        System.out.println(JSONObject.toJSONString(jsonResult));
    }
}
