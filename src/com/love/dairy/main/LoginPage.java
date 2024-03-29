package com.love.dairy.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.love.dairy.game.Game;
import com.love.dairy.game.PicView;
import com.love.dairy.main.renren.RenrenSDKDemo;
import com.love.dairy.pojo.ImageInfo;
import com.love.dairy.sql.DataHelper;
import com.love.dairy.utils.FileDownload;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends BaseActivity implements OnClickListener{
	public static String IMAGE_ID = "IMAGE_ID"; 
	public static String OPEN_TYPE_PATH = "OPEN_TYPE_PATH"; 
	private int request_code_path = 123;
	private int imagePosition = -1;
	public void onCreate(Bundle save){
		super.onCreate(save);
		setContentView(R.layout.flip_login);
		imagePosition= getIntent().getIntExtra(IMAGE_ID, -1);
		findViewById(R.id.btnOK).setOnClickListener(this);
		findViewById(R.id.btnRenren).setOnClickListener(this);
		findViewById(R.id.btnPath).setOnClickListener(this);
		findViewById(R.id.btnAbout).setOnClickListener(this);
		
		if(getIntent().getIntExtra(OPEN_TYPE_PATH, -1)!=-1){
			getPicPath();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.stack_pop,
					R.anim.slide_out_to_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnOK){
			String etTitle = ((EditText)(findViewById(R.id.etTitle))).getText().toString();
			String etContent = ((EditText)(findViewById(R.id.etContent))).getText().toString();
			ImageInfo info = new ImageInfo();
			info.content = etContent;
			info.ivDate = "一年前";
			info.title = etTitle;
			info.path =MainActivity.path + MainActivity.photoIds[imagePosition];
			info.name = "Love_"+MainActivity.photoIds[imagePosition];
			DataHelper dh = new DataHelper(getApplicationContext());
			long result =  dh.addUserInfo(info);
			if(result > 0){
				Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
			}
			dh.close();
			finish();
		}else if(v.getId() == R.id.btnRenren){
			Intent intent = new Intent();
			intent.setClass(this, RenrenSDKDemo.class);
			startActivity(intent);
		}else if(v.getId() == R.id.btnPath){
			getPicPath();
		}else {
			Intent i = null;
				i = new Intent(this, Game.class);
				i.setAction("NEW_GAME_ACTION");
				Bundle bundle = new Bundle();
				bundle.putInt("imageId",imagePosition);
				i.putExtras(bundle);
	    		
			startActivity(i);
    		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    		finish();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			if(requestCode  == request_code_path){
				Uri originalUri = data.getData();
				if (originalUri != null)
				{
					String path = originalUri.getPath();
					if(originalUri.getScheme().equals("content")){
						String[] proj = { MediaStore.Images.Media.DATA };
						Cursor actualimagecursor = managedQuery(originalUri,proj,null,null,null);
						int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						actualimagecursor.moveToFirst();
						path = actualimagecursor.getString(actual_image_column_index);
					}
					saveSharedPreferencesData(IMAGE_PATH, path.substring(0,path.lastIndexOf("/")+1));
					Intent intent = new Intent();
					intent.setClass(this, MainActivity.class);
		 			startActivity(intent);
					finish();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 获取歌曲文件路径
	 * @return
	 */
	public void getPicPath(){
		final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, request_code_path);
	}
}
