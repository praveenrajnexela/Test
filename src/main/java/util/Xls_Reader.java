package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Xls_Reader 
{
   // public static String filename = "src/config/testcases/TestData.xlsx";
    public  String path;
    public  FileInputStream fis = null;
    public  FileOutputStream fileOut =null;
    public XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row   =null;
    private XSSFCell cell = null;

    public Xls_Reader(String path) 
    {
        this.path=path;
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            //HSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // returns the row count in a sheet
    public int getRowCount(String sheetName)
    {
    	int index = workbook.getSheetIndex(sheetName);
    	
    	if(index==-1)
            return 0;
        else{
            sheet = workbook.getSheetAt(index);
            int number=sheet.getLastRowNum()+1;
            
            return number;
        }
    }

    // returns the data from a cell
    @SuppressWarnings("deprecation")
	public String getCellData(String sheetName,String colName,int rowNum)
    {
        try
        {
            if(rowNum <=0)
                return "";

            int index = workbook.getSheetIndex(sheetName);
            int col_Num=-1;
            if(index==-1)
                return "";

            sheet = workbook.getSheetAt(index);
            row=sheet.getRow(0);
            for(int i=0;i<row.getLastCellNum();i++){
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
                    col_Num=i;
            }
            if(col_Num==-1)
                return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum-1);
            if(row==null)
                return "";
            cell = row.getCell(col_Num);

            if(cell==null)
                return "";
            //System.out.println(cell.getCellType());
            if(cell.getCellType()==CellType.STRING)
                return cell.getStringCellValue();
            else if(cell.getCellType()==CellType.NUMERIC || cell.getCellType()==CellType.FORMULA ){

                String cellText  = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    Calendar cal =Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    cellText =
                            (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" +
                            cal.get(Calendar.MONTH)+1 + "/" +
                            cellText;

                    //System.out.println(cellText);

                }



                return cellText;
            }else if(cell.getCellType()==CellType.BLANK)
                return "";
            else
                return String.valueOf(cell.getBooleanCellValue());

        }
        catch(Exception e){

            e.printStackTrace();
            return "row "+rowNum+" or column "+colName +" does not exist in xls";
        }
    }
    
    //to get value from SummarySheet
    public String getFormulaCellData(String sheetName,String colName,int rowNum)
    {
    	 try
         {
             if(rowNum <=0)
                 return "";

             int index = workbook.getSheetIndex(sheetName);
             int col_Num=-1;
             if(index==-1)
                 return "";

             sheet = workbook.getSheetAt(index);
             row=sheet.getRow(0);
             for(int i=0;i<row.getLastCellNum();i++){
                 //System.out.println(row.getCell(i).getStringCellValue().trim());
                 if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
                     col_Num=i;
             }
             if(col_Num==-1)
                 return "";

             sheet = workbook.getSheetAt(index);
             row = sheet.getRow(rowNum-1);
             if(row==null)
                 return "";
             cell = row.getCell(col_Num);

             if(cell==null)
                 return "";
             //System.out.println(cell.getCellType());
             if(cell.getCellType()==CellType.STRING)
                 return cell.getStringCellValue();
             else if(cell.getCellType()==CellType.NUMERIC || cell.getCellType()==CellType.FORMULA )
             {
            	 String cellText = "";
            	 
            	 if(cell.getCellType()==CellType.FORMULA)
            	 {
            		 FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            		 CellValue cellValue = evaluator.evaluate(cell);
            		 double cellValues =  cellValue.getNumberValue();
            		 cellText = String.valueOf(cellValues);
            	 }
            	 if (HSSFDateUtil.isCellDateFormatted(cell)) 
                 {
                     // format in form of M/D/YY
                     double d = cell.getNumericCellValue();
                     Calendar cal =Calendar.getInstance();
                     cal.setTime(HSSFDateUtil.getJavaDate(d));
                     cellText =
                             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                     cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" +
                             cal.get(Calendar.MONTH)+1 + "/" +
                             cellText;
                 }
                 
                 return cellText;
             }
             else if(cell.getCellType()==CellType.BLANK)
                 return "";
             else
                 return String.valueOf(cell.getBooleanCellValue());

         }
         catch(Exception e){

             e.printStackTrace();
             return "row "+rowNum+" or column "+colName +" does not exist in xls";
         }
        	
    }
    
    
    public String getCellData2(String sheetName,String colName,int rowNum)
    {
        try
        {
            if(rowNum <=0)
                return "";

            int index = workbook.getSheetIndex(sheetName);
            int col_Num=-1;
            if(index==-1)
                return "";

            sheet = workbook.getSheetAt(index);
            row=sheet.getRow(1);
            for(int i=0;i<row.getLastCellNum();i++)
            {
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
                    col_Num=i;
            }
            if(col_Num==-1)
                return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum-1);
            if(row==null)
                return "";
            cell = row.getCell(col_Num);

            if(cell==null)
                return "";
            //System.out.println(cell.getCellType());
            if(cell.getCellType()==CellType.STRING)
                return cell.getStringCellValue();
            else if(cell.getCellType()==CellType.NUMERIC || cell.getCellType()==CellType.FORMULA ){

                String cellText  = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    Calendar cal =Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    cellText =
                            (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" +
                            cal.get(Calendar.MONTH)+1 + "/" +
                            cellText;

                    //System.out.println(cellText);

                }



                return cellText;
            }else if(cell.getCellType()==CellType.BLANK)
                return "";
            else
                return String.valueOf(cell.getBooleanCellValue());

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "row "+rowNum+" or column "+colName +" does not exist in xls";
        }
    }

    // returns the data from a cell
    @SuppressWarnings("deprecation")
	public String getCellData(String sheetName,int colNum,int rowNum){
        try{
            if(rowNum <=0)
                return "";

            int index = workbook.getSheetIndex(sheetName);

            if(index==-1)
                return "";


            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum-1);
            if(row==null)
                return "";
            cell = row.getCell(colNum);
            if(cell==null)
                return "";

            if(cell.getCellType()==CellType.STRING)
                return cell.getStringCellValue();
            else if(cell.getCellType()==CellType.NUMERIC || cell.getCellType()==CellType.FORMULA ){

                String cellText  = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    Calendar cal =Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    cellText =
                            (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.MONTH)+1 + "/" +
                            cal.get(Calendar.DAY_OF_MONTH) + "/" +
                            cellText;

                    // System.out.println(cellText);

                }



                return cellText;
            }else if(cell.getCellType()==CellType.BLANK)
                return "";
            else
                return String.valueOf(cell.getBooleanCellValue());
        }
        catch(Exception e){

            e.printStackTrace();
            return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
        }
    }

    // returns true if data is set successfully else false
    public boolean setCellData(String sheetName,String colName,int rowNum, String data){
        try{
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if(rowNum<=0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum=-1;
            if(index==-1)
                return false;


            sheet = workbook.getSheetAt(index);


            row=sheet.getRow(0);
            for(int i=0;i<row.getLastCellNum();i++){
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum=i;
            }
            if(colNum==-1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum-1);
            if (row == null)
                row = sheet.createRow(rowNum-1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            // cell style
            //CellStyle cs = workbook.createCellStyle();
            //cs.setWrapText(true);
            //cell.setCellStyle(cs);
            cell.setCellValue(data);

            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean setCellData2(String sheetName,String colName,int rowNum, String data)
    {
        try{
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if(rowNum<=0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum=-1;
            if(index==-1)
                return false;


            sheet = workbook.getSheetAt(index);
            row=sheet.getRow(1);
            for(int i=0;i<row.getLastCellNum();i++){
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum=i;
            }
            if(colNum==-1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum-1);
            if (row == null)
                row = sheet.createRow(rowNum-1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            // cell style
            //CellStyle cs = workbook.createCellStyle();
            //cs.setWrapText(true);
            //cell.setCellStyle(cs);
            cell.setCellValue(data);

            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    //to write dcn
    public boolean setCellData3(String sheetName,String colName,int rowNum, String data)
    {
        try{
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if(rowNum<=0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum=-1;
            if(index==-1)
                return false;


            sheet = workbook.getSheetAt(index);
            row=sheet.getRow(1);
            for(int i=0;i<row.getLastCellNum();i++)
            {
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equals(colName))
                {
                    colNum=i;
                    break;
                }
            }
            if(colNum==-1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum-1);
            if (row == null)
                row = sheet.createRow(rowNum-1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);
            
            cell.setCellType(CellType.STRING);
            cell.setCellValue(data);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean setCellData3(String sheetName,String colName,int rowNum, int data)
    {
        try{
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if(rowNum<=0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum=-1;
            if(index==-1)
                return false;


            sheet = workbook.getSheetAt(index);
            row=sheet.getRow(1);
            for(int i=0;i<row.getLastCellNum();i++){
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum=i;
            }
            if(colNum==-1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum-1);
            if (row == null)
                row = sheet.createRow(rowNum-1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            // cell style
            //CellStyle cs = workbook.createCellStyle();
            //cs.setWrapText(true);
            //cell.setCellStyle(cs);
            cell.setCellValue(data);

            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    //update formaul in Test Suite Sheet
    public boolean setCellDataFormula(String sheetName,String colName,int rowNum)
    {
    	try
    	{
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if(rowNum<=0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum=-1;
            if(index==-1)
                return false;


            sheet = workbook.getSheetAt(index);


            row=sheet.getRow(0);
            for(int i=0;i<row.getLastCellNum();i++){
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum=i;
            }
            if(colNum==-1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum-1);
            if (row == null)
                row = sheet.createRow(rowNum-1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);
            //cell.setCellFormula(CONFIG.getProperty("Formula"));
            cell.setCellType(CellType.FORMULA);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        }
        catch(Exception e)
    	{
            e.printStackTrace();
            return false;
        }
        return true;
           	
    }
    
    //update formula in Summary Sheet
    public boolean setCellDataFormula2(String sheetName,String colName,int rowNum, String formula)
    {
    	try
    	{
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if(rowNum<=0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum=-1;
            if(index==-1)
                return false;


            sheet = workbook.getSheetAt(index);


            row=sheet.getRow(0);
            for(int i=0;i<row.getLastCellNum();i++){
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum=i;
            }
            if(colNum==-1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum-1);
            if (row == null)
                row = sheet.createRow(rowNum-1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);
            cell.setCellFormula(formula);
            cell.setCellType(CellType.FORMULA);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        }
        catch(Exception e)
    	{
            e.printStackTrace();
            return false;
        }
        return true;
           	
    }


    // returns true if data is set successfully else false
    public boolean setCellData(String sheetName,String colName,int rowNum, String data,String url){
        //System.out.println("setCellData setCellData******************");
        try{
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if(rowNum<=0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum=-1;
            if(index==-1)
                return false;


            sheet = workbook.getSheetAt(index);
            //System.out.println("A");
            row=sheet.getRow(0);
            for(int i=0;i<row.getLastCellNum();i++){
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
                    colNum=i;
            }

            if(colNum==-1)
                return false;
            sheet.autoSizeColumn(colNum); //ashish
            row = sheet.getRow(rowNum-1);
            if (row == null)
                row = sheet.createRow(rowNum-1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);
            
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);

            fileOut.close();

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }



    // returns true if sheet is created successfully else false
    public boolean addSheet(String  sheetname){

        FileOutputStream fileOut;
        try {
            workbook.createSheet(sheetname);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // returns true if sheet is removed successfully else false if sheet does not exist
    public boolean removeSheet(String sheetName){
        int index = workbook.getSheetIndex(sheetName);
        if(index==-1)
            return false;

        FileOutputStream fileOut;
        try {
            workbook.removeSheetAt(index);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    // find whether sheets exists
    public boolean isSheetExist(String sheetName){
        int index = workbook.getSheetIndex(sheetName);
        if(index==-1){
            index=workbook.getSheetIndex(sheetName.toUpperCase());
            if(index==-1)
                return false;
            else
                return true;
        }
        else
            return true;
    }

    // returns number of columns in a sheet
    public int getColumnCount(String sheetName){
        // check if sheet exists
        if(!isSheetExist(sheetName))
            return -1;

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(0);

        if(row==null)
            return -1;

        return row.getLastCellNum();



    }
    //String sheetName, String testCaseName,String keyword ,String URL,String message
    public boolean addHyperLink(String sheetName,String screenShotColName,String testCaseName,int index,String url,String message){
        //System.out.println("ADDING addHyperLink******************");

        url=url.replace('/', '/');
        if(!isSheetExist(sheetName))
            return false;

        sheet = workbook.getSheet(sheetName);

        for(int i=2;i<=getRowCount(sheetName);i++){
            if(getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)){
                //System.out.println("**caught "+(i+index));
                setCellData(sheetName, screenShotColName, i+index, message,url);
                break;
            }
        }


        return true;
    }
    public int getCellRowNum(String sheetName,String colName,String cellValue){

        for(int i=2;i<=getRowCount(sheetName);i++){
            if(getCellData(sheetName,colName , i).equalsIgnoreCase(cellValue)){
                return i;
            }
        }
        return -1;

    }
    
    public XSSFSheet getSheet(String sheetName)
    {
    	int index = workbook.getSheetIndex(sheetName);
    	if(index==-1)
            return null;
        else
        {
            sheet = workbook.getSheetAt(index);
            return sheet;
        }
    }
    
    public int getRowIndex(String sheetName, String value, String ColName)
    {
    	int rowIndex = 0;
    	
    	int rowcount = getRowCount(sheetName);
    	
    	for(int i=2; i<=rowcount;i++) 
    	{
    		String cellData = getCellData(sheetName, ColName, i);
    		
    		if(cellData.equals(value))
    		{
    			rowIndex = i;
    			break;
    		}
    		
    		/*if(row.getCell(i).getStringCellValue().trim().equals(value))
            {
            	rowIndex = i;
            }*/
    	}
		return rowIndex;
    }
    
    public int getcolumnIndex(String sheetName,String ColName)
    {
    	int columnIndex = 0;
        int index = workbook.getSheetIndex(sheetName);
        sheet = workbook.getSheetAt(index);
        row=sheet.getRow(1);
        for(int i=0;i<row.getLastCellNum();i++)
        {
            //System.out.println(row.getCell(i).getStringCellValue().trim());
            if(row.getCell(i).getStringCellValue().trim().equals(ColName.trim()))
            {
            	columnIndex=i;
            	break;
            }
        }
		return columnIndex;
    }
    
    public int getcolumnIndex2(String sheetName,String ColName)
    {
    	int columnIndex = 0;
        int index = workbook.getSheetIndex(sheetName);
        sheet = workbook.getSheetAt(index);
        row=sheet.getRow(0);
        for(int i=0;i<row.getLastCellNum();i++)
        {
            //System.out.println(row.getCell(i).getStringCellValue().trim());
            if(row.getCell(i).getStringCellValue().trim().contains(ColName))
            {
            	columnIndex=i;
            	break;
            }
        }
		return columnIndex;
    }
    

   /* // to run this on stand alone
    public static void main(String arg[]) throws IOException{

        //System.out.println(filename);
        Xls_Reader datatable = null;


        datatable = new Xls_Reader("src/Framework_XL_Files/Controller.xlsx");
        for(int col=0 ;col< datatable.getColumnCount("TC5"); col++){
            System.out.println(datatable.getCellData("TC5", col, 1));
        }
    }*/


}
