# webim
基于openfire+asmack的Android即时通信工具


![first](http://g4.ihostimg.com/g4/201504171911520lapv.jpeg)
![first](http://g4.ihostimg.com/g4/20150417191716lskqf.jpg)
![first](http://g4.ihostimg.com/g4/20150417191729ggvsd.jpg)
![first](http://g4.ihostimg.com/g4/201504171917374pf29.jpg)
![first](http://g4.ihostimg.com/g4/20150417191744dbysw.jpg)
![first](http://g4.ihostimg.com/g4/20150417191315512xq.jpeg)
教程地址：http://www.cnblogs.com/lfk-dsk/
    本地化之章！

　往期传送门：

　　1.http://www.cnblogs.com/lfk-dsk/p/4398943.html

　　2.http://www.cnblogs.com/lfk-dsk/p/4411625.html

　　3.http://www.cnblogs.com/lfk-dsk/p/4412126.html

　　4.http://www.cnblogs.com/lfk-dsk/p/4413693.html

　　5.http://www.cnblogs.com/lfk-dsk/p/4419418.html

　　6.http://www.cnblogs.com/lfk-dsk/p/4433319.html

    真没想到这东西居然还会写到第七篇，很多亲们的反馈和鼓励给了我很大的动力啊，最近正好朋友也在做这个东西，但是我们用一样的库和后端做的东西居然不一样（他用这个做了联网弹幕！），不过确实发现了一些问题比如asmack的库内容参差不齐，很多提供方法都不一样，看来使用者们都或多或少的对源码进行了一些修改，都变得乱糟糟的呢！

    这里就提供一下我自己用的asmack库吧，省的大家用的不一样：http://files.cnblogs.com/files/lfkdsk/asmack.zip

    这次的博文先从完成的效果开始吧，这个并不是最终稿，因为虽然完成了很多的功能啊，但是还有一些问题，比如接收数据没用广播啊，监听的ChatListener没放在server里啊，好友系统并不完善啊，很多的东西还没有完成，到时候还要根据这些东西进行一些修改，但是现在需要的功能已经够用了，接着的那些东西也不过是要应用asmack库里的东西而已了，好了先上效果图：

    

blob.png

（1.首先登录界面增加了Ip的选项，更具有Spark的android版的感觉，通用性强了，不过我之后要是想上线让大家用可能会删掉）

   

blob.png

（2.现在不打开具体的聊天窗口，程序也不会蹦了，因为原来是直接向listview里面添加数据，可是listview还没初始化所以会崩）

blob.png

（3.打开具体的useractivity就会取出原来的数据然后还有新打印的出来的）

blob.png

（4.信息由于是走数据库了，所以分发也不会想原来一样出错了）                                                                                                      　　　　　　　　　    

　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　 blob.png    

（5.我不想用传统的通知栏，那个用户能禁用，哈哈哈哈哈哈哈，我用了定制的Toast，在桌面的时候接到消息就会弹出一个Toast提示！）

好了这就是我们本次要达成的效果，让我们一个一个来！

        1.首先从主界面的输入IP开始：

 1  <TableRow>
 2             <TextView
 3                 android:textColor="#ffc2c6c6"
 4                 android:layout_height="wrap_content"
 5                 android:text="Ip"/>
 6             <EditText
 7                 android:id="@+id/login_ip"
 8                 android:hint="Input your Ip"
 9                 android:maxLines="1"
10                 android:layout_height="wrap_content"
11                 />
12         </TableRow>
        先在TableLayout里添加。

        静态数据类里添加：

1     //ip名称
2     public static String My_Ip = "";
 

        主活动的添加：

ip = (EditText) findViewById(R.id.login_ip);
 

        check_init()函数里添加：

 1  private void checkbox_init() {//checkbox判断函数
 2         //判断记住密码多选框的状态
 3         if(sp.getBoolean("ISCHECK", false))
 4         {
 5             //设置默认是记录密码状态
 6             check_save.setChecked(true);
 7             ip.setText(sp.getString("USER_IP", ""));
 8             name.setText(sp.getString("USER_NAME",""));
 9             password.setText(sp.getString("PASSWORD",""));
10             //判断自动登陆多选框状态
11             if(sp.getBoolean("AUTO_ISCHECK", false))
12             {
13                 //设置默认是自动登录状态
14                 check_auto.setChecked(true);
15                 //跳转界面
16                 //account=sp.getString("USER_NAME","");
17                 //pwd=sp.getString("PASSWORD","");
18                 Log.i("======================"+account,pwd+"===================================");
19                 accountLogin();
20             }
21         }
22     }
23     private void setCheck_save(){
24         if(check_save.isChecked())
25         {
26             //记住用户名、密码、
27             editor = sp.edit();
28             editor.putString("USER_IP", user.My_Ip);
29             editor.putString("USER_NAME", account);
30             editor.putString("PASSWORD",pwd);
31             editor.apply();
32         }
33     }
 

        这个是把Ip添加进记录。

        修改登录的方法：

 1    private void accountLogin() {
 2         new Thread() {
 3             public void run() {
 4                 user.My_Ip = ((EditText)findViewById(R.id.login_ip))
 5                         .getText().toString();
 6                 account = ((EditText) findViewById(R.id.login_name))
 7                         .getText().toString();
 8                 pwd = ((EditText) findViewById(R.id.login_password)).getText()
 9                         .toString();
10                 boolean is = ConnecMethod.login(account, pwd);
11                 if (is) {
12                     insHandler.sendEmptyMessage(1);
13                     // 将用户名保存
14                     user.UserName = account+"@lfkdsk/Spark 2.6.3";
15                     user.UserName_= account;
16                     setCheck_save();
17                 } else {
18                     insHandler.sendEmptyMessage(0);
19                 }
20             }
21         }.start();
22     }
 

   　 但是要是逐层的为函数添加参数，然后无限的传参也是一种不太现实的方法，所以我们在XMpp连接的地方直接调用静态存储的IP:

 1     public static boolean openConnection() {
 2         try {
 3             connConfig = new ConnectionConfiguration(user.My_Ip, 5222);
 4             // 设置登录状态为离线
 5             connConfig.setSendPresence(false);
 6             // 断网重连
 7             connConfig.setReconnectionAllowed(true);
 8             con = new XMPPConnection(connConfig);
 9             con.connect();
10             return true;
11         } catch (Exception e) {
12 
13         }
14         return false;
15     }
 

   　 这样我们登陆的时候就能手动指定ip或者是域名了，增强了通用性。

　　 2.本地化数据的具体操作：

　　1.新建一个类作为本地数据库的模版：

 1 package com.lfk.webim.appli;
 2 
 3 import android.util.Log;
 4 
 5 /**
 6  * Created by Administrator on 2015/5/26.
 7  */
 8 public class TalkLogs {
 9     private String ID = "_id";              //数据库主键，自增
10     private String With_Id ="with_id";      //和谁聊天
11     private String Logs = "talklogs";       //聊天记录
12     private String If_read = "_ifread";     //是否已读
13     private String dbname;                  //数据表名---为用户名，即user.UserName_
14     private String CREAT_DB = "";           //数据库新建的语句
15 
16     public TalkLogs(String dbname){
17         this.dbname = dbname;
18         giveanameto(dbname);
19         Log.e("dbname=====", this.dbname);
20         Log.e("dbname参数=====",dbname);
21     }
22     private void giveanameto(String dbname){
23         CREAT_DB = "CREATE TABLE if not exists "+dbname+"("
24                 +this.ID +" integer primary key autoincrement,"
25                 +this.With_Id+","
26                 +this.If_read+" integer,"
27                 + this.Logs+")";
28     }
29     public String returnAString(){
30         Log.e("CREAT_DB===========",CREAT_DB);
31         return CREAT_DB;
32     }
33 }
 

 1 package com.lfk.webim.appli;
 2 
 3 import android.content.Context;
 4 import android.database.sqlite.SQLiteDatabase;
 5 import android.database.sqlite.SQLiteOpenHelper;
 6 import android.widget.Toast;
 7 
 8 /**
 9  * Created by Administrator on 2015/5/25.
10  */
11 public class SQLiteHelper extends SQLiteOpenHelper {
12     private Context mcontext;
13     public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
14         super(context, name, factory, version);
15         mcontext = context;
16     }
17     @Override
18     public void onCreate(SQLiteDatabase db) {
19         //db.execSQL(CREAT_DB);
20         Toast.makeText(mcontext, "succeed collect！", Toast.LENGTH_SHORT).show();
21     }
22 
23     @Override
24     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
25     }
26 }
　　写一个空的SQLiteHelper，把表放在后面。

　　代码里做了详细的注释，类里面传入用户名，返回新建表的String语句。

1     public void CreateNewTable(){
2         sqLiteHelper = new SQLiteHelper(this,"user_logs.db",null,1);    //新建.db文件
3         sqLiteDatabase = sqLiteHelper.getWritableDatabase();            
4         TalkLogs talklog = new TalkLogs(user.UserName_);                //获取新建表的语句
5         sqLiteDatabase.execSQL(talklog.returnAString());                //新建表
6         Toast.makeText(friend.this, user.UserName_+" Create success",Toast.LENGTH_SHORT).show();
7         Log.e(user.UserName_, "success!!!");
8         //sqLiteDatabase.close();
9         }
　　在friend的activity里面，新建该方法，每次进入朋友界面，新建以用户名为名的表，因为用的SQL语句写了if exist 所以已有的不会新建。

1 final ClientConServer server = new ClientConServer(this,mhandler,this.sqLiteDatabase);
　　对工具类进行实例化，可以解决静态方法不能用在非静态上下文的问题，这里传入context，handler，和数据库，数据库是为了防止打开重复，传入handler是为了桌面Toast

　　2.修改后的friend活动：

friend活动
 　  3.工具类进行了很大的修改，详细讲解：

工具类
1 ClientConServer(Context context,Handler handler,SQLiteDatabase sqLiteDatabase){
2         this.context = context;
3         this.handlers = handler;
4         this.sqLiteDatabase = sqLiteDatabase;
5         getFriends();
6         getChat();
7         System.out.print(isAppForground(context));
8     }
　　3.1构造函数，用于实例化。

 1  private void addUnread(ContentValues values,String s1){
 2         if (isAppForground(context)&& s1.equals(user.FromName_)) {
 3             Cursor cursor = sqLiteDatabase.rawQuery("Select * From "+user.UserName_+" where with_id ="+"\""+user.FromName_+"\""+"And _ifread ="+0,null);
 4             if(cursor.moveToFirst()) {
 5                 do {
 6                     String talklogs = cursor.getString(cursor.getColumnIndex("talklogs"));
 7                     useractivity.mConversationArrayAdapter.add(talklogs);
 8                     Log.e(talklogs, "================");
 9                 }while (cursor.moveToNext());
10             }
11             if(!isAppForground(context))
12                 useractivity.mConversationArrayAdapter.notifyDataSetChanged();
13             cursor.close();
14         }
15         values.clear();
16     }
　　3.2搜索所有与我聊天的人的记录，并且为未读。

public boolean isAppForground(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }
        return true;
    }
    public boolean isHome(Context mContext){
        ActivityManager mActivityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes(mContext).contains(rti.get(0).topActivity.getPackageName());
        }
    /**
     * 获得属于桌面的应用的应用包名称
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes(Context mContext) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = mContext.getPackageManager();
        //属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo ri : resolveInfo){
            names.add(ri.activityInfo.packageName);
            System.out.println(ri.activityInfo.packageName);
        }
        return names;
    }
}
　　3.3判断现在栈顶的活动是什么？还有是否在桌面。

　　3.4消息的分发机制

　　这里需要讲解一下我的聊天机制，ChatListener接收数据，然后存入数据库，如果现在的栈顶活动为朋友界面（有一个判断），就默默的存进去，如果是聊天界面就把所有数据库里的取出来，把所有的消息都设为已读（_ifread=1），放在listview里，

然后如果有新消息的话,就用3.1的方法把所有的_ifread的消息取出来，即时的显示在listview里面，如果现在为桌面，就把东西存入然后再Toast出来。

　　所以handler就是重要的消息分发机制：

 1  private  Handler handler = new Handler(){
 2         public void handleMessage(android.os.Message m) {
 3             Message msg = new Message();
 4             msg = (Message) m.obj;
 5             //把从服务器获得的消息通过发送
 6             String[] message = new String[]{ msg.getFrom(), msg.getBody()};
 7             System.out.println("==========收到消息  From==========="+ message[0]);
 8             System.out.println("==========收到消息  Body===========" + message[1]);
 9             String s = msg.getFrom();
10             String s1 = s.split("@")[0];
11             ContentValues values = new ContentValues();
12             if(message[1]!=null) {
13                 if (user.UserName.equals(message[0])) {
14                     values.put("with_id",s1);
15                     values.put("talklogs", "ME: " + msg.getBody());
16                     values.put("_ifread",0);
17                     Log.e("存入：", "ME: " + msg.getBody());
18                     sqLiteDatabase.insert("\"" + user.UserName_ + "\"", null, values);
19                     values.clear();
20                 } else {
21                     values.put("with_id", s1);
22                     values.put("talklogs", s1 + "说：" + msg.getBody());
23                     values.put("_ifread", 0);
24                     Log.e("存入：", s1 + "说：" + msg.getBody());
25                     sqLiteDatabase.insert("\"" + user.UserName_ + "\"", null, values);
26                     addUnread(values,s1);
27                 }
28                 if(isHome(context)){
29                     sendToast(msg,s1);
30                 }
31             }
32         }
33     };
 

　　4.改进后的聊天界面：

聊天活动
 

 1     private void OpenDatabase(){
 2         String DB_NAME = "user_logs.db"; //保存的数据库文件名
 3         String PACKAGE_NAME = "com.lfk.webim";// 应用的包名
 4         String DB_PATH = "/data"
 5                 + Environment.getDataDirectory().getAbsolutePath() +"/"
 6                 + PACKAGE_NAME+ "/databases"; // 在手机里存放数据库的位置
 7         File myDataPath = new File(DB_PATH);
 8         String dbfile = myDataPath+"/"+DB_NAME;
 9         database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
10         if(!checkColumnExist1(database,user.UserName_,user.FromName_)){
11             Cursor cursor = database.rawQuery("Select * From "+user.UserName_+" where with_id ="+"\""+user.FromName_+"\"",null);
12             if(cursor.moveToFirst()) {
13                 do {
14                     String talklogs = cursor.getString(cursor.getColumnIndex("talklogs"));
15                     mConversationArrayAdapter.add(talklogs);
16                     Log.e(talklogs, "================");
17                 }while (cursor.moveToNext());
18             }
19             database.execSQL("UPDATE "+user.UserName_+" SET _ifread = 1 "+"Where with_id ="+"\""+user.FromName_+"\"");
20             cursor.close();
21             //database.close();
22         }
23     }
 

　　每次进入都先打开数据库，并且把所有的聊天对应的人的数据库信息取出来，然后把所有的已读标志设为1；

　　

 1     public static boolean checkColumnExist1(SQLiteDatabase db, String tableName, String columnName) {
 2         boolean result = false ;
 3         Cursor cursor = null ;
 4         try{
 5             //查询一行
 6             cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0", null );
 7             result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
 8         }catch (Exception e){
 9             Log.e("","checkColumnExists1..." + e.getMessage()) ;
10         }finally{
11             if(null != cursor && !cursor.isClosed()){
12                 cursor.close() ;
13             }
14         }
15         return result ;
16     }
 

　　这里用到了一个方法，用来判断表里有没有你的这个字段，如果没有就不用添加到listview里，否则你没和他聊过会崩！！

 1     public Handler mhandle = new Handler() {
 2         public void handleMessage(android.os.Message m) {
 3             switch (m.what) {
 4                 case 1:
 5                     Toast.makeText(useractivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
 6                     break;
 7                 case 0:
 8                     String respond = (String) m.obj;
 9                     Log.i("---", respond);
10                     ContentValues values = new ContentValues();
11                     values.put("with_id",user.FromName_);
12                     values.put("talklogs","ME: "+respond);
13                     values.put("_ifread",0);
14                     Log.e("存入：", "ME: " + respond);
15                     database.insert("\"" + user.UserName_ + "\"", null, values);
16                     values.clear();
17                     mConversationArrayAdapter.add("ME: "+respond);
18                     mConversationArrayAdapter.notifyDataSetChanged();
19                     break;
20                 case 2:
21                     //addTheListview();
22                     break;
23             }
24         }
25     };
 

　　在自己发送消息的时候，存入数据库，然后添加到listview里面。

　　3.桌面显示的Toast:

 1 <?xml version="1.0" encoding="utf-8"?>
 2 <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
 3     android:id="@+id/llToast"
 4     android:layout_width="wrap_content"
 5     android:layout_height="wrap_content"
 6     android:background="#ffffffff"
 7     android:orientation="vertical" >
 8 
 9     <TextView
10         android:id="@+id/tvTitleToast"
11         android:layout_width="match_parent"
12         android:layout_height="wrap_content"
13         android:layout_margin="1dip"
14         android:background="#bb000000"
15         android:gravity="center"
16         android:text="Title"
17         android:textColor="#ffffffff" />
18 
19     <LinearLayout
20         android:id="@+id/llToastContent"
21         android:layout_width="wrap_content"
22         android:layout_height="wrap_content"
23         android:layout_marginBottom="1dip"
24         android:layout_marginLeft="1dip"
25         android:layout_marginRight="1dip"
26         android:background="#44000000"
27         android:orientation="vertical"
28         android:padding="15dip" >
29 
30         <ImageView
31             android:id="@+id/tvImageToast"
32             android:layout_width="wrap_content"
33             android:layout_height="wrap_content"
34             android:layout_gravity="center"
35             android:contentDescription="@string/hello_world"
36             android:src="@drawable/toast_image" />
37 
38         <TextView
39             android:id="@+id/tvTextToast"
40             android:layout_width="128dp"
41             android:layout_height="wrap_content"
42             android:gravity="center"
43             android:paddingLeft="10dip"
44             android:paddingRight="10dip"
45             android:singleLine="false"
46             android:text="自定义显示语句"
47             android:textColor="#ff000000" />
48     </LinearLayout>
49 
50 </LinearLayout>
 

　　1.添加一个toast的布局文件，写出来的样子是这样的：　　

　　　　

　　2.friend里面添加一个方法进行处理：

 1     public void showCustomToast(String s1, String s2) {//新建显示TOAST
 2         // 通用的布局加载器
 3         LayoutInflater inflater = getLayoutInflater();
 4         // 加载根容器，方便用于后面的view的定位
 5         View layout = inflater.inflate(R.layout.toast_view, (ViewGroup)findViewById(R.id.llToast));
 6         // 设置图片的源文件
 7         ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
 8         image.setImageResource(R.drawable.toast_image);
 9         // 设置title及内容
10         TextView title = (TextView) layout.findViewById(R.id.tvTitleToast);
11         title.setText(s1);
12         TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
13         text.setText(s2);
14         Toast tempToast = new Toast(getApplicationContext());
15         // 设置位置
16         tempToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);
17         // 设置显示时间
18         tempToast.setDuration(Toast.LENGTH_SHORT);
19         tempToast.setView(layout);
20         tempToast.show();
21     }
22 }
 

　　3.显示：

　　　刚才在friend里面我们对工具类进行了实例化，传入了handler，就是要在工具类用handler回调，用friend的handler进行处理：

　　

1 if(isHome(context)){
2                     sendToast(msg,s1);
3                 }
　　如果在桌面：

1 private void sendToast(Message msg,String s1){
2         android.os.Message message_send = new android.os.Message();
3         Bundle bundle = new Bundle();
4         bundle.putString("name",s1);
5         bundle.putString("text",msg.getBody());
6         message_send.obj = bundle;
7         message_send.what = 0;
8         handlers.sendMessage(message_send);
9     }
　　打包名字，信息发到friend的handler里去。

　　

 1  public  Handler mhandler = new Handler()
 2     {
 3         public void handleMessage(android.os.Message message)
 4         {
 5             switch (message.what) {
 6                 case 0:
 7                     Bundle bundle = (Bundle)message.obj;            //桌面Toast的解决方法
 8                     String s1 = bundle.getString("name");
 9                     String s2 = bundle.getString("text");
10                     showCustomToast(s1,s2);
11                     break;
12                 case 1: {
13                     String temp = (String) message.obj;
14                     friend.mArrayAdapter.add(temp);
15                     break;
16                 }
17             }
18         }
19     };
　　friend的Handler里处理一下，就能显示出来了。

　　这一次就说这么多吧，整个Demo已经能像正常的IM应用一样使用了，还有一些自己的修改方法，在下一篇的后续里会增加加好友啊，

加组，转入server里的一系列方法，敬请期待！

 　　喜欢就点赞吧！！！

　　
