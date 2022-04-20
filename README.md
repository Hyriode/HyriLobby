# 🧭 HyriLobby

📁 **Type :** Plugin

⚙ **Version :** 1.0.0

📘 **Cahier des charges :** [HyriLobby](https://docs.google.com/document/d/1sUgYGwAo-cxQhQ2ldSk_WwF3FmrTk6QfVvZCSQ9aQxE/edit?usp=sharing)

---

## 📚 API Usage

- To get the API instance, use the `get()` method.

````java
final LobbyAPI api = LobbyAPI.get();
````

- From this instance, you can get all the managers. To use them, simply follow the JavaDoc.

````java
final LobbyAPI api = LobbyAPI.get();
final Supplier<LobbyJumpManager> jump = api.getJumpManager();
final Supplier<LobbyPacketManager> packet = api.getPacketManager();
final Supplier<LobbyPlayerManager> player = api.getPlayerManager();
final Supplier<LobbyLeaderboardManager> leaderboard = api.getLeaderboardManager();
````
- Every manager has a `get()`, `save()`, `delete()`, `getAllKeys()` and `getAllKeysAsValues()` methods to interact with redis, inherited from [ILobbyDataManager](https://github.com/Hyriode/HyriLobby/blob/develop/API/src/main/java/fr/hyriode/lobby/api/redis/ILobbyDataManager.java).

- All packets are sent on PubSub through the channel "lobby" -> `LobbyPacketManager#CHANNEL`. You need to subscribe with HyriAPI to this channel to receive jumps and leaderboards related packets.

---

## 🔨 TODO

- [LeaderboardHandler](https://github.com/Hyriode/HyriLobby/blob/develop/src/main/java/fr/hyriode/lobby/leaderboard/LeaderboardHandler.java#L96)
- Flying island around lobby
- Finish custom gui, auto switch to classic gui when new game added on the network
- Games npc position handler with command, like jump