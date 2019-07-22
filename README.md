# 问答
  一个基于SpringBoot的问答网站，用户可以在这里分享你的知识、经验和见解  
  *反正也没有人看，我就在这里把我的开发历程写一下吧*
  
  ## 2019/7/18
  今天是第一次上传到Git，已经写好几个后端接口了，也撸出来几个前端页面
  ### 首先当然是首页啦
  <img src="https://github.com/ggbbone/img/blob/master/img/1563671360(1).png"/>
  
  这个首页呢由三部分构成（其实每个页面都是这样的）  
  头部导航栏保持显示，主要负责页面导航，搜索资源，发表问题的入口，还有用户的操作  
  页面的主体就是我们的问题展示，目前只显示的是问题的标题和该问题的详细说明，后面会改成问题标题和该问题的热门回答内容（没错，像知乎一样）  
  
  ### 然后是问题的详情页面
  <img src="https://github.com/ggbbone/img/blob/master/img/1563671431(1).png?raw=true" />
  
  这个页面显示问题的详细信息  
  然后下面会加载该问题的回答列表，有默认（按点赞数量和评论数量）和按时间倒序排序  
  ### 接着是这个发表回答的wangEditor富文本编辑器，轻量又好用
  <img src="https://github.com/ggbbone/img/blob/master/img/1563671471(1).png?raw=true" />
  
  我前端用的是Vue框架，目前只有回答内容是用v-html显示数据的，有xss攻击的隐患，用了一个第三方js过滤可能xss攻击，不过好像过滤范围有点大了，部分color属性都给过滤了  
 
  ### 下面是回答的一些操作
  
  #### 这个是回答的评论展开时的亚子
  <img src="https://github.com/ggbbone/img/blob/master/img/1563671502(1).png?raw=true" />
  
  #### 这个是回复评论展开的输入框
  <img src="https://github.com/ggbbone/img/blob/master/img/1563671541(1).png?raw=true" />
  
  #### 这是回复
  <img src="https://github.com/ggbbone/img/blob/master/img/1563671573(1).png?raw=true" />
  
  ## 2019/7/21
  其实上面的内容都是我今天才写的
  
  ### 昨天已经把消息中心的页面撸出来了
  <img src="https://github.com/ggbbone/img/blob/master/img/1563673878(1).png?raw=true" style="max-width:500px;max-width:500px;"/>
  
  目前这个页面是静态的，今天开发消息中心的后端接口，不过在这之前先把点赞功能做好  
  点赞功能我的设计思路是，每个问题在redis中存2个集合，分别是对某个实体（回答，评论，回复等）的点赞和点踩状态，value值为所有点赞或点踩的用户集合 
  这样也可以直接获得点赞总人数，通过springboot的自动化定时将点赞总人数同步到Mysql数据库  
  还要实现单向/优先队列处理事件  
  接着向队列添加一个点赞消息通知的事件  
  
  
  
  
  
