package project.tigerim;




import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.content.Intent;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*; 

public class LoginActivity extends BaseActivity implements OnClickListener{
	private EditText passwordET,usernameET; 
	private Button loginBT,registerBT; 
	public RadioGroup mRadioGroup1;
    public RadioButton mRadio1, mRadio2;
    String ServerUrl="gmail.com";

	@Override
	protected void setContentView() {
		setContentView(R.layout.login);
	}
	

	@Override
	protected void setListener() {
		loginBT.setOnClickListener(this);
		mRadioGroup1.setOnCheckedChangeListener(mChangeRadio);
	}

	 private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            // TODO Auto-generated method stub
	            if (checkedId == mRadio1.getId()) {
	                // 把mRadio1的内容传到mTextView1
	            	ServerUrl="gmail.com";
	                 
	            } else if (checkedId == mRadio2.getId()) {
	                // 把mRadio2的内容传到mTextView1 
	            	ServerUrl="rooyee.im";
	            	
	            }
	        }
	    };

	public void findview() {
		passwordET = (EditText) findViewById(R.id.passwordET);
		usernameET = (EditText) findViewById(R.id.usernameET);
		loginBT = (Button) findViewById(R.id.loginBT);
		registerBT = (Button) findViewById(R.id.registerBT);
		 mRadioGroup1 = (RadioGroup) findViewById(R.id.myRadioGroup);
	     mRadio1 = (RadioButton) findViewById(R.id.myRadioButton1);
	     mRadio2 = (RadioButton) findViewById(R.id.myRadioButton2);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBT:
			final String username=usernameET.getText().toString();
			final String password=passwordET.getText().toString();
			account=username;
			showPb();
			new Thread(){
				
				public void run() {
					try {
						ConnectionConfiguration config=new ConnectionConfiguration(ServerUrl, 5222);
						System.out.println(ServerUrl);
						config .setTruststorePath("/system/etc/security/cacerts.bks");
						config.setTruststorePassword("changeit");
						config.setTruststoreType("bks");
						config.setSASLAuthenticationEnabled(true);
						conn=new XMPPConnection(config);
						ProviderManager pm=ProviderManager.getInstance();
						configure(pm);
						conn.connect();
						conn.login(username, password);
						Presence presence = new Presence(Presence.Type.available);
						presence.setMode(Presence.Mode.chat);
						presence.setStatus("online");
						conn.sendPacket(presence);
						Message msg=Message.obtain();
						msg.what=DISMISS_PROGRESS;
						handler.sendMessage(msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			break;
		}
	}
	
	/**
	 * 处理进度条消失后的逻辑
	 */
	@Override
	public void afterDismissProgressLogic() {
		/**
		 * 设置文件监听，当有文件发送过来时自动接收
		 */
		new ServiceDiscoveryManager(conn);
		manager=new FileTransferManager(conn);//初始化文件传输管理器
		manager.addFileTransferListener(new FileTransferListener() {
			
			public void fileTransferRequest(FileTransferRequest request) {
				final IncomingFileTransfer incoming = request.accept();
				final String filename=incoming.getFileName();
					
				new Thread(){
					public void run() {
						try {
							System.out.println("开始下载");
							Message msg=Message.obtain();
							msg.what=DOWNLOAD_FILE;
							handler.sendMessage(msg);
//							InputStream in=incoming.recieveFile();
//							File file=new File(Environment.getExternalStorageDirectory(), filename);
//							FileOutputStream out=new FileOutputStream(file);
//							byte[] b=new byte[1024];
//							int len=0;
//							while((len=in.read(b))!=-1){
//								out.write(b, 0, len);
//							}
//							out.flush();
//							out.close();
							incoming.recieveFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					};
				}.start();
			}
		});
		Intent friendsIntent=new Intent(LoginActivity.this, FriendsActivity.class);
		startActivity(friendsIntent);
	}
	
	public void configure(ProviderManager pm) {  
		  
	//  Private Data Storage  
	pm.addIQProvider("query","jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());  
	  
	//  Time  
	try {  
	    pm.addIQProvider("query","jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));  
	} catch (ClassNotFoundException e) {  
	    Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");  
	}  
	  
	//  Roster Exchange  
	pm.addExtensionProvider("x","jabber:x:roster", new RosterExchangeProvider());  
	  
	//  Message Events  
	pm.addExtensionProvider("x","jabber:x:event", new MessageEventProvider());  
	  
	//  Chat State  
	pm.addExtensionProvider("active","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());  
	pm.addExtensionProvider("composing","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());   
	pm.addExtensionProvider("paused","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());  
	pm.addExtensionProvider("inactive","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());  
	pm.addExtensionProvider("gone","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());  
	  
	//  XHTML  
	pm.addExtensionProvider("html","http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());  
	  
	//  Group Chat Invitations  
	pm.addExtensionProvider("x","jabber:x:conference", new GroupChatInvitation.Provider());  
	  
	//  Service Discovery # Items      
	pm.addIQProvider("query","http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());  
	  
	//  Service Discovery # Info  
	pm.addIQProvider("query","http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());  
	  
	//  Data Forms  
	pm.addExtensionProvider("x","jabber:x:data", new DataFormProvider());  
	  
	//  MUC User  
	pm.addExtensionProvider("x","http://jabber.org/protocol/muc#user", new MUCUserProvider());  
	  
	//  MUC Admin      
	pm.addIQProvider("query","http://jabber.org/protocol/muc#admin", new MUCAdminProvider());  
	  
	//  MUC Owner      
	pm.addIQProvider("query","http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());  
	  
	//  Delayed Delivery  
	pm.addExtensionProvider("x","jabber:x:delay", new DelayInformationProvider());  
	  
	//  Version  
	try {  
	    pm.addIQProvider("query","jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));  
	} catch (ClassNotFoundException e) {  
	    //  Not sure what's happening here.  
	}  
	  
	//  VCard  
	pm.addIQProvider("vCard","vcard-temp", new VCardProvider());  
	  
	//  Offline Message Requests  
	pm.addIQProvider("offline","http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());  
	  
	//  Offline Message Indicator  
	pm.addExtensionProvider("offline","http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());  
	  
	//  Last Activity  
	pm.addIQProvider("query","jabber:iq:last", new LastActivity.Provider());  
	  
	//  User Search  
	pm.addIQProvider("query","jabber:iq:search", new UserSearch.Provider());  
	  
	//  SharedGroupsInfo  
	pm.addIQProvider("sharedgroup","http://www.jivesoftware.org/protocol/sharedgroup", new SharedGroupsInfo.Provider());  
	  
	//  JEP-33: Extended Stanza Addressing  
	pm.addExtensionProvider("addresses","http://jabber.org/protocol/address", new MultipleAddressesProvider());  
	  
	//   FileTransfer  
	pm.addIQProvider("si","http://jabber.org/protocol/si", new StreamInitiationProvider());  
	  
	pm.addIQProvider("query","http://jabber.org/protocol/bytestreams", new BytestreamsProvider());  
	  
	//  Privacy  
	pm.addIQProvider("query","jabber:iq:privacy", new PrivacyProvider());  
	pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());  
	pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.MalformedActionError());  
	pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadLocaleError());  
	pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadPayloadError());  
	pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadSessionIDError());  
	pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.SessionExpiredError());
	}  
	}
