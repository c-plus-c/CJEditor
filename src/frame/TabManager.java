package frame;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import file.FileUnit;

//タブに関する処理を行う
public class TabManager extends JFrame{

	public TabManager()
	{
		super();
	}

	//新規テキストファイル選択時の編集エリアとタブを生成
	public void MakeNewTab(final FrameWrapper Frame,String str,int i,final JTabbedPane tp,final JLabel lab,final ArrayList<FileUnit> files){
		JTextPane area=new JTextPane();
		//テキストファイルのどこにカーソルがあるかを取得するようにする
		class TextPaneCangeCursor implements CaretListener,DocumentListener{
			JTextPane area;
			public TextPaneCangeCursor(JTextPane p){
				area=p;
			}
			public void caretUpdate(CaretEvent e){
				String str = area.getText();
				str = str.replaceAll("\r\n", "\n");
				str = str.substring(0, area.getSelectionEnd()) + "a";
				String line[] = str.split("\n");
				int sum=0;
				for(int i=0;i<line.length-1;++i){
					sum+=line[i].length();
				}
				lab.setText((tp.getTabCount()!=0)?"現在位置"+line.length + "行目"+(area.getSelectionEnd()-sum-line.length+2)+"列目":"");
			}
			//もし開いているテキストペーンが更新されたらされたら、保存していないまま更新されたフラグをtrueにする
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				if(tp.getTabCount()>=1){
					files.get(tp.getSelectedIndex()).m_Editted=true;
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				if(tp.getTabCount()>=1){
					files.get(tp.getSelectedIndex()).m_Editted=true;
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				if(tp.getTabCount()>=1){
					files.get(tp.getSelectedIndex()).m_Editted=true;
				}
			}
		}
		area.addCaretListener(new TextPaneCangeCursor(area));
		area.getDocument().addDocumentListener(new TextPaneCangeCursor(area));
		//タブ内に載せるアイコンを選択
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout(5,5));
		JLabel label = new JLabel(str,UIManager.getIcon("FileView.fileIcon"),JLabel.LEFT);
		panel.add(label,BorderLayout.CENTER);

		ClassLoader loader = this.getClass().getClassLoader();

		URL url = loader.getResource("1168_16.png");
		Image im = null;

		try
		{
			im = this.createImage((ImageProducer)url.getContent());
		}catch(Exception ex)
		{

		}

		ImageIcon icon = new ImageIcon(im);
		JLabel closeLabel = new JLabel(icon);
		//タブ内の「✕」ボタン(closelabel.gif)をクリックすると、そのタブが閉じる。保存されていないまま閉じようとしたら、保存を促すメッセージを表示し、保存の手続きを行う
		closeLabel.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent event) {
		        // ボタンが押されたタブを選択
		        Component tabComp = ((Component)event.getSource()).getParent();
		        int index = tp.indexOfTabComponent(tabComp);

				if(files.get(index).m_Editted){
					int option = JOptionPane.showConfirmDialog(Frame.m_Frame, files.get(index).m_FileName+"は変更されてるよ... 保存する?","警告", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
					if(option==JOptionPane.YES_OPTION){
						if(files.get(index).m_FileName.length()>=11&&files.get(index).m_FileName.substring(0,11).equals("*新規テキストファイル")){
							try {
								files.get(index).SaveFileDetailed(Frame.m_Frame, (JTextPane)tp.getComponentAt(index).getComponentAt(400,300).getComponentAt(400,300),Frame.CurrentEncoding());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							try {
								files.get(index).SaveFile((JTextPane)tp.getComponentAt(index).getComponentAt(400, 300).getComponentAt(400, 300),Frame.CurrentEncoding());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
		        // タブを削除
		        tp.remove(index);
		        files.remove(index);
		        lab.setText("");
		    }
		});
		//タブ内にラベルやタイトル名、閉じるためのラベルを選択
		//そのタブに対応するテキストペーンも追加する
		area.setText(files.get(i-1).m_Buffer);
		panel.add(closeLabel,BorderLayout.EAST);
		JScrollPane scrollpane1=new JScrollPane(area);
		tp.addTab(null,scrollpane1);
		tp.setTabComponentAt(i-1, panel);
	}

	//既存のファイル選択時の編集エリアとタブを生成
	public void MakeNewTab(final FrameWrapper Frame,FileUnit f,int i,final JTabbedPane tp,final JLabel lab,final ArrayList<FileUnit> files){
		final JTextPane area=new JTextPane();
		//テキストファイルのどこにカーソルがあるかを取得するようにする
		class TextPaneCangeCursor implements CaretListener,DocumentListener{
			JTextPane area;
			public TextPaneCangeCursor(JTextPane p){
				area=p;
			}
			public void caretUpdate(CaretEvent e){
				String str = area.getText();
				str = str.replaceAll("\r\n", "\n");
				str = str.substring(0, area.getSelectionEnd()) + "a";
				String line[] = str.split("\n");
				int sum=0;
				for(int i=0;i<line.length-1;++i){
					sum+=(line[i].length());
				}
				lab.setText((tp.getTabCount()!=0)?"現在位置"+line.length + "行目"+(area.getSelectionEnd()-sum-line.length+2)+"列目":"");
			}
			//もし開いているテキストペーンが更新されたらされたら、保存していないまま更新されたフラグをtrueにする
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				if(tp.getTabCount()>=1){
					files.get(tp.getSelectedIndex()).m_Editted=true;
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				if(tp.getTabCount()>=1){
					files.get(tp.getSelectedIndex()).m_Editted=true;
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				if(tp.getTabCount()>=1){
					files.get(tp.getSelectedIndex()).m_Editted=true;
				}
			}
		}
		area.addCaretListener(new TextPaneCangeCursor(area));
		area.getDocument().addDocumentListener(new TextPaneCangeCursor(area));

		//テキストエリアにファイルから取得した文字列をセット
		area.setText(f.m_Buffer);

		//タブ内に載せるアイコンを選択
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout(5,5));

		JLabel label = new JLabel(f.m_FileName,UIManager.getIcon("FileView.fileIcon"),JLabel.LEFT);
		panel.add(label,BorderLayout.CENTER);
		//タブ内の「✕」ボタン(closelabel.gif)をクリックすると、そのタブが閉じる。保存されていないまま閉じようとしたら、保存を促すメッセージを表示し、保存の手続きを行う

		ClassLoader loader = this.getClass().getClassLoader();

		URL url = loader.getResource("1168_16.png");
		Image im = null;

		try
		{
			im = this.createImage((ImageProducer)url.getContent());
		}catch(Exception ex)
		{

		}
		ImageIcon icon = new ImageIcon(im);
		JLabel closeLabel = new JLabel(icon);
		closeLabel.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent event) {
		        // ボタンが押されたタブを選択
		        Component tabComp
		            = ((Component)event.getSource()).getParent();
		        int index = tp.indexOfTabComponent(tabComp);
				if(files.get(index).m_Editted){
					int option = JOptionPane.showConfirmDialog(Frame.m_Frame, files.get(index).m_FileName+"は変更されてるよ... 保存する?","警告", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
					if(option==JOptionPane.YES_OPTION){
						if(files.get(index).m_FileName.length()>=11&&files.get(index).m_FileName.substring(0,11).equals("*新規テキストファイル")){
							try {
								files.get(index).SaveFileDetailed(Frame.m_Frame, (JTextPane)tp.getComponentAt(index).getComponentAt(400,300).getComponentAt(400,300),Frame.CurrentEncoding());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							try {
								files.get(index).SaveFile((JTextPane)tp.getComponentAt(index).getComponentAt(400, 300).getComponentAt(400, 300),Frame.CurrentEncoding());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
		        // タブを削除
		        tp.remove(index);
		        files.remove(index);
		        lab.setText("");
		    }
		});
		panel.add(closeLabel,BorderLayout.EAST);
		//タブ内にラベルやタイトル名、閉じるためのラベルを選択
		//そのタブに対応するテキストペーンも追加する
		JScrollPane scrollpane1=new JScrollPane(area);
		tp.addTab(null,scrollpane1);
		tp.setTabComponentAt(i-1, panel);
	}
}
