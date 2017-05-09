# cs0320 Term Project

**Team Members:** Michael Colonna, Tynan Couture-Rashid, 	Tymani Ratchford, Brendan Woo

**Project Idea:** Soundpaint

**Mentor TA:** Nina Polshakova - nina_polshakova@brown.edu

## Project Requirements
Table of Contents

  - Abstract
  - What the Survey Data Tells Us
  - User Experience
    - User needs and goals
    - User actions in service of goals
	- Competitor Analysis
	- Limitations
  - Acceptance Criteria


### Abstract
Our proposal is an application called Soundpaint. Soundpaint is a platform that allows users to put a filter on their videos comparable to how Snapchat or Instagram functions except our filters are based on the sound of the video. We want to filter uploaded videos and allow users to edit and download the filtered video.

### 1. What the Survey Data Tells Us
Our team put together a Google Form in order to understand how Brown students use image and video filters in their daily lives, if an audio-dictated visual generator would be interesting, and if so, how best to integrate that generator into their daily social media experiences.

92.3% of respondents said that they share video/images/sound content online. When asked what they use image filtering for, 50% replied "Sharing content on social media", as opposed to "Creating art pieces" and "Work". When asked what network they share this content on, 44.4% of those respondents replied Instagram and 29.6% replied Facebook, while only 7.4% replied Youtube and 3.7% replied Vimeo. This leads us to believe that the general student body enjoys sharing their filtered visual content more with each other than across a wider, more public network.

When asked how much effort they put into filtering their images/video on a scale of 1-5 (5 = most effort), 61.5% reported an effort level of "2". Moreover, when asked how long they spent editing their last Instagram post, 76.9% replied "Under 5 minutes". This tells us that when filtering their visual content, students prefer simple, accessible, fast controls with quick response times over a meticulous modification process.

However, when asked what program or application users edit visuals with, 28.5% - the largest percentage - replied "Photoshop". Combined with the previous conclusion, this leads us to believe that users would like access to in-depth controls if they want to minutely tweak their filtering, but that the ability to use these controls competently should not be necessary to have a fulfilling experience.

### 2. User Experience
The following is a description of user needs as they approach our platform, as well as details on how our program will respond to these requirements.

#### 2.1 User needs and goals

The users of Soundpaint need a platform to modify a video stream with a given audio stream. Their technological skills and goals may vary widely, and the platform should provide preset filter options, as well as options to adjust. In essence, the users needs revolve around creating visually interesting content, and the platform should provide the opportunity to do that.

The users also need a web-based platform with the choice to make both public and private content. These attributes are major components of many successful services (Github, Facebook). In order to meet this requirement, the platform will be deployed to the web, and allow users to save, edit, and share content they own.

#### 2.2 User actions in service of those goals
The user flow for the application will be as follows:
  - Create their own media
    1. Upload a video file
    2. Upload an optional audio file
    3. Alter parameters, change sound files
    4. Preview their changes
    5. Save their changes
    6. Share their content in app
  - Watch other people's media
    1. View source
	  2. Alter parameters
	  3. Preview changes
    4. Save new version

### 3. Competitor Analysis

In terms of simple video filters, there is considerable competition among mainstream applications, ranging from the preset filters of Snapchat to the precise control of color correction software like Adobe SpeedGrade. There are many applications that allow one to tweak the visuals of a video. Despite these other tools, there is no clear option for users that want to modify video according to audio information. Currently, users must learn to edit video and use complex programs like Adobe Premiere. SoundPaint allows users to easily create lively and exciting sonic/visual relationships in their videos.

### 4. Limitations

There are several possible limitations to be considered. After a visual filter has been generated and applied to a user's video, there is the computational cost of rendering the final, compressed video. In addition, a database of a given user's videos should be stored and remain easily accessible. Because of video rendering and data storage needs, we may need to run our platform on specialized hardware (GPU processing may be an option).

Copyright may also be a limitation, if users seek to upload content they do not own.

### 5. Acceptance Criteria

To receive a passing grade for our project we specify the following:
  - A way for users to upload a video.
  - A way for users to view the filtered video in app
    - The filter should modify a copy of the original video and modify properties of that video stream (distortion, noise, hue, saturation etc.) in response to properties of the audio stream (frequency, volume).
    - user interface with a reasonable number of options and presets (at least 5 of each).

To receive an A for our project we specify implementing all 3 of the following:
  - A way for users to share their content in application with other users
  - A way for users to make their content private
  - A way to view filtering parameters about properties in the audio or video streams and change them

And either of the following:
  - A way to preview content while editing it
  - A way to add a mp3, mp4, mov or wav file

## Project Specs and Mockup
Link to specifications document: https://docs.google.com/a/brown.edu/document/d/1lnFEAuJiW8MjU71Sb_Z8oBYdg1og1QFHBP-ArPOF-t8/edit?usp=sharing

Link to mockup: https://drive.google.com/a/brown.edu/file/d/0B_pACDIkRt1KNEN4ejE0c05Eb1k/view?usp=sharing

## Project Design Presentation
Link to slides: https://docs.google.com/presentation/d/1hZ_CHeDIkWk6YHuNqeIFijCxhqpxoGB6dlJXI7woM54/edit#slide=id.p

Link to design document: https://docs.google.com/document/d/1hOBbtX_IHccqkQDJ4cta6Wf1UuBJqWfdkLJO7WXDAE4/edit

## How to Build and Run
'mvn package' will build the latest version of the source. './run --gui [--port XXXX]'  will run the frontend on port 4567 by default. 

'db [DATABASE PATH]' will set a database from which to run the frontend. A nonexistent database path will result in a newly created database that follows the schema.

Go to localhost:4567/ to begin using the application.

## End User Documentation
https://docs.google.com/document/d/1RhxHhSGVLdinzQACggG_XZppQL-_K3X_90fYgzLX1TM/edit?usp=sharing

## Acknowledgements

Two classes WavFile and WavFileException are by an A.Greensted, with a provided link http://www.labbookpages.co.uk. These files were found open sourced online.

The library at https://github.com/ajmas/JH-Labs-Java-Image-Filters was helpful for certain image filters.

The code for Emboss and Bulge filters are sourced from StackOverflow.



