package file;
import javax.swing.*;

import java.io.*;

import javax.swing.JFileChooser;
//開かれているテキストファイルのパス、ファイル名、内容、エンコードなどを管理。保存処理も行う。
public class FileUnit {
	public String m_FilePath;//ファイルへのパス
	public String m_FileName;//ファイル名
	public String m_Buffer;//ファイルの内容
	public String m_Encode;//ファイルのエンコード
	public boolean m_Editted;//ファイルが保存されていないまま編集が行われたか否か。
	
	//デフォルトコンストラクタ(未使用)
	public FileUnit(){
		m_FilePath="";
		m_FileName="*新規テキストファイル";
		m_Buffer="";
		m_Encode="";
		m_Editted=false;
	}
	
	//「ファイル」→「新規作成」から新たなテキストエリアタブを表示した時、ファイル情報を初期化するコンストラクタ。
	public FileUnit(int num,String enc) throws IOException{
		m_FilePath="";
		m_FileName="*新規テキストファイル"+Integer.toString(num);//"*新規テキストファイル"+文字列(既に開かれているタブ数+1)をファイル名に設定。これがタブに表示される。
		m_Buffer="";//ファイルの内容を設定
		m_Encode=enc;//ファイルのエンコードを設定
		m_Editted=false;
		String line;
		//UTF-8エンコードのdammy.txtファイルを読み込む。(これをしないとテキストエリア内の文字列の検索や置き換えがうまくいかない)
		BufferedReader readfile = new BufferedReader(new InputStreamReader(new FileInputStream("./dammy.txt"),"UTF-8"));
		while((line=readfile.readLine())!=null){
			m_Buffer+=(line+"\n");
		}
	}
	
	public FileUnit(String s1,String s2){
		m_FilePath=s1;
		m_FileName=s2;
		m_Buffer="";
		m_Encode="";
		m_Editted=false;
	}

	//「ファイル」→「開く」メニューからファイルを選択して開く時に呼び出されるコンストラクタ
	public FileUnit(File fi,String enc) throws Exception {
		String line;
		//選択されたファイルの情報を取得し、ファイルを読み込むクラスのインスタンスを作成。
		BufferedReader readfile = new BufferedReader(new InputStreamReader(new FileInputStream(fi),enc));
		//ファイルへの絶対パスを取得し、ファイルのパスへ登録する。
		this.m_FilePath=fi.getAbsolutePath();
		//ファイルの名前を取得し、ファイル名へ登録する。
		this.m_FileName=fi.getName();
		this.m_Buffer="";
		//エンコードを設定。
		this.m_Encode=enc;
		//ファイルの内容をすべて読み込む
		m_Editted=false;
		while((line=readfile.readLine())!=null){
			m_Buffer+=(line+"\n");
		}
	}
	
	//ファイルを上書き保存するメソッド
	public void SaveFile(JTextPane jTextPane,String Encode) throws IOException{
		m_Encode=Encode;
		//エンコードを指定して、ファイル書き出しクラスのインスタンスを作成
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(m_FilePath),Encode)));
		//テキストエリアの内容を取得。
		m_Buffer=jTextPane.getText();
		//ファイルストリームに書き込む。
		pw.println(jTextPane.getText());
		pw.close();
		//保存終了メッセージの表示
		JOptionPane.showMessageDialog(null, "保存したよっ!!(*⌒∇⌒*)");
	}

	public String SaveFileDetailed(JFrame m_Frame,JTextPane jTextPane,String Encode) throws IOException{
		m_Encode=Encode;
		//ファイル保存ダイアログの表示
		JFileChooser filechooser = new JFileChooser();
		//保存するか否かを取得
		int selected=filechooser.showSaveDialog(m_Frame);
		//ファイルが保存される時
		if(selected==JFileChooser.APPROVE_OPTION){
			//選択されたファイルの情報を取得
			File sav = filechooser.getSelectedFile();
			//エンコードを指定して書きだす
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sav),Encode)));
			//テキストエリア内の内容を取得
			m_Buffer=jTextPane.getText();
			//ファイルストリームに書きだす
			pw.println(jTextPane.getText());
			pw.close();
			//保存終了メッセージの表示
			JOptionPane.showMessageDialog(null, "保存したよっ!!(*⌒∇⌒*)");
			//保存名を返す。
			return sav.getName();
		}
		//ファイルを保存しないときはnullを返す。
		return null;
	}
}
