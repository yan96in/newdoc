package com.bigd.utl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;

import com.meluo.newdoc.db.DiseaseProvider;
import com.meluo.newdoc.db.DiseaseProvider.DiseaseConstants;

import java.io.InputStream;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelUtl {

	public static void E2D(Context c) {
		ContentResolver mContentResolver = c.getContentResolver();
		AssetManager am = c.getAssets();
		InputStream is = null;
		try {
			is = am.open("data.xls");
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			int row = sheet.getRows();

			for (int i = 0; i < row; i++) {
				if (!Checking.isNullorBlank(sheet.getCell(0, i).getContents())) {
					ContentValues values = new ContentValues();
					values.put(DiseaseConstants.NAME, sheet.getCell(0, i)
							.getContents());
					values.put(DiseaseConstants.DETAIL, sheet.getCell(1, i)
							.getContents());
					values.put(DiseaseConstants.TREAT, sheet.getCell(2, i)
							.getContents());
					values.put(DiseaseConstants.OTHER, sheet.getCell(3, i)
							.getContents());
					values.put(DiseaseConstants.TYPE_ID, sheet.getCell(4, i)
							.getContents());
					mContentResolver
							.insert(DiseaseProvider.CONTENT_URI, values);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test(Context c) {
		AssetManager am = c.getAssets();
		InputStream is = null;

		try {
			is = am.open("data.xls");

			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			int row = sheet.getRows();
			HashMap<String, String> hm;
			for (int i = 0; i < row; ++i) {
				Cell cellarea = sheet.getCell(0, i);
				Cell cellschool = sheet.getCell(1, i);
				System.out.println(cellarea.getContents() + ":"
						+ cellschool.getContents());
				hm = new HashMap<String, String>();
				hm.put("AREA", cellarea.getContents());
				hm.put("SCHOOL", cellschool.getContents());

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
