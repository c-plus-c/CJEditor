package frame;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;


public class WidgetDefinition {
	public static void Regist(JFrame jframe,ActionListener al){
		JMenuBar menubar=new JMenuBar();
		ArrayList<String> str=new ArrayList<String>();

		//トップレベルのメニューを登録する
		str.add("ファイル");
		str.add("編集");
		str.add("エンコーディング");
		str.add("検索");
		JMenu menu[] = new JMenu[str.size()];
		for(int i=0;i<str.size();++i){
			menu[i] = new JMenu(str.get(i));
			menubar.add(menu[i]);
		}
		str.clear();
		//「ファイル」内のメニューを登録する
		str.add("新規作成");
		str.add("開く");
		str.add("上書き保存");
		str.add("名前を付けて保存");
		JMenuItem jmenu1[];
		jmenu1=new JMenuItem[str.size()];
		for(int i=0;i<str.size();++i){
			jmenu1[i]=new JMenuItem(str.get(i));
			menu[0].add(jmenu1[i]);
			jmenu1[i].addActionListener(al);
		}
		str.clear();
		//「編集」内のメニューを登録する
		str.add("コピー");
		str.add("切り取り");
		str.add("貼付け");
		str.add("全て選択");
		JMenuItem jmenu2[];
		jmenu2=new JMenuItem[str.size()];
		for(int i=0;i<str.size();++i){
			jmenu2[i]=new JMenuItem(str.get(i));
			menu[1].add(jmenu2[i]);
			jmenu2[i].addActionListener(al);
		}
		str.clear();
		//「エンコード」内のメニューを登録する
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
		//「検索」内のメニューを登録する
		str.add("検索");
		str.add("置き換え");
		str.add("逐次検索");
		JMenuItem jmenu3[];
		jmenu3=new JMenuItem[str.size()];
		for(int i=0;i<str.size();++i){
			jmenu3[i]=new JMenuItem(str.get(i));
			menu[3].add(jmenu3[i]);
			jmenu3[i].addActionListener(al);
		}
		str.clear();
		//フレームウィンドウのメニューに実際に登録する
		jframe.setJMenuBar(menubar);
	}
}
