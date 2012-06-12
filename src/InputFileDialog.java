
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * �u�t�@�C�����J���vdialog���J���{�^���DreturnFile()�ɂ���đI�������t�@�C����File�N���X��Ԃ��D
 * @author hys
 *
 * @see
 */
@SuppressWarnings("serial")
public class InputFileDialog extends JButton {

	public static final File[] file = new File[3];
	public static final int TRG = PanelTable.TRG;
	public static final int SRC = PanelTable.SRC;
	public static final int ALIGN = PanelTable.ALIGN;
	public static String directory = null;

	public InputFileDialog(int buttonKind){
		this.init(buttonKind);
	}

	private void init(int buttonKind){
		ActionListenerOpenDialog actionListenerOpenDialog = new ActionListenerOpenDialog(buttonKind);
		this.addActionListener(actionListenerOpenDialog);
		switch(buttonKind){
		case TRG:
			this.setText("Open Target");
			break;
		case SRC:
			this.setText("Open Source");
			break;
		case ALIGN:
			this.setText("Open Alignment");
			break;
		}
	}

	class ActionListenerOpenDialog implements ActionListener {
		private String dialogTitle;
		private int fileSelectionMode;
		private FileFilter fileFilter;
		private boolean fileFilterFlag = false;
		private int buttonKind;
		public ActionListenerOpenDialog(int buttonKind){
			this.buttonKind = buttonKind;
			fileFilter = new txtFileFilter();
			fileFilterFlag = true;

			switch(buttonKind){
			case TRG :
				dialogTitle = "Open Target";
				fileSelectionMode = JFileChooser.FILES_ONLY;
				break;
			case SRC :
				dialogTitle = "Open Source";
				fileSelectionMode = JFileChooser.FILES_ONLY;
				break;
			case ALIGN :
				dialogTitle = "Open Alignment";
				fileSelectionMode = JFileChooser.FILES_ONLY;
				break;
			}
		}
		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser filechooser = new JFileChooser(directory);
			filechooser.setDialogTitle(dialogTitle);
			filechooser.setFileSelectionMode(fileSelectionMode);
			if(fileFilterFlag){
				filechooser.addChoosableFileFilter(fileFilter);
			}
			int selected = filechooser.showOpenDialog(null);
			if (selected == JFileChooser.APPROVE_OPTION){
				directory = filechooser.getSelectedFile().getParent();
                PanelTable.loadData(buttonKind, filechooser.getSelectedFile());
			}
		}

	}
}
