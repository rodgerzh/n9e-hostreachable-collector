# n9e-hostreachable-collector
### 介绍
A centralized collector for collecting host reachable metric.<br/>
一个集中式的采集器，用于采集n9e中的endpoint表中的主机是否网络通达。<br/>
按以下格式将采集到的指标通过HTTP POST push到monapi的/api/transfer/push。
```
[
{
	"endpoint": "192.168.21.169",
	"metric": "host.net.reachable",
	"tags": "srcIp=192.168.10.11,collector=demo.1",
	"timestamp": 1599059859,
	"value": 1,
	"step": 10
},
...
{
	"endpoint": "192.168.69.199",
	"metric": "host.net.reachable",
	"tags": "srcIp=192.168.10.11,collector=demo.1",
	"timestamp": 1599059859,
	"value": 1,
	"step": 10
}
]
```
### 启动
```
cd script
./start-collector.sh
```
### 配置文件解释
config/cfg.json

```
{
	"collectorId":"demo.1",    #标志这个采集器程序，这个会加到上传指标的tag里
	"monapiUrl": "http://192.61.69.200:80",    #n9e monapi的地址
	"monapiUserName": "user",    #n9e monapi的用户名
	"monapiPassword": "userdemima",    #n9e monapi的密码
	"detecHostReachableTimeout":6000,    #探测endpoint是否连通的超时时间
	"batchDetecReachableIntervalMilliSeconds":20000,  #探测endpoint是否连通的间隔时长，单位毫秒
	"reportStep":20,    #上报指标的step
	"syncEndpointsIntervalSeconds":120,    #同步endpoint清单的间隔时长
	"meticName":"host.net.reachable",    #上报指标的名称
	"useEndpointMatch": true,    #是否用正则来匹配endpoint
	"endpointMatchRegExp": "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$",    #匹配endpoint的正则
	"useNodeNameMatch": false,  #是否用正则来匹配endpoint的分组名
	"nodeNameMatchRegExp": ".*服务器.*"  #匹配endpoint的分组名的正则
}

```
