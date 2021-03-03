# n9e-hostreachable-collector
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
