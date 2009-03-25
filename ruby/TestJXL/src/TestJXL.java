import java.io.File;
import java.util.Date;
import jxl.*;
import jxl.write.*;

public class TestJXL {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readExcel();

	}

	public static void readExcel() {

		try {
			Workbook workbook = Workbook.getWorkbook(new File(
					"excels/whohar_us_mapXlf.xls"));
			Sheet sheet = workbook.getSheet("Display1");
			System.out.println(sheet.getName());

			Cell a1 = sheet.getCell(1, 1);
//			Cell b2 = sheet.getCell(1, 1);
//			Cell c2 = sheet.getCell(2, 1);

			String stringa1 = a1.getContents();
//			String stringb2 = b2.getContents();
//			String stringc2 = c2.getContents();

			System.out.println(stringa1);
//			System.out.println(stringb2);
//			System.out.println(stringc2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readProtectedExcel() {
		try {
			Workbook workbook = Workbook.getWorkbook(new File(
					"excels/FriendsXlf.xls"));
			WritableWorkbook copy = Workbook.createWorkbook(new File(
					"excels/unprotected.xls"), workbook);

			WritableSheet[] sheets = copy.getSheets();

			for (WritableSheet sheet : sheets) {
				sheet.getSettings().setProtected(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
