Fingon beta README

------------------------------------------------------------------------------
 Content
------------------------------------------------------------------------------

 - Overview
 - System requirements
 - Installation
 - Alternate JSAPI implementations

------------------------------------------------------------------------------
 Overview
------------------------------------------------------------------------------

Fingon is an auxiliary look and feel for the Java Swing applications. 
It plugs an auditory user interface to your application using speech synthesis and sounds. 
It adds some assistive features to the user interface for the visually impaired people:

* button: says the label or emits a sound once the button is actioned.
* toggle button / radio button / checkbox: emits a different sound when the button is selected or unselected.
* menu item: says the label of the menu item when the mouse hovers it. Plays a sound if actioned.
* list: says the label of the selected item.
* tree: says the label of the selected node. Emits a different sound when a node is expanded or collapsed. Says a node was added or removed from a parent node.
* progress bar: plays a music when a progress bar is running.
* textfield, textarea, editor pane, text pane: says the word just typed or the part of text selected.
* password field: emits a sound each time the user types a character.
* tooltip: says the text of the displayed tooltips.
* message dialog: says the message of the dialog and emits a sound according to the message type (error, warning, information).
* slider: if the ticks are displayed, emits a sound whose pitch and volume are linked to the value selected. The small values emit low pitch sounds, the big values emit high pitch sounds. Major ticks use maximum volume, minor ticks use medium ticks. If the labels are displayed, says the selected label instead.
* table: says the label of the selected cell, or the first cell of the selected row. Warns the user that a column has been moved, added or removed from the table.
* spinner: says the typed or selected value in the spinner.
* progress bar: emits a series of sounds when the bar is dragged. Their pitch is proportional to the position in the progress bar: sounds emited to the left/top of the progress bar are low, the ones emited to the right/bottom of the progress bar are high.

------------------------------------------------------------------------------
 System requirements
------------------------------------------------------------------------------

Fingon requires a Java Runtime Environment (JRE) 1.4 or higher.

------------------------------------------------------------------------------
 Installation
------------------------------------------------------------------------------

add the following line in your code before instantiating any Swing component:

	UIManager.addAuxiliaryLookAndFeel(new FingonLookAndFeel());

add the following jar files in your classpath:
	
	The auxiliary look and feel itself:
	 - fingon.jar
	 - log4j-1.2.4.jar
	Java Sound implementation:
	 - tritonus_share-0.3.6.jar
		MP3 support:
		 - jl1.0.1.jar
		 - mp3spi1.9.4.jar
		OGG Vorbis support (optional):
		 - tritonus_jorbis-0.3.6.jar
		 - jogg-0.0.7.jar
		 - jorbis-0.0.15.jar
	Java Speech API (JSAPI):
	 - jsapi.jar
		JSAPI implementation FreeTTS (US voice):
		 - cmu_us_kal.jar
		 - cmudict04.jar
		 - cmulex.jar
		 - en_us.jar
		 - freetts-jsapi10.jar
		 - freetts.jar

copy speech.properties in the user's home directory (ex: C:\Documents and Settings\Paul-Emile)

copy soundbank.gm either in the home directory of your application, or in <JRE_HOME>/lib/audio

------------------------------------------------------------------------------
 Alternate JSAPI implementations
------------------------------------------------------------------------------

Fingon is delivered with FreeTTS, a JSAPI implementation entirely written in Java.
It is an open source project providing one low quality voice in the US language.

If you are no satisfied with FreeTTS, you can use an alternate implementation. 
TalkingJava from CloudGarden (http://www.cloudgarden.com/JSAPI/index.html) 
is an implementation providing a bridge between JSAPI and the Speech API (SAPI) of Microsoft.
It allows you to use any SAPI 4 or 5 synthesizers in your Java program. 
As a consequence, it will only work under Windows.
This is a proprietary product, free for non-commercial use only.

To switch to TalkingJava, follow these steps:

	- remove all FreeTTS jars from your classpath
	- download TalkingJava zip file from http://cloudgarden1.com/TalkingJavaSDK-170.zip
	- extract and add cgjsapi170.dll in the library path of your application (ex: lib\windows\x86\)
	- extract and add cgjsapi.jar in the extension path of your application (ex: lib\ext\)
	- start your application with the following VM arguments:
		java -cp lib\fingon.jar;... -Djava.ext.dirs=lib\ext -Djava.library.path=lib\windows\x86 mypackage.MyMainClass 

Others JSAPI implementations exist but they have not been tested with Fingon.
