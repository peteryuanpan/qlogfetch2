
com.peter.command.CommandHelp
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

com.peter.command.CommandReg
```
Usage: qlogfetch2 reg -ak <AccessKey> -sk <SecretKey> [-source <Source>]
        Set access key and secret key [and source]

        Explain
          - You can use "-source" to specify different source
            Source contains "qiniu", "aliyun and default source is "qiniu"

        Example
          - qlogfetch2 reg -ak "AccessKey" -sk "SecretKey"
          - qlogfetch2 reg -ak "AccessKey" -sk "SecretKey" -source "qiniu"
          - qlogfetch2 reg -ak "AccessKey" -sk "SecretKey" -source "aliyun"
```

com.peter.command.CommandInfo
```
Usage: qlogfetch2 info
        show access key and secret key and source
```

com.peter.command.CommandListLog
```
Usage: qlogfetch2 listlog -date <Date> -domains <Domains> [-d]
        List all log urls of domains from date to date

        Explain
          - You should use "-date" to specify a datetime or a datetime closed range used by ":"
            A datetime format must be "yyyy-MM-dd" or "yyyy-MM-dd-HH"
            A datetime closed range format must be "datetime:datetime"
          - You should use "-domains" to specify a domain or a few of domains split by ";"
            A character of domain must be in range of [0-9], [A-Z], [a-z] or [.-]
          - You can use "-d" to print debug log

        Example
          - qlogfetch2 listlog -date "2020-01-01" -domains "img.abc.com;video.abc.com"
          - qlogfetch2 listlog -date "2020-01-01-00" -domains "img.abc.com"
          - qlogfetch2 listlog -date "2020-01-01-00:2020-01-02-23" -domains "img.abc.com"
```

com.peter.command.CommandDownLog
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

com.peter.command.CommandDownList
```
Usage: qlogfetch2 downlist -src <SourceFile> -dest <DestDir> [-overwrite] [-worker <Worker>] [-retry <Retry>] [-d]
        Download all urls of sourcefile to destination directory

        Explain
          - You should use "-src" to specify a sourcefile
            The source file should contains url which begins with "http://" or "https://"
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
          - qlogfetch2 downlist -src "./urls.txt" -dest "./logfolder"
          - qlogfetch2 downlist -src "./urls.txt" -dest "./logfolder" -worker 10
          - qlogfetch2 downlist -src "./urls.txt" -dest "./logfolder" -worker 50 -overwrite -retry 5
```

com.peter.command.CommandDomains
```
Usage: qlogfetch2 domains [-limit <limit>] [-d]
        List all of domains under this account

        Explain
          - You can use "-limit" to set limit of listing
            Limit must be larger than 0
          - You can use "-d" to print debug log
        
        Example
          - qlogfetch2 domains
          - qlogfetch2 domains -limit 1000
```
