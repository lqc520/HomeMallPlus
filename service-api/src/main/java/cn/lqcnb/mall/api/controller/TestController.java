package cn.lqcnb.mall.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import okhttp3.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author lqc520
 * @Description: test
 * @date 2020/3/30 0:25
 * @see cn.lqcnb.mall.api.controller
 */
@RestController
@Api("test")
@RequestMapping("test")
public class TestController {

        public String baseUrl = "http://localhost:8088/oauth/check_token?token=";
        @GetMapping("check_token")
        @ApiOperation("检测")
        @ApiImplicitParam(name = "token" ,value = "令牌",paramType="query",required = true)
        public void test(String token){
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(new Request.Builder().url(baseUrl + token).get().build());
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println(response.body().string());
                }
            });
        }
}
