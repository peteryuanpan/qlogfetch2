# qlogfetch2

本工具的功能及命令，与七牛云实现的qlogfetch工具非常相似，链接：[qlogfetch](https://developer.qiniu.com/fusion/tools/1665/qlogfetch)

基本功能
- 本地目录下设置账号AK/SK，目前支持七牛云和阿里云
- 列举指定域名及日期下所有日志URL
- 并发下载指定域名及日期下所有日志URL
- 并发下载指定文件中的URL
- 列举账号下所有域名

使用方法
- 本地安装java环境
- 从 [Releases](https://github.com/peteryuanpan/qlogfetch2/releases) 中下载最新版本jar包
- 命令行下执行 java -jar qlogfetch2.jar，根据提示操作

### Commands

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

java -jar qlogfetch2.jar downlog -date 2020-12-03-03:2020-12-05-20 -domains a.b.com -dest ./log -worker 10 -overwrite
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
