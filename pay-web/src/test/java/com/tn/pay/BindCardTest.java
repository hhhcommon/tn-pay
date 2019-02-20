package com.tn.pay;

import com.tn.pay.api.BindCardAPI;
import com.tn.pay.dto.BindCardDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration(initializers = {TestContextInitializer.class})
public class BindCardTest {

    @Autowired
    private BindCardAPI bindCardService;

    @Test
    public void readySign() {
        BindCardDTO dto = new BindCardDTO();

        dto.setSceneId(1);
        dto.setFundsId(1);
        dto.setProductId(1);
        dto.setProviderId(1);

        dto.setUserName("马少旭");
        dto.setCertNo("610523199205193317");
        dto.setMobile("18109213146");
        dto.setUserId("Tn18109213146");
        dto.setBankCardType(101);
        dto.setCertType("01");
        dto.setBankAccount("6214830118021673");

        Boolean respDTO = bindCardService.readySign(dto);
        System.out.println(respDTO);
    }

    @Test
    public void confirmSign() {
        BindCardDTO dto = new BindCardDTO();

        dto.setSceneId(1);
        dto.setFundsId(1);
        dto.setProductId(1);
        dto.setProviderId(1);

        dto.setBankAccount("6214830118021673");
        dto.setSms("123456");

        Boolean respDTO = bindCardService.confirmSign(dto);
        System.out.println(respDTO);
    }

    @Test
    public void queryBind() {
        BindCardDTO dto = new BindCardDTO();

        dto.setSceneId(1);
        dto.setFundsId(1);
        dto.setProductId(1);
        dto.setProviderId(1);

        dto.setBankAccount("6214830118021673");
        dto.setUserId("Tn18109213146");

        Boolean respDTO = bindCardService.queryBind(dto);
        System.out.println(respDTO);
    }

    @Test
    public void abolishBind() {
        BindCardDTO dto = new BindCardDTO();

        dto.setSceneId(1);
        dto.setFundsId(1);
        dto.setProductId(1);
        dto.setProviderId(1);

        dto.setBankAccount("6214830118021673");
        dto.setUserId("Tn18109213146");

        Boolean respDTO = bindCardService.abolishBind(dto);
        System.out.println(respDTO);
    }
}
