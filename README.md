# FoodGreen

## Project Summary
On Facebook, there are several community groups where people connect with each other. They make a post on different topics which provide useful information. There are many people who want to sell their food dishes or start their catering service. So, they put an advertisement regarding their food item along with its price and description. The problem is that the buyer has to make comments if they are willing to buy the dish and has to pass through a tedious process of ordering food. To address this problem, we propose a user-driven centralized platform where people can buy and sell their homemade dishes at an affordable rate. The application differs from other food ordering platform which provides ready-made restaurant food. The target audience of our application will be immigrants and international students as they have fewer options for their homemade food. Moreover, this can be an ideal platform for those people who are diet and health conscious.  

## Libraries
[1] android-upload-service: This library is used to upload images to the server from the application. When the seller wants to add new order, the functionality to upload image from gallery is added using this library. We are storing image to the server and image name in the firebase. Source: [https://github.com/gotev/android-upload-service] 

[2] picasso: This library is used to show images from server in the application. Using the image name from firebase, the application fetches image from server and shows in activity using picasso. Source: [https://github.com/square/picasso] 

[3] firebase: This library is used to setup and process data with firebase. As the application needs to work with real-time data, we have used firebase to store the data. Source: [https://firebase.google.com/docs/android/setup] 

## Installation notes
To install this application, no extra files are needed to add. It is recommended to give all the permissions after installing the application to run the application smoothly. 

## Feature section
### buyer module
This is one of the main features where buyers can buy the dishes that every seller has posted. Additionally, every buyer has an option to place a new order of an item and find a potential seller for it.  While placing an order a buyer has to enter food category, dish name, description and other details regarding the food item.

### Seller module
Sellers can see the post of food dishes created by the buyer. Every seller has an option to make a bid of the dish. As a result, the buyer will have several options for food dishes with a different price. Additionally, the seller can provide a new dish using the new order option. The seller can submit food name, the category of the food, image and other order related details.

### Filter
Our application provides a filtered view using which a user can select specific options of the food dishes. The user can filter food dishes based on the food category and price. Thus, the user can see only specific dishes which fulfil the selected criteria. 

### Uploading image
This feature allows the seller to upload the image of the food dish. This image will be stored in the server and the path will be stored in firebase. The image is shown in the activity using Picasso library. 

### SMS Notification
Our application allows buyers to place an order based on their convenience. Once the order is confirmed, a notification about the order will be sent to the seller through SMS. The message will contain personal and contact information of buyer and seller. When the seller adds a bid to the order of buyer, an SMS notification is sent to the buyer about the bid.

### Database
Firebase is used to store real-time data generated from the application. It will also store account details of buyer and seller. Moreover, details of food dishes and orders placed will be fetched dynamically and displayed on the user screen.  

## Sources
[1]   "Android Studio Tutorial - Order Foods Part 1 ( SignUp , SignIn and Welcome Screen)", YouTube, 2019. [Online]. Available: https://www.youtube.com/watch?v=Ad41Bh704ms&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V. [Accessed: 16- Feb- 2019]. 

[2]   "Android Sending SMS", www.tutorialspoint.com, 2019. [Online]. Available: https://www.tutorialspoint.com/android/android_sending_sms.htm. [Accessed: 06- Mar- 2019]. 

[3]   "[Android Example] Pick Image from Gallery or Camera", AndroidClarified, 2019. [Online]. Available: https://androidclarified.com/pick-image-gallery-camera-android/. [Accessed: 11- Mar- 2019]. 

[4]     H. android? and F. Puffelen, "How to get child of child value from firebase in android?", Stack Overflow, 2019. [Online]. Available: https://stackoverflow.com/questions/43293935/how-to-get-child-of-child-value-from-firebase-in-android. [Accessed: 02- Mar- 2019]. 

[5]    "Read and Write Data on Android  |  Firebase Realtime Database  |  Firebase", Firebase, 2019. [Online]. Available: https://firebase.google.com/docs/database/android/read-and-write. [Accessed: 27- Feb- 2019]. 

[6]    B. Khan, "Android Upload Image using Android Upload Service", Simplified Coding, 2019. [Online]. Available: https://www.simplifiedcoding.net/android-upload-image-to-server/. [Accessed: 14- Mar- 2019]. 

[7]   "Listview image with text in android", YouTube, 2019. [Online]. Available: https://www.youtube.com/watch?v=_YF6ocdPaBg. [Accessed: 13- Feb- 2019]. 

[8]   GitHub. (2019). gotev/android-upload-service. [online] Available at: https://github.com/gotev/android-upload-service [Accessed 16 Mar. 2019]. 

[9]   H. [duplicate], F. Ahmed and A. Gosemath, "How to show image from server to my ImageView in Android", Stack Overflow, 2019. [Online]. Available: https://stackoverflow.com/questions/40503253/how-to-show-image-from-server-to-my-imageview-in-android. [Accessed: 17- Mar- 2019]. 

[10]   "square/picasso", GitHub, 2019. [Online]. Available: https://github.com/square/picasso. [Accessed: 17- Mar- 2019]. 

[11]   "Add Firebase to your Android project  |  Firebase", Firebase, 2019. [Online]. Available: https://firebase.google.com/docs/android/setup. [Accessed: 27- Feb- 2019]. 

[12]   A. bar et al., "Android - styling seek bar", Stack Overflow, 2019. [Online]. Available: https://stackoverflow.com/questions/16163215/android-styling-seek-bar. [Accessed: 02- Mar- 2019].

[13] A. Chugh, "Android Date Time Picker Dialog - JournalDev", JournalDev, 2019. [Online]. Available: https://www.journaldev.com/9976/android-date-time-picker-dialog. [Accessed: 12- Feb- 2019]. 

[14]   T. Dawson, "Vegan Spicy Ramen Noodles - Love is in my Tummy", Love is in my Tummy, 2019. [Online]. Available: https://loveisinmytummy.com/2016/11/vegan-spicy-ramen-noodles.html. [Accessed: 23- Mar- 2019]. 

[15]  "Steam Workshop :: arghhh", Steamcommunity.com, 2019. [Online]. Available: https://steamcommunity.com/sharedfiles/filedetails/?id=630725432. [Accessed: 18- Mar- 2019]. 
