package org.nap.diabuddy_companion_server.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.DTO.UserShareDTOForSearch;
import org.nap.diabuddy_companion_server.entity.VO.UserVOForSearch;
import org.nap.diabuddy_companion_server.entity.user.User;
import org.nap.diabuddy_companion_server.entity.user.UserShare;
import org.nap.diabuddy_companion_server.service.user.UserService;
import org.nap.diabuddy_companion_server.service.user.UserShareService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/share")
public class UserShareController {

    @Resource
    private UserShareService userShareService;

    @Resource
    private UserService userService;

    @GetMapping("/search")
    public R<UserVOForSearch> searchUser(@RequestParam("phone") String phone){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userService.getOne(queryWrapper);

        UserVOForSearch vo = new UserVOForSearch();

        if(user == null){
            return R.error("不存在该用户");
        }else{
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setFullName(user.getFullName());
        }
        return R.success(vo);
    }

    @PostMapping("/add")
    public R<Object> addApplyFriend(@RequestBody UserShareDTOForSearch userShareDTOForSearch){

        if(userShareDTOForSearch.getUserId().equals(userShareDTOForSearch.getFriendId())){
            return R.error("不能添加自己为好友");
        }

        LambdaQueryWrapper<UserShare> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserShare::getUserId, userShareDTOForSearch.getUserId());
        queryWrapper.eq(UserShare::getFriendId, userShareDTOForSearch.getFriendId());
        UserShare one = userShareService.getOne(queryWrapper);

        if(one != null){
            return R.error("你们已经是好友了");
        }

        UserShare userShare = new UserShare();
        userShare.setUserId(userShareDTOForSearch.getUserId());
        userShare.setFriendId(userShareDTOForSearch.getFriendId());
        userShare.setStatus("waiting");

        userShareService.save(userShare);

        return R.success(null);
    }

    @GetMapping("/wait-list/{userId}")
    public R<HashMap> getFriendApplyList(@PathVariable("userId") Integer userId){
        HashMap<String, List> res = new HashMap<>();

        LambdaQueryWrapper<UserShare> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserShare::getFriendId, userId);
        queryWrapper.eq(UserShare::getStatus, "waiting");
        List<UserShare> userShareList = userShareService.list(queryWrapper);
        List<UserVOForSearch> voList = new ArrayList<>();
        if(userShareList.isEmpty()){
            return R.success(res);
        }

        for( UserShare userShare : userShareList ){
            User user = userService.getById(userShare.getUserId());
            UserVOForSearch vo = new UserVOForSearch();

            vo.setId(user.getId());
            vo.setPhone(user.getPhone());
            vo.setFullName(user.getFullName());
            vo.setUsername(user.getUsername());

            voList.add(vo);
        }


        res.put("list",voList);
        return R.success(res);
    }

    @PutMapping("/add")
    @Transactional
    public R<Object> replyFriendApply(@RequestBody UserShareDTOForSearch dto){
        LambdaQueryWrapper<UserShare> queryWrapper = new LambdaQueryWrapper<>();

        //前端传过来是反的，这里需要反过来
        queryWrapper.eq(UserShare::getUserId, dto.getFriendId());
        queryWrapper.eq(UserShare::getFriendId, dto.getUserId());
        queryWrapper.eq(UserShare::getStatus, "waiting");

        UserShare one = userShareService.getOne(queryWrapper);

        if(one == null){
            return R.error("该请求不存在");
        }

        // 把该记录的status改成相应的agree或refuse
        one.setStatus(dto.getStatus());
        boolean isUpdated = userShareService.updateById(one);

        // userId和friendId反过来，创建一条记录
        UserShare userShareMirror = new UserShare();
        userShareMirror.setUserId(dto.getUserId());
        userShareMirror.setFriendId(dto.getFriendId());
        userShareMirror.setStatus(dto.getStatus());
        boolean isSaved = userShareService.save(userShareMirror);

        if(isUpdated && isSaved){
            return R.success(null);
        }else{
            return R.error("意外失败，请重试");
        }

    }

    @GetMapping("/list/{userId}")
    @Transactional
    public R<HashMap<String, List<UserVOForSearch>>> getFriendList(@PathVariable("userId") Integer userId){
        LambdaQueryWrapper<UserShare> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserShare::getUserId, userId);
        queryWrapper.eq(UserShare::getStatus, "agree");

        List<UserShare> userShareList = userShareService.list(queryWrapper);
        if(userShareList.isEmpty()){
            return R.success(null);
        }

        List<UserVOForSearch> voList = new ArrayList<>();

        for(UserShare userShare : userShareList){

            UserVOForSearch vo = new UserVOForSearch();

            //用friendId查出User实体
            User friend = userService.getById(userShare.getFriendId());

            if(friend == null){
                return R.error("用户不存在");
            }

            // id传friendId，用户点击可用friendId数据
            vo.setId(friend.getId());
            vo.setBirthday(friend.getBirthday());
            vo.setUsername(friend.getUsername());
            vo.setFullName(friend.getFullName());
            vo.setPhone(friend.getPhone());

            voList.add(vo);
        }

        HashMap<String, List<UserVOForSearch>> res = new HashMap<>();

        res.put("list",voList);

        return R.success(res);
    }

    @DeleteMapping("/del")
    @Transactional
    public R<Object> deleteFriend(@RequestParam("user_id") Integer userId, @RequestParam("friend_id") Integer friendId){

        LambdaQueryWrapper<UserShare> queryWrapperA = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<UserShare> queryWrapperB = new LambdaQueryWrapper<>();

        queryWrapperA.eq(UserShare::getUserId, userId)
                     .eq(UserShare::getFriendId, friendId)
                     .eq(UserShare::getStatus, "agree");

        queryWrapperB.eq(UserShare::getUserId, friendId)
                     .eq(UserShare::getFriendId, userId)
                     .eq(UserShare::getStatus, "agree");

        boolean removeA = userShareService.remove(queryWrapperA);
        boolean removeB = userShareService.remove(queryWrapperB);

        if(removeA && removeB){
            return R.success(null);
        }else{
            return R.error("删除失败，请重试");
        }
    }
}
