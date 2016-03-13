Asana Weekly Report [![Build Status](https://travis-ci.org/hhimanshu/asanaweeklyreport.svg?branch=master)](https://travis-ci.org/hhimanshu/asanaweeklyreport)  [![Coverage Status](https://coveralls.io/repos/github/hhimanshu/asanaweeklyreport/badge.svg?branch=master)](https://coveralls.io/github/hhimanshu/asanaweeklyreport?branch=master)
====

## What? ##
This project retrieves your asana tasks for the week and sends out an email as highlights for the week.

## Audience ##
This project is useful for people who track their productivity and use Asana as a tool of choice.

## Why? ##
This goes back when I was working with [SunRun](http://www.sunrun.com). We were working on building [BrightPath](http://www.sunrun.com/why-sunrun/about/news/press-releases/sunrun-unveils-brightpath-first-end-end-automated-software) and were required to submit our weekly work as part of DOE grant. While initially I was resistant with this idea of additional work, I soon started to appreciate this approach. The main reasons I still do this (even though it is not required with my current employer) are 
- Reflect on the week and get a sense of accomplishment.
- Feedback on what went well and what didn't
- Identify what could be improved
- How I am spending my time

# Pre-requisites
- You need to have `Scala` and `sbt` installed. If you are on Mac, you can use [homebrew](http://brew.sh/) to install [both](http://brewformulas.org/search?utf8=%E2%9C%93&search%5Bterm%5D=scala&commit=Search)
- You need to get a Personal Access Token from Asana. You can follow the steps [here](https://asana.com/guide/help/api/api). You **do not** need to share this with anyone.
- You need to have a project in asana for which you want to get the weekly digest
- You need to have an gmail account. This account will be used to send email. You **do not** need to share your credentials.

## How I manage Asana for my workflow ##
While many of you have similar or better (please share your ideas) ways to manage their tasks, I prefer to keep my tasks in Calendar view. 

Every morning, I create a laundry list of items that I need to work on. This is usually coming via office emails, my personal task list or asks from people. I add these tasks in today's date (which basically creates a due date of today on these tasks).

Every evening I change the status to complete for tasks I have completed. For incomplete tasks, I either move them for tomorrow or leave them as is. A snapshot of my week looks like  

![AsanaWeeklyTasks](/images/AsanaWeeklyTasks.png?raw=true "AsanaWeeklyTasks")


## How to run ##
- Clone the repository
```
$ git clone git@github.com:hhimanshu/asanaweeklyreport.git
```

- Create a file with following properties
```
$ cat >> ~/Downloads/weekly.conf
access_token=my_access_token
project_name=Daily
email.username="my_email_account@gmail.com"
email.password=my_email_password
email.recipients="me@office.com,me@home.com"
```

The `project_name` can be located from your Asana dashboard. For example, in my case, the project name is `Daily`  

![AsanaWeeklyProject](/images/AsanaWeeklyProject.png?raw=true "AsanaWeeklyProject")

- Compile & Test
```
$ sbt clean compile test
````

- Pack
```
sbt pack
```

- Run
```
$ target/pack/bin/weekly ~/Downloads/weekly.conf 
Email sent successfully
```
If all goes well, you would see this message on the console and by now you should have received email in your inbox. This would look similar to following  

![AsanaWeeklyEmail](/images/AsanaWeeklyEmail.png?raw=true "AsanaWeeklyEmail")