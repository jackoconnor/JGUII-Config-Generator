JGUII-Config-Generator
======================

A small program designed to be used with the JGUII library. It automates the process of creating 
[JGUII] (https://github.com/jackoconnor/JGUII) config files.

# Get it
Go to the [latest release] (https://github.com/jackoconnor/JGUII-Config-Generator/releases) and download the current jar file.

## How to use it
Execute the jar file and you will be greeted by two windows. 
In the foreground you should notice a small window like this: ![Foreground Window] (/Main-window-overview.png)

And in the background a translucent window.

Using the software is simple:

1. Open the first application you wish to use (for example, firefox).
2. Now while leaving firefox maximised, maximise the config generator (both windows).
3. Firefox should now be visible through the translucent window.
4. In the "Application Name" field type the name of the application, in this case, "firefox" (without the quotes). Note that this name will be used as the name of the binary that is executed when JGUII starts the application and the name that is used to search for the window by title when it is opened (the search is case insensitive, however, so "firefox" will work for "Mozilla Firefox").
5. Now click all of the points you wish to record on the translucent window. For example, the location of the search bar etc. (Note that if you only wish to use JGUII for opening, closing and focusing windows, you need not click on any points and may skip this step and the next one).
6. All the points you have clicked will have been recorded in a table in the smaller window. Fill in the name field for all the points with whatever name you desire. 
7. If you wish to add more applications to the config file, click "Next Application" and repeat from step 1.
8. When you have all the desired applications added, click "Finish" and save the config file wherever you wish.

###Get JGUII
If you haven't already, head over to [https://github.com/jackoconnor/JGUII] (https://github.com/jackoconnor/JGUII) to get
JGUII and learn how to use it!
