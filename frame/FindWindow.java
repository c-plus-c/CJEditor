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
//検索ウィンドウを扱うクラス
public class FindWindow extends JFrame{
	//検索する文字列の内容を書き込むテキストフィールド
	private JTextField m_Query;
	//検索ボタンフィールド
	private JButton m_SearchButton;
	//検索先テキストペーンフィールド
	public JTextPane m_Field;
	
	FindWindow(){
		//ウィンドウのタイトルを"文字列を検索"に指定
		super("文字列を検索");	
		//ウィンドウサイズの指定
		setSize(400,100);
		//リサイズ不可にする
		setResizable(false);
		
		//この検索ウィンドウが閉じられる時、背景を黄色にするハイライトを消すようにする
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				TextProcessor.ResetAttribute(m_Field);
			}
		});
		JPanel p = new JPanel();
		//パネルに追加するテキストフィールドと検索ボタンのインスタンスを作成する。
		m_Query=new JTextField();
		m_Query.setColumns(20);
		m_SearchButton = new JButton("検索");
		
		//検索ボタンが押された時に、テキストペーン内で文字列を検索し、見つかった場所の背景を黄色にハイライトするようにする。
		class ActionAdapter implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				if(m_Query.getText().length()!=0||!m_Query.getText().isEmpty())
					TextProcessor.FindString(m_Query.getText(), m_Field);
			}
		};
		m_SearchButton.addActionListener(new ActionAdapter());
		
		//パネルにテキストフィールド、ボタン、ラベルなどを追加
		JLabel jl = new JLabel("検索したい文字列を入力して検索ボタンを押してください。");
		p.add(jl);
		p.add(m_Query);
		p.add(m_SearchButton);
		getContentPane().add(p,BorderLayout.CENTER);
		//ウィンドウの「✕」ボタンが押された時に、このウィンドウを隠すようにする。
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	//FindWindowのコンストラクタ。
	FindWindow(String title,JTextPane jfield){
		super(title);
		m_Field=jfield;
		setSize(400,100);
	}
	//FindWindowが取り扱うテキストペーンをセットするメソッド
	public void SetJTextPane(JTextPane jfield){
		m_Field=jfield;
	}
	
}
