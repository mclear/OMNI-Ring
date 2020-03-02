# NFC Ring OMNI - Developer Edition Ring


**Read the Wiki to Get Started**

https://github.com/mclear/OMNI-Ring/wiki


# Introduction

This tutorial is aimed at developers who are familiar with Java and it might not be suitable for total beginners.

NFC Ring will not be held responsible if you brick your device by using bad commands or wrong keys. You have been warned!

This tutorial shows you how to:

1. set up JavaCard toolchain on Ubuntu
2. build example applet package
3. load example applet on an NFC Developer Edition ring



**Required skills:**

- Java
- Command line (PS/bash)
- git



**Required equipment:**

* Computer with Windows 10 or Ubuntu 18.04.2 LTS.
* PCSC compatible NFC reader.
  * For creating this guide we used ACS ACR 122U.
* A Developer Edition ring from NFCRing. If you don't have it, you can order from nfcring.com.



# Setting up JavaCard toolchain

## Build tools

### Ubuntu

We are going to setup the toolchain on Ubuntu 18.04.2 LTS. This guide should also work on earlier versions of Ubuntu.

First of all, we need to install couple of prerequisites:

`$ sudo apt install git ant openjdk-8-jdk`

- git – for downloading code from GitHub repository
- ant –  it’s like a make but for building Java projects
- openjdk-8-jdk - java development kit, needed for compiling Java code



### Windows

Prerequisites:

- JDK 8 by Oracle or OpenJDK
  - AdoptOpenJDK - <https://adoptopenjdk.net/index.html>
- Ant - <https://ant.apache.org/bindownload.cgi> (download apache-ant-xxxxxx-bin.zip)
  - Ant comes without an installer. You have to extract the zip file.
- Git - https://gitforwindows.org/.

Setup Windows environment variables:

- JAVA_HOME - pointing to a folder where JDK is installed, (example: C:\Program Files\AdoptOpenJDK\jdk-8.0.212.03-hotspot\)
- ANT_HOME - point to folder where Ant is extracted to (example: C:\Users\LM\Downloads\apache-ant-1.10.5-bin\apache-ant-1.10.5/)
- Add ant/bin folder to path

**Note: After changing environment variables, terminal needs to be restarted for changes to take effect.**

Open up a PowerShell terminal and run ant command.

You should see a response like:

```
PS C:\Users\LM> ant
Buildfile: build.xml does not exist!
Build failed
PS C:\Users\LM>
```

Great, now we have verified that ant is working from command line.



## Applet loading utility

To load an applet on a ring we will use a GlobalPlatformPro (written by Martian Paljak) utility. This is open source software and freely available on GitHub. GPPro can be used to load and delete applets, change keys, change CPLC, lock cards and many more. More details about GlobalPlatformPro can be found at <https://github.com/martinpaljak/GlobalPlatformPro/>



To download the utility, go to https://github.com/martinpaljak/GlobalPlatformPro/releases/latest. Based on  your operating system download one of the files:

* Ubuntu - gp.jar
* Windows - gp.exe

Move downloaded file to your project root directory.



On Ubuntu OS, the utility can be executed the following way (in Terminal):

```bash
$ java -jar gp.jar --list
```

On Windows (PowerShell):

```powershell
> ./gp.exe --list
```

**Note: For consistency throughout the tutorial, all GlobalPlatformPro commands will be written in Linux style. For running the commands on Windows please replace 'java -jar gp.jar' with './gp.exe'.**



# Building example project

Before we can start building, we need to clone a example project repository:

- Windows - use Git Bash (Start → Git → Git Bash)
- Ubuntu - use normal terminal

```
$ git clone --recurse-submodules git@github.com:mclear/OMNI-Ring.git
```

This repository is using a [oracle_javacard_sdks](https://github.com/martinpaljak/oracle_javacard_sdks) git submodule created by Martin Paljak. It’s all the Javacard SDK versions packaged into single repository to ease the toolchain setup. That's why we use --recurse-submodules flag to pull submodule code as well as our example project.



Once git finished pulling all the code from the GitHub repository, you should end up with following project structure:

- /src/ - source files
  - MyApplet.java - example applet source code
- /sdks/ - oracle_javacard_sdks submodule
  - multiple folders using the name format of jcXXX_kit, where XXX’s are the Javacard versions
- /build.xml - ANT build configuration file



There is one more utility we need to download before we can successfully build our example project.

Download ant-javacard.jar from [ant-javacard repository release page](https://github.com/martinpaljak/ant-javacard/releases/latest) and put it in the project root folder (where the build.xml is).

Now we can try to build the project. To do so, open a terminal and navigate to the folder where build.xml is located and run the following command:

```
$ ant build
```

The `ant` should generate the similar output:

```
lm@lm-VirtualBox:~/dev-ring-getting-started$ ant build
Buildfile: /home/lm/dev-ring-getting-started/build.xml

build:
      [cap] INFO: using JavaCard 3.0.1 SDK in sdks/jc303_kit
      [cap] INFO: Setting package name to ExampleProject
      [cap] Building CAP with 1 applet from package ExampleProject (AID: 0102030405)
      [cap] ExampleProject.MyApplet 0102030405060708
  [compile] Compiling files from /home/lm/dev-ring-getting-started/src
  [compile] Compiling 1 source file to /tmp/jccpro9900755542760480669
  [convert] [ INFO: ] Converter [v3.0.3]
  [convert] [ INFO: ]     Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved.
  [convert]     
  [convert]     
  [convert] [ INFO: ] conversion completed with 0 errors and 0 warnings.
 [javacard] NB! Please use JavaCard SDK 3.0.5u3 or later for verifying!
   [verify] Verification passed
      [cap] CAP saved to /home/lm/dev-ring-getting-started/MyApplet.cap

BUILD SUCCESSFUL
Total time: 1 second
```

To double-confirm successful build, check that the MyApplet.cap exists in project root folder.



# Loading applet on a ring

## Deleting factory loaded applet

Our rings come preloaded with a simple NDEF applet containing a website link to this tutorial. Before we can load our custom applet on, we need to remove it.

To remove the applet, run the following command:

```
$ java -jar gp.jar --delete D276000085
```

To verify that the applet has been deleted and/or there are no other applets on, use the following command:

```
$ java -jar gp.jar --list
```



The list output for a ring with applet(s) will look like this:

List output for **factory loaded ring:**

```
Warning: no keys given, using default test key 404142434445464748494A4B4C4D4E4F
ISD: A000000151000000 (OP_READY)
     Privs:   SecurityDomain, CardLock, CardTerminate, CardReset, CVMManagement, TrustedPath, AuthorizedManagement, TokenVerification, GlobalDelete, GlobalLock, GlobalRegistry, FinalApplication, ReceiptGeneration

APP: D2760000850101 (SELECTABLE)
     Privs:

PKG: D276000085 (LOADED)
     Applet:  D2760000850101
```



List output **after deleting factory applet:**

```
Warning: no keys given, using default test key 404142434445464748494A4B4C4D4E4F
ISD: A000000151000000 (OP_READY)
     Privs:   SecurityDomain, CardLock, CardTerminate, CardReset, CVMManagement, TrustedPath, AuthorizedManagement, TokenVerification, GlobalDelete, GlobalLock, GlobalRegistry, FinalApplication, ReceiptGeneration
```



## Loading new applet on a ring

To load recently built applet on a ring, use following command:

```
$ java -jar gp.jar --install MyApplet.cap --default
```

Congratulations! You have just built a simple applet and loaded it on a ring.



To validate, we can send an APDU to the ring using GlobalPlatformPro.

```
$ java -jar gp.jar --apdu 0000000000 -d
```

This produces output:

```
$ java -jar gp.jar --apdu 0000000000 -d
GlobalPlatformPro 19.01.22-0-gf94d7f5
# Detected readers from JNA2PCSC
[*] ACS ACR122U PICC Interface 0
SCardConnect("ACS ACR122U PICC Interface 0", T=*) -> T=1, 3B8C80015040CE595E00000011778183B0
SCardBeginTransaction("ACS ACR122U PICC Interface 0")
A>> T=1 (4+0000) 00000000 00
A<< (0012+2) (15ms) 48656C6C6F20576F726C6421 9000
A>> T=1 (4+0000) 00A40400 00
A<< (0097+2) (85ms) 6F5F8408A000000151000000A553734906072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6B03640B06092A864886FC6B040215650B06092A864886FC6B020101660C060A2B060104012A026E01039F6E01019F6501FE 9000
[TRACE] GlobalPlatform -  [6F]
[TRACE] GlobalPlatform -      [84] A000000151000000
[TRACE] GlobalPlatform -      [A5]
[TRACE] GlobalPlatform -          [73]
[TRACE] GlobalPlatform -              [06] 2A864886FC6B01
[TRACE] GlobalPlatform -              [60]
[TRACE] GlobalPlatform -                  [06] 2A864886FC6B020202
[TRACE] GlobalPlatform -              [63]
[TRACE] GlobalPlatform -                  [06] 2A864886FC6B03
[TRACE] GlobalPlatform -              [64]
[TRACE] GlobalPlatform -                  [06] 2A864886FC6B040215
[TRACE] GlobalPlatform -              [65]
[TRACE] GlobalPlatform -                  [06] 2A864886FC6B020101
[TRACE] GlobalPlatform -              [66]
[TRACE] GlobalPlatform -                  [06] 2B060104012A026E0103
[TRACE] GlobalPlatform -          [9F6E] 01
[TRACE] GlobalPlatform -          [9F65] FE
[DEBUG] GlobalPlatform - Auto-detected ISD: A000000151000000
SCardEndTransaction(ACS ACR122U PICC Interface 0)
SCardDisconnect("ACS ACR122U PICC Interface 0", true)
```

Lines 8-9 are the ones we are interested in.

- Line 8 - `A>> T=1 (4+0000) 00000000 00` - we can see the 0000000000 APDU is being transmitted
- Line 9 - `A<< (0012+2) (15ms) 48656C6C6F20576F726C6421 9000`- response from the ring. Putting “48656C6C6F20576F726C6421” through hex-to-ascii converter, we get: “Hello World!”

**Note: If you are getting 6D 00 or 6E 00 error response then try removing a ring for few seconds and put it back on the reader.**



This concludes the quick start tutorial on how to set up basic toolchain, build an applet from source and how to load it on a ring.

Check out the other tutorial section below to find other tutorials that can come handy in developing the applet.



# Other guides/tutorials/interesting links

- JavaCard tutorials - old but still relevant - <http://javacard.vetilles.com/tutorial/>
- OpenPGP - <https://github.com/mclear/OMNI-Ring/wiki/Guide---OpenPGP>
- Curated list of applets - <https://github.com/EnigmaBridge/javacard-curated-list>

# Development API targets

* JavaCard 3.0.1
* GlobalPlatform 2.2.1
