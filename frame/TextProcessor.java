package frame;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import javax.swing.*;
import javax.swing.text.StyleConstants;

public class TextProcessor {
	private static int m_Current=0;
	private static boolean m_RecentReplaced=false;
	private static boolean m_found=false;
	private static boolean m_init=false;
	
	//���Z�b�g����
	public static void ResetParam(){
		m_Current=0;
		m_RecentReplaced=false;
		m_found=false;
		m_init=false;
	}
	//�e�L�X�g�y�[������m_Current�Ԗڈȍ~�̏ꏊ����Aquery�����񂪁A�ŏ��Ɍ�����Ƃ�������o���A�w�i�����F�Ńn�C���C�g����
	//���̌��������ꏊ���A�V����m_Current�Ƃ���B
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
	

	//�e�L�X�g�y�[�����őO��̌����Ŕ������ꂽ�ӏ����A�u����������������ɒu��������
	//�u���������ꏊ�̈����V����m_Current�Ƃ���B
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

	//�e�L�X�g�G���A(�y�[��)����query�������S�ĒT���o���A���������ꏊ�̔w�i�����F�Ńn�C���C�g����
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
	//FindStr���\�b�h��񓯊��I�Ɏ��s
	public static void FindString(final String query,final JTextPane jfield){
		SwingUtilities.invokeLater(new Runnable(){
			
			public void run(){
				FindStr(query,jfield);
			}
		});
	}

	//�w�肵���e�L�X�g�G���A(�y�[��)���ł̃n�C���C�g�����Z�b�g����
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

	//�e�L�X�g�G���A(�y�[��)����query�̕������result�ɒu��������B
	private static void ReplaceStr(final String query,final String result,final JTextPane jfield){
		String re=jfield.getText().replaceAll(query, result);
		jfield.setText(re);
	}
	//ReplaceStr���\�b�h��񓯊��I�Ɏ��s
	public static void ReplaceString(final String query,final String result,final JTextPane jfield){
		SwingUtilities.invokeLater(new Runnable(){
			
			public void run(){
				ReplaceStr(query,result,jfield);
			}
		});		
	}
}
