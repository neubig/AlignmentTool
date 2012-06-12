
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;


@SuppressWarnings("unchecked")
public class PanelTable extends JPanel{

	private static final long serialVersionUID = -5335691153210117889L;
	public static final int TRG = 0;
	public static final int SRC = 1;
	public static final int ALIGN = 2;
	public static ArrayList<String[]>[] arrayListOfSetData = new ArrayList[3];
	public static ArrayList<Point> tablePointOfLine = new ArrayList<Point>();
	public static ArrayList<JTable> MappingTable = new ArrayList<JTable>();
	public static ArrayList<JList> TrgWordTable = new ArrayList<JList>(),SrcWordTable = new ArrayList<JList>();
	public static ArrayList<JScrollPane> scrollPane = new ArrayList<JScrollPane>();
	public static ArrayList<String[][]> stringTable = new ArrayList();
	public static JLabel tate = new JLabel();
	public static JLabel yoko = new JLabel();
	public static JLabel pages = new JLabel();
	public static int rowHeight = 16;
	public static int charWidth = 14;
	public static int currPage = 0, trgSize = 0, srcSize = 0;
	//public static DefaultTableModel defaultTableModel;
	public static Dimension size = Main.PANEL_TABLE_SIZE;
	public CardLayout cardLayout = new CardLayout();


	public PanelTable(JLabel pageLabel){
		this.setLayout(cardLayout);
		this.setSize(size);
		pages = pageLabel;
	}

	public static String[][] checkTable(int TrgWordCount,int SrcWordCount,String[] MappingData){
		String[][] checkMap = new String[TrgWordCount][SrcWordCount];
		for (int i=0;i<TrgWordCount;i++){
			for (int j=0;j<SrcWordCount;j++){
				checkMap[i][j] = " ";
			}
		}
		Point checkPoint = new Point();
		for (int i =0 ; i<MappingData.length;i++){
			if( MappingData[i].indexOf("-") >0){
				String[] stringPair = MappingData[i].split("-");
				checkPoint.x = Integer.parseInt(stringPair[1]);
				checkPoint.y = Integer.parseInt(stringPair[0]);
				checkMap[checkPoint.x][checkPoint.y] = "X";
			}
		}
		return checkMap;
	}


    public static void loadData(int pos, File file) {
        arrayListOfSetData[pos] = new ArrayList<String[]>();
        InputStreamReader fileReader = null;
		try{
			String[] wordArray;
			FileInputStream is = new FileInputStream(file);
			fileReader = new InputStreamReader(is, "UTF-8");
			BufferedReader reader = new BufferedReader(fileReader);
			String line;
			for(int j=0;(line = reader.readLine()) != null;j++){
				wordArray = line.split(" ");
				arrayListOfSetData[pos].add(wordArray);
			}
		}	catch(IOException es){
		}
		finally{ try{ if(fileReader != null) fileReader.close(); } catch(IOException dae){} }
    }

	public void reGenerateTable(){
		PanelTable.arrayListOfSetData = arrayListOfSetData;
		trgSize = arrayListOfSetData[TRG].size();
		srcSize = arrayListOfSetData[SRC].size();
        if(trgSize != srcSize) {
            JOptionPane.showMessageDialog(null, "Source file and target file length don't match: source="+srcSize+", target="+trgSize);
            return;
        }
        // Pad the alignment array to make sure that it matches size
        if(arrayListOfSetData[ALIGN] == null)
            arrayListOfSetData[ALIGN] = new ArrayList<String[]>();
        while(arrayListOfSetData[ALIGN].size() < srcSize)
            arrayListOfSetData[ALIGN].add(new String[0]);
		currPage = 1;
		for (int i=0;i<trgSize;i++){
			JList TrgWordList = new JList(arrayListOfSetData[TRG].get(i));
			TrgWordList.setFixedCellHeight(rowHeight);
			TrgWordTable.add(TrgWordList);
			JList SrcWordList = new JList(arrayListOfSetData[SRC].get(i));
			SrcWordTable.add(SrcWordList);

			String[][] checkMap = checkTable(arrayListOfSetData[TRG].get(i).length,
					arrayListOfSetData[SRC].get(i).length,
					arrayListOfSetData[ALIGN].get(i));
			stringTable.add(checkMap);

			DefaultTableModel defaultTableModel = new DefaultTableModel((String[]) arrayListOfSetData[SRC].get(i),0);
			for(int j = 0 ; j < arrayListOfSetData[TRG].get(i).length ; j++){
				defaultTableModel.addRow(checkMap[j]);
			}
			final JTable jtable = new JTable(defaultTableModel);

			jtable.setCellSelectionEnabled(true);
			jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jtable.setDefaultRenderer(Object.class,new BirowTableRenderer());
			jtable.setRowHeight(rowHeight);
			jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			String[] jWords = arrayListOfSetData[SRC].get(i);
			for(int j = 0; j < jWords.length; j++)
				jtable.getColumnModel().getColumn(j).setPreferredWidth(jWords[j].length()*charWidth+6);

			jtable.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					Point point = e.getPoint();
					int row= jtable.rowAtPoint(point);
					int column = jtable.columnAtPoint(point);
					check(row,column,jtable);
				}
			});
			jtable.addMouseMotionListener(new MouseAdapter(){
				public void mouseMoved(MouseEvent e) {
					//System.out.println(e.getPoint());
					LabelsMousePointGet.moveLabels(e.getPoint());
				}
			});
			MappingTable.add(jtable);
			JScrollPane jscrollPane = new JScrollPane(MappingTable.get(i));
			jscrollPane.setRowHeaderView(TrgWordTable.get(i));
			//scrollPane.setColumnHeaderView(SrcWordTable);
			jscrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jscrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jscrollPane.setSize(size);
			scrollPane.add(jscrollPane);
		}
		pages.setText(currPage+"/"+trgSize);
		for (int i=0;i<scrollPane.size();i++){
			String ID = ((Integer)i).toString();
			this.add(ID,scrollPane.get(i));
		}
	}
	public String[][] getCheckMap(int pageNumber){
		int columnSize = PanelTable.MappingTable.get(pageNumber).getModel().getColumnCount();
		int rowSize = PanelTable.MappingTable.get(pageNumber).getModel().getRowCount();
		String[][] checkMap = new String[rowSize][columnSize];
		for(int column = 0;column < columnSize;column++){
			for (int row = 0;row < rowSize;row++){
				checkMap[row][column] = (String) PanelTable.MappingTable.get(pageNumber).getModel().getValueAt(row,column);
			}
		}
		return checkMap;
	}
	public void setCheckMap(String[][] checkMap,int pageNumber){
		int columnSize = PanelTable.MappingTable.get(pageNumber).getModel().getColumnCount();
		int rowSize = PanelTable.MappingTable.get(pageNumber).getModel().getRowCount();
		//System.out.println(checkMap[0].length  + " " +  columnSize + " " +  checkMap.length  + " " +  rowSize);
		if (checkMap[0].length == columnSize && checkMap.length == rowSize){
			for(int column = 0;column < columnSize;column++){
				for (int row = 0;row < rowSize;row++){
					if(checkMap[row][column].equals("X")){
						PanelTable.MappingTable.get(pageNumber).getModel().setValueAt("X",row,column);
					}else{
						PanelTable.MappingTable.get(pageNumber).getModel().setValueAt("",row,column);
					}
				}
			}

		}else{
			System.out.println("wrong input!!@PanelTable");
		}
	}

	private static void check(int row,int column,JTable jtable){
		if(jtable.getModel().getValueAt(row, column).equals("X")){
			jtable.getModel().setValueAt("",row, column);
		}else{
			jtable.getModel().setValueAt("X",row, column);
		}
	}
	class MyTableModel extends DefaultTableModel{
		private static final long serialVersionUID = -2677379363148411290L;

		MyTableModel(String[] columnNames, int rowNum){
			super(columnNames, rowNum);
		}

		public Class getColumnClass(int col){
			return getValueAt(0, col).getClass();
		}
	}

	//�J�[�h���C�A�E�g��ύX����֐�
	public void showNextPanel(){
		cardLayout.next(this);
		if(++currPage > trgSize) currPage = 1;
		pages.setText(currPage+"/"+trgSize);
	}
	public void showPreviousPanel(){
		cardLayout.previous(this);
		if(--currPage == 0) currPage = trgSize;
		pages.setText(currPage+"/"+trgSize);
	}
	public void showSpecifiedPanel(int i){
		String ID = ((Integer)i).toString();
		cardLayout.show(this, ID);
		currPage = i;
		pages.setText(currPage+"/"+trgSize);
	}


}
