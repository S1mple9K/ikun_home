使用说明

一、项目说明：
  该项目为一个类似于论坛，用户可进行发帖，点赞，回复。供ikun们使用

二、目录说明：
  1.ikun_net_user为vite项目，导入vscode后需使用npm install命令来下载依赖，请确保安装并配置Node.js，下载完成请使用npm run dev运行
  2.ikun_common为实体类模块，供ikun_user引用
  3.ikun_user为后台业务模块具体实现，使用技术为：ssm springboot mp springdata mongodb redis mysql，使用前请确保配置Maven环境，Mongodb、Redis、Mysql运行环境，导入idea后修改application.yml配置项为自己的配置
  并将短信服务和oss存储服务对应信息修改为自己的
