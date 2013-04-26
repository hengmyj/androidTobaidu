package project.tigerim;

import java.util.ArrayList;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	protected static final int SHOW_PROGRESS = 0;
	protected static final int DISMISS_PROGRESS = 1;
	protected static final int DOWNLOAD_FILE=2;
	public static String account;
	public static XMPPConnection conn;
	public ProgressBar pb;
	public static ArrayList<Activity> activityHolder = new ArrayList<Activity>();
	public static FileTransferManager manager;//����һ���ļ����������
	/**
	 * ����һ��ȫ�ֵ�handler
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_PROGRESS:
				showPb();
				afterShowProgressLogic();
				break;

			case DISMISS_PROGRESS:
				dismissPb();
				afterDismissProgressLogic();
				break;
			case DOWNLOAD_FILE:
				Toast.makeText(getApplicationContext(), "���ļ���", 1).show();
				break;
			}
		}
	};
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		setContentView();
		init();
		findview();
		setListener();
		super.onCreate(savedInstanceState);
		
	};
	/**
	 * ���ü����¼�
	 */
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Ϊactivity����layout
	 */
	protected void setContentView() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * ����activity���һЩ��ʼ������
	 */
	public void init() {
		activityHolder.add(this);
	
	}

	/**
	 * Ѱ�ҿؼ�
	 */
	abstract void findview();

	/**
	 * ��ʾ�������ķ���
	 */
	public void showPb() {
		pb = (ProgressBar) findViewById(R.id.progressbar);
		pb.setVisibility(View.VISIBLE);
	}

	/**
	 * ���ؽ�����
	 */
	public void dismissPb() {
		pb.setVisibility(View.GONE);
	}

	/**
	 * handler����ʾ��������Ĵ����߼������������ิд��ʵ���Լ����߼�
	 */
	public void afterShowProgressLogic() {
		// TODO Auto-generated method stub

	};

	/**
	 * handler������ʧ��������Ĵ����߼������������ิд��ʵ���Լ����߼�
	 */
	public void afterDismissProgressLogic() {
		// TODO Auto-generated method stub

	}

	/**
	 * �ж����û����º��˼�ʱ���˳�Ӧ�ó���
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				AlertDialog.Builder builder = new Builder(
						this);
				builder.setTitle("��ʾ");
				builder.setMessage("ȷ��Ҫ�˳�Ӧ�ó�����?");
				builder.setPositiveButton("ȷ��", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						conn.disconnect();
						for(int i=0;i<activityHolder.size();i++){
							if(activityHolder.get(i)!=null)
								activityHolder.get(i).finish();
						}
						activityHolder.clear();
						
					}
				});
				builder.setNegativeButton("ȡ��", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
				builder.create().show();
			}

		}

		return true;
	}

}
