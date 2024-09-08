import Vue from 'vue';
//配置路由
import VueRouter from 'vue-router'
Vue.use(VueRouter);
    // 解决多次点击左侧菜单报错问题
    const VueRouterPush = VueRouter.prototype.push
    VueRouter.prototype.push = function push (to) {
    return VueRouterPush.call(this, to).catch(err => err)
    }
//1.创建组件
import Index from '@/views/index'
import Home from '@/views/home'
import Login from '@/views/login'
import NotFound from '@/views/404'
import UpdatePassword from '@/views/update-password'
import pay from '@/views/pay'
import register from '@/views/register'
import center from '@/views/center'
import beifen from '@/views/modules/databaseBackup/beifen'
import huanyuan from '@/views/modules/databaseBackup/huanyuan'

     import users from '@/views/modules/users/list'
    import dictionary from '@/views/modules/dictionary/list'
    import forum from '@/views/modules/forum/list'
    import jiaolian from '@/views/modules/jiaolian/list'
    import jiaolianCollection from '@/views/modules/jiaolianCollection/list'
    import jiaolianLiuyan from '@/views/modules/jiaolianLiuyan/list'
    import jiaolianYuyue from '@/views/modules/jiaolianYuyue/list'
    import jiaoxiao from '@/views/modules/jiaoxiao/list'
    import kaoshix from '@/views/modules/kaoshix/list'
    import kaoshixYuyue from '@/views/modules/kaoshixYuyue/list'
    import news from '@/views/modules/news/list'
    import yonghu from '@/views/modules/yonghu/list'
    import config from '@/views/modules/config/list'
    import dictionaryForumState from '@/views/modules/dictionaryForumState/list'
    import dictionaryJiaolian from '@/views/modules/dictionaryJiaolian/list'
    import dictionaryJiaolianCollection from '@/views/modules/dictionaryJiaolianCollection/list'
    import dictionaryJiaolianYuyueYesno from '@/views/modules/dictionaryJiaolianYuyueYesno/list'
    import dictionaryJiaoxiao from '@/views/modules/dictionaryJiaoxiao/list'
    import dictionaryKaoshix from '@/views/modules/dictionaryKaoshix/list'
    import dictionaryKaoshixYuyueYesno from '@/views/modules/dictionaryKaoshixYuyueYesno/list'
    import dictionaryNews from '@/views/modules/dictionaryNews/list'
    import dictionarySex from '@/views/modules/dictionarySex/list'





//2.配置路由   注意：名字
const routes = [{
    path: '/index',
    name: '首页',
    component: Index,
    children: [{
      // 这里不设置值，是把main作为默认页面
      path: '/',
      name: '首页',
      component: Home,
      meta: {icon:'', title:'center'}
    }, {
      path: '/updatePassword',
      name: '修改密码',
      component: UpdatePassword,
      meta: {icon:'', title:'updatePassword'}
    }, {
      path: '/pay',
      name: '支付',
      component: pay,
      meta: {icon:'', title:'pay'}
    }, {
      path: '/center',
      name: '个人信息',
      component: center,
      meta: {icon:'', title:'center'}
    }, {
        path: '/huanyuan',
        name: '数据还原',
        component: huanyuan
    }, {
        path: '/beifen',
        name: '数据备份',
        component: beifen
    }, {
        path: '/users',
        name: '管理信息',
        component: users
    }
    ,{
        path: '/dictionaryForumState',
        name: '帖子状态',
        component: dictionaryForumState
    }
    ,{
        path: '/dictionaryJiaolian',
        name: '教练类型',
        component: dictionaryJiaolian
    }
    ,{
        path: '/dictionaryJiaolianCollection',
        name: '收藏表类型',
        component: dictionaryJiaolianCollection
    }
    ,{
        path: '/dictionaryJiaolianYuyueYesno',
        name: '预约状态',
        component: dictionaryJiaolianYuyueYesno
    }
    ,{
        path: '/dictionaryJiaoxiao',
        name: '展示类型',
        component: dictionaryJiaoxiao
    }
    ,{
        path: '/dictionaryKaoshix',
        name: '考试类型',
        component: dictionaryKaoshix
    }
    ,{
        path: '/dictionaryKaoshixYuyueYesno',
        name: '预约状态',
        component: dictionaryKaoshixYuyueYesno
    }
    ,{
        path: '/dictionaryNews',
        name: '公告类型',
        component: dictionaryNews
    }
    ,{
        path: '/dictionarySex',
        name: '性别类型',
        component: dictionarySex
    }
    ,{
        path: '/config',
        name: '轮播图',
        component: config
    }


    ,{
        path: '/dictionary',
        name: '字典',
        component: dictionary
      }
    ,{
        path: '/forum',
        name: '论坛',
        component: forum
      }
    ,{
        path: '/jiaolian',
        name: '教练',
        component: jiaolian
      }
    ,{
        path: '/jiaolianCollection',
        name: '教练收藏',
        component: jiaolianCollection
      }
    ,{
        path: '/jiaolianLiuyan',
        name: '教练留言',
        component: jiaolianLiuyan
      }
    ,{
        path: '/jiaolianYuyue',
        name: '教练预约',
        component: jiaolianYuyue
      }
    ,{
        path: '/jiaoxiao',
        name: '驾校信息',
        component: jiaoxiao
      }
    ,{
        path: '/kaoshix',
        name: '考试通知',
        component: kaoshix
      }
    ,{
        path: '/kaoshixYuyue',
        name: '考试预约',
        component: kaoshixYuyue
      }
    ,{
        path: '/news',
        name: '公告通知',
        component: news
      }
    ,{
        path: '/yonghu',
        name: '学员',
        component: yonghu
      }


    ]
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: {icon:'', title:'login'}
  },
  {
    path: '/register',
    name: 'register',
    component: register,
    meta: {icon:'', title:'register'}
  },
  {
    path: '/',
    name: '首页',
    redirect: '/index'
  }, /*默认跳转路由*/
  {
    path: '*',
    component: NotFound
  }
]
//3.实例化VueRouter  注意：名字
const router = new VueRouter({
  mode: 'hash',
  /*hash模式改为history*/
  routes // （缩写）相当于 routes: routes
})

export default router;
