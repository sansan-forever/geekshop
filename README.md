# 极客商城～GeekShop
一个面向开发者的、基于Spring+GraphQL+Angular的、无前端(headless)电商框架。框架亮点：

* **无前端(headless)框架**，后端提供基于标准GraphQL的API接口，支持前后分离开发模型，前端框架无关，用户可以根据需要灵活选择各种前端技术，如Angular，Reactjs或者Vuejs等。
* **面向开发者的框架**，支持基于契约的类型安全的GralphQL API接口，提升开发者生产率。
* **开源**，基于[MIT license](https://tldrlegal.com/license/mit-license)，所有源代码开放在github上。

**注意，该框架主要为教学而开发，目前是Alpha状态，如需生产化，需自己严格测试+定制**。

## 电商框架功能亮点

* 支持产品和变体(Products & Variants)
* 支持库存管理(Stock management)
* 支持产品分面和基于分面的搜索(Product facets & faceted search)
* 支持产品分类/产品集(Product categories / collections)
* 支持产品搜索(Product Search)
* 支持支付供应商集成(Payment provider integrations)
* 支持快递供应商集成(Shipping provider integrations)
* 支持打折和推广(Discounts and promotions)
* 支持多种管理员角色和细粒度权限控制(Multiple administrators with fine-grained permissions)
* 支持基于Angular的管理后台(Built-in admin interface)
* 支持访客直接结账模式(Guest checkouts)
* 支持集成多种登录认证方式(Multiple Authentication Methods)

## 技术栈

1. 后端开发框架：Spring Boot 2.x
2. 前端(管理后台)开发框架：Angular 10.x
3. GraphQL框架：[graphql-java-kickstart](https://github.com/graphql-java-kickstart/graphql-spring-boot)
4. 持久层框架：[Mybatis-Plus](https://mybatis.plus/)
5. 异步任务框架：Guava EventBus
6. 安全框架：自研，基于JWT + AOP
7. 数据库存储：支持H2(测试)和MySQL(生产)

## 如何运行

### 一、如何运行测试

通过maven运行所有单元和集成测试：

```shell
mvn clean test
```

目前已经编写**527**个测试用例，覆盖大部分框架逻辑，且所有测试全部在本地跑通过。测试时默认使用嵌入式H2数据库。

```shell
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.42 s - in io.geekshop.e2e.StockControlTest
[INFO] Running io.geekshop.common.utils.TimeSpanUtilTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.01 s - in io.geekshop.common.utils.TimeSpanUtilTest
[INFO] Running io.geekshop.data_import.ImportParserTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.024 s - in io.geekshop.data_import.ImportParserTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 527, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  02:22 min
[INFO] Finished at: 2021-01-11T18:08:32+08:00
[INFO] ------------------------------------------------------------------------
```

### 二、如何运行应用

在Intellij IDE中运行Spring Boot启动类**GeekShopApplication**，默认使用嵌入式H2数据库。

访问GraphQL Playground：

```shell
http://127.0.0.1:8080/playground
```

关于GraphQL Playground如何使用，不复杂，请自行网上找资料。具体每个API的接口规范，可以参考GraphQL Playground反射出来的Schema文档，或者直接看源码resources/graphql中的schema文档。

如果不想用GraphQL Playground，也可以使用最新版本的Postman访问GraphQL端点：

```
http://127.0.0.1:8080/graphql
```

具体操作也不复杂，请自行摸索或网上找资料。

## 有待开发功能(TODO List)

- [ ] [Plan 2021 Q1]支持基于Angular的后台管理(Admin)界面
- [ ] [Plan 2021 Q1]支持基于Angular的演示用的购物Shopping App
- [ ] [Plan 2021 Q1]支持MySQL数据库。目前暂支持嵌入式H2数据库
- [ ] [Plan 2021 Q1]支持本地图片存储和图片缩放/剪切
- [ ] [Plan 2021 Q1]支持事件可DB持久化的异步任务机制，集成[killbill common queue](https://github.com/killbill/killbill-commons/tree/master/queue)。目前仅支持事件不可持久化的Guava EventBus
- [ ] [Plan 2021 Q1]Docker自动部署脚本
- [ ] [Plan 2021 Q1]开发者文档
- [ ] [Plan 2021 Q1]性能测试脚本+性能测试
- [ ] [Plan 2021 Q2]极客时间课程《Spring + GraphQL电商中台实战》
- [ ] [Plan 2021 Q3/Q4] 微服务版本+K8s部署+课程
- [ ] 汉化，目前仅支持英文，需企业自行定制，欢迎pull request。
- [ ] 支持ElasticSearch产品搜索，需企业定制，欢迎pull request。目前仅支持基于DB的简单文本搜索(Like方式)。
- [ ] 支持图片的云存储和处理，需企业自行定制，欢迎pull request。目前暂支持本地存储和简单图片处理。
- [ ] 集成微信/支付宝等支付方式，需企业自行定制，欢迎pull request。目前仅支持Mock支付方式。
- [ ] 集成快递供应商，需企业自行定制，欢迎pull request。目前仅支持Mock快递方式。

## 源码目录说明

Java源码
```
├── java
│   └── io
│       └── geekshop
│           ├── GeekShopApplication.java # Spring Boot主应用入口，可直接运行
│           ├── common # 公共类
│           ├── config # Spring配置Beans
│           ├── custom # 安全/GraphQL/Mybatis等定制类
│           ├── data_import # 产品数据导入
│           ├── email # 邮件发送功能
│           ├── entity # 实体层
│           ├── eventbus # 异步事件处理
│           ├── exception # 异常类
│           ├── mapper # MyBatis-Plus Mapper
│           ├── options # 配置项
│           ├── resolver # GraphQL API resolvers(相当于控制器层)
│           ├── service # 服务层
│           └── types # GraphQL类型(相当于DTO)
```

Resoures源码

```
└── resources
    ├── application-mysql.yml # 支持mysql数据库的spring配置文件
    ├── application.yml # spring配置文件，默认支持h2数据库
    ├── banner.txt # banner
    ├── db
    │   ├── h2 # h2数据库schema
    │   └── mysql # mysql数据库schema
    ├── graphql
    │   ├── admin-api # 管理端GraphQL API契约
    │   ├── common # 公共类型契约
    │   ├── shop-api # 购物端GraphQL API契约
    │   └── type # 传输对象类型契约
    └── templates
        └── email # 邮件模版
```

测试Java源码：

```
├── java
│   └── io
│       └── geekshop
│           ├── ApiClient.java # 测试用GraphQL API client，支持admin和shop模式
│           ├── ApiException.java # API调用异常
│           ├── GeekShopGraphQLTest.java # 集成测试注解(基于SpringBootTest)
│           ├── MockDataService.java # 填充测试用Mock数据的服务
│           ├── PopulateOptions.java # 填充测试用Mock数据的选项
│           ├── common # 对公共类的一些测试
│           ├── config # 测试用Spring配置Beans
│           ├── data_import # 对产品导入功能的测试
│           ├── e2e # 对GraphQL resolvers的端到端测试，对整个电商框架的主要测试代码都在该目录中！
│           ├── event # 测试用事件处理类
│           ├── service # 对服务层助手类的一些测试
│           └── utils # 一些测试用工具类
```

测试Resources源码：

```
.
├── application.yml # 测试用Spring应用配置
├── fixtures # 测试用产品Mock数据
├── graphql # 测试用API调用GraphQL文件，说明调用的API和返回哪些字段，主要用于上面测试Java源码的e2e测试。
└── test_fixtures # 用于测试产品数据导入功能的一些Mock数据
```

## 感谢

该框架主要参考[vendure-ecommerce电商框架](https://github.com/vendure-ecommerce/vendure)(基于TypeScript/Nestjs/Angular)，感谢原作者的贡献🙏

## Copyright

#### Copyright © 2020-present GeekXYZ. All rights reserved.