# Dodian 2020!
This is a new Dodian version based off of Professor Oak's Elvarg release. This has been heavily modified. We have added dependency injection to it among other things.


## How to setup and start developing

#### Setting up project in IntelliJ
1. Clone the repository by using this link: https://github.com/Nozemi/Dodian.git. This can be done through the **VCS -> Get from Version Control** in IntelliJ's top menu.
2. IntelliJ might spend a bit of time before it opens the project. But then gives you a little notice in the right hand corner asking "IntelliJ IDEA found a Gradle build script". Then you click **Import Gradle Project**. Then wait for it to finish, might take a few minutes. There should be a progressbar in the right hand corner of IntelliJ.

#### Running Server & Client
1. In the right side of IntelliJ, there should be a tab called **Gradle**. Expand that and find a tree structure, in that tree structure you should find: **dodian -> Tasks -> dodian-game**.
2. Inside **dodian-game**, you should find two tasks (runClient and runServer). Double click them to start said service.
3. Now you should be able to develop and test what you develop your local server/client instance. Good luck!

#### Testing with the appropriate player rights
When you run the server in dev mode. It will create some dummy data, among other things dummy users. Reference [DummyAccounts.java](./game-server/src/main/java/net/dodian/extend/plugins/dev/dummydata/DummyAccounts.java) for users.
You specify the usernames found there for password. Example if you want to be an admin, you pick whatever username you desire and **admin** for password.

#### How to write plugins?
Currently plugins are contained in their own package in this project. There are some dummy plugins that serves the purpose of showing that events are working and how to hook up to them and use them. Reference the plugins found in [TestPlugins](./game-server/src/main/java/net/dodian/extend/plugins/dev).
