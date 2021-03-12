package com.kcbs.webforum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.common.Constant;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.model.pojo.User;
import com.kcbs.webforum.model.request.UserInfoReq;
import com.kcbs.webforum.service.UserService;
import com.kcbs.webforum.utils.JwtUtils;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
@Api(tags = "用户接口",description = "登录成功后需要携带token访问，把token放在请求头当中，注意区分“用户关注用户”与“用户关注分类”")
@RestController
public class UserController {
    @Resource
    UserService userService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query", dataType = "String")
    })
    public ApiRestResponse register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("code") String veriftcode) throws WebforumException {
        userService.register(username, password, veriftcode);
        return ApiRestResponse.success();
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String")
    })
    public ApiRestResponse login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) throws WebforumException {
        return userService.login(username, password, request);
    }


    @GetMapping("/getUser")
    @ApiOperation("获取用户信息")
    public ApiRestResponse getUser(HttpServletRequest request,@RequestParam(required = false) Long userId) throws WebforumException {
        return userService.getUser(request,userId);
    }

    @ApiOperation("获取验证码")
    @GetMapping("/getCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, paramType = "query", dataType = "String")
    })
    public ApiRestResponse getCode(@RequestParam("username") String username, @RequestParam("email") String email) throws WebforumException {
        userService.getCode(username, email);
        return ApiRestResponse.success();
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public ApiRestResponse logout(HttpServletRequest request) throws WebforumException {
        User user = JwtUtils.getUser(request);
        Jedis jedis = new Jedis(Constant.HOST, Constant.PORT);
        try {
            jedis.del(jedis.get(String.valueOf(user.getUserId())));
            jedis.del(String.valueOf(user.getUserId()));
        } finally {
            jedis.close();
        }
        return ApiRestResponse.success();
    }

    @ApiOperation("用户更新签名")
    @PostMapping("/user/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personalizedSignature", value = "个性签名", required = true, paramType = "query", dataType = "String")
    })
    public ApiRestResponse update(@RequestParam String personalizedSignature, HttpServletRequest request) throws WebforumException, JsonProcessingException {
        userService.updatepersonalizedSignature(personalizedSignature, request);
        return ApiRestResponse.success();
    }

    @ApiOperation("管理员登录")
    @PostMapping("/adminLogin")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String")
    })
    public ApiRestResponse adminlogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) throws WebforumException {
        return userService.adminlogin(username, password, request);
    }


    @ApiOperation("上传头像")
    @PostMapping("/user/upload")
    public ApiRestResponse headSculptureupload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws WebforumException {
        return userService.uploadhead(request, file);
    }


    @GetMapping("/getFunsList")
    @ApiOperation("获取用户粉丝列表(默认查询token用户,传参则查询该用户)")
    public ApiRestResponse getFunsList(@RequestParam(value = "beSubscribe", required = false) Long beSubscribe, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, HttpServletRequest request) throws WebforumException {
        return userService.getFuns(request, beSubscribe, pageNum, pageSize);
    }

    @GetMapping("/getSubscribeList")
    @ApiOperation("获取用户关注列表(默认查询token用户,传参则查询该用户)")
    public ApiRestResponse getSubscribeList(@RequestParam(value = "subscribe",required = false) Long subscribe,@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize") Integer pageSize,HttpServletRequest request) throws WebforumException {
        return userService.getSubscribeList(request,subscribe,pageNum,pageSize);
    }


    @GetMapping("/getFunsNum")
    @ApiOperation("获取用户粉丝数(默认查询token用户,传参则查询该用户)")
    public ApiRestResponse getUserFunsNum(@RequestParam(value = "userId",required = false) Long userId,HttpServletRequest request) throws WebforumException {
        return userService.getUserFunsNum(request,userId);
    }

    @GetMapping("/getSubscribeNum")
    @ApiOperation("获取用户关注数(默认查询token用户,传参则查询该用户)")
    public ApiRestResponse getUserSubscribeNum(@RequestParam(value = "userId",required = false) Long userId,HttpServletRequest request) throws WebforumException {
        return userService.getUserSubscribeNum(request,userId);
    }

    @GetMapping("/user/getCategory")
    @ApiOperation("获取用户关注分类列表")
    public ApiRestResponse getCategotyByUser(HttpServletRequest request) throws WebforumException {
        return userService.getCategoryByUser(request);
    }

    @PutMapping("/user/subscribe")
    @ApiOperation("用户关注分类")
    public ApiRestResponse SubscibeCategory(HttpServletRequest request,@RequestParam("categoryId") Long categoryId) throws WebforumException {
        return userService.SubscribeCategory(request,categoryId);
    }

    @DeleteMapping("/user/subscribe")
    @ApiOperation("用户取消关注分类")
    public ApiRestResponse delSubscibeCategory(HttpServletRequest request,@RequestParam("categoryId") Long categoryId) throws WebforumException {
        return userService.delSubscribeCategory(request,categoryId);
    }


    @PostMapping("/findPassword")
    @ApiOperation("忘记密码，通过邮箱+验证码登录,登录后提示更改密码")
    public ApiRestResponse FindPassword(@RequestParam String email) throws WebforumException {
        return userService.findPassword(email);
    }

    @PostMapping("/updatePassword")
    @ApiOperation("更改密码")
    public ApiRestResponse updatePassword(@RequestParam String password,HttpServletRequest request) throws WebforumException, NoSuchAlgorithmException {
        return userService.updatePassword(password,request);
    }


    @PostMapping("/admin/Ban")
    @ApiOperation("管理员封禁用户(通过id)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "被封禁人id", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "banTime", value = "封禁时长(单位/秒)", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "banMsg", value = "封禁原因", required = true, paramType = "query", dataType = "String")
    })
    public ApiRestResponse BanUser(@RequestParam("userId") Long userId,@RequestParam("banTime")Integer banTime,@RequestParam("banMsg")String banMsg) throws WebforumException {
        return userService.banUserForId(userId,banTime,banMsg);
    }


    @PostMapping("/admin/BanByName")
    @ApiOperation("管理员封禁用户(通过name)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "被封禁人名称", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "banTime", value = "封禁时长(单位/秒)", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "banMsg", value = "封禁原因", required = true, paramType = "query", dataType = "string")
    })
    public ApiRestResponse BanUserByName(@RequestParam("username") String username,@RequestParam("banTime")Integer banTime,@RequestParam("banMsg")String banMsg) throws WebforumException {
        return userService.banUserForName(username,banTime,banMsg);
    }


    @PostMapping("/admin/unBan")
    @ApiOperation("管理员解封用户(通过id)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "被封禁人id", required = true, paramType = "query", dataType = "long")
           })
    public ApiRestResponse unBanUser(@RequestParam("userId") Long userId) throws WebforumException {
        return userService.unBanUserForId(userId);
    }


    @PostMapping("/admin/unBanByName")
    @ApiOperation("管理员解封用户(通过name)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "被封禁人名称", required = true, paramType = "query", dataType = "string")
    })
    public ApiRestResponse unBanUser(@RequestParam("username") String username) throws WebforumException {
        return userService.unBanUserForName(username);
    }



    @GetMapping("/admin/getUser")
    @ApiOperation("管理员获取用户信息")
    public ApiRestResponse getUser(@RequestParam Long userId) throws WebforumException {
        return userService.getUserByAdmin(userId);
    }


    @GetMapping("/searchUserByString")
    @ApiOperation("模糊查询与content相关User")
    public ApiRestResponse searchUser(@RequestParam Integer pageNum,@RequestParam Integer pageSize,String content) throws WebforumException {
        return userService.searchUser(pageNum,pageSize,content);
    }


    @PostMapping("/user/improveInfo")
    @ApiOperation("完善个人信息")
    public ApiRestResponse improveInfo(@Valid UserInfoReq userInfo,HttpServletRequest request) throws WebforumException {
        userService.improveInfo(userInfo,request);
        return ApiRestResponse.success();
    }
}
