# LinkPreviewEditText

[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![MinSDK](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![Build Status](https://travis-ci.org/skymansandy/linkPreviewEditText.svg?branch=master)](https://travis-ci.org/skymansandy/linkPreviewEditText)

## A simple EditText library (built with AppCompatEditText and JSoup) where you can get preview of links typed in the EditText (For example, like in WhatsApp)


## Features:

 - Simple to use
 - Just type whatever you feel like, and if there's a link in it, A preview of the link is given to you with a LinkInfo object. As simple as that 
 - Extended from AppCompatEditText, So normal theming still holds.
 - Great callbacks to know when certain preview related events occur. For example, Link detected while typing, Preview fetched, No Preview
 - Methods to close the preview manually
 
 
# Demonstration
|Demo linkPreviewEditText|
|:---:|
|![](art/demoLinkPreviewEditText.gif)|

Have a look at the example app to see how a preview could have been shown like WhatsApp.

 
# Usage
## Dependency:

 
 ```
 dependencies {
      implementation 'in.codeshuffle.linkpreviewedittext:LinkPreviewEditText:1.0.0'
 }
 ```
 
 ## XML Usage
 ```xml
   <in.codeshuffle.linkpreviewedittext.LinkPreviewEditText
              android:id="@+id/linkPreviewEditText"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              android:background="@null"
              android:hint="@string/write_something"
              android:padding="10dp" />
              
              <!--You can do normal theming to this view-->
 ```
 
 ## Java Usage
 ```java
        //Get view reference
        LinkPreviewEditText linkPreviewEditText = findViewById(R.id.linkPreviewEditText);
        
        //Set whether to detect links automatically as you type the content. Turn it off to make it behave like a normal EditText.
        linkPreviewEditText.detectLinksWhileTyping(true);
        
        //Method to close the preview (if a preview open at the time you call this, you'll get a callback onNoLinkPreview. You can do your UI changes here)
        linkPreviewEditText.closePreview();
        
 ``` 
 
 ### Listeners available
          
Implement the given interface and override these stuff:

```java

          //Implement this to your class
          yourClass extends someBaseClass implements LinkPreviewListener
          
          //Set the listener
          linkPreviewEditText.setLinkPreviewListener(this);
            
          //You'll have these callback methods.
          //On getting a link preview of automatically detected url in the EditText, You'll get this callback with the LinkInfo object
          //LinkInfo explained later below this file
          void onLinkPreviewOpen(LinkInfo linkInfo);
            
          //You'll get this callback whenever an existing preview is closed for either manually closing it, or closed because of some internal purpose
          void onNoLinkPreview();
          
          //A link was found when content was being typed
          void onLinkFound(String url);
            
          //In the rarest of the events when there is something wrong, you'll get this callback with the error message 
          void onLinkPreviewError(String errorMsg);
  ``` 
  
 ## LinkInfo
 ```java
 
          //You have the following method for the linkInfo object
          getUrl() //Get the origianlly typed url
          getDomainUrl() //domain url
          getTitle() //page title
          getDescription() //short description of the page
          getImageUrl() //page image url
          getSiteName() //Page name
          getMediaType() //page type 
          getFaviconUrl() //favicon url
   ``` 
 
 ## Important
 ```xml

    <!--You need Internet permission for the link preview to work. Add it in Manifest.-->
    <uses-permission android:name="android.permission.INTERNET"/>
 
 ```
 
 # Credits
 - JSoup
 
 License
 -------
 
     Copyright 2019 SkyManSandy
 
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0
 
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.


## Support on Beerpay
Hey dude! Help me out for a couple of :beers:!

[![Beerpay](https://beerpay.io/skymansandy/linkPreviewEditText/badge.svg?style=beer-square)](https://beerpay.io/skymansandy/linkPreviewEditText)  [![Beerpay](https://beerpay.io/skymansandy/linkPreviewEditText/make-wish.svg?style=flat-square)](https://beerpay.io/skymansandy/linkPreviewEditText?focus=wish)