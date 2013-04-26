package project.tigerim;

import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.RosterEntry;

import com.zxzh.baiduloation.BaiduLoaction;
import com.zxzh.baiduloation.LocationObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends BaseActivity {
	protected static final int UPDATE_MESSAGE = 0;
	protected static final int UPDATE_myMESSAGE = 1;
//	private ExpandableListView epdLV;
	private EditText sendAddressET;
	private EditText sendcontentET;
	private BaiduLoaction baiduLoaction;
	private LocationObject object=new LocationObject();

	public Handler handler1 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// ��ʾ�Է������͵���Ϣ
			case UPDATE_MESSAGE:
				String result = (String) msg.getData().get("result");
				getmsgET.setText(getmsgET.getText() + "\n" + result);
				break;
			// ��ʾ�Լ������͵���Ϣ
			case UPDATE_myMESSAGE:
				getmsgET.setText(getmsgET.getText()
						+ "\n"
						+ conn.getUser().substring(0,
								conn.getUser().indexOf("@")) + ":"
						+ sendcontentET.getText());
				sendcontentET.setText("");
				break;
			}
		};
	};
	private TextView getmsgET;
	private ChatManager chatmanager;

	@Override
	void findview() {
//		epdLV = (ExpandableListView) findViewById(R.id.epdLV);
		sendAddressET = (EditText) findViewById(R.id.sendAddressET);
		sendcontentET = (EditText) findViewById(R.id.sendcontentET);
		getmsgET = (TextView) findViewById(R.id.getmsgET);
	}

	@Override
	protected void setListener() {
		Roster.setDefaultSubscriptionMode(SubscriptionMode.accept_all);
		Roster roster = conn.getRoster();
		try {
			RosterGroup group=roster.getGroup("friends");
//			roster.createEntry("xiaobai3@wanglq.com", "xiaobai3", friends);
			//System.out.println(3);
			System.out.println("��ӳɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Collection<RosterEntry> entries = roster.getEntries();
		System.out.println(entries.size());
		for (RosterEntry entry : entries) {
			System.out.println(entry.toString());
			//System.out.println(entry.getName());
		}
//		conn.getRoster().addRosterListener(arg0)
		//�������������
		chatmanager = conn.getChatManager();
		//Ϊ������ע������¼����������������û�����Ϣ
		chatmanager.addChatListener(new ChatManagerListener() {

			public void chatCreated(Chat chat, boolean arg1) {
				chat.addMessageListener(new MessageListener() {
					public void processMessage(Chat arg0, Message arg1) {
						String result = arg1.getFrom().substring(0,
								arg1.getFrom().indexOf("@"))
								+ ":" + arg1.getBody();
						android.os.Message msg = android.os.Message.obtain();
						msg.what = UPDATE_MESSAGE;
						Bundle data = new Bundle();
						data.putString("result", result);
						msg.setData(data);
						handler1.sendMessage(msg);
					}
				});
			}
		});
	}

	/**
	 * �����ť������Ϣ
	 */

	public void send(View view) {
		final String sendAddress = sendAddressET.getText().toString();
//		final String sendContent = sendcontentET.getText().toString();
		final String sendContent=sendcontentET.getText().toString()+"GPS:"+object.x+";"+object.y;
		// �����ϢΪ���򷵻�
		if ("".equals(sendContent))
			return;
		final Chat newchat = chatmanager.createChat(sendAddress, null);
		new Thread() {
			public void run() {
				try {
					newchat.sendMessage(sendContent);
					// ������Ϣ��֪ͨui���и���
					android.os.Message msg = android.os.Message.obtain();
					msg.what = UPDATE_myMESSAGE;
					handler1.sendMessage(msg);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};

		}.start();
	}

	@Override
	protected void setContentView() {
		super.setContentView();
		setContentView(R.layout.contacts);
		baiduLoaction=new BaiduLoaction(this,object);
	}

	@Override
	protected void onPause() {
		super.onPause();
		baiduLoaction.closeLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		baiduLoaction.startLocation();
	}

}
