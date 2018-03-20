# jeeves

[![Build Status](https://travis-ci.org/kanjielu/jeeves.svg?branch=master)](https://travis-ci.org/kanjielu/jeeves)

A smart WeChat bot.

[中文文档](https://github.com/kanjielu/jeeves/blob/master/README.zh-CN.md)

## Getting Started

### Requirements
JDK 8

### How to run
```
mvn spring-boot:run
```

You should see the following logs.

![alt text](https://github.com/kanjielu/jeeves/blob/master/images/login1.png?raw=true "Await Scanning")

After scanning and confirming login

![alt text](https://github.com/kanjielu/jeeves/blob/master/images/login2.png?raw=true "Login Successfully")

### Login process
![alt text](https://github.com/kanjielu/jeeves/blob/master/images/login-process-diagram.png?raw=true "Login process diagram")

### Example
`com.cherry.jeeves.MessageHandlerImpl` is provided as an example of jeeves. You can modify the code in `MessageHandlerImpl` yourself or create another Spring Bean of `MessageHandler` to meet your requirements.

The default behaviors that are set in `MessageHandlerImpl` are:
* Auto-save thumb images in image messages to local disk.
* Auto-reply plain text messages.
* Accept all friend invitations.
* After accepting friend invitations, set alias to the friend.
* Log for all other events.

## Usages
### Events
A bean that implements `MessageHandler` will be notified on all the following events.

#### Text message
```java
void onReceivingPrivateTextMessage(Message message);
```

| Parameters | Meaning |
| --- | --- |
| `message` | received message |

#### Image message
```java
void onReceivingPrivateTextMessage(Message message, String thumbImageUrl, String fullImageUrl);
```

| Parameters | Meaning |
| --- | --- |
| `message` | received message |
|`thumbImageUrl`| url of image in small size|
|`fullImageUrl`| url of image in full size|

#### ChatRoom text message
```java
void onReceivingChatRoomTextMessage(Message message);
```

| Parameters | Meaning |
| --- | --- |
| `message` | received message |

#### ChatRoom image message
```java
void onReceivingChatRoomImageMessage(Message message, String thumbImageUrl, String fullImageUrl);
```

| Parameters | Meaning |
| --- | --- |
| `message` | received message |
|`thumbImageUrl`| url of image in small size|
|`fullImageUrl`| url of image in full size|

#### Received a friend invitation
```java
boolean onReceivingFriendInvitation(RecommendInfo info);
```

| Parameters | Meaning |
| --- | --- |
| `RecommendInfo` | friend invitation infomation |

| Returns|
| --- |
| true if to accept the invitation  ( type:`boolean` )|

#### Callback after accepting a friend invitation
```java
boolean postAcceptFriendInvitation(Message message);
```

| Parameters | Meaning |
| --- | --- |
| `message` | friend invitation message |

#### New chatrooms found
```java
void onNewChatRoomsFound(Set<Contact> chatRooms);
```

| Parameters | Meaning |
| --- | --- |
| `chatRooms` | a list of new chatrooms |

#### Chatrooms deleted
```java
void onChatRoomsDeleted(Set<Contact> chatRooms);
```

| Parameters | Meaning |
| --- | --- |
| `chatRooms` | a list of chatrooms that are deleted |

#### Members changed in a chatroom
```java
void onChatRoomMembersChanged(Contact chatRoom, Set<ChatRoomMember> membersJoined, Set<ChatRoomMember> membersLeft);
```

| Parameters | Meaning |
| --- | --- |
| `chatRoom` | the chatroom where members changed happened |
| `membersJoined` | a list of members that joined the chatroom |
| `membersLeft` | a list of members that left the chatroom |

#### New friends found
```java
void onNewFriendsFound(Set<Contact> contacts);
```

| Parameters | Meaning |
| --- | --- |
| `contacts` | a list of new friends |

#### Friends deleted
```java
void onFriendsDeleted(Set<Contact> contacts);
```

| Parameters | Meaning |
| --- | --- |
| `contacts` | a list of friends that are deleted |

#### New media platforms found
```java
void onNewMediaPlatformsFound(Set<Contact> mps);
```

| Parameters | Meaning |
| --- | --- |
| `mps` | a list of new media platforms |

#### Media platforms deleted
```java
void onMediaPlatformsDeleted(Set<Contact> mps);
```

| Parameters | Meaning |
| --- | --- |
| `mps` | a list of media platforms that are deleted |

#### Red packet received
```java
void onRedPacketReceived(Contact contact);
```

| Parameters | Meaning |
| --- | --- |
| `contact` | where the red packet is recevied |

### API
`WechatHttpService` has provided a bundle of apis that you can use to interact with the server.

#### Get all the contacts
```java
Set<Contact> getContact()
```
| Returns |
| --- |
| all the contacts, including friends, chatrooms and media platforms  ( type: `Set<Contact>` )|

#### Get all the members in given chatrooms
```java
Set<Contact> batchGetContact(Set<String> list)
```
| Parameters | Meaning |
| --- | --- |
| `list` | the list of usernames of chatrooms |

| Returns |
| --- |
| chatrooms populated with all the members ( type: `Set<Contact>` )|

#### Send plain text
```java
void sendText(String userName, String content)
```
| Parameters | Meaning |
| --- | --- |
| `userName` | the username of the contact that you send message to |
| `content` | the content of the message |

#### Set alias to given contact
```java
void setAlias(String userName, String newAlias)
```
| Parameters | Meaning |
| --- | --- |
| `userName` | the username of the contact that you set alias to |
| `newAlias` | the alias |

#### logout
```java
void logout()
```

#### Create a chatroom
```java
void createChatRoom(String[] userNames, String topic)
```
| Parameters | Meaning |
| --- | --- |
| `userNames` | the usernames of the contacts who are invited to the chatroom |
| `topic` | the topic(or nickname) |

#### Invite a contact to a certain chatroom
```java
void addChatRoomMember(String chatRoomUserName, String userName)
```
| Parameters | Meaning |
| --- | --- |
| `chatRoomUserName` | chatroom username |
| `userName` | contact username |

#### Delete a contact from a certain chatroom (if you're the owner!)
```java
void deleteChatRoomMember(String chatRoomUserName, String userName)
```
| Parameters | Meaning |
| --- | --- |
| `chatRoomUserName` | chatroom username |
| `userName` | member username |

#### Download images in the conversation

Note that it's better not to download image directly. This method has included cookies in the request.

```java
byte[] downloadImage(String url)
```
| Parameters | Meaning |
| --- | --- |
| `url` | the url of the image |

| Returns |
| --- |
| the data of the image  ( type: `byte[]` )|

## FAQ
> Q: What protocols is jeeves running on?

A: Jeeves is running on WeChat web protocols.

> Q: How is jeeves different from other WeChat bots?

A: Jeeves is aimed to disguise itself as a normal web WeChat app. So we value **details**. Jeeves not only submits requests which are essential to login process, but also submits those are used for cross-platform status synchronization, status report and so on. The more details jeeves imitate, the safer your account is. Jeeves provides the following imitations.
* Jeeves starts login process with requesting the login page (default as https://wx.qq.com) while most other bots skip directly to getting uuid.
* Jeeves stores all the cookies carefully. It evens brings cookies in a request for getting images, which makes the request look like it's from a real browser. 
* During the login process, when the QR code is expired, jeeves will start over the whole login process to get a new QR code. But in the following requests, a `refreshTimes` cookie is inserted, which indicates how many times that jeeves has started over. This is the way a **real** web WeChat app works.
* Jeeves knows how to generate a random code/timestamp just as web WeChat do. We've studied some javascript code of web WeChat.
* A `statusNotify` request with `StatusNotifyCode.READED` is used to notify the mobile WeChat app that all the messages in a given conversation have been read. Jeeves takes care of it for you. When you send a plain text message to a contact, jeeves would check if there're any unread messages in the conversation between you and the contact. If true, jeeves will first send out `statusNotify` to mark all these message read prior to the message request, which makes sense in a real world case.

> Q: What can I do using jeeves?

A: Jeeves is a perfect tool if you'd like to store all the messages locally. As a mischief, you can send the messages that others have recalled back to chatroom. Use your imagination!

> Q: What can't jeeves do?

A: Jeeves is still in masterelopment. Some complicated features such as sending an image is still in the todo list. Find all the available events and apis in the [Usages](#usages).

> Q: Can jeeves prevent itself from disconnecting from server?

A: Jeeves can't guarantee it. We're still working on it. Usually jeeves can stay for hours, up to 2 days. To stay connected as long as possible, **DON'T** have any unusual behaviors that real humans don't have. For example, sending 100 messages in one second.

> Q: Why is my account blocked on web WeChat?

A: It depends on lots of factors. Tencent has statistics of all the behaviors and data of your account. Some unusual behaviors would put your account in risk. For example, sending messages to a person doesn't exist or you're not allowed to chat with. Additionally, Tencent has a list of the limits on all kinds of actions that an account can take. If your account exceeds the boundary, it could be blocked. For example, too many times of login in a short time.

## Known Issues
* Jeeves is using [ZXing](https://github.com/zxing/zxing) for printing QR in terminal. It's seldom that `com.google.zxing.NotFoundException` is thrown. We're still investigating into it. To workaround it, just restart jeeves.

## Warning
Using any WeChat bots, including jeeves, could cause your account be blocked. It's at your own risk.

## Credits
Jeeves project is inspired by [ItChat](https://github.com/littlecodersh/ItChat) and [WeixinBot](https://github.com/Urinx/WeixinBot).

## Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/kanjielu/jeeves/issues).

## License

[MIT](LICENSE)
