# MVVM Light Toolkit
---
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android--MVVMLight-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/3737)

A toolkit help to build Android MVVM Application，We have more
attributes for Data Binding  of View(like Uri for ImageView) ,we create some command for deal with event( like click of Button),also have a global message pipe to communicate with other ViewModel.
##Download##

```groovy
 compile 'com.kelin.mvvmlight:library:1.0.0'
```
 
requires at least android gradle plugin 1.5.0.

##Usage##
---
####中文文档：[MVVM Light Toolkit使用指南](http://www.jianshu.com/p/43ea7a531700)####
####引申阅读：[如何构建Android MVVM应用程序](http://www.jianshu.com/p/2fc41a310f79)####

###Data Binding###


Binding URI to the ImageView with bind:uri will make it loading bitmap from URI and render to ImageView automatically.
   ```xml
   <ImageView
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     android:layout_alignParentRight="true"
     bind:uri="@{viewModel.imageUrl}"
     bind:placeholderImageRes="@{R.drawable.ic_launcher}"/>
   ```
   Fresco.initialize(this) is require,because of loading image use Fresco default).
   ```java
     public class MyApp extends Application {
       @Override
       public void onCreate() {
           super.onCreate();
           Fresco.initialize(this);
      }
   ```

   ---  
    
   **Example**

   ![image.gif](http://upload-images.jianshu.io/upload_images/966283-2e13447dfd5028a1.gif?imageMogr2/auto-orient/strip)
   
   ---

AdapterView like ListView、RecyclerView 、ViewPager is convenient, bind it to the collection view with app:items and app:itemView,You should use an ObservableList to automatically update your view based on list changes.

   ```xml
    <android.support.v7.widget.RecyclerView
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       bind:itemView="@{viewModel.itemView}"
       bind:items="@{viewModel.itemViewModel}"
       bind:layoutManager="@{LayoutManagers.linear()}"
   ```
   
   In ViewModel define itemViewModel and itemView
   
   ```java
    public final ObservableList<ViewModel> itemViewModel = new ObservableArrayList<>();
    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.layoutitem_list_view);
   ```
   
   Adapter，ViewHolder ..is Not Required:
   
   ---   
   
   **Example**
   
   ![listview_databinding.gif](http://upload-images.jianshu.io/upload_images/966283-fb4ae1cdc79ff478.gif?imageMogr2/auto-orient/strip)
   
   ---
 **Other attributes supported:**

  - *ImageView*

     ```xml
     <attr name="uri" />
     <!--width for ResizeOptions (use Fresco to load bitmap). -->
     <attr name="request_width" format="integer" />
     <!--height for ResizeOptions (use Fresco to load bitmap). -->
     <attr name="request_height" format="integer" />
     <attr name="placeholderImageRes" format="reference|color" />
      ```

  - *ListView*、*ViewPager*、*RecyclerView*

     ```xml
     <!-- require ItemView  or  ItemViewSelector   -->
     <attr name="itemView" />
     <!-- require List<ViewModel> bind to ItemView to presentation.-->
     <attr name="items" />
     <!-- require a adapter which type of BindingRecyclerViewAdapter<T> to AdapterView-->
     <attr name="adapter" />
     <attr name="dropDownItemView" format="reference" />
     <attr name="itemIds" format="reference" />
     <attr name="itemIsEnabled" format="reference" />
     <!-- require PageTitles<T>-->
     <attr name="pageTitles" format="reference" />
     ```

  - *ViewGroup*

     ```xml
     <!-- require ItemView  or ItemViewSelector -->
     <attr name="itemView" />
     <!-- require List<ViewModel> bind to ItemView to presentation.-->
     <attr name="viewModels" format="reference" />
     ```

  - *EditText*

     ```xml
     <!-- require boolean value to decide whether requestFocus for view. -->
     <attr name="requestFocus"  format="boolean" />
     ```

  - *SimpleDraweeView*

     ```xml
     <!-- require String to load Image"-->
     <attr name="uri" />
     ```

  - *WebView*

     ```xml
     <!-- require String render to html show in webview-->
     <attr name="render" format="string" />
     ```


###Command Binding###
---

When RecyclerView scroll to end of list,we have onLoadMoreCommand to deal with event.

   ```xml
      <android.support.v7.widget.RecyclerView
           android:id="@+id/recyclerView"
           android:layout_width="match_parent"
           android:layout_height="match_parent"
           bind:onLoadMoreCommand="@{viewModel.loadMoreCommand}"/>
   ```

In ViewModel define a ReplyCommand<Integer> field to deal with this event.

  ```java
     public final ReplyCommand<Integer> loadMoreCommand = new ReplyCommand<>(
        (count) -> {
            /*count: count of list items*/
             int page=count / LIMIT +1;
             loadData(page)
        });
  ```
  
   ---   
   
   **Example**
  
   ![listview load more](http://upload-images.jianshu.io/upload_images/966283-4774960c95b4e1e5.gif?imageMogr2/auto-orient/strip)
 
   ---
 
   Deal with click event of View is more convenient:

  ```xml
   <Button
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              bind:clickCommand="@{viewModel.btnClickCommand}" />
  ```
In ViewModel define a ReplyCommand btnClickCommand will be call when click event occur.

  ```java
   public ReplyCommand btnClickCommand = new ReplyCommand(() -> {
         do something...
      });
  ```
  
  ---   
  
  **Example** 
   
  ![clickCommand.gif](http://upload-images.jianshu.io/upload_images/966283-127546f950733b22.gif?imageMogr2/auto-orient/strip)
  
  ---
  
  **onRefreshCommand to SwipeRefreshLayout**
  
   ![refresh.gif](http://upload-images.jianshu.io/upload_images/966283-ea1e70f3282003aa.gif?imageMogr2/auto-orient/strip)
   
  ---
  
More command binding is supported:

  - *View*

    ```xml
    <!-- require ReplyCommand to deal with view click event. -->
    <attr name="clickCommand" format="reference" />
    <!-- require ReplyCommand<Boolean> to deal with view focus change event.
    ReplyCommand would has params which means if view hasFocus.-->
    <attr name="onFocusChangeCommand" format="reference" />
    <!-- require ReplyCommand<MotionEvent> -->
    <attr name="onTouchCommand" />
    ```

  - *ListView*、*RecyclerView*

    ```xml
    <!-- require ReplyCommand<Integer> -->
    <attr name="onScrollStateChangedCommand" />
    <!-- require ReplyCommand<ListViewScrollDataWrapper> -->
    <attr name="onScrollChangeCommand" />
    <!-- require ReplyCommand<Integer> count of list items-->
    <attr name="onLoadMoreCommand" format="reference" />
    ```
  - *ViewPager*

    ```xml
    <!--require ReplyCommand<ViewPagerDataWrapper> -->
    <attr name="onPageScrolledCommand" format="reference" />
    <!--require ReplyCommand<Integer> -->
    <attr name="onPageSelectedCommand" format="reference" />
    <!--require ReplyCommand<Integer> -->
    <attr name="onPageScrollStateChangedCommand" format="reference" />
    ```

  - *EditText*

    ```xml
    <!--require ReplyCommand<TextChangeDataWrapper> -->
    <attr name="beforeTextChangedCommand" format="reference" />
    <!--require ReplyCommand<TextChangeDataWrapper> -->
    <attr name="onTextChangedCommand" format="reference" />
    <!--require ReplyCommand<String> -->
    <attr name="afterTextChangedCommand" format="reference" />
    ```

  - *ImageView*

    ```xml
     <!--  require ReplyCommand<Bitmap> -->
     <attr name="onSuccessCommand" format="reference" />
     <!--require ReplyCommand<CloseableReference<CloseableImage>> -->
     <attr name="onFailureCommand" format="reference" />
    ```

  - *ScrollView*、*NestedScrollView*

    ```xml
    <!-- require ReplyCommand<ScrollDataWrapper> -->
    <attr name="onScrollChangeCommand" />
    <!-- require ReplyCommand<NestScrollDataWrapper> -->
    <attr name="onScrollChangeCommand" />
    ```

  - *SwipeRefreshLayout*

    ```xml
    <!-- require RelayCommand<> -->
    <attr name="onRefreshCommand" format="reference" />
    ```

###Messenger###
---
**simplifies the communication between ViewModel(major) or any components**

  ---   
  
  **Example**
  
  ![Messenger](http://upload-images.jianshu.io/upload_images/966283-e0db0a55d2a58e33.gif?imageMogr2/auto-orient/strip)
 
  ---

- global message broadcast without deliver data

     ```java
     /* TOKEN: like Action of broadcast with who register this token will be notified when event occur.*/
       Messenger.Default().sendNoMsg(TOKEN);
       /*context: it usually to be a activity ，this parameter is represent to
               a receiver which is mean for convenient when unregister message.
       TOKEN: like Action of broadcast with who register this token will be notified when event occur.
       (data)->{ }:Action to deal with event. */
       Messenger.Default().register(context, TOKEN, () -> { });
    ```
- global message broadcast (carry data to receiver)

     ```java
      Messenger.getDefault().send(data, TOKEN)
      /*context:
      TOKEN:
      Data.class: type of deliver data.
     (data)->{ }: function to deal with event which has data is deliver by sender.*/
      Messenger.getDefault().register(context, TOKEN, Data.class, (data) -> { });
  ```
- send to specify target (inactive)

     ```java
     Messenger.getDefault().sendToTarget(T message, R target)
     Messenger.getDefault().sendNoMsgToTargetWithToken(Object token,R target)
     Messenger.getDefault().sendNoMsgToTarget(Object target)
     ```

- cancel register

     ```java
      Messenger.getDefault().unregister(Object recipient)"
      /* Usually Usage*/
      @Override
      protected void onDestroy() {
              super.onDestroy();
              Messenger.getDefault().unregister(this);
       }
     ```
     
     
## License
   ```
    Copyright 2016 Kelin Hong
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
   ``` 