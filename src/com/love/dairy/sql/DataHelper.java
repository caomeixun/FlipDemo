package com.love.dairy.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.love.dairy.pojo.ImageInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * ��ɾ������
 * @author leon~
 * @time 2011-10-12����04:34:52
 * Email:caomeixun_li@sohu.com
 */
	public class DataHelper {
		//���ݿ�����
		private static String DB_NAME = "myloveData.db";
		//���ݿ�汾
		private static int DB_VERSION = 2;
		private SQLiteDatabase db;
		private SqliteHelper dbHelper;
	
		public DataHelper(Context context){
			dbHelper=new SqliteHelper(context,DB_NAME, null, DB_VERSION);
			db= dbHelper.getWritableDatabase();
		}
		
		public void close()
		{
			if(db!=null)
				db.close();
			if(dbHelper!=null)
				dbHelper.close();
		}
		//��ȡusers���е�UserID��Access Token��Access Secret�ļ�¼
		public List<ImageInfo> getUserList()
		{
			List<ImageInfo> userList = new ArrayList<ImageInfo>();
			Cursor cursor=db.query(SqliteHelper.TB_NAME, null, null, null, null, null, ImageInfo.ID+" DESC");
			cursor.moveToFirst();
			while(!cursor.isAfterLast()&& (cursor.getString(1)!=null)){
				ImageInfo image=new ImageInfo();
				image.id = cursor.getString(0);
				image.name = cursor.getString(1);
				image.ivDate = cursor.getString(2);
				image.path = cursor.getString(3);
				image.content = cursor.getString(4);
				image.title = cursor.getString(5);
				userList.add(image);
				cursor.moveToNext();
			}
			cursor.close();
			return userList;
		}
		public ImageInfo getImageInfo(String imagePath)
		{
			ImageInfo image = null;
			Cursor cursor=db.query(SqliteHelper.TB_NAME, null, ImageInfo.PATH + "='" + imagePath+"'", null, null, null, ImageInfo.ID+" DESC");
			cursor.moveToFirst();
			while(!cursor.isAfterLast()&& (cursor.getString(1)!=null)){
				image=new ImageInfo();
				image.id = cursor.getString(0);
				image.name = cursor.getString(1);
				image.ivDate = cursor.getString(2);
				image.path = cursor.getString(3);
				image.content = cursor.getString(4);
				image.title = cursor.getString(5);
				cursor.moveToNext();
			}
			cursor.close();
			return image;
		}
		
		
	//�ж�users���е��Ƿ����ĳ��UserID�ļ�¼
		public Boolean haveUserInfo(String imagePath)
		{
			Boolean b=false;
			Cursor cursor=db.query(SqliteHelper.TB_NAME, null,  ImageInfo.PATH + "='" + imagePath+"'", null, null, null,null);
			b=cursor.moveToFirst();
			Log.e("HaveUserInfo",b.toString());
			cursor.close();
			return b;
		}
		
//		//����users��ļ�¼������UserId�����û��ǳƺ��û�ͼ��
//		public int UpdateUserInfo(String userName,Bitmap userIcon,String UserId)
//		{
//			ContentValues values = new ContentValues();
//			values.put(UserInfo.USERNAME, userName);
//			// BLOB���� 
//			final ByteArrayOutputStream os = new ByteArrayOutputStream(); 
//			// ��Bitmapѹ����PNG���룬����Ϊ100%�洢 
//			userIcon.compress(Bitmap.CompressFormat.PNG, 100, os); 
//			// ����SQLite��Content��������Ҳ����ʹ��raw 
//			values.put(UserInfo.USERICON, os.toByteArray());
//			int id= db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "=" + UserId, null);
//			Log.e("UpdateUserInfo2",id+"");
//			return id;
//		}
		
		//����users��ļ�¼
		public long updateUserInfo(ImageInfo image)
		{
			ContentValues values = new ContentValues();
			values.put(ImageInfo.TITLE, image.title);
			values.put(ImageInfo.CONTENT, image.content);
			values.put(ImageInfo.IVDATE, image.ivDate);
			values.put(ImageInfo.NAME, image.name);
			values.put(ImageInfo.PATH, image.path);
			int id= db.update(SqliteHelper.TB_NAME, values, ImageInfo.PATH + "='" + image.path+"'", null);
			Log.e("UpdateUserInfo",id+"");
			return id;
		}
		
		//���users��ļ�¼
		public Long addUserInfo(ImageInfo image)
		{
			if(haveUserInfo(image.path)){
				return updateUserInfo(image);
			}
			ContentValues values = new ContentValues();
			values.put(ImageInfo.ID, image.id);
			values.put(ImageInfo.TITLE, image.title);
			values.put(ImageInfo.CONTENT, image.content);
			values.put(ImageInfo.IVDATE, image.ivDate);
			values.put(ImageInfo.NAME, image.name);
			values.put(ImageInfo.PATH, image.path);
			Long uid = db.insert(SqliteHelper.TB_NAME, ImageInfo.ID, values);
			Log.e("SaveUserInfo",uid+"");
			return uid;
		}
		
		
		//ɾ��users��ļ�¼
		public int delUserInfo(String imagePath){
			int id= db.delete(SqliteHelper.TB_NAME, ImageInfo.PATH + "='" + imagePath+"'", null);
			Log.e("DelUserInfo",id+"");
			return id;
			}
		}
