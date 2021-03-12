package com.kcbs.webforum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.common.Constant;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import com.kcbs.webforum.model.dao.CategoryMapper;
import com.kcbs.webforum.model.dao.SubscribeMapper;
import com.kcbs.webforum.model.dao.UCSubscribeMapper;
import com.kcbs.webforum.model.dao.UserMapper;
import com.kcbs.webforum.model.pojo.Category;
import com.kcbs.webforum.model.pojo.UCSubscribe;
import com.kcbs.webforum.model.pojo.User;
import com.kcbs.webforum.model.request.UserInfoReq;
import com.kcbs.webforum.model.vo.CategoryVO;
import com.kcbs.webforum.model.vo.UserVo;
import com.kcbs.webforum.service.SendMailService;
import com.kcbs.webforum.service.UserService;
import com.kcbs.webforum.utils.JedisUtil;
import com.kcbs.webforum.utils.JwtUtils;
import com.kcbs.webforum.utils.MD5Util;
import com.kcbs.webforum.utils.TimeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    SendMailService sendMailService;
    @Resource
    UCSubscribeMapper ucSubscribeMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    SubscribeMapper subscribeMapper;

    //注册
    @Override
    public void register(String username, String password,String veriftcode) throws WebforumException {
        if (password.length() < 8) {
            throw new WebforumException(WebforumExceptionEnum.PASSWORD_TOO_SHORT);
        }
        if (StringUtils.isEmpty(veriftcode)){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (userMapper.selectByName(username)!=null){
            throw new WebforumException(WebforumExceptionEnum.NAME_EXISTED);
        }
        Jedis jedis = new Jedis(Constant.HOST,Constant.PORT);
        try {
            jedis.select(1);
            String code = jedis.get(username);
            if (!veriftcode.equals(code)){
                throw new WebforumException(WebforumExceptionEnum.WRONG_CODE);
            }
            jedis.select(2);
            User user = new User();
            user.setUsername(username);
            user.setEmail(jedis.get(username));
            user.setPassword(MD5Util.getMd5Str(password));
            int count = userMapper.insertSelective(user);
            if (count!=1){
                throw new WebforumException(WebforumExceptionEnum.INSERT_FAILED);
            }
            jedis.del(username);
            jedis.select(1);
            jedis.del(username);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        finally {
            jedis.select(0);
            jedis.close();
        }

    }

    //获取验证码
    @Override
    public void getCode(String username,String email) throws WebforumException {
        if (StringUtils.isEmpty(email)) {
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (StringUtils.isEmpty(username)){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (userMapper.selectByEmail(email)!=null){
            throw new WebforumException(WebforumExceptionEnum.EMAIL_EXISTED);
        }
        if (userMapper.selectByName(username)!=null){
            throw new WebforumException(WebforumExceptionEnum.NAME_EXISTED);
        }
        String code = String.valueOf(Math.random()).substring(2,8);
        Jedis jedis = new Jedis(Constant.HOST,Constant.PORT);
        try {
         jedis.select(1);
         jedis.set(username, code);
         jedis.expire(username,300);
         jedis.select(2);
         jedis.set(username,email);
         jedis.expire(username,300);
         boolean b = sendMailService.sendMail(email, Constant.SUBJECT, Constant.CONTENT1 + code + Constant.CONTENT2);
         if (!b){
             jedis.select(1);
             jedis.del(username);
             jedis.select(2);
             jedis.del(username);
             throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
         }
        }finally {
            jedis.select(0);
            jedis.close();
        }
    }

    //登录
    @Override
    public ApiRestResponse login(String username, String password, HttpServletRequest request) throws WebforumException {
        User result = getUser(username, password);
        result.setPassword(null);
        Jedis jedis = null;
        try {
        jedis = new Jedis(Constant.HOST,Constant.PORT);
        //根据id获取内存中的token
        if (!jedis.exists(String.valueOf(result.getUserId()))){
            String token = JwtUtils.getJwtToken(String.valueOf(result.getUserId()),username);
            String json = JSONObject.toJSONString(ApiRestResponse.success(result));
                jedis.set(token,json);
                jedis.set(String.valueOf(result.getUserId()),token);
                jedis.expire(token, Constant.TIME);
                jedis.expire(String.valueOf(result.getUserId()),Constant.TIME);
            return ApiRestResponse.success(token);
        }
        return ApiRestResponse.success(jedis.get(String.valueOf(result.getUserId())));
        }catch (JedisConnectionException e){
            e.printStackTrace();
            throw new WebforumException(WebforumExceptionEnum.JEDIS_FAILED);
        }
        finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    //更新签名
    @Override
    public void updatepersonalizedSignature(String personalizedSignature, HttpServletRequest request) throws WebforumException{
        if (StringUtils.isEmpty(personalizedSignature)){
            throw new WebforumException(WebforumExceptionEnum.NEED_CONTENT);
        }
        ApiRestResponse res = JwtUtils.checkToken(request);
        User user = JSONObject.parseObject(res.getData().toString(), User.class);
        user.setPersonalizedSignature(personalizedSignature);
        user.setUpdateTime(null);
        userMapper.updateByPrimaryKeySelective(user);
        //更新缓存中的User对象
        updateUser(user);
    }

    //管理员登录
    @Override
    public ApiRestResponse adminlogin(String username, String password,HttpServletRequest request) throws WebforumException {
        User result = getUser(username, password);
        result.setPassword(null);
        if (result.getRole()==1){
            throw new WebforumException(WebforumExceptionEnum.NEED_ADMIN);
        }
        Jedis jedis = new Jedis(Constant.HOST,Constant.PORT);
        //根据id获取内存中的token
        String resulttoken = jedis.get(String.valueOf(result.getUserId()));
        if (resulttoken==null||resulttoken.equals("")){
            String token = JwtUtils.getJwtToken(String.valueOf(result.getUserId()),username);
            String json = JSONObject.toJSONString(ApiRestResponse.success(result));
            try {
                jedis.set(token,json);
                jedis.set(String.valueOf(result.getUserId()),token);
                jedis.expire(token, Constant.TIME);
                jedis.expire(String.valueOf(result.getUserId()),Constant.TIME);
            }catch (Exception e){
                return ApiRestResponse.error(WebforumExceptionEnum.LOGIN_FAILED);
            }finally {
                jedis.close();
            }
            return ApiRestResponse.success(token);
        }
        return ApiRestResponse.success(resulttoken);
    }

    //上传头像
    @Override
    public ApiRestResponse uploadhead(HttpServletRequest request, MultipartFile file) throws WebforumException {
        ApiRestResponse res = JwtUtils.checkToken(request);
        User user = JSONObject.parseObject(res.getData().toString(), User.class);
        //生成文件名UUID+文件名
        String newFileName = createFileName(file);
        //创建文件夹
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        //目标文件
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        addFile(file, fileDirectory, destFile);
        try {
            String fromuser = user.getHeadSculpture();
            if (!StringUtils.isEmpty(fromuser)&&!user.getHeadSculpture().equals("http://47.111.9.152:8088/images/morentouxiang.png")) {
                File file2 = new File(Constant.FILE_UPLOAD_DIR+fromuser.substring(fromuser.lastIndexOf("/")));
                boolean b = file2.delete();
                if (!b){
                    return ApiRestResponse.error(WebforumExceptionEnum.UPLOAD_FAILED);
                }
            }
            String path = getHost(new URI(request.getRequestURL() + "")) + "/images/" + newFileName;
            user.setHeadSculpture(path);
            user.setUpdateTime(null);
            userMapper.updateByPrimaryKeySelective(user);
            //更新缓存中的User对象
            updateUser(user);
            return ApiRestResponse.success(path);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(WebforumExceptionEnum.UPLOAD_FAILED);
        }
    }

    /**
     *
     * @param file MultipartFile
     * @param fileDirectory 目标文件夹
     * @param destFile 目标文件 文件夹名+文件名
     * @throws WebforumException
     */
    public static void addFile(MultipartFile file, File fileDirectory, File destFile) throws WebforumException {
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                throw new WebforumException(WebforumExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称UUID
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + suffixName;
    }

    //获取用户粉丝列表
    @Override
    public ApiRestResponse getFuns(HttpServletRequest request, Long beSubscribe, Integer pageNum, Integer pageSize)throws WebforumException{
        Long userid = PostServiceImpl.checkpageNumAndpageSize(pageNum, pageSize, beSubscribe, request);
        PageHelper.startPage(pageNum,pageSize);
        List<UserVo> subscribe = userMapper.getSubscribeId(userid);
        for (UserVo user:subscribe){
            user.setSubscribeStatus(0);
            if (subscribeMapper.checkSubscribeById(user.getUserId(),userid)==1) {
                 user.setSubscribeStatus(2);
             }
        }
        PageInfo pageInfo = new PageInfo(subscribe);
        return ApiRestResponse.success(pageInfo);
    }

    private ApiRestResponse getUserList(List<Long> subscribeId) {
        List<User> users = new ArrayList<>();
        for (Long id : subscribeId) {
            User user = userMapper.selectByPrimaryKey(id);
            if (user != null) {
                user.setPassword(null);
                users.add(user);
            }
        }
        PageInfo pageInfo = new PageInfo(users);
        return ApiRestResponse.success(pageInfo);
    }

    //获取Host方法
    public static URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                    null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    //获取用户方法
    private User getUser(String username, String password) throws WebforumException {
        User result = null;
        result= userMapper.selectByName(username);
        if (result == null) {
            result = userMapper.selectByEmail(username);
            if (result==null){
                throw new WebforumException(WebforumExceptionEnum.WRONG_USERNAME);
            }
        }
        if (result.getIsBan()==1){
            if (result.getEndTime()!=null&&result.getEndTime().getTime()>System.currentTimeMillis()){
                throw new WebforumException(10027,"该用户因:\""+result.getBanMessage()+"\"已被管理员于\""+TimeUtils.stampToDate(result.getStartTime().getTime())+"\"封禁，将于\""+ TimeUtils.stampToDate(result.getEndTime().getTime())+"\"解封,有问题请联系QQ:310695702");
            }
            result.setIsBan(0);
            userMapper.updateByPrimaryKeySelective(result);
        }
        Jedis jedis = null;
        try {
            jedis = new Jedis(Constant.HOST,Constant.PORT);
            jedis.select(1);
            if (jedis.exists(result.getUsername())&&(jedis.get(result.getUsername()).equals(password)||result.getPassword().equals(MD5Util.getMd5Str(password)))) {
                return result;
            }
            if (!result.getPassword().equals(MD5Util.getMd5Str(password))) {
                throw new WebforumException(WebforumExceptionEnum.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }finally {
            if (jedis!=null){
                jedis.select(0);
                jedis.close();
            }
        }
        return result;
    }

    //更新用户方法
    public void updateUser(User user) throws WebforumException {
        Jedis jedis = new Jedis(Constant.HOST,Constant.PORT);
        String token = jedis.get(String.valueOf(user.getUserId()));
        String json = JSONObject.toJSONString(ApiRestResponse.success(user));
        try {
            jedis.set(token,json);
            jedis.expire(token, Constant.TIME);
            jedis.expire(String.valueOf(user.getUserId()),Constant.TIME);
        }catch (Exception e){
            throw new WebforumException(WebforumExceptionEnum.LOGIN_EXPIRED);
        }finally {
            jedis.close();
        }

    }

    //检查管理员权限方法
    @Override
    public Boolean checkAdminRole(User currentUser){
        if (currentUser.getRole()==1){
            return false;
        }
        return true;
    }

    //获取用户分类列表
    @Override
    public ApiRestResponse getCategoryByUser(HttpServletRequest request) throws WebforumException {
        User user = JwtUtils.getUser(request);
        List<Category> categoryList = ucSubscribeMapper.getCategoryByUserId(user.getUserId());
        List<CategoryVO> categoryVOS = new ArrayList<>();
        for (Category category:categoryList){
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category,categoryVO);
            categoryVO.setPostNum(categoryMapper.selectPostNum(categoryVO.getCategoryId()));
            categoryVOS.add(categoryVO);
        }
        return ApiRestResponse.success(categoryVOS);
    }

    //关注分类
    @Override
    public ApiRestResponse SubscribeCategory(HttpServletRequest request, Long categoryId) throws WebforumException {
        if (categoryId==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        User user = JwtUtils.getUser(request);
        List<UCSubscribe> ucSubscribeList = ucSubscribeMapper.selectByUCID(user.getUserId(), categoryId);
        if (ucSubscribeList.size()==1){
            ucSubscribeList.get(0).setIsDel((byte) 0);
            int i = ucSubscribeMapper.updateByPrimaryKey(ucSubscribeList.get(0));
            if (i==1){
                return ApiRestResponse.success();
            }
            return ApiRestResponse.error(WebforumExceptionEnum.SUBSCRIBE_FAILED);
        }
        UCSubscribe ucSubscribe = new UCSubscribe();
        ucSubscribe.setUserId(user.getUserId());
        ucSubscribe.setCategoryId(categoryId);
        ucSubscribe.setIsDel((byte) 0);
        int i = ucSubscribeMapper.insertSelective(ucSubscribe);
        if (i==1){
            return ApiRestResponse.success();
        }
        return ApiRestResponse.error(WebforumExceptionEnum.SUBSCRIBE_FAILED);
    }

    //取消关注分类
    @Override
    public ApiRestResponse delSubscribeCategory(HttpServletRequest request, Long categoryId) throws WebforumException {
        List<UCSubscribe> ucSubscribeList = getUcSubscribeList(request, categoryId);
        if (ucSubscribeList.size()==1) {
            ucSubscribeList.get(0).setIsDel((byte) 1);
            int i = ucSubscribeMapper.updateByPrimaryKeySelective(ucSubscribeList.get(0));
            if (i == 1) {
                return ApiRestResponse.success();
            }
            return ApiRestResponse.error(WebforumExceptionEnum.DELSUBSCRIBE_FAILED);
        }
        return ApiRestResponse.error(WebforumExceptionEnum.DELSUBSCRIBE_FAILED);
    }

    private List<UCSubscribe> getUcSubscribeList(HttpServletRequest request, Long categoryId) throws WebforumException {
        if (categoryId==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        User user = JwtUtils.getUser(request);
        return ucSubscribeMapper.selectByUCID(user.getUserId(), categoryId);
    }

    //通过token获取用户
    @Override
    public ApiRestResponse getUser(HttpServletRequest request,Long userId) throws WebforumException {
        User user = null;
        UserVo userVo = new UserVo();
        if (userId!=null){
            user = userMapper.selectByPrimaryKey(userId);
            user.setPassword(null);
            try {
                if (subscribeMapper.checkSubscribeById(user.getUserId(), JwtUtils.getUser(request).getUserId())==1) {
                    if (subscribeMapper.checkSubscribeById(JwtUtils.getUser(request).getUserId(),user.getUserId())==1){
                        userVo.setSubscribeStatus(2);
                    }else {
                        userVo.setSubscribeStatus(1);
                    }
                }else {
                    userVo.setSubscribeStatus(0);
                }
            }catch (Exception e){
                userVo.setSubscribeStatus(-1);
            }

        }else {
            user = JwtUtils.getUser(request);
            userVo.setSubscribeStatus(1);
        }
        BeanUtils.copyProperties(user,userVo);
        if (userMapper.selectWordNumberByUserId(user.getUserId())==null) {
            userVo.setWordNumber(0);
        }else {
            userVo.setWordNumber(userMapper.selectWordNumberByUserId(user.getUserId()));
        }


        return ApiRestResponse.success(userVo);
    }

    //获取用户关注列表
    @Override
    public ApiRestResponse getSubscribeList(HttpServletRequest request, Long subscribe, Integer pageNum, Integer pageSize) throws WebforumException {
        Long userid = PostServiceImpl.checkpageNumAndpageSize(pageNum, pageSize, subscribe, request);
        PageHelper.startPage(pageNum,pageSize);
        List<UserVo> subscribeId = userMapper.getBeSubscribeId(userid);
        for (UserVo user:subscribeId){
            user.setSubscribeStatus(1);
            if (subscribeMapper.checkSubscribeById(userid,user.getUserId())==1) {
                user.setSubscribeStatus(2);
            }
        }
        PageInfo pageInfo = new PageInfo(subscribeId);
        return ApiRestResponse.success(pageInfo);
    }

    @Override
    public ApiRestResponse getUserFunsNum(HttpServletRequest request, Long userId) throws WebforumException {
        Long id = CheckId(request, userId);
        return ApiRestResponse.success(userMapper.getSubscribeId(id).size());
    }

    private Long CheckId(HttpServletRequest request, Long userId) throws WebforumException {
        Long id = null;
        if (userId!=null){
            id = userId;
        }else {
            id = JwtUtils.getUser(request).getUserId();
        }
        if (id ==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return id;
    }

    @Override
    public ApiRestResponse getUserSubscribeNum(HttpServletRequest request, Long userId) throws WebforumException {
        Long id = CheckId(request, userId);
        return ApiRestResponse.success(userMapper.getBeSubscribeId(id).size());
    }

    @Override
    public ApiRestResponse findPassword(String email) throws WebforumException {
        if (email==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        User user = userMapper.selectByEmail(email);
        if (user==null){
            throw new WebforumException(WebforumExceptionEnum.WRONG_USERNAME);
        }
        String code = String.valueOf(Math.random()).substring(2,8);
        Jedis jedis = new Jedis(Constant.HOST,Constant.PORT);
        try {
            jedis.select(1);
            jedis.set(user.getUsername(), code);
            jedis.expire(user.getUsername(),300);
            boolean b = sendMailService.sendMail(email, Constant.SUBJECT, Constant.CONTENT1 + code + Constant.CONTENT2);
            if (!b){
                jedis.select(1);
                jedis.del(user.getUsername());
                throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
            }
        }finally {
            jedis.select(0);
            jedis.close();
        }

        return ApiRestResponse.success();
    }

    @Override
    public ApiRestResponse updatePassword(String password,HttpServletRequest request) throws WebforumException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(password)){
            throw new WebforumException(WebforumExceptionEnum.NEED_PASSWORD);
        }
        if (password.length()<8){
            throw new WebforumException(WebforumExceptionEnum.PASSWORD_TOO_SHORT);
        }
        User user = JwtUtils.getUser(request);
        user.setUpdateTime(null);
        user.setPassword(MD5Util.getMd5Str(password));
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i==1){
            return ApiRestResponse.success();
        }
        return ApiRestResponse.error(WebforumExceptionEnum.UPDATE_FAILED);
    }

    @Override
    public ApiRestResponse banUserForId(Long userId, Integer banTime, String banMsg) throws WebforumException {
        return banUser(banTime, banMsg, userId==null, userMapper.selectByPrimaryKey(userId));
    }

    @Override
    public ApiRestResponse unBanUserForId(Long userId) throws WebforumException {
        return UnBanUser(userId==null, userMapper.selectByPrimaryKey(userId));
    }

    @Override
    public ApiRestResponse getUserByAdmin(Long userId) throws WebforumException {
        if (userId==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        User user = userMapper.selectByPrimaryKey(userId);
        user.setPassword(null);
        return ApiRestResponse.success(user);
    }

    @Override
    public ApiRestResponse banUserForName(String username, Integer banTime, String banMsg) throws WebforumException {
        return banUser(banTime, banMsg, username == null, userMapper.selectByName(username));
    }

    @Override
    public ApiRestResponse unBanUserForName(String username) throws WebforumException {
        return UnBanUser(username == null, userMapper.selectByName(username));
    }

    @Override
    public ApiRestResponse searchUser(Integer pageNum, Integer pageSize, String content) throws WebforumException {
        if (pageNum==null||pageSize==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (StringUtils.isEmpty(content)){
            return ApiRestResponse.success(new PageInfo());
        }
        PageHelper.startPage(pageNum,pageSize,"user_id");
        List<User> users = userMapper.searchUserByString(content);
        PageInfo pageInfo = new PageInfo(users);
        return ApiRestResponse.success(pageInfo);
    }

    @Override
    public void improveInfo(UserInfoReq userInfo, HttpServletRequest request) throws WebforumException {
        User user = JwtUtils.getUser(request);
        if (!StringUtils.isEmpty(userInfo.getPersonalWebsite())){
            user.setPersonalWebsite(userInfo.getPersonalWebsite());
        }
        if (!StringUtils.isEmpty(userInfo.getQq())){
            user.setQq(userInfo.getQq());
        }
        if (!StringUtils.isEmpty(userInfo.getWechat())){
            user.setWechat(userInfo.getWechat());
        }
        if (!StringUtils.isEmpty(userInfo.getSchool())){
            user.setSchool(userInfo.getSchool());
        }
        user.setSex(userInfo.getSex());
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i!=1){
            throw new WebforumException(WebforumExceptionEnum.UPDATEINFO_FAILED);
        }
        JedisUtil.updateUsertoRedis(user,userMapper);
    }

    private ApiRestResponse UnBanUser(boolean b, User user2) throws WebforumException {
        if (b) {
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        User user = user2;
        user.setIsBan(0);
        user.setEndTime(new Date());
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i == 1) {
            return ApiRestResponse.success();
        }
        return ApiRestResponse.error(WebforumExceptionEnum.UNBANERROR);
    }

    private ApiRestResponse banUser(Integer banTime, String banMsg, boolean b, User user2) throws WebforumException {
        if (b || banTime == null || banMsg == null) {
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        User user = user2;
        if (user == null) {
            throw new WebforumException(WebforumExceptionEnum.WRONG_USERNAME);
        }
        user.setIsBan(1);
        user.setStartTime(new Date());
        user.setEndTime(TimeUtils.getEndDate(banTime));
        user.setBanMessage(banMsg);
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i == 1) {
            Jedis jedis = null;
            try {
                jedis = new Jedis(Constant.HOST, Constant.PORT);
                if (jedis.exists(String.valueOf(user.getUserId())) && jedis.exists(jedis.get(String.valueOf(user.getUserId())))) {
                    jedis.del(jedis.get(String.valueOf(user.getUserId())));
                    jedis.del(String.valueOf(user.getUserId()));
                }
                return ApiRestResponse.success();
            } catch (JedisConnectionException e) {
                e.printStackTrace();
                throw new WebforumException(WebforumExceptionEnum.JEDIS_FAILED);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return ApiRestResponse.error(WebforumExceptionEnum.BANERROR);
    }



}
