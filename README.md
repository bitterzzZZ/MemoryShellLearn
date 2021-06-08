# 0x00 动机

仓库主要分享一下学习内存马以来的成果：

- 几个jsp文件，可以直接注入tomcat的listener、filter、servlet内存马
- spring mvc 结合JNDI注入可以使用的java代码，通过java恶意类可以注入litener、filter、servlet、controller和interceptor内存马(tomcat环境下)

看了大佬们的无私技术分享文章，学到很多东西，所以把收集的文章列举在后面，respect ！

# 0x01 文章汇总

在学习的过程中，又想到了内存马结合菜刀和冰蝎的使用，所以研究了一下写了一篇文章(联动冰蝎的具体代码可以见仓库内的controller内存马）

[针对spring mvc的controller内存马-学习和实验(注入菜刀和冰蝎可用的马](https://www.cnblogs.com/bitterz/p/14820898.html)

跟着landgrey大佬的文章走了一遍spring mvc的拦截器添加和调用过程，记录了一下

[针对Spring MVC的Interceptor内存马](https://www.cnblogs.com/bitterz/p/14859766.html)

然后是学习阶段看的文章：

**filter内存马**

- [中间件内存马注入&冰蝎连接(附更改部分代码)](https://mp.weixin.qq.com/s/eI-50-_W89eN8tsKi-5j4g)
- [filter内存马技术！_localhost01-CSDN博客](https://blog.csdn.net/localhost01/article/details/107340698)
- [基于Tomcat无文件Webshell研究](https://mp.weixin.qq.com/s?__biz=MzI0NzEwOTM0MA==&mid=2652474966&idx=1&sn=1c75686865f7348a6b528b42789aeec8&scene=21#wechat_redirect)
- [Tomcat内存马学习 一](http://wjlshare.com/archives/1529)
- [Tomcat 内存马学习(二)：结合反序列化注入内存马 - 木头的小屋](http://wjlshare.com/archives/1541)
- [Java安全之基于Tomcat实现内存马 - nice_0e3 - 博客园](https://www.cnblogs.com/nice0e3/p/14622879.html)

**servlet内存马**

- [java内存马综述-servlet和listener型](https://mp.weixin.qq.com/s/YhiOHWnqXVqvLNH7XSxC9w)

**listener型**

- [Tomcat下基于Listener的内存Webshell分析](http://foreversong.cn/archives/1547)

**Spring controller内存马**

- [基于内存 Webshell 的无文件攻击技术研究 - 安全客，安全资讯平台](https://www.anquanke.com/post/id/198886#h2-6)

**Spring Interceptor内存马**

- [利用 intercetor 注入 spring 内存 webshell](https://landgrey.me/blog/19/)

**其它前提研究-获取request对象**

- [基于tomcat的内存 Webshell 无文件攻击技术](https://xz.aliyun.com/t/7388)
- [Tomcat中一种半通用回显方法](https://xz.aliyun.com/t/7348)

**Weblogic注入内存马**

- [中间件内存马注入&冰蝎连接(附更改部分代码)](https://mp.weixin.qq.com/s/eI-50-_W89eN8tsKi-5j4g)
- [weblogic 无文件webshell的技术研究](https://www.cnblogs.com/potatsoSec/p/13162792.html)

**java agent内存马**

- [利用“进程注入”实现无文件复活 WebShell](https://www.freebuf.com/articles/web/172753.html)

**内存马查杀**

- [Tomcat 内存马检测](https://www.anquanke.com/post/id/219177)
- [查杀Java web filter型内存马 | 回忆飘如雪](http://gv7.me/articles/2020/kill-java-web-filter-memshell/)
- [Filter/Servlet型内存马的扫描抓捕与查杀 | 回忆飘如雪](http://gv7.me/articles/2020/filter-servlet-type-memshell-scan-capture-and-kill/)
- [基于javaAgent内存马检测查杀指南-华盟网](https://www.77169.net/html/278275.html)
- https://github.com/alibaba/arthas
- https://github.com/LandGrey/copagent
- https://github.com/c0ny1/java-memshell-scanner
- https://syst1m.com/post/memory-webshell/



