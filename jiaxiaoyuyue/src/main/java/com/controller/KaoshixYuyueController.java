
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 考试预约
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/kaoshixYuyue")
public class KaoshixYuyueController {
    private static final Logger logger = LoggerFactory.getLogger(KaoshixYuyueController.class);

    private static final String TABLE_NAME = "kaoshixYuyue";

    @Autowired
    private KaoshixYuyueService kaoshixYuyueService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private ForumService forumService;//论坛
    @Autowired
    private JiaolianService jiaolianService;//教练
    @Autowired
    private JiaolianCollectionService jiaolianCollectionService;//教练收藏
    @Autowired
    private JiaolianLiuyanService jiaolianLiuyanService;//教练留言
    @Autowired
    private JiaolianYuyueService jiaolianYuyueService;//教练预约
    @Autowired
    private JiaoxiaoService jiaoxiaoService;//驾校信息
    @Autowired
    private KaoshixService kaoshixService;//考试通知
    @Autowired
    private NewsService newsService;//公告通知
    @Autowired
    private YonghuService yonghuService;//学员
    @Autowired
    private UsersService usersService;//管理员


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("学员".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        else if("教练".equals(role))
            params.put("jiaolianId",request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = kaoshixYuyueService.queryPage(params);

        //字典表数据转换
        List<KaoshixYuyueView> list =(List<KaoshixYuyueView>)page.getList();
        for(KaoshixYuyueView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        KaoshixYuyueEntity kaoshixYuyue = kaoshixYuyueService.selectById(id);
        if(kaoshixYuyue !=null){
            //entity转view
            KaoshixYuyueView view = new KaoshixYuyueView();
            BeanUtils.copyProperties( kaoshixYuyue , view );//把实体数据重构到view中
            //级联表 考试通知
            //级联表
            KaoshixEntity kaoshix = kaoshixService.selectById(kaoshixYuyue.getKaoshixId());
            if(kaoshix != null){
            BeanUtils.copyProperties( kaoshix , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setKaoshixId(kaoshix.getId());
            }
            //级联表 学员
            //级联表
            YonghuEntity yonghu = yonghuService.selectById(kaoshixYuyue.getYonghuId());
            if(yonghu != null){
            BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setYonghuId(yonghu.getId());
            }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody KaoshixYuyueEntity kaoshixYuyue, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,kaoshixYuyue:{}",this.getClass().getName(),kaoshixYuyue.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");
        else if("学员".equals(role))
            kaoshixYuyue.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        Wrapper<KaoshixYuyueEntity> queryWrapper = new EntityWrapper<KaoshixYuyueEntity>()
            .eq("kaoshix_id", kaoshixYuyue.getKaoshixId())
            .eq("yonghu_id", kaoshixYuyue.getYonghuId())
            .in("kaoshix_yuyue_yesno_types", new Integer[]{1,2})
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        KaoshixYuyueEntity kaoshixYuyueEntity = kaoshixYuyueService.selectOne(queryWrapper);
        if(kaoshixYuyueEntity==null){
            kaoshixYuyue.setKaoshixYuyueYesnoTypes(1);
            kaoshixYuyue.setCreateTime(new Date());
            kaoshixYuyueService.insert(kaoshixYuyue);
            return R.ok();
        }else {
            if(kaoshixYuyueEntity.getKaoshixYuyueYesnoTypes()==1)
                return R.error(511,"有相同的待审核的数据");
            else if(kaoshixYuyueEntity.getKaoshixYuyueYesnoTypes()==2)
                return R.error(511,"有相同的审核通过的数据");
            else
                return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody KaoshixYuyueEntity kaoshixYuyue, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,kaoshixYuyue:{}",this.getClass().getName(),kaoshixYuyue.toString());
        KaoshixYuyueEntity oldKaoshixYuyueEntity = kaoshixYuyueService.selectById(kaoshixYuyue.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
//        else if("学员".equals(role))
//            kaoshixYuyue.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

            kaoshixYuyueService.updateById(kaoshixYuyue);//根据id更新
            return R.ok();
    }


    /**
    * 审核
    */
    @RequestMapping("/shenhe")
    public R shenhe(@RequestBody KaoshixYuyueEntity kaoshixYuyueEntity, HttpServletRequest request){
        logger.debug("shenhe方法:,,Controller:{},,kaoshixYuyueEntity:{}",this.getClass().getName(),kaoshixYuyueEntity.toString());

        KaoshixYuyueEntity oldKaoshixYuyue = kaoshixYuyueService.selectById(kaoshixYuyueEntity.getId());//查询原先数据

//        if(kaoshixYuyueEntity.getKaoshixYuyueYesnoTypes() == 2){//通过
//            kaoshixYuyueEntity.setKaoshixYuyueTypes();
//        }else if(kaoshixYuyueEntity.getKaoshixYuyueYesnoTypes() == 3){//拒绝
//            kaoshixYuyueEntity.setKaoshixYuyueTypes();
//        }
        kaoshixYuyueService.updateById(kaoshixYuyueEntity);//审核

        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<KaoshixYuyueEntity> oldKaoshixYuyueList =kaoshixYuyueService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        kaoshixYuyueService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //.eq("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
        try {
            List<KaoshixYuyueEntity> kaoshixYuyueList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            KaoshixYuyueEntity kaoshixYuyueEntity = new KaoshixYuyueEntity();
//                            kaoshixYuyueEntity.setKaoshixYuyueUuidNumber(data.get(0));                    //预约编号 要改的
//                            kaoshixYuyueEntity.setKaoshixId(Integer.valueOf(data.get(0)));   //考试 要改的
//                            kaoshixYuyueEntity.setYonghuId(Integer.valueOf(data.get(0)));   //用户 要改的
//                            kaoshixYuyueEntity.setKaoshixYuyueYesnoTypes(Integer.valueOf(data.get(0)));   //预约状态 要改的
//                            kaoshixYuyueEntity.setKaoshixYuyueYesnoText(data.get(0));                    //审核回复 要改的
//                            kaoshixYuyueEntity.setCreateTime(date);//时间
                            kaoshixYuyueList.add(kaoshixYuyueEntity);


                            //把要查询是否重复的字段放入map中
                                //预约编号
                                if(seachFields.containsKey("kaoshixYuyueUuidNumber")){
                                    List<String> kaoshixYuyueUuidNumber = seachFields.get("kaoshixYuyueUuidNumber");
                                    kaoshixYuyueUuidNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> kaoshixYuyueUuidNumber = new ArrayList<>();
                                    kaoshixYuyueUuidNumber.add(data.get(0));//要改的
                                    seachFields.put("kaoshixYuyueUuidNumber",kaoshixYuyueUuidNumber);
                                }
                        }

                        //查询是否重复
                         //预约编号
                        List<KaoshixYuyueEntity> kaoshixYuyueEntities_kaoshixYuyueUuidNumber = kaoshixYuyueService.selectList(new EntityWrapper<KaoshixYuyueEntity>().in("kaoshix_yuyue_uuid_number", seachFields.get("kaoshixYuyueUuidNumber")));
                        if(kaoshixYuyueEntities_kaoshixYuyueUuidNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(KaoshixYuyueEntity s:kaoshixYuyueEntities_kaoshixYuyueUuidNumber){
                                repeatFields.add(s.getKaoshixYuyueUuidNumber());
                            }
                            return R.error(511,"数据库的该表中的 [预约编号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        kaoshixYuyueService.insertBatch(kaoshixYuyueList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }




    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = kaoshixYuyueService.queryPage(params);

        //字典表数据转换
        List<KaoshixYuyueView> list =(List<KaoshixYuyueView>)page.getList();
        for(KaoshixYuyueView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        KaoshixYuyueEntity kaoshixYuyue = kaoshixYuyueService.selectById(id);
            if(kaoshixYuyue !=null){


                //entity转view
                KaoshixYuyueView view = new KaoshixYuyueView();
                BeanUtils.copyProperties( kaoshixYuyue , view );//把实体数据重构到view中

                //级联表
                    KaoshixEntity kaoshix = kaoshixService.selectById(kaoshixYuyue.getKaoshixId());
                if(kaoshix != null){
                    BeanUtils.copyProperties( kaoshix , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setKaoshixId(kaoshix.getId());
                }
                //级联表
                    YonghuEntity yonghu = yonghuService.selectById(kaoshixYuyue.getYonghuId());
                if(yonghu != null){
                    BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setYonghuId(yonghu.getId());
                }
                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody KaoshixYuyueEntity kaoshixYuyue, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,kaoshixYuyue:{}",this.getClass().getName(),kaoshixYuyue.toString());
        Wrapper<KaoshixYuyueEntity> queryWrapper = new EntityWrapper<KaoshixYuyueEntity>()
            .eq("kaoshix_id", kaoshixYuyue.getKaoshixId())
            .eq("yonghu_id", kaoshixYuyue.getYonghuId())
            .in("kaoshix_yuyue_yesno_types", new Integer[]{1,2})
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        KaoshixYuyueEntity kaoshixYuyueEntity = kaoshixYuyueService.selectOne(queryWrapper);
        if(kaoshixYuyueEntity==null){
            kaoshixYuyue.setKaoshixYuyueYesnoTypes(1);
            kaoshixYuyue.setCreateTime(new Date());
        kaoshixYuyueService.insert(kaoshixYuyue);

            return R.ok();
        }else {
            if(kaoshixYuyueEntity.getKaoshixYuyueYesnoTypes()==1)
                return R.error(511,"有相同的待审核的数据");
            else if(kaoshixYuyueEntity.getKaoshixYuyueYesnoTypes()==2)
                return R.error(511,"有相同的审核通过的数据");
            else
                return R.error(511,"请不要重复预约");
        }
    }

}

