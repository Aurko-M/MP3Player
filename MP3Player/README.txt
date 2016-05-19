== Zip File Contents

mp3audio.jar
	A library with classes that support:
	1. Playing MP3 files in the background
	   (that is, in a separate Java Thread).
	   Class AudioPlayer in package edu.rit.se.swen383.audio.
	2. Wrapping an MP3 file to allow querying for information
	   such as duration, artist, album, genre, etc.
	   Class AudioSource in package edu.rit.se.swen383.audio.
PlayList.java
	A Java class that supports creating MP3 play lists, including
	playing files, auto-advancing at the end of a file to the
	next entry in the list, retrieving information on an MP3 file,
	etc.
MP3Player.java
	A Java class that initializes a PlayList from the MP3 files
	passed as command line arguments, then executes keyboard commands
	to manipulate the playlist and play audio files.
*.mp3
	A set of MP3 files consisting of 15-20 second snippets from
	a variety of songs.
javadoc
	A directory with documentation for MP3Player and PlayList as well
	as the pre-compiled library classes AudioPlayer, AudioSource, and
	AudioSourceException.

== Preparation for compiling and running the player

For both compiling and running the program you must use a tailored
Java classpath:
	1. For Unix, Linux, MacOS the class path is
	   -cp .:mp3audio.jar
	2. For Windows the class path is
	   -cp .;mp3audio.jar
The assumption is that you'll run the program from the same
directory where the mp3audio.jar file is located.

The only difference in the class paths is whether colon (:) or
semicolon (;) is used as a separator, but using the correct form
is critical. In what follows I will assume you are running under
Windows.

== Compiling the program

Execute the command:
	javac -cp .;mp3audio.jar MP3Player.java PlayList.java
This will produce the following .class files:
	MP3Player.class
	PlayList.class

== Running the program

Execute the command:
	java -cp .;mp3audio.jar MP3Player *.mp3
This will create a PlayList from the MP3 file arguments.

To start playing from the first file, type 'p'.

Commands
========
+ = Play the file after the current one.
- = Play the file before the current one.
@ = Replay the current file.
h = Print this command list.
i [n] = Print information on file #'n'
        (or the current file if 'n' is omitted).
p [n] = Terminate any playback and start playing
        AudioSource #'n' (default 0).
P = Pause playback.
R = Resume playback.
t = Print the playback position in seconds.
s = Print number of playlist entries.
q = Quit the player.

MJL	Jan 2015
