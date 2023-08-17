package FRAMEWORK;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class ConnectDataSheet extends BrowserClass {

//	public static void main(String[] args) throws FilloException {
//		System.out.println("Hello");
//		m1();
//	}
	public static String Si_No;
	public static String TestCase_No;
	public static String MODULE;
	public static String PropertyName;
	public static String PropertyValue;
	public static String Datafield;
	public static String Action;
	public static String Description;
	public static String Neg_Description;
	static LocatorClass xget;
	static ConnectDataSheet consheet;
	static UtilScreenshotAndReport utilClass;
	public static String destFileScrnshot;
	public static String status;
	public static int pass = 0, fail = 0;

	// In this method DataSheetGet(String fileName) recive the datasheet file name
	// check the maincontroler module is same as datasheet module name
	// then read all the datasheet and iterate all the row in the object type
	// use forloop and if the forloop of i==0 then call the extendreport
	// then iterate all the row value in the single single variable
	// then pass the testcase_id and the all the description to the extentreport to
	// print the info.
	// then call the this xpathpick() and pass the all value what i have retrived to
	// the row and column value.
	// after retrive the all the value the call to the extentflush() to stop the
	// generate report.

	public static void DataSheetGet(String fileName) throws FilloException, InterruptedException, IOException {
		consheet = new ConnectDataSheet();
		xget = new LocatorClass();
		utilClass = new UtilScreenshotAndReport();

		Fillo fillo = new Fillo();
		Connection conn = fillo.getConnection(
				System.getProperty("user.dir") + File.separator + "DataSheet" + File.separator + fileName);
		try {

			String query = "SELECT * FROM Sheet1 WHERE RUNSTATUS='Y' and MODULE='" + ConnectToMainController.Module
					+ "'";
			Recordset recordset = conn.executeQuery(query);

			// new type and logic explanation***************************************

			/*
			 * tale jou logic lekhichi First re sabu column name get kariki list re store
			 * kariba List<String> columnName = r.getFieldNames();
			 * 
			 * Then row value paei gote object type array list kariba gote row ku gote
			 * object type array list
			 * 
			 * Then gote row ra total column value ku get kariki first object type arraylist
			 * re store kariba //rowValue.add(r.getField(s));
			 * 
			 * Then sabu jaka object type array list ku gote object type array list re store
			 * kariba //rowList.add(rowValue); [Srno, Module, PageName, RunStatus, Control,
			 * ObjectType] this is a single object type array list in a single row ,This
			 * type we get the all the row and addd a particular object type array list
			 * 
			 * [[Srno, Module, PageName, RunStatus, Control, ObjectType],[Srno, Module,
			 * PageName, RunStatus, Control, ObjectType],[Srno, Module, PageName, RunStatus,
			 * Control, ObjectType]]
			 * 
			 * this way to store all the row value in the object type to store the
			 * particular objecttype arraylist ...To check it the two square bracket lisk
			 * as[[]] object type array list
			 */

			List<Object> rowsList = new ArrayList<>();

			while (recordset.next()) {
				List<String> columns = recordset.getFieldNames();
				List<Object> rowValues = new ArrayList<>();
				for (String column : columns) {
					rowValues.add(recordset.getField(column));
				}
				rowsList.add(rowValues);
			}

			// this type is working properly****************************

//		List<String> al = new ArrayList<String>();
//		while (recordset.next()) {
//			LOCATOR=recordset.getField("LOCATOR");
//			PropertyValue = recordset.getField("PropertyValue");
//			Datafield = recordset.getField("Datafield");
//			Action = recordset.getField("Action");
//			al.add(LOCATOR);
//			al.add(XPATH);
//			al.add(Datafield);
//			al.add(Action);
//		}******************************************************

//		for (Object o : rowsList) {
//			System.out.println(o);
//		}

			// **************************************************

//		System.out.println(rowsList);
//		return rowsList;

			utilClass.CsvFileCreate();
			utilClass.WriteCSVFile("Si_No", "TestCase_No", "Status", "Screenshot_Path");
			utilClass.extentReport(); ///// call the extent report class

			int i;
			for (i = 0; i < rowsList.size(); i++) {

//				if (i == 0) {
//					utilClass.extentReport(); ///// call the extent report class		
//				}

				destFileScrnshot = null;
				status = "PASS";

				List<Object> row = (List<Object>) rowsList.get(i);

				Si_No = (String) row.get(0);
				TestCase_No = (String) row.get(1);
				MODULE = (String) row.get(2);
				PropertyName = (String) row.get(4);
				PropertyValue = (String) row.get(5);
				Datafield = (String) row.get(6);
				Action = (String) row.get(7);
				Description = (String) row.get(8);
				Neg_Description = (String) row.get(9);

//			 System.out.println("LOCATOR NAME=========================>"+LOCATOR+"\n"+"PropertyValue====================>"+PropertyValue+"\n"+"Datafield=============>"+Datafield+"\n"+"ActionType========>"+Action);
				System.out.println();
				System.out.println("SI_No             ====================> " + Si_No);
				System.out.println("TestCase_No       ====================> " + TestCase_No);
				System.out.println("PropertyName      ====================> " + PropertyName);
				System.out.println("PropertyValue     ====================> " + PropertyValue);
				System.out.println("Datafield         ====================> " + Datafield);
				System.out.println("ActionType        ====================> " + Action);

				if (Description != null && !Description.isEmpty()) {
					String testCaseAndDescription = TestCase_No.concat(" " + Description);
					UtilScreenshotAndReport.testCaseCreate(testCaseAndDescription);
					if (!Action.equalsIgnoreCase("CheckVisibility")) {
						UtilScreenshotAndReport.testcaseInfo(Description);
					}
				}

				/*TestCase_No, PropertyName, PropertyValue, Datafield, Action, Description,
							Neg_Description, driver*/
				
				try {
					xget.xpathpick();
					
					pass++;
					System.out.println("Pass = "+pass+"   "+"Fail = "+fail);
					
				} catch (Exception e) {
					
//					UtilScreenshotAndReport.test.fail(e);
					e.printStackTrace();
					fail++;
					System.out.println("Pass = "+pass+"   "+"Fail = "+fail);
				}

				if (!Action.contains("wait") && Action.equalsIgnoreCase("CheckVisibility")) {
					utilClass.WriteCSVFile(Si_No, TestCase_No, status, destFileScrnshot);
				}

//				UtilScreenshotAndReport.test.log(Status.INFO, Description);
//			xget.xpathpick((String) row.get(1), (String) row.get(2), (String) row.get(3), (String) row.get(4), driver);

//			 for( j=1;j<row.size();j++)
//			 {
//				 if(j==1)
//				 {
//					 LOCATOR=(String) row.get(j);
//				 }
//				 
//				 else if(j==2) {
//					 PropertyValue=(String) row.get(j);
//				 }
//				 
//				 else if(j==3) {
//					 Datafield=(String) row.get(j);
//				 }
//				 
//				 else if(j==4) {
//					 Action=(String) row.get(j);
//				 }
//
//			 }
//			System.out.println("LOCATOR NAME=========================>" + (String) row.get(1) + "\n"
//					+ "PropertyValue====================>" + (String) row.get(2) + "\n" + "Datafield=============>"
//					+ (String) row.get(3) + "\n" + "ActionType========>" + (String) row.get(4));

//			 xget.xpathpick(LOCATOR, PropertyValue, Datafield, Action, driver);

			}
			if (i == rowsList.size()) {

				System.out.println("\n");
				
				System.out.println("Pass = "+pass+"   "+"Fail = "+fail);

				System.out.println("********************  Successfully Completed  ********************" + "\n");
			}
		} catch (Exception e) {
			System.err.print(e.getMessage());
			e.printStackTrace();
		}

		finally {
			utilClass.ExtentFlush();
		}

	}

	//////////////////////////////////////// DataField Read
	//////////////////////////////////////// kariba
	//////////////////////////////////////// sheet2//////////////////////////////

	// In this method DataFieldRead(to receive the datasheet filed value to get the
	// sheet2 value)
	// first check the datafield if it is not null then get the datafield value and
	// pass the value to this method actrds();

	static String hello;
	static ActionClass actClass;

	public static void DataFieldRead(String TestCase_No, WebElement webElement, List<WebElement> webElements,
			String Datafield, String Action, String Description, String Neg_Description, WebDriver driver)
			throws FilloException, InterruptedException, IOException {
		actClass = new ActionClass();

		if (Datafield != null && !Datafield.isEmpty())

		{

			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(System.getProperty("user.dir") + File.separator + "DataSheet"
					+ File.separator + ConnectToMainController.TestFlow_Path);
			String query = "SELECT * FROM Sheet2";
			Recordset recordset = conn.executeQuery(query);
			while (recordset.next()) {
				hello = recordset.getField(Datafield);
				System.out.println(
						"DataFiels For Sheet2====================================================" + hello + "\n");
				ActionClass.actrds(TestCase_No, webElement, webElements, hello, Action, Description, Neg_Description,
						driver);

			}
		}

		else {
			ActionClass.actrds(TestCase_No, webElement, webElements, hello, Action, Description, Neg_Description,
					driver);
		}

	}

}