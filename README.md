# Pre-work - *CodePathTodo*

**CodePathTodo** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item. User can filter active, completed and all tasks

Submitted by: **Vortana Say**

Time spent: ** Approx. 14** hours spent in total

## User Stories

The following **required** functionality is completed:

* [ done] User can **successfully add and remove items** from the todo list
* [ done] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [ done] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [ done] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [ done] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [ done] Add support for completion due dates for todo items (and display within listview item)
* [ not prefer ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [ done] Add support for selecting the priority of each todo item (and display in listview item)
* [ done] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!
* Architecture android MVP
* User can filter active, completed and all tasks
* DialogFragment for todo detail

## Video Walk through

Here's a walk through of implemented user stories:

<img src='http://imgur.com/a/IZHi8' width="600" />

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:**
Impression so far: Building an android app should not be a problem, however, building an efficient and scalable Android app needs right architecture and technology choices.

I used to be a full stack web developer and here are the main similarities and contrasts I found:
- Similarities:
    - Architecture for Separate Concern
    - OOP
    - Best Practices
- Contrasts:
    - Android need emulator to run the app
    - Need to be careful with the resources (device has limited resources)
    - Layout needs to support different screens of devices

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:**
I used RecyclerView rather than ListView because RecyclerView enforce the ViewHolder pattern.
Adapter is a connector between UI components (layout) and the data of the list items.
ConvertView is used to improve the performance of the the adapter. It reuse the old view if possible.

## Notes

Credit to Google Architecture for Best practices. By understanding Android MVP is essential, once understand the flow then we should be able to quickly implement the features.

## License

    Copyright 2017 Vortana Say

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.