* Dubbo 集成报错，fastjson2.JSONException: not support none serializable class

```
 java.io.IOException: org.apache.dubbo.common.serialize.SerializationException: com.alibaba.fastjson2.JSONException: not support none serializable class
```


```
dubbo:
    application:
        # Serializable 接口检查模式，Dubbo 中默认配置为 `true` 开启检查
        check-serializable: false
        # 检查模式分为三个级别：STRICT 严格检查，WARN 告警，DISABLE 禁用
        serialize-check-status: DISABLE
```

```
 原因
 实体类没有实现Serializable接口，Dubbo 3.2 版本中默认为 STRICT 严格检查级别。
 
 1.Dubbo 3.1 版本中默认为 WARN 告警级别，3.2 版本中默认为 STRICT 严格检查级别。
 2.Dubbo 3.2.0 版本开始默认序列化方式从 hessian2 切换为 fastjson2
 
```

```
dubbo-dependencies-zookeeper-curator5
<dubbo.version>3.2.15</dubbo.version>
中引入的zookeeper版本为
<zookeeper_version>3.8.4</zookeeper_version>
注册中心尽量保持一致
```