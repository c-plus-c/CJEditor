package file;
import javax.swing.*;

import java.io.*;

import javax.swing.JFileChooser;
//�J����Ă���e�L�X�g�t�@�C���̃p�X�A�t�@�C�����A���e�A�G���R�[�h�Ȃǂ��Ǘ��B�ۑ��������s���B
public class FileUnit {
	public String m_FilePath;//�t�@�C���ւ̃p�X
	public String m_FileName;//�t�@�C����
	public String m_Buffer;//�t�@�C���̓��e
	public String m_Encode;//�t�@�C���̃G���R�[�h
	public boolean m_Editted;//�t�@�C�����ۑ�����Ă��Ȃ��܂ܕҏW���s��ꂽ���ۂ��B
	
	//�f�t�H���g�R���X�g���N�^(���g�p)
	public FileUnit(){
		m_FilePath="";
		m_FileName="*�V�K�e�L�X�g�t�@�C��";
		m_Buffer="";
		m_Encode="";
		m_Editted=false;
	}
	
	//�u�t�@�C���v���u�V�K�쐬�v����V���ȃe�L�X�g�G���A�^�u��\���������A�t�@�C����������������R���X�g���N�^�B
	public FileUnit(int num,String enc) throws IOException{
		m_FilePath="";
		m_FileName="*�V�K�e�L�X�g�t�@�C��"+Integer.toString(num);//"*�V�K�e�L�X�g�t�@�C��"+������(���ɊJ����Ă���^�u��+1)���t�@�C�����ɐݒ�B���ꂪ�^�u�ɕ\�������B
		m_Buffer="";//�t�@�C���̓��e��ݒ�
		m_Encode=enc;//�t�@�C���̃G���R�[�h��ݒ�
		m_Editted=false;
		String line;
		//UTF-8�G���R�[�h��dammy.txt�t�@�C����ǂݍ��ށB(��������Ȃ��ƃe�L�X�g�G���A���̕�����̌�����u�����������܂������Ȃ�)
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

	//�u�t�@�C���v���u�J���v���j���[����t�@�C����I�����ĊJ�����ɌĂяo�����R���X�g���N�^
	public FileUnit(File fi,String enc) throws Exception {
		String line;
		//�I�����ꂽ�t�@�C���̏����擾���A�t�@�C����ǂݍ��ރN���X�̃C���X�^���X���쐬�B
		BufferedReader readfile = new BufferedReader(new InputStreamReader(new FileInputStream(fi),enc));
		//�t�@�C���ւ̐�΃p�X���擾���A�t�@�C���̃p�X�֓o�^����B
		this.m_FilePath=fi.getAbsolutePath();
		//�t�@�C���̖��O���擾���A�t�@�C�����֓o�^����B
		this.m_FileName=fi.getName();
		this.m_Buffer="";
		//�G���R�[�h��ݒ�B
		this.m_Encode=enc;
		//�t�@�C���̓��e�����ׂēǂݍ���
		m_Editted=false;
		while((line=readfile.readLine())!=null){
			m_Buffer+=(line+"\n");
		}
	}
	
	//�t�@�C�����㏑���ۑ����郁�\�b�h
	public void SaveFile(JTextPane jTextPane,String Encode) throws IOException{
		m_Encode=Encode;
		//�G���R�[�h���w�肵�āA�t�@�C�������o���N���X�̃C���X�^���X���쐬
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(m_FilePath),Encode)));
		//�e�L�X�g�G���A�̓��e���擾�B
		m_Buffer=jTextPane.getText();
		//�t�@�C���X�g���[���ɏ������ށB
		pw.println(jTextPane.getText());
		pw.close();
		//�ۑ��I�����b�Z�[�W�̕\��
		JOptionPane.showMessageDialog(null, "�ۑ��������!!(*�܁ށ�*)");
	}

	public String SaveFileDetailed(JFrame m_Frame,JTextPane jTextPane,String Encode) throws IOException{
		m_Encode=Encode;
		//�t�@�C���ۑ��_�C�A���O�̕\��
		JFileChooser filechooser = new JFileChooser();
		//�ۑ����邩�ۂ����擾
		int selected=filechooser.showSaveDialog(m_Frame);
		//�t�@�C�����ۑ�����鎞
		if(selected==JFileChooser.APPROVE_OPTION){
			//�I�����ꂽ�t�@�C���̏����擾
			File sav = filechooser.getSelectedFile();
			//�G���R�[�h���w�肵�ď�������
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sav),Encode)));
			//�e�L�X�g�G���A���̓��e���擾
			m_Buffer=jTextPane.getText();
			//�t�@�C���X�g���[���ɏ�������
			pw.println(jTextPane.getText());
			pw.close();
			//�ۑ��I�����b�Z�[�W�̕\��
			JOptionPane.showMessageDialog(null, "�ۑ��������!!(*�܁ށ�*)");
			//�ۑ�����Ԃ��B
			return sav.getName();
		}
		//�t�@�C����ۑ����Ȃ��Ƃ���null��Ԃ��B
		return null;
	}
}
