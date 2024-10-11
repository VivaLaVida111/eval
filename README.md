# 金牛城管局考评系统
## 项目简介
金牛城管局考评系统是一个基于SpringBoot+Vue的前后端分离的项目，此为后端代码仓库。主要功能是按相关文件从多个方面对金牛区所属的13个街道进行考评打分。通过权限控制，不同角色的用户可以从各自分管的方向进行考评。并最终进行可视化展示，提供前端数据展示、图表展示，方便用户查看。
# Quick Start Up
## 配置信息
application.yml 中配置启动信息

mock为后端服务具体配置，在本地启动时配置
```
url: jdbc:mysql://localhost:3306/eval?useSSL=true&serverTimezone=UTC
```
## 启动项目
SpringBoot 通过EvalApplication启动即可。
## 使用的组件
项目整合了Mybatis-Plus作为数据持久层，并用于业务代码的快速自动生成。
## 特殊说明
scheduled目录下的代码为定时任务，用于每天定时生成考评数据。因为目前暂无真实考评数据，所以这里生成数据用于调试。