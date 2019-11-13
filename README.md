# SMS-Gateway
中国移动通信互联网短信网关接口协议CMPP2.0及模拟器
本文用的是中国移动通信互联网短信网关接口协议CMPP2.0，参考华为smproxy.jar包并进行了一些问题的修复和封装。

# 华为smproxy.jar包存在乱码问题：
1、提交短信

CMPP_Connect_REP----->AuthenticatorISMG

CMPP_Submit_REP------>MsgId

CMPP_Submit---------->msg_Content

2、状态报告

CMPP_Deliver--------->MsgId

3、上行消息

CMPP_Deliver--------->MsgId

CMPP_Deliver--------->MsgContent

解决办法通过调整底层源码AuthenticatorISMG、MsgId输出为HexString，MsgContent中文转码

# 通过TypeConvert获取msgId
TypeConvert.bytesToHexString(msgId, msgId.length);

# 中文转码问题
经测试 需要编码为GBk或GB2312

# 如有不明白的地方还请您添加我的微信号：clt928

