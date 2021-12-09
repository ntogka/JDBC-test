import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Test3 {

    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pst = null;
        Scanner scanner = null;
        ResultSet rs = null;


        try {
            OracleDataSource ds = new OracleDataSource();
            ds.setURL("jdbc:oracle:thin:@db12c.qnr.com.gr:1521/p12c.qnr.com.gr");
            ds.setUser("modesto");
            ds.setPassword("modesto");
            con = ds.getConnection();

            int id = 0;

            Calendar calendar = Calendar.getInstance();
            java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

            String q1 = "select wec_id from work_energy_control where wec_trade_date = ? and wec_version = ?";

            String query1 = "delete from work_energy where we_wec_id = ? ";
            String query2 = "update work_energy set we_pa_energy = we_pa_energy + ? and we_ma_energy = we_ma_energy + ? where we_wec_id = ?";

            scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Date : ");
                String date = scanner.next();
                java.sql.Date sqlDate = parseUserDate(date);
                if (sqlDate == null)
                    sqlDate = startDate;

                System.out.print("Object Version [1/2/3]: ");
                int version = scanner.nextInt();
                if (version == 1 || version == 2 || version == 3) {
                    System.out.println("Good");
                }else {
                    System.out.println("Invalid version. Try again.");
                    version = scanner.nextInt();
                }

                System.out.print("RTU time : ");
                String hour = scanner.next();
                /*if (hour.matches("([01]?[0-9]|2[0-3]):[00]|[15]|[30]|[45]")) {
                    System.out.println("Good");
                } else {
                    System.out.println("Invalid RTU time. Try again.");
                    hour = scanner.next();
                }*/

                pst = con.prepareStatement(q1);
                pst.setString(1, date);
                pst.setInt(2, version);
                rs = pst.executeQuery();
                System.out.println(q1);
                while (rs.next()) {
                    id = rs.getInt("wec_id");
                    System.out.println(id);
                }

                System.out.print("Action [U/D]: ");
                String action = scanner.next();

                if (action.equalsIgnoreCase("U")) {

                    System.out.print("+A or -A: ");
                    String A = scanner.next();
                    System.out.print("Price: ");
                    int price = scanner.nextInt();
                    int zero = 0;

                    if (A.equalsIgnoreCase("+A")) {
                        pst = con.prepareStatement(query2);
                        pst.setInt(1, price);
                        pst.setInt(2, zero);
                        pst.setInt(3, id);

                        System.out.println(query2);
                        int rowCount2 = pst.executeUpdate();
                        System.out.println("rowCount2 = " + rowCount2);
                        System.out.println("Updated");
                        break;
                    } else if (A.equalsIgnoreCase("-A")) {
                        pst = con.prepareStatement(query2);
                        pst.setInt(1, zero);
                        pst.setInt(2, price);
                        pst.setInt(3, id);

                        System.out.println(query2);
                        int rowCount3 = pst.executeUpdate();
                        System.out.println("rowCount2 = " + rowCount3);
                        System.out.println("Updated");
                        break;
                    } else {
                        System.out.println("Invalid. Try again.");
                        A = scanner.next();
                    }

                } else if (action.equalsIgnoreCase("D")) {

                    pst = con.prepareStatement(query1);
                    pst.setInt(1, id);
                    System.out.println(query1);
                    int rowCount1 = pst.executeUpdate();
                    System.out.println("rowCount1 = " + rowCount1);

                    System.out.println("Deleted");
                    break;
                } else {
                    System.out.println("Invalid. Try again.");
                    action = scanner.next();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                scanner.close();
                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static java.sql.Date parseUserDate(String dt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //09/12/2021
            java.util.Date myDate = sdf.parse(dt);
            java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
            return sqlDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}



