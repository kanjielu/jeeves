# jeeves

[![Build Status](https://travis-ci.org/kanjielu/jeeves.svg?branch=master)](https://travis-ci.org/kanjielu/jeeves)

A smart WeChat bot.

[English Docs](https://github.com/kanjielu/jeeves/blob/master/README.md)

## Getting Started

### 运行环境
JDK 8

### 如何运行
```
mvn spring-boot:run
```

启动后，会看到如下的日志信息：

![alt text](https://github.com/kanjielu/jeeves/blob/master/images/login1.png?raw=true "Await Scanning")

扫过二维码，点击确认登录后

![alt text](https://github.com/kanjielu/jeeves/blob/master/images/login2.png?raw=true "Login Successfully")

### 登录过程
![alt text](https://github.com/kanjielu/jeeves/blob/master/images/login-process-diagram.png?raw=true "Login process diagram")

### 示例
`com.cherry.jeeves.MessageHandlerImpl` 是一个jeeves默认提供的一个示例程序。如果需要自定义修改，可以直接改动 `MessageHandlerImpl` 的代码，或者创建另一个继承`MessageHandler`的Spring Bean。

已经在 `MessageHandlerImpl` 设置的jeeves行为有:
* 自动将消息里的缩略图存在运行环境本地；
* 自动回复1对1的文本消息；
* 接受任何人的好友邀请；
* 在接受好友邀请后，为他设置一个别名；
* 对于其他的事件，记录日志。

## 如何使用
### 事件
只要一个Spring Bean继承了`MessageHandler`，它会收到如下消息。

#### 文本消息
```java
void onReceivingPrivateTextMessage(Message message);
```

| 参数 | 含义 |
| --- | --- |
| `message` | 收到的消息 |

#### 图片消息
```java
void onReceivingPrivateTextMessage(Message message, String thumbImageUrl, String fullImageUrl);
```

| 参数 | 含义 |
| --- | --- |
| `message` | 收到的消息 |
|`thumbImageUrl`| 缩略图URL |
|`fullImageUrl`| 原图URL |

#### 聊天群文本消息
```java
void onReceivingChatRoomTextMessage(Message message);
```

| 参数 | 含义 |
| --- | --- |
| `message` | 收到的消息 |

#### 聊天群图片消息
```java
void onReceivingChatRoomImageMessage(Message message, String thumbImageUrl, String fullImageUrl);
```

| 参数 | 含义 |
| --- | --- |
| `message` | 收到的消息 |
|`thumbImageUrl`| 缩略图URL |
|`fullImageUrl`| 原图URL |

#### 收到加好友邀请
```java
boolean onReceivingFriendInvitation(RecommendInfo info);
```

| 参数 | 含义 |
| --- | --- |
| `RecommendInfo` | 加好友邀请 |

| 返回值 |
| --- |
| 如果接受该好友请求，则返回true。否则返回false  ( type:`boolean` )|

#### 接受了好友邀请后的回调
```java
boolean postAcceptFriendInvitation(Message message);
```

| 参数 | 含义 |
| --- | --- |
| `message` | 加好友邀请 |

#### 发现新的聊天群
```java
void onNewChatRoomsFound(Set<Contact> chatRooms);
```

| 参数 | 含义 |
| --- | --- |
| `chatRooms` | 新的聊天群 |

#### 聊天群被删除
```java
void onChatRoomsDeleted(Set<Contact> chatRooms);
```

| 参数 | 含义 |
| --- | --- |
| `chatRooms` | 被删除的聊天群 |

#### 聊天群的用户有变动
```java
void onChatRoomMembersChanged(Contact chatRoom, Set<ChatRoomMember> membersJoined, Set<ChatRoomMember> membersLeft);
```

| 参数 | 含义 |
| --- | --- |
| `chatRoom` | 发生变动的聊天群 |
| `membersJoined` | 新增人员 |
| `membersLeft` | 离群人员 |

#### 发现新好友
```java
void onNewFriendsFound(Set<Contact> contacts);
```

| 参数 | 含义 |
| --- | --- |
| `contacts` | a list of new friends |

#### 好友被删除
```java
void onFriendsDeleted(Set<Contact> contacts);
```

| 参数 | 含义 |
| --- | --- |
| `contacts` | 被删除的好友 |

#### 发现新的公众号
```java
void onNewMediaPlatformsFound(Set<Contact> mps);
```

| 参数 | 含义 |
| --- | --- |
| `mps` | 新的公众号 |

#### 公众号被删除
```java
void onMediaPlatformsDeleted(Set<Contact> mps);
```

| 参数 | 含义 |
| --- | --- |
| `mps` | 被删除的公众号 |

#### 收到红包
```java
void onRedPacketReceived(Contact contact);
```

| 参数 | 含义 |
| --- | --- |
| `contact` | 发红包的好友或者群 |

### API
`WechatHttpService` 已经提供了各式各样的API供您使用。

#### 获取所有联系人、群、公众号
```java
Set<Contact> getContact()
```
| 返回值 |
| --- |
| 所有联系人、群、公众号  ( type: `Set<Contact>` )|

#### 获取聊天群里的所有成员
```java
Set<Contact> batchGetContact(Set<String> list)
```
| 参数 | 含义 |
| --- | --- |
| `list` | 所需查询成员的聊天群的userName列表 |

| 返回值 |
| --- |
| 查询的聊天群，每个聊天群已经包含了成员信息 ( type: `Set<Contact>` )|

#### 发送文本消息
```java
void sendText(String userName, String content)
```
| 参数 | 含义 |
| --- | --- |
| `userName` | 对方的username |
| `content` | 文本消息内容 |

#### 为一个联系人设置备注
```java
void setAlias(String userName, String newAlias)
```
| 参数 | 含义 |
| --- | --- |
| `userName` | 需要设置备注的联系人的username |
| `newAlias` | 新备注 |

#### 登出
```java
void logout()
```

#### 创建一个新群
```java
void createChatRoom(String[] userNames, String topic)
```
| 参数 | 含义 |
| --- | --- |
| `userNames` | 需要加入该新群的联系人名单 |
| `topic` | 群名称 |

#### 邀请某个联系人加入群
```java
void addChatRoomMember(String chatRoomUserName, String userName)
```
| 参数 | 含义 |
| --- | --- |
| `chatRoomUserName` | 群的username |
| `userName` | 联系人的username |

#### 将一个成员踢出群（需要群主权限）
```java
void deleteChatRoomMember(String chatRoomUserName, String userName)
```
| 参数 | 含义 |
| --- | --- |
| `chatRoomUserName` | 群的username |
| `userName` | 被踢成员的username |

#### 下载聊天信息中的图片

请注意：最好使用这个方法下载图片，因为它在请求时会携带所需的cookie信息。

```java
byte[] downloadImage(String url)
```
| 参数 | 含义 |
| --- | --- |
| `url` | 图片URL |

| 返回值 |
| --- |
| 图片的二进制数据  ( type: `byte[]` )|

## FAQ
> Q: Jeeves使用了哪种微信协议？

A: Jeeves是使用Web微信的协议。

> Q: Jeeves和其他的微信机器人有何不同？

A: Jeeves的目标就是把自己伪装成一个普通的Web微信客户端，因此我们很重视细节。Jeeves除了会发送登录流程中必需的请求，还会发送其他的一些请求，例如：跨平台的状态同步请求，客户端状态上报请求等。Jeeves能模拟的细节越多，您的账号就越安全。当前，Jeeves已经能模拟但不限于以下几点：

* 登录流程中，首先请求[登录页面](https://wx.qq.com)。一个正常的客户端，一定是先请求登录页面，然后再用javascript开始请求服务器的，但是大多数微信机器人都没有这么做，他们直接跳到获取UUID的步骤了。

* Jeeves保存了绝大多数的cookie信息，甚至在请求图片的时候，也会携带cookie信息，尽可能地模拟浏览器的真实行为。 

* 在登录过程中，如果二维码过期了，jeeves会从头启动登录流程，获取新的二维码。在这个过程中，会加入一个`refreshTimes`的cookie信息。该信息标识了Web微信客户端已经第几次重新获取二维码。Jeeves的这个行为，就是**真实环境**中浏览器客户端的行为。

* Jeeves知道如何正确生成随机数和随机时间戳，因为我们研读过Web微信的javascript代码。

* 一个状态上报请求，如果带有`StatusNotifyCode.READED`，那这个请求是为了告诉手机移动端，该会话的所有消息都已读了。Jeeves会替您检查，并在必要的时候发出这个请求。每当您发一条消息的时候，jeeves都会检查，在这个会话中，是否之前有未读消息。如果有的话，它会在真正的聊天消息发出去之前，发送状态上报请求。这是正确且合理的。试想，在使用Web微信聊天的过程中，您是否可能在还没有读之前消息的状态下继续发送消息呢？

> Q: Jeeves能做什么？

A: Jeeves可以很好的将您的聊天记录按照您的要求存到本地磁盘。或者可以玩一个恶作剧：将别人已经撤回的消息重新发出来。发挥自己的想象吧！

> Q: Jeeves不能做什么?

A: Jeeves有些功能还在开发。例如上传图片等功能还没有完成。可以在[Usages](#usages)中查看有哪些功能已经完成了。

> Q: Jeeves能不断线吗?

A: 不能保证。通常来说，jeeves可以保证在线几个小时到两天。为了尽可能长地在线，请不要有一些不正常的行为，例如在1秒内发送100条消息。

> Q: 为什么我的微信账号被禁止登录了?

A: 有很多方面的原因。腾讯对您账号的使用行为和数据有各种统计。如果您有一些不正常的行为，就有可能被禁止登录。例如给一个不存在的人发送消息，或者给一个您根本不应该可以聊天的人（例如非好友）发送消息。 除此之外，腾讯对每个账户的各种日常行为设定一个上限次数。如果您对账号超过了这个次数，也有可能被认为是不正常使用，导致被禁。例如在短时间内登录次数太多。

## 已知问题
* Jeeves 使用 [ZXing](https://github.com/zxing/zxing) 来在终端输出二维码。在比较少的情况下，ZXing会抛出`com.google.zxing.NotFoundException`。该问题还在调查中，一般来说，重启jeeves，获得新的二维码就避开这个问题。 

## 警告
使用任何微信机器人，包括jeeves，都有可能导致您的账号被禁用。Jeeves不对此负责，请谨慎使用。

## 感谢
Jeeves 是受到 [ItChat](https://github.com/littlecodersh/ItChat) and [WeixinBot](https://github.com/Urinx/WeixinBot) 的启发而创建的。

## 问题反馈
有任何bug或者疑问，请使用 [Github Issues](https://github.com/kanjielu/jeeves/issues).

## 协议

[MIT](LICENSE)
