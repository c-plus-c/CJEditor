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
//逐次検索と置き換えを扱うクラス
public class OneByOne extends JFrame{
	//検索する文字列の内容を書き込むテキストフィールド
	private JTextField m_Query;
	//置き換える後の文字列の内容を書き込むテキストフィールド
	private JTextField m_Request;
	//検索ボタンと置き換えボタンのフィールド
	private JButton m_SearchButton;
	private JButton m_ReplaceButton;
	JTextPane m_Field;
	
	OneByOne(){
		//このウィンドウの設定
		super("逐次検索と置き換え");	
		setSize(420,150);
		
		//このウィンドウを閉じた時に、検索された場所の背景を黄色にハイライトするのを取り消す
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				TextProcessor.ResetParam();
				TextProcessor.ResetAttribute(m_Field);
			}
		});
		//パネルに追加するテキストフィールドと置き換えボタンのインスタンスを作成する
		JPanel p = new JPanel();
		m_Query=new JTextField();
		m_Query.setColumns(20);
		
		m_Request = new JTextField();
		m_Request.setColumns(20);
		m_ReplaceButton = new JButton("置き換え");
		m_SearchButton = new JButton("検索");
		//検索ボタンが押されたら、以前に見つかった場所以降の次の文字列を検索する
		class ActionAdapter1 implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				if(m_Query.getText().length()!=0||!m_Query.getText().isEmpty()){
					TextProcessor.FindNext(m_Query.getText(),m_Field);
				}
			}
		};
		//検索ボタンが押されたら、以前に見つかった場所の文字列を置き換える
		class ActionAdapter2 implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				if(m_Query.getText().length()!=0||!m_Query.getText().isEmpty()){
					TextProcessor.Replace(m_Query.getText(),m_Request.getText(), m_Field);
				}
			}
		};
		//ボタンにアクションを登録
		m_SearchButton.addActionListener(new ActionAdapter1());
		m_ReplaceButton.addActionListener(new ActionAdapter2());
		//パネルにテキストフィールド、ボタン、ラベルなどを追加
		JLabel jl = new JLabel("検索したい文字列と置き換え後のテキストを入力してください。");
		p.add(jl);
		p.add(new JLabel("検索テキスト"));
		p.add(m_Query);
		p.add(new JLabel("置き換え後テキスト"));
		p.add(m_Request);
		p.add(m_SearchButton);
		p.add(m_ReplaceButton);
		getContentPane().add(p,BorderLayout.CENTER);
		//リサイズ不可
		setResizable(false);
		//ウィンドウの「✕」ボタンが押された時に、このウィンドウを隠すようにする。
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	//コンストラクタ
	OneByOne(String title,JTextPane jfield){
		super(title);
		m_Field=jfield;
		setSize(400,100);
	}
	//取り扱うテキストペーンをセットするメソッド
	public void SetJTextPane(JTextPane jfield){
		m_Field=jfield;
	}
}
