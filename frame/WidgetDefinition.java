package frame;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;


public class WidgetDefinition {
	public static void Regist(JFrame jframe,ActionListener al){
		JMenuBar menubar=new JMenuBar();
		ArrayList<String> str=new ArrayList<String>();
		
		//�g�b�v���x���̃��j���[��o�^����
		str.add("�t�@�C��");
		str.add("�ҏW");
		str.add("�G���R�[�f�B���O");
		str.add("����");
		JMenu menu[] = new JMenu[str.size()];
		for(int i=0;i<str.size();++i){
			menu[i] = new JMenu(str.get(i));
			menubar.add(menu[i]);
		}
		str.clear();
		//�u�t�@�C���v���̃��j���[��o�^����
		str.add("�V�K�쐬");
		str.add("�J��");
		str.add("�㏑���ۑ�");
		str.add("���O��t���ĕۑ�");
		JMenuItem jmenu1[];
		jmenu1=new JMenuItem[str.size()];
		for(int i=0;i<str.size();++i){
			jmenu1[i]=new JMenuItem(str.get(i));
			menu[0].add(jmenu1[i]);
			jmenu1[i].addActionListener(al);
		}
		str.clear();
		//�u�ҏW�v���̃��j���[��o�^����
		str.add("�R�s�[");
		str.add("�؂���");
		str.add("�\�t��");
		str.add("�S�đI��");
		JMenuItem jmenu2[];
		jmenu2=new JMenuItem[str.size()];
		for(int i=0;i<str.size();++i){
			jmenu2[i]=new JMenuItem(str.get(i));
			menu[1].add(jmenu2[i]);
			jmenu2[i].addActionListener(al);
		}
		str.clear();
		//�u�G���R�[�h�v���̃��j���[��o�^����
		str.add("UTF-8");
		str.add("Shift_Jis");
		str.add("EUC-JP");
		str.add("UTF-16");
		str.add("ANSI");
		JRadioButtonMenuItem jmenu6[];
		jmenu6=new JRadioButtonMenuItem[str.size()];
		ButtonGroup group = new ButtonGroup();
		
		for(int i=0;i<str.size();++i){
			jmenu6[i]=new JRadioButtonMenuItem(str.get(i));
			if(i==0) jmenu6[i].setSelected(true);
			group.add(jmenu6[i]);
			menu[2].add(jmenu6[i]);
			jmenu6[i].addActionListener(al);
		}
		str.clear();
		//�u�����v���̃��j���[��o�^����
		str.add("����");
		str.add("�u������");
		str.add("��������");
		JMenuItem jmenu3[];
		jmenu3=new JMenuItem[str.size()];
		for(int i=0;i<str.size();++i){
			jmenu3[i]=new JMenuItem(str.get(i));
			menu[3].add(jmenu3[i]);
			jmenu3[i].addActionListener(al);
		}
		str.clear();
		//�t���[���E�B���h�E�̃��j���[�Ɏ��ۂɓo�^����
		jframe.setJMenuBar(menubar);			
	}
}
