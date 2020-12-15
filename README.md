# qlogfetch2

基本功能
- 本地目录下设置账号AK/SK，目前支持七牛云和阿里云
- 列举指定域名及日期下所有日志URL
- 并发下载指定域名及日期下所有日志URL
- 并发下载指定本地文件中的URL
- 列举账号下所有域名

本工具的功能及命令，与七牛云实现的qlogfetch工具非常相似，链接：[qlogfetch](https://developer.qiniu.com/fusion/tools/1665/qlogfetch)

qlogfetch2比qlogfetch多了一些新功能点
- 支持下载阿里云日志
- 支持指定日期以小时为单位（qlogfetch只能以天为单位）
- 支持指定本地文件下载URL

使用方法
- 本地安装java环境
- 从 [Releases](https://github.com/peteryuanpan/qlogfetch2/releases) 中下载最新版本jar包
- 命令行下执行 java -jar qlogfetch2.jar，根据提示操作，同时可以根据操作系统编写脚本（见下文Examples），将命令简化为 qlogfetch2

### Commands

|Name|Desription|Qiniu|Aliyun|
|--|--|--|--|
|reg|Set access key and secret key [and source]|||
|info|Show access key and secret key and source|||
|listlog|List all log urls of domains from date to date|[Link](https://developer.qiniu.com/fusion/api/1226/download-the-log)|[Link](https://help.aliyun.com/document_detail/91154.html)|
|downlog|Donwload all log urls of domains from date to date to destination directory|[Link](https://developer.qiniu.com/fusion/api/1226/download-the-log)|[Link](https://help.aliyun.com/document_detail/91154.html)|
|downlist|Download all urls of sourcefile to destination directory|||
|domains|List all of domains under this account|[Link](https://developer.qiniu.com/fusion/api/4246/the-domain-name#9)|[Link](https://help.aliyun.com/document_detail/91188.html)|

help
```
Qlogfetch2 version1.0.0

Supported commands:
      help      show this message
       reg      set access key and secret key [and source]
      info      show access key and secret key and source
   listlog      list all log urls of domains from date to date
   downlog      donwload all log urls of domains from date to date to destination directory
  downlist      download all urls of sourcefile to destination directory
   domains      list all of domains under this account
```

downlog
```
Usage: qlogfetch2 downlog -date <Date> -domains <Domains> -dest <DestDir> [-overwrite] [-worker <Worker>] [-retry <Retry>] [-d]
        Donwload all log urls of domains from date to date to destination directory

        Explain
          - You should use "-date" to specify a datetime or a datetime closed range used by ":"
            A datetime format must be "yyyy-MM-dd" or "yyyy-MM-dd-HH"
            A datetime closed range format must be "datetime:datetime"
          - You should use "-domains" to specify a domain or a few of domains split by ";"
            A character of domain must be in range of [0-9], [A-Z], [a-z] or [.-]
          - You should use "-dest" to specify a directory for download folder
            The directory will be created if not exist
          - You can use "-overwrite" to set whether or not overwrite a file if file exists
            The default value of "-overwrite" is false
          - You can use "-worker" to set concurrency level
            The concurrency level must be in range of [1, 300] and default value of "-worker" is 1
          - You can use "-retry" to set how many retry times if a http request is failed
            The retry times must be in range of [0, 10] and default value of "-retry" is 3
          - You can use "-d" to print debug log

        Example
          - qlogfetch2 downlog -date "2020-01-01" -domains "img.abc.com;video.abc.com" -dest "./logfolder"
          - qlogfetch2 downlog -date "2020-01-01-00" -domains "img.abc.com" -dest "./logfolder" -worker 10
          - qlogfetch2 downlog -date "2020-01-01-00:2020-01-02-23" -domains "img.abc.com" -dest "./logfoler" -worker 50 -overwrite -retry 5
```

### Examples

Windows下，编写一个qlogfetch2.bat文件，将文件目录加入环境变量

absolute_path是操作系统中文件的完整路径（下同）
```
@echo off
java -jar <absolute_path>/qlogfetch2.jar %*
```

Linux下，编写一个qlogfetch2.sh文件，将文件目录加入环境变量
```
java -jar <absolute_path>/qlogfetch2.jar $*
```

设置七牛云账号ak/sk（可以不指定-source，默认是qiniu）

qlofetch2 reg -ak AccessKey -sk SecretKey
```
2020-12-15 02:35:25.167 [main] c.p.e.ExecuteReg[23] - Write account info to .qlogfetch2\account.json
```

设置阿里云账号ak/sk

qlofetch2 reg -ak AccessKey -sk SecretKey -source aliyun
```
2020-12-15 02:35:25.167 [main] c.p.e.ExecuteReg[23] - Write account info to .qlogfetch2\account.json
```

根据域名和日期下载日志

qlogfetch2 downlog -date 2020-12-03-03:2020-12-05-20 -domains a.b.com -dest ./log -worker 10 -overwrite
```
2020-12-15 02:35:28.167 [main] c.p.e.d.ExecuteDownLog[24] - Start to list log urls of domain
2020-12-15 02:35:29.415 [main] c.p.e.d.ExecuteDownLog[38] - Finished listing log urls of domain
2020-12-15 02:35:29.416 [main] c.p.e.d.ExecuteDownLog[57] - Start to download urls
2020-12-15 02:35:30.656 [pool-1-thread-3] c.p.e.d.ExecuteDownLogAbstract[92] - Success to download url http://cdnlog-sh-public.oss-cn-shanghai.aliyuncs.com/v1.l1cache/234581/a.b.com/2020_12_03/a.b.com_2020_12_03_040000_050000.gz?Expires=1608575728&O
SSAccessKeyId=LTAIviCc6zy8x3xa&Signature=K5jdu0nmKERNwAAIM0sZbAx%2FaBw%3D size 1.46 MB
2020-12-15 02:35:30.795 [pool-1-thread-6] c.p.e.d.ExecuteDownLogAbstract[92] - Success to download url http://cdnlog-sh-public.oss-cn-shanghai.aliyuncs.com/v1.l1cache/234581/a.b.com/2020_12_03/a.b.com_2020_12_03_070000_080000.gz?Expires=1608575728&O
SSAccessKeyId=LTAIviCc6zy8x3xa&Signature=Q1I24EyzIrQQhXn%2F6P2A7V%2F3%2BJ8%3D size 2.09 MB
...
2020-12-15 02:35:45.908 [pool-1-thread-3] c.p.e.d.ExecuteDownLogAbstract[92] - Success to download url http://cdnlog-sh-public.oss-cn-shanghai.aliyuncs.com/v1.l1cache/234581/a.b.com/2020_12_05/a.b.com_2020_12_05_190000_200000.gz?Expires=1608575729&O
SSAccessKeyId=LTAIviCc6zy8x3xa&Signature=EAgULWgdDinjcB3SmqY0t1DjZD8%3D size 2.44 MB
2020-12-15 02:35:45.931 [main] c.p.e.d.ExecuteDownLog[62] - Finished downloading urls
2020-12-15 02:35:45.932 [main] c.p.e.d.ExecuteDownLog[63] - TotalSize: 172.29 MB
2020-12-15 02:35:45.932 [main] c.p.e.d.ExecuteDownLog[64] - TotalTimeCost: 16.51 second
2020-12-15 02:35:45.932 [main] c.p.e.d.ExecuteDownLog[65] - AverageSpeed: 10.44 MB/s
2020-12-15 02:35:45.932 [main] c.p.e.d.ExecuteDownLog[66] - SuccessNumber: 65
2020-12-15 02:35:45.933 [main] c.p.e.d.ExecuteDownLog[67] - FailedNumber: 0
```

根据本地文件下载日志

qlogfetch2 downlist -src C:\Users\Admin\Desktop\urls.txt -dest log -worker 10 -overwrite
```
2020-12-15 10:24:13.700 [main] c.p.e.d.ExecuteDownLog[43] - Start to read urls from C:\Users\Admin\Desktop\urls.txt
2020-12-15 10:24:13.703 [main] c.p.e.d.ExecuteDownLog[51] - Finished reading urls from C:\Users\Admin\Desktop\urls.txt
2020-12-15 10:24:13.725 [main] c.p.e.d.ExecuteDownLog[57] - Start to download urls
2020-12-15 10:24:15.146 [pool-1-thread-1] c.p.e.d.ExecuteDownLogAbstract[92] - Success to download url http://cdnlog2.oss-cn-hangzhou.aliyuncs.com/v1.l1cache/234581/a.b.com/2020_12_01/a.b.com_2020_12_01_030000_040000.gz?Expires=1608570579&OSSAccessKeyId=LTAIkNf2IHcKd0kT&Signature=qOTcvlligX0IJFCfz22%2FixmJgH8%3D size 940.51 KB
...
2020-12-15 10:27:06.907 [pool-1-thread-9] c.p.e.d.ExecuteDownLogAbstract[92] - Success to download url http://cdnlog-sh-public.oss-cn-shanghai.aliyuncs.com/v1.l1cache/234581/c.d.com/2020_12_01/c.d.com_2020_12_01_034800_035000.gz?Expires=1608570580&OSSAccessKeyId=LTAIviCc6zy8x3xa&Signature=gQkGaqf%2BEI6Y5H6Vn3wYc1Gd2mU%3D size 15.31 MB
2020-12-15 10:27:07.204 [pool-1-thread-6] c.p.e.d.ExecuteDownLogAbstract[92] - Success to download url http://cdnlog-sh-public.oss-cn-shanghai.aliyuncs.com/v1.l1cache/234581/c.d.com/2020_12_01/c.d.com_2020_12_01_035800_040000.gz?Expires=1608570580&OSSAccessKeyId=LTAIviCc6zy8x3xa&Signature=2H40tT7BPDWzKsSkN1AsBSXQAQA%3D size 14.91 MB
2020-12-15 10:27:07.338 [main] c.p.e.d.ExecuteDownLog[62] - Finished downloading urls
2020-12-15 10:27:07.338 [main] c.p.e.d.ExecuteDownLog[63] - TotalSize: 474.72 MB
2020-12-15 10:27:07.339 [main] c.p.e.d.ExecuteDownLog[64] - TotalTimeCost: 45.7 second
2020-12-15 10:27:07.339 [main] c.p.e.d.ExecuteDownLog[65] - AverageSpeed: 10.39 MB/s
2020-12-15 10:27:07.339 [main] c.p.e.d.ExecuteDownLog[66] - SuccessNumber: 31
2020-12-15 10:27:07.340 [main] c.p.e.d.ExecuteDownLog[67] - FailedNumber: 0
```

下载失败了怎么办？

目前还未支持 -failed-list 功能，控制台的日志会同时打印一份到本地目录的 .log.txt 文件中，可以手动过滤日志文件，提取出下载失败的urls，放到新文件中，再用 qlogfetch2 downlist 功能下载

并发线程数设置多少？

由于是日志下载，单次请求的码率会比较大，我进行过测试，一般设置 10 - 50 就够了（看客户端带宽），再往上走可能会出现 Read time out 或 服务端5XX 的情况，如果是码率比较小的接口（比如 listbucket），可以设置大很多（比如100-300），这个时候主要瓶颈在 qps，但日志下载不适用
