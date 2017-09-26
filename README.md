# jeeves

[![Build Status](https://travis-ci.org/kanjielu/jeeves.svg?branch=master)](https://travis-ci.org/kanjielu/jeeves)
[![Coverage Status](https://coveralls.io/repos/github/kanjielu/jeeves/badge.svg?branch=master)](https://coveralls.io/github/kanjielu/jeeves?branch=master)

A smart wechat bot.

## Getting Started

### Requirements
JDK 8

### How to run
```
mvn spring-boot:run
```

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
|`thumbImageUrl`| image in small size|
|`fullImageUrl`| image in full size|

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
|`thumbImageUrl`| image in small size|
|`fullImageUrl`| image in full size|

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

### API
`WechatHttpService` has provided a bundle of apis that you can use to interact with the server.

####
```java
Set<Contact> getContact()
```
| Returns |
| --- |
| get all the contacts, including friends, chatrooms and media platforms  ( type: `Set<Contact>` )|

```java
Set<Contact> batchGetContact(Set<String> list)
```
| Parameters | Meaning |
| --- | --- |
| `list` | a |

| Returns |
| --- |
| get all the members in given chatrooms  ( type: `Set<Contact>` )|

## Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/kanjielu/jeeves/issues).
