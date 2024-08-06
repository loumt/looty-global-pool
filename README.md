> window下查看端口是否被占用

```$xslt
netstat -aon|findstr "8080"
taskkill /pid 4484 -f
```
