package frame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.*;

import file.FileUnit;


//フレーム内の機能やフレームの管理を行うクラス
public class FrameWrapper implements ActionListener{
	public JFrame m_Frame;
	private JTabbedPane m_TabbedPane;
	private ArrayList<FileUnit> m_files;
	private JLabel m_BottomLabel;
	private FindWindow m_FindWindow;
	private ReplaceWindow m_ReplaceWindow;
	private OneByOne m_OneByOne;
	
	//現在のエディターのエンコーディングを取得するメソッド
	public String CurrentEncoding(){
		for(int i=0;i<m_Frame.getJMenuBar().getMenu(2).getItemCount();++i){
			if(m_Frame.getJMenuBar().getMenu(2).getItem(i).isSelected()) return m_Frame.getJMenuBar().getMenu(2).getItem(i).getText();
		}
		return "NOT FOUND";
	}

	//ファイルを開くメソッド
	private void OpenFile() throws Exception{
		//ファイルを開くダイアログを表示して、その結果を取得する。
		JFileChooser filechooser = new JFileChooser();
		int selected=filechooser.showOpenDialog(m_Frame);
		//ファイルが選択された場合
		if(selected==JFileChooser.APPROVE_OPTION){
			//ファイルの情報を取得
			File fil = filechooser.getSelectedFile();
			//ファイルの情報をファイル情報管理配列に追加
			FileUnit f=new FileUnit(fil,CurrentEncoding());
			m_files.add(f);
			//新しいタブで、ファイルの内容を開く。
			TabManager.MakeNewTab(this,f,m_files.size(),m_TabbedPane,m_BottomLabel,m_files);
			
		}
	}

	private void InitFrame(){
		//ウィンドウの情報を初期化する
		m_Frame=new JFrame("CJEditor");
		m_Frame.setSize(800, 600);
		m_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	//タブの初期化を行うメソッド
	private void InitTabbedPane(){
		m_TabbedPane=new JTabbedPane();
		
		//選択しているタブが変更された時、ウィンドウのタイトルを"CJEditor"+(タブで開いたファイルへのパス)にセットするようにする。
		class myListener implements ChangeListener{
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if(m_TabbedPane.getTabCount()<=0) {
					m_Frame.setTitle("CJEditor");
					return;
				}
				if(m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName.length()>=11&&m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName.substring(0,11).equals("*新規テキストファイル")){
					m_Frame.setTitle("CJEditor:"+m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName);
				}else{
					m_Frame.setTitle("CJEditor:"+m_files.get(m_TabbedPane.getSelectedIndex()).m_FilePath);
				}
			}
		}
		m_TabbedPane.addChangeListener(new myListener());
	}
	private void RegistTab(){
		//タブのレイアウトを登録
		m_Frame.add(m_TabbedPane,BorderLayout.CENTER);
		m_BottomLabel = new JLabel();
		m_Frame.add(m_BottomLabel,BorderLayout.SOUTH);
	}

	//メニューバーから特定の要項がクリックされた時に、各機能を動作させる。
	public void actionPerformed(ActionEvent e){
		String event=e.getActionCommand();
		//「ファイル」→「新規作成」が選択された時
		if(event=="新規作成"){
			FileUnit f;
			try {
				//ファイル情報管理クラスのインスタンスを作成
				f = new FileUnit(m_files.size()+1,CurrentEncoding());
				//ファイル情報管理クラスの配列に追加
				m_files.add(f);
				//新たなタブとテキスト変種エリアを生成する。
				TabManager.MakeNewTab(this,f.m_FileName,m_files.size(), m_TabbedPane,m_BottomLabel,m_files);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		//「ファイル」→「開く」が選択された時
		}else if(event=="開く"){
				try {
					//ファイルを開くメソッドを実行
					OpenFile();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		//「ファイル」→「上書き保存」が選択された時
		}else if(event=="上書き保存"){
			//現在開かれているタブのインデックスを取得
			int i=m_TabbedPane.getSelectedIndex();
			if(i==-1){
				//ファイルがひとつも開かれていない時に、それを伝えるメッセージを表示する
				JOptionPane.showMessageDialog(null, "ふぇぇ... ファイルが一つも開かれてないよぉ... ＞＜");
				return;
			//新規テキストファイルの場合
			}else if(m_files.get(i).m_FileName.length()>=11&&m_files.get(i).m_FileName.substring(0,11).equals("*新規テキストファイル")){
				try {
					//ファイル保存ダイアログを通じてファイルを保存し、保存先のパスを取得する
					String s=m_files.get(i).SaveFileDetailed(m_Frame, (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400,300).getComponentAt(400,300),CurrentEncoding());
					//タブ内で、ファイル名を格納しているラベルを取得
					JLabel j = (JLabel)(m_TabbedPane.getTabComponentAt(m_TabbedPane.getSelectedIndex()).getComponentAt(5, 2));
					//ファイルが保存された場合
					if(s!=null){
						m_files.get(i).m_FileName=s;//保存されたファイル名を代入
						j.setText(s);//ファイル名を格納しているラベルの値を、保存されたファイル名に変更
						m_files.get(i).m_Editted=false;//保存した後編集されたか否かのフラグを偽にする
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//既存のテキストファイルの場合
			}else{
				try {
					//上書き保存を行う
					m_files.get(i).SaveFile((JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400,300).getComponentAt(400, 300),CurrentEncoding());
					//保存した後編集されたか否かのフラグを偽にする
					m_files.get(i).m_Editted=false;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		//「ファイル」→「名前を付けて保存」が選択された時
		}else if(event=="名前を付けて保存"){
			//現在開かれているタブのインデックスを取得
			int i=m_TabbedPane.getSelectedIndex();
			if(i==-1){
				//ファイルがひとつも開かれていない時に、それを伝えるメッセージを表示する
				JOptionPane.showMessageDialog(null, "ふぇぇ... ファイルが一つも開かれてないよぉ... ＞＜");
				return;
			//新規テキストファイルの場合
			}else if(m_files.get(i).m_FileName.length()>=11&&m_files.get(i).m_FileName.substring(0,11).equals("*新規テキストファイル")){
				try {
					//ファイル保存ダイアログを通じてファイルを保存し、保存先のパスを取得する
					String s=m_files.get(i).SaveFileDetailed(m_Frame, (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400,300).getComponentAt(400,300),CurrentEncoding());
					//タブ内で、ファイル名を格納しているラベルを取得
					JLabel j = (JLabel)(m_TabbedPane.getTabComponentAt(m_TabbedPane.getSelectedIndex()).getComponentAt(5, 2));
					//ファイルが保存された場合
					if(s!=null){
						m_files.get(i).m_FileName=s;//保存されたファイル名を代入
						j.setText(s);//ファイル名を格納しているラベルの値を、保存されたファイル名に変更
						m_files.get(i).m_Editted=false;//保存した後編集されたか否かのフラグを偽にする
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//既存のテキストファイルの場合
			}else{
				try {
					//ファイル保存ダイアログを通じてファイルを保存する
					m_files.get(i).SaveFileDetailed(m_Frame, (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400,300).getComponentAt(400, 300),CurrentEncoding());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		//「検索」→「検索」メニューが選択されたとき
		}else if(event=="検索"){
			if(m_TabbedPane.getComponentCount()!=0){
				//検索ウィンドウのタイトルを"文字列を検索"+ファイル名の文字列表現にセット
				m_FindWindow.setTitle("文字列を検索-"+m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName);
				//検索ウィンドウを可視化
				m_FindWindow.setVisible(true);
				//検索先テキストエリア(テキストペーン)を開かれているタブ内のテキストペーンに変更
				m_FindWindow.SetJTextPane((JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300));
			}else{
				//ファイルがひとつもひらかれていないとき
				JOptionPane.showMessageDialog(null, "ふぇぇ... ファイルが一つも開かれてないよぉ... ＞＜");
			}
		//「検索」→「置き換え」メニューが選択された時
		}else if(event=="置き換え"){
			if(m_TabbedPane.getComponentCount()!=0){
				//置き換えウィンドウのタイトルを"文字列を置き換え"+ファイル名の文字列表現にセット
				m_ReplaceWindow.setTitle("文字列を置き換え-"+m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName);
				//置き換えウィンドウを可視化
				m_ReplaceWindow.setVisible(true);
				//置き換え先テキストエリア(テキストペーン)を開かれているタブ内のテキストペーンに変更
				m_ReplaceWindow.SetJTextPane((JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300));
			}else{
				//ファイルがひとつもひらかれていないとき
				JOptionPane.showMessageDialog(null, "ふぇぇ... ファイルが一つも開かれてないよぉ... ＞＜");
			}
			//「検索」→「逐次検索」メニューが選択された時
		}else if(event=="逐次検索"){
			if(m_TabbedPane.getComponentCount()!=0){
				//逐次検索と置き換えウィンドウのタイトルを"逐次検索と置き換え"+ファイル名の文字列表現にセット
				m_OneByOne.setTitle("逐次検索と置き換え-"+m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName);
				//逐次検索と置き換えウィンドウを可視化
				m_OneByOne.setVisible(true);
				//逐次検索と置き換え先テキストエリア(テキストペーン)を開かれているタブ内のテキストペーンに変更
				m_OneByOne.SetJTextPane((JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300));
			}else{
				//ファイルがひとつもひらかれていないとき
				JOptionPane.showMessageDialog(null, "ふぇぇ... ファイルが一つも開かれてないよぉ... ＞＜");
			}
		//「編集」→「コピー」
		}else if(event=="コピー"){
			if(m_TabbedPane.getComponentCount()!=0){
				//選択されているタブのテキストペーンを取得し、クリップボードにコピー
				JTextPane p = (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300);
				p.copy();
			}else{
				//ファイルがひとつもひらかれていないとき
				JOptionPane.showMessageDialog(null, "ふぇぇ... ファイルが一つも開かれてないよぉ... ＞＜");
			}
			//「編集」→「切り取り」
		}else if(event=="切り取り"){
			//選択されているタブのテキストペーンを取得し、クリップボードにカット
			if(m_TabbedPane.getComponentCount()!=0){
				JTextPane p = (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300);
				p.cut();
			}else{
				//ファイルがひとつもひらかれていないとき
				JOptionPane.showMessageDialog(null, "ふぇぇ... ファイルが一つも開かれてないよぉ... ＞＜");
			}
			//「編集」→「貼付け」
		}else if(event=="貼付け"){
			//選択されているタブのテキストペーンを取得し、貼付け
			if(m_TabbedPane.getComponentCount()!=0){
				JTextPane p = (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300);
				p.paste();
			}else{
				//ファイルがひとつもひらかれていないとき
				JOptionPane.showMessageDialog(null, "ふぇぇ... ファイルが一つも開かれてないよぉ... ＞＜");
			}
			//「編集」→「全て選択」
		}else if(event=="全て選択"){
			//選択されているタブのテキストペーンを取得し、全て選択
			if(m_TabbedPane.getComponentCount()!=0){
				JTextPane p = (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300);
				p.selectAll();
			}else{
				//ファイルがひとつもひらかれていないとき
				JOptionPane.showMessageDialog(null, "ふぇぇ... ファイルが一つも開かれてないよぉ... ＞＜");
			}				
		}
	}

	//フレームウィンドウの初期化
	public FrameWrapper(){
		//ファイル情報管理クラス配列の初期化
		m_files = new ArrayList<FileUnit>();
		//検索、置き換え、逐次検索のウィンドウを初期化
		m_FindWindow = new FindWindow();
		m_ReplaceWindow = new ReplaceWindow();
		m_OneByOne = new OneByOne();
		//フレーム、タブ、ウィジェット定義、タブの内容を初期化
		InitFrame();
		InitTabbedPane();
		WidgetDefinition.Regist(m_Frame, this);
		RegistTab();
		
		//ウィンドウを閉じる前に、保存されていないまま編集するファイルがあったら保存を促すメッセージボックスを表示し、保存する場合、
		//新規テキストファイルなら保存ダイアログを通じて保存、既存のファイルならそのまま上書き保存する。
		m_Frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				for(int i=0;i<m_files.size();i++){
					if(m_files.get(i).m_Editted){
						int option = JOptionPane.showConfirmDialog(m_Frame, m_files.get(i).m_FileName+"は変更されてるよ... 保存する?","警告", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
						if(option==JOptionPane.YES_OPTION){
							if(m_files.get(i).m_FileName.length()>=11&&m_files.get(i).m_FileName.substring(0,11).equals("*新規テキストファイル")){
								try {
									m_files.get(i).SaveFileDetailed(m_Frame, (JTextPane)m_TabbedPane.getComponentAt(i).getComponentAt(400,300).getComponentAt(400,300),CurrentEncoding());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								try {
									m_files.get(i).SaveFile((JTextPane)m_TabbedPane.getComponentAt(i).getComponentAt(400, 300).getComponentAt(400, 300),CurrentEncoding());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		});
	}

	//フレームの可視と不可視を設定するメソッド
	public void setFrameVisible(boolean t){
		m_Frame.setVisible(t);
	}
	
}
