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


//�t���[�����̋@�\��t���[���̊Ǘ����s���N���X
public class FrameWrapper implements ActionListener{
	public JFrame m_Frame;
	private JTabbedPane m_TabbedPane;
	private ArrayList<FileUnit> m_files;
	private JLabel m_BottomLabel;
	private FindWindow m_FindWindow;
	private ReplaceWindow m_ReplaceWindow;
	private OneByOne m_OneByOne;
	
	//���݂̃G�f�B�^�[�̃G���R�[�f�B���O���擾���郁�\�b�h
	public String CurrentEncoding(){
		for(int i=0;i<m_Frame.getJMenuBar().getMenu(2).getItemCount();++i){
			if(m_Frame.getJMenuBar().getMenu(2).getItem(i).isSelected()) return m_Frame.getJMenuBar().getMenu(2).getItem(i).getText();
		}
		return "NOT FOUND";
	}

	//�t�@�C�����J�����\�b�h
	private void OpenFile() throws Exception{
		//�t�@�C�����J���_�C�A���O��\�����āA���̌��ʂ��擾����B
		JFileChooser filechooser = new JFileChooser();
		int selected=filechooser.showOpenDialog(m_Frame);
		//�t�@�C�����I�����ꂽ�ꍇ
		if(selected==JFileChooser.APPROVE_OPTION){
			//�t�@�C���̏����擾
			File fil = filechooser.getSelectedFile();
			//�t�@�C���̏����t�@�C�����Ǘ��z��ɒǉ�
			FileUnit f=new FileUnit(fil,CurrentEncoding());
			m_files.add(f);
			//�V�����^�u�ŁA�t�@�C���̓��e���J���B
			TabManager.MakeNewTab(this,f,m_files.size(),m_TabbedPane,m_BottomLabel,m_files);
			
		}
	}

	private void InitFrame(){
		//�E�B���h�E�̏�������������
		m_Frame=new JFrame("CJEditor");
		m_Frame.setSize(800, 600);
		m_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	//�^�u�̏��������s�����\�b�h
	private void InitTabbedPane(){
		m_TabbedPane=new JTabbedPane();
		
		//�I�����Ă���^�u���ύX���ꂽ���A�E�B���h�E�̃^�C�g����"CJEditor"+(�^�u�ŊJ�����t�@�C���ւ̃p�X)�ɃZ�b�g����悤�ɂ���B
		class myListener implements ChangeListener{
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if(m_TabbedPane.getTabCount()<=0) {
					m_Frame.setTitle("CJEditor");
					return;
				}
				if(m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName.length()>=11&&m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName.substring(0,11).equals("*�V�K�e�L�X�g�t�@�C��")){
					m_Frame.setTitle("CJEditor:"+m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName);
				}else{
					m_Frame.setTitle("CJEditor:"+m_files.get(m_TabbedPane.getSelectedIndex()).m_FilePath);
				}
			}
		}
		m_TabbedPane.addChangeListener(new myListener());
	}
	private void RegistTab(){
		//�^�u�̃��C�A�E�g��o�^
		m_Frame.add(m_TabbedPane,BorderLayout.CENTER);
		m_BottomLabel = new JLabel();
		m_Frame.add(m_BottomLabel,BorderLayout.SOUTH);
	}

	//���j���[�o�[�������̗v�����N���b�N���ꂽ���ɁA�e�@�\�𓮍삳����B
	public void actionPerformed(ActionEvent e){
		String event=e.getActionCommand();
		//�u�t�@�C���v���u�V�K�쐬�v���I�����ꂽ��
		if(event=="�V�K�쐬"){
			FileUnit f;
			try {
				//�t�@�C�����Ǘ��N���X�̃C���X�^���X���쐬
				f = new FileUnit(m_files.size()+1,CurrentEncoding());
				//�t�@�C�����Ǘ��N���X�̔z��ɒǉ�
				m_files.add(f);
				//�V���ȃ^�u�ƃe�L�X�g�ώ�G���A�𐶐�����B
				TabManager.MakeNewTab(this,f.m_FileName,m_files.size(), m_TabbedPane,m_BottomLabel,m_files);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		//�u�t�@�C���v���u�J���v���I�����ꂽ��
		}else if(event=="�J��"){
				try {
					//�t�@�C�����J�����\�b�h�����s
					OpenFile();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		//�u�t�@�C���v���u�㏑���ۑ��v���I�����ꂽ��
		}else if(event=="�㏑���ۑ�"){
			//���݊J����Ă���^�u�̃C���f�b�N�X���擾
			int i=m_TabbedPane.getSelectedIndex();
			if(i==-1){
				//�t�@�C�����ЂƂ��J����Ă��Ȃ����ɁA�����`���郁�b�Z�[�W��\������
				JOptionPane.showMessageDialog(null, "�ӂ���... �t�@�C��������J����ĂȂ��悧... ����");
				return;
			//�V�K�e�L�X�g�t�@�C���̏ꍇ
			}else if(m_files.get(i).m_FileName.length()>=11&&m_files.get(i).m_FileName.substring(0,11).equals("*�V�K�e�L�X�g�t�@�C��")){
				try {
					//�t�@�C���ۑ��_�C�A���O��ʂ��ăt�@�C����ۑ����A�ۑ���̃p�X���擾����
					String s=m_files.get(i).SaveFileDetailed(m_Frame, (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400,300).getComponentAt(400,300),CurrentEncoding());
					//�^�u���ŁA�t�@�C�������i�[���Ă��郉�x�����擾
					JLabel j = (JLabel)(m_TabbedPane.getTabComponentAt(m_TabbedPane.getSelectedIndex()).getComponentAt(5, 2));
					//�t�@�C�����ۑ����ꂽ�ꍇ
					if(s!=null){
						m_files.get(i).m_FileName=s;//�ۑ����ꂽ�t�@�C��������
						j.setText(s);//�t�@�C�������i�[���Ă��郉�x���̒l���A�ۑ����ꂽ�t�@�C�����ɕύX
						m_files.get(i).m_Editted=false;//�ۑ�������ҏW���ꂽ���ۂ��̃t���O���U�ɂ���
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//�����̃e�L�X�g�t�@�C���̏ꍇ
			}else{
				try {
					//�㏑���ۑ����s��
					m_files.get(i).SaveFile((JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400,300).getComponentAt(400, 300),CurrentEncoding());
					//�ۑ�������ҏW���ꂽ���ۂ��̃t���O���U�ɂ���
					m_files.get(i).m_Editted=false;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		//�u�t�@�C���v���u���O��t���ĕۑ��v���I�����ꂽ��
		}else if(event=="���O��t���ĕۑ�"){
			//���݊J����Ă���^�u�̃C���f�b�N�X���擾
			int i=m_TabbedPane.getSelectedIndex();
			if(i==-1){
				//�t�@�C�����ЂƂ��J����Ă��Ȃ����ɁA�����`���郁�b�Z�[�W��\������
				JOptionPane.showMessageDialog(null, "�ӂ���... �t�@�C��������J����ĂȂ��悧... ����");
				return;
			//�V�K�e�L�X�g�t�@�C���̏ꍇ
			}else if(m_files.get(i).m_FileName.length()>=11&&m_files.get(i).m_FileName.substring(0,11).equals("*�V�K�e�L�X�g�t�@�C��")){
				try {
					//�t�@�C���ۑ��_�C�A���O��ʂ��ăt�@�C����ۑ����A�ۑ���̃p�X���擾����
					String s=m_files.get(i).SaveFileDetailed(m_Frame, (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400,300).getComponentAt(400,300),CurrentEncoding());
					//�^�u���ŁA�t�@�C�������i�[���Ă��郉�x�����擾
					JLabel j = (JLabel)(m_TabbedPane.getTabComponentAt(m_TabbedPane.getSelectedIndex()).getComponentAt(5, 2));
					//�t�@�C�����ۑ����ꂽ�ꍇ
					if(s!=null){
						m_files.get(i).m_FileName=s;//�ۑ����ꂽ�t�@�C��������
						j.setText(s);//�t�@�C�������i�[���Ă��郉�x���̒l���A�ۑ����ꂽ�t�@�C�����ɕύX
						m_files.get(i).m_Editted=false;//�ۑ�������ҏW���ꂽ���ۂ��̃t���O���U�ɂ���
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//�����̃e�L�X�g�t�@�C���̏ꍇ
			}else{
				try {
					//�t�@�C���ۑ��_�C�A���O��ʂ��ăt�@�C����ۑ�����
					m_files.get(i).SaveFileDetailed(m_Frame, (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400,300).getComponentAt(400, 300),CurrentEncoding());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		//�u�����v���u�����v���j���[���I�����ꂽ�Ƃ�
		}else if(event=="����"){
			if(m_TabbedPane.getComponentCount()!=0){
				//�����E�B���h�E�̃^�C�g����"�����������"+�t�@�C�����̕�����\���ɃZ�b�g
				m_FindWindow.setTitle("�����������-"+m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName);
				//�����E�B���h�E������
				m_FindWindow.setVisible(true);
				//������e�L�X�g�G���A(�e�L�X�g�y�[��)���J����Ă���^�u���̃e�L�X�g�y�[���ɕύX
				m_FindWindow.SetJTextPane((JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300));
			}else{
				//�t�@�C�����ЂƂ��Ђ炩��Ă��Ȃ��Ƃ�
				JOptionPane.showMessageDialog(null, "�ӂ���... �t�@�C��������J����ĂȂ��悧... ����");
			}
		//�u�����v���u�u�������v���j���[���I�����ꂽ��
		}else if(event=="�u������"){
			if(m_TabbedPane.getComponentCount()!=0){
				//�u�������E�B���h�E�̃^�C�g����"�������u������"+�t�@�C�����̕�����\���ɃZ�b�g
				m_ReplaceWindow.setTitle("�������u������-"+m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName);
				//�u�������E�B���h�E������
				m_ReplaceWindow.setVisible(true);
				//�u��������e�L�X�g�G���A(�e�L�X�g�y�[��)���J����Ă���^�u���̃e�L�X�g�y�[���ɕύX
				m_ReplaceWindow.SetJTextPane((JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300));
			}else{
				//�t�@�C�����ЂƂ��Ђ炩��Ă��Ȃ��Ƃ�
				JOptionPane.showMessageDialog(null, "�ӂ���... �t�@�C��������J����ĂȂ��悧... ����");
			}
			//�u�����v���u���������v���j���[���I�����ꂽ��
		}else if(event=="��������"){
			if(m_TabbedPane.getComponentCount()!=0){
				//���������ƒu�������E�B���h�E�̃^�C�g����"���������ƒu������"+�t�@�C�����̕�����\���ɃZ�b�g
				m_OneByOne.setTitle("���������ƒu������-"+m_files.get(m_TabbedPane.getSelectedIndex()).m_FileName);
				//���������ƒu�������E�B���h�E������
				m_OneByOne.setVisible(true);
				//���������ƒu��������e�L�X�g�G���A(�e�L�X�g�y�[��)���J����Ă���^�u���̃e�L�X�g�y�[���ɕύX
				m_OneByOne.SetJTextPane((JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300));
			}else{
				//�t�@�C�����ЂƂ��Ђ炩��Ă��Ȃ��Ƃ�
				JOptionPane.showMessageDialog(null, "�ӂ���... �t�@�C��������J����ĂȂ��悧... ����");
			}
		//�u�ҏW�v���u�R�s�[�v
		}else if(event=="�R�s�["){
			if(m_TabbedPane.getComponentCount()!=0){
				//�I������Ă���^�u�̃e�L�X�g�y�[�����擾���A�N���b�v�{�[�h�ɃR�s�[
				JTextPane p = (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300);
				p.copy();
			}else{
				//�t�@�C�����ЂƂ��Ђ炩��Ă��Ȃ��Ƃ�
				JOptionPane.showMessageDialog(null, "�ӂ���... �t�@�C��������J����ĂȂ��悧... ����");
			}
			//�u�ҏW�v���u�؂���v
		}else if(event=="�؂���"){
			//�I������Ă���^�u�̃e�L�X�g�y�[�����擾���A�N���b�v�{�[�h�ɃJ�b�g
			if(m_TabbedPane.getComponentCount()!=0){
				JTextPane p = (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300);
				p.cut();
			}else{
				//�t�@�C�����ЂƂ��Ђ炩��Ă��Ȃ��Ƃ�
				JOptionPane.showMessageDialog(null, "�ӂ���... �t�@�C��������J����ĂȂ��悧... ����");
			}
			//�u�ҏW�v���u�\�t���v
		}else if(event=="�\�t��"){
			//�I������Ă���^�u�̃e�L�X�g�y�[�����擾���A�\�t��
			if(m_TabbedPane.getComponentCount()!=0){
				JTextPane p = (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300);
				p.paste();
			}else{
				//�t�@�C�����ЂƂ��Ђ炩��Ă��Ȃ��Ƃ�
				JOptionPane.showMessageDialog(null, "�ӂ���... �t�@�C��������J����ĂȂ��悧... ����");
			}
			//�u�ҏW�v���u�S�đI���v
		}else if(event=="�S�đI��"){
			//�I������Ă���^�u�̃e�L�X�g�y�[�����擾���A�S�đI��
			if(m_TabbedPane.getComponentCount()!=0){
				JTextPane p = (JTextPane)m_TabbedPane.getSelectedComponent().getComponentAt(400, 300).getComponentAt(400, 300);
				p.selectAll();
			}else{
				//�t�@�C�����ЂƂ��Ђ炩��Ă��Ȃ��Ƃ�
				JOptionPane.showMessageDialog(null, "�ӂ���... �t�@�C��������J����ĂȂ��悧... ����");
			}				
		}
	}

	//�t���[���E�B���h�E�̏�����
	public FrameWrapper(){
		//�t�@�C�����Ǘ��N���X�z��̏�����
		m_files = new ArrayList<FileUnit>();
		//�����A�u�������A���������̃E�B���h�E��������
		m_FindWindow = new FindWindow();
		m_ReplaceWindow = new ReplaceWindow();
		m_OneByOne = new OneByOne();
		//�t���[���A�^�u�A�E�B�W�F�b�g��`�A�^�u�̓��e��������
		InitFrame();
		InitTabbedPane();
		WidgetDefinition.Regist(m_Frame, this);
		RegistTab();
		
		//�E�B���h�E�����O�ɁA�ۑ�����Ă��Ȃ��܂ܕҏW����t�@�C������������ۑ��𑣂����b�Z�[�W�{�b�N�X��\�����A�ۑ�����ꍇ�A
		//�V�K�e�L�X�g�t�@�C���Ȃ�ۑ��_�C�A���O��ʂ��ĕۑ��A�����̃t�@�C���Ȃ炻�̂܂܏㏑���ۑ�����B
		m_Frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				for(int i=0;i<m_files.size();i++){
					if(m_files.get(i).m_Editted){
						int option = JOptionPane.showConfirmDialog(m_Frame, m_files.get(i).m_FileName+"�͕ύX����Ă��... �ۑ�����?","�x��", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
						if(option==JOptionPane.YES_OPTION){
							if(m_files.get(i).m_FileName.length()>=11&&m_files.get(i).m_FileName.substring(0,11).equals("*�V�K�e�L�X�g�t�@�C��")){
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

	//�t���[���̉��ƕs����ݒ肷�郁�\�b�h
	public void setFrameVisible(boolean t){
		m_Frame.setVisible(t);
	}
	
}
