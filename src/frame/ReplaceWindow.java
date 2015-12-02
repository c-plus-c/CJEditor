package frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
//置き換えウィンドウを扱うクラス
public class ReplaceWindow extends JFrame{
	//置き換える前の文字列の内容を書き込むテキストフィールド
	private JTextField m_Query;
	//置き換える後の文字列の内容を書き込むテキストフィールド
	private JTextField m_Request;
	//置き換えボタンフィールド
	private JButton m_SearchButton;
	//置き換え先テキストペーンフィールド
	public JTextPane m_Field;
	
	ReplaceWindow(){
		//ウィンドウのタイトルをとウィンドウサイズを指定
		super("文字列を置き換え");	
		setSize(430,150);
		//パネルに追加するテキストフィールドと置き換えボタンのインスタンスを作成する。
		JPanel p = new JPanel();
		m_Query=new JTextField();
		m_Query.setColumns(20);
		
		m_Request = new JTextField();
		m_Request.setColumns(20);
		m_SearchButton = new JButton("置き換え");
		//「置き換え」ボタンがクリックされた時、テキストペーン内で文字列を置き換える。
		class ActionAdapter implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				if(m_Query.getText().length()!=0||!m_Query.getText().isEmpty()){
					TextProcessor.ReplaceString(m_Query.getText(),m_Request.getText(), m_Field);
				}
			}
		};
		//パネルにテキストフィールド、ボタン、ラベルなどを追加
		m_SearchButton.addActionListener(new ActionAdapter());
		JLabel jl = new JLabel("置き換えたい文字列を入力して置き換えボタンを押してください。");
		p.add(jl);
		p.add(new JLabel("置き換え前テキスト"));
		p.add(m_Query);
		p.add(new JLabel("置き換え後テキスト"));
		p.add(m_Request);
		p.add(m_SearchButton);
		getContentPane().add(p,BorderLayout.CENTER);
		//リサイズ不可にする
		setResizable(false);
		//ウィンドウの「✕」ボタンが押された時に、このウィンドウを隠すようにする。
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	//コンストラクタ
	ReplaceWindow(String title,JTextPane jfield){
		super(title);
		m_Field=jfield;
		setSize(400,100);
	}
	//取り扱うテキストペーンをセットするメソッド
	public void SetJTextPane(JTextPane jfield){
		m_Field=jfield;
	}
	
}
