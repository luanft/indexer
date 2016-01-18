package test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import indexer.Config;
import indexer.DataColumn;
import indexer.DataTable;
import indexer.Engine;
import mysqldc.DatabaseType;
import mysqldc.SQLServerConnect;

public class Main {

	public static void main(String[] time) {
		Engine eng = new Engine();
		int hour = 23;
		if (time.length > 0) {
			hour = Integer.parseInt(time[0]);
		}
		if (eng.Init()) {
			System.out.print("\n Started!");
			try {
				Date date = new Date();
				boolean is_update = false;
				while (true) {
					if (date.getHours() == hour) {
						if (!is_update) {
							System.out.print("\n Updating for " + date);
							eng.createIndexFile();
							is_update = true;
							System.out.print("\n Updated for " + new Date());
						}
					} else {
						is_update = false;
					}
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.print("Error when indexing data!");
			}
		} else {
			System.out.print("Cann't connect database server!");
		}

	}

}
