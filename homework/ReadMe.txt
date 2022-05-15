本项目使用的技术：
1 本项目后端代码参考了DDD的分层模式和设计原则，进行代码文件的分层管理。
  DDD，即领域驱动设计模型(Domain-Driven Design)
  参考理论 https://domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf
2 面包的结算逻辑使用了策略模式
3 java反射
  结算策略通过java反射技术，根据配置动态加载结算接口的实现类。
  若后续需求需要提供框架层面扩展的话，可以类似java中的spi技术，新的结算策略可以已jar形式依赖进去，并在指定的文件中写好类路径。
4 hutool
  java的工具类
  官网地址：https://hutool.cn/
5 lombok
  注解方式实现java bean的getter/setter/builder，增强代码可读性
  官网地址：https://projectlombok.org/
6 PowerMock
  部分业务逻辑需要获取当前时间，单元测试需要通过PowerMock对java原生的Date进行代理mock，方便测试。
  官网地址：https://github.com/powermock/powermock/wiki

























