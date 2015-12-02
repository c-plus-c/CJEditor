package frame;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TextProcessor {
	private static int m_Current=0;
	private static boolean m_RecentReplaced=false;
	private static boolean m_found=false;
	private static boolean m_init=false;

	//リセット処理
	public static void ResetParam(){
		m_Current=0;
		m_RecentReplaced=false;
		m_found=false;
		m_init=false;
	}
	//テキストペーン内のm_Current番目以降の場所から、query文字列が、最初に見つかるところを検出し、背景を黄色でハイライトする
	//その見つかった場所を、新たなm_Currentとする。
	public static int FindNext(String query,JTextPane jfield){
		if(m_RecentReplaced==false&&m_init==true){
			m_Current+=query.length();
		}
		m_init=true;
		m_RecentReplaced=false;
		String field=jfield.getText();
		m_Current=field.indexOf(query,m_Current);
		if(m_Current==-1){
			m_Current=0;
			m_Current=field.indexOf(query,m_Current);
		}
		m_found=true;

		StyledDocument doc = (StyledDocument) jfield.getDocument();
		SimpleAttributeSet plane = new SimpleAttributeSet();
		doc.setCharacterAttributes(0, field.length(), plane, true);

		plane = new SimpleAttributeSet();
		StyleConstants.setBackground(plane, Color.YELLOW);
		doc.setCharacterAttributes(m_Current, query.length(), plane,true);
		return m_Current;
	}


	//テキストペーン内で前回の検索で発見された箇所を、置き換えたい文字列に置き換える
	//置き換えた場所の一つ次を新たなm_Currentとする。
	public static int Replace(String query,String result,JTextPane jfield){
		m_RecentReplaced=true;
		if(m_found==true){
			StringBuffer field=new StringBuffer(jfield.getText());

			StyledDocument doc = (StyledDocument) jfield.getDocument();
			SimpleAttributeSet plane = new SimpleAttributeSet();
			doc.setCharacterAttributes(0, field.length(), plane, true);
			field.replace(m_Current, m_Current+query.length(), result);
			jfield.setText(field.toString());
			m_Current+=result.length();
			return FindNext(query,jfield);
		}else{
			return -1;
		}
	}

	//テキストエリア(ペーン)内のquery文字列を全て探し出し、見つかった場所の背景を黄色でハイライトする
	private static void FindStr(String query,JTextPane jfield){
		String field=jfield.getText();
		StyledDocument doc = (StyledDocument) jfield.getDocument();
		SimpleAttributeSet plane = new SimpleAttributeSet();
		doc.setCharacterAttributes(0, field.length(), plane, true);

		SimpleAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setBackground(attr, Color.YELLOW);
		String[] keywords={query};
		for(int i=0;i<keywords.length;++i){
			String keyword = keywords[i];
			int pos = field.indexOf(keyword);
			while(pos!=-1){
				doc.setCharacterAttributes(pos, keyword.length(), attr, true);
				pos = field.indexOf(keyword, pos+keyword.length());
			}
		}
	}
	//FindStrメソッドを非同期的に実行
	public static void FindString(final String query,final JTextPane jfield){
		SwingUtilities.invokeLater(new Runnable(){

			public void run(){
				FindStr(query,jfield);
			}
		});
	}

	//指定したテキストエリア(ペーン)内でのハイライトをリセットする
	public static void ResetAttribute(final JTextPane jfield){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				StyledDocument doc = (StyledDocument) jfield.getDocument();
				SimpleAttributeSet plane = new SimpleAttributeSet();
				doc.setCharacterAttributes(0, jfield.getText().length(), plane, true);

				SimpleAttributeSet attr = new SimpleAttributeSet();
				doc.setCharacterAttributes(0, jfield.getText().length(), attr, true);
			}
		});
	}

	//テキストエリア(ペーン)内のqueryの文字列をresultに置き換える。
	private static void ReplaceStr(final String query,final String result,final JTextPane jfield){
		String re=jfield.getText().replaceAll(query, result);
		jfield.setText(re);
	}
	//ReplaceStrメソッドを非同期的に実行
	public static void ReplaceString(final String query,final String result,final JTextPane jfield){
		SwingUtilities.invokeLater(new Runnable(){

			public void run(){
				ReplaceStr(query,result,jfield);
			}
		});
	}
}
