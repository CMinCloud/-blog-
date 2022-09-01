# blog-parent

感谢老哥的部署教程：https://blog.csdn.net/dolpin_ink/article/details/123056852

#### 介绍
码神之路blog项目

#### 软件架构
软件架构说明：
项目讲解说明：

1. 提供前端工程，只需要实现后端接口即可
2. 项目以单体架构入手，先快速开发，不考虑项目优化，降低开发负担
3. 开发完成后，开始优化项目，提升编程思维能力
4. 比如页面静态化，缓存，云存储，日志等
5. docker部署上线
6. 云服务器购买，域名购买，域名备案等

项目使用技术 ：
springboot + mybatisplus+redis+mysql

前端：
    需要的前置知识：
        vue基础，
        node.js
    现成Vue，项目部署需要在config\prod.env.js中修改 BASE_API为服务器ip或域名

    前端项目开发中运行：
    npm install
    npm run build
    npm run dev

后端：
    springboot + mybatisPlus + mybatis("xml写sql的操作")+ redis(这个会启动就行) + mysql(8.0)


部署阶段：
   再次
感谢老哥的部署教程：https://blog.csdn.net/dolpin_ink/article/details/123056852

前置知识：
    docker：
        基本使用+镜像容器+数据卷
    nginx:
        这个目前没学，基础入门即可
    linux：
        在服务器终端操作 需要 基本的linux命令操作

#### 安装教程

1.  将vue前端项目打包到云服务器
2.  将springboot后端项目（主网页，下面称为app）（后台管理系统，下面称为admin）打包到云服务器
3.  利用docker部署mysql,redis,ngix,app,admin。然后用docker-compose进行一个服务编排，使得项目分次序启动。

