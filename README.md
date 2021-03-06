# SummerProjectCC

## Main Project
### Texus Holden in Java
![Poker Game](https://user-images.githubusercontent.com/71058334/127112991-8888b7e8-fe70-4306-a5b5-9a45a15edc8a.PNG)

### Tetris
![Tetris](https://user-images.githubusercontent.com/71058334/127113068-01101eea-00e2-46b9-b0e5-e5fef99c141b.PNG)


We can learn how to use Git and Github here.
Git and Github are very useful to administrate files but it's not easy to get used to using them.
Actually, we don't need to use them at this point.

## Requirement
* IDE IntelliJ (https://www.jetbrains.com/ja-jp/idea/download/)
* Github
* Java (https://www.oracle.com/java/technologies/javase-jdk16-downloads.html)


## Installation
For mac users, Git is preinstalled in Terminal. But for windows users, you need to need install git in order to use git commands to share your work.

Download installer from below link.
https://git-scm.com/downloads



Execute installer and install git to your computer.

## Important!! Set PATH environment
We want to execute git command from command prompt so set path environment in your computer. Select "Use Git from the Windows Command Prompt" from below choices.

![image](https://user-images.githubusercontent.com/71058334/116184288-6ea2f280-a75a-11eb-813f-1520f797e616.png)


## How to download project to your computer
First, move to the folder you want to download project folder into with Terminal(macOS) or Command Prompt(windows)
Next, type following texts and push enter.

```
git clone https://github.com/fes7713/SummerProjectCC
```
And move to SummerProject folder.
```
cd SummerProjectCC
```
if git commands fail at this point, you may need to reinstall your git.

## How to set configuration
set your username. (You may exclude quotations)
```
git config --global user.name "First-name Family-name"
```
set your email address. (You may exclude quotations)
```
git config --global user.email "username@example.com"
```

## How to upload your edition to Github
Now you can edit files in Simple_Game. And when you made something for example 'loop', you should summarize the edited files with the following text.
```
git add -A
```

Then, add your summarized files to your repository (this is called commit) wiht the following text.
```
git commit -m 'make a loop'
```

To share your edition, you should find your remote repository (it's here).
```
git remote add origin https://github.com/fes7713/SummerProjectCC.git
```

If you registered a wrong URL, register a correct URL with the following text.
```
git remote set-url origin https://github.com/fes7713/SummerProjectCC.git
```

# important
To get the newest files from remote repository, type the following text.
```
git pull origin main
```

Now you can share your edition by uploading your commit to Github with 'push'.
```
git push origin main
```
if you get rejected at this stage, other contributers may changed folder before your upload so please pull project before pushing and update your project to the latest one
and upload your progress by pushing.
