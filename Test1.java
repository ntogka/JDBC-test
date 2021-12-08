import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test1 {

    public static void main(String[] args) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            OracleDataSource ds = new OracleDataSource();
            ds.setURL("jdbc:oracle:thin:@db12c.qnr.com.gr:1521/p12c.qnr.com.gr");
            ds.setUser("modesto");
            ds.setPassword("modesto");
            con = ds.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(
                        "select l_admin_descr_gr as Locations, count (entity_details.entd_l_id) as Entities\n" +
                        "from locations, ENTITY_DETAILS, ENTITY\n" +
                        "where L_ID = entity_details.entd_l_id\n" +
                                //"and entity_details.ent_id = ENTITY.ENT_ID\n" +
                        "group by l_admin_descr_gr");
            System.out.println("Locations\tEntities");
            System.out.println("----------------------------");
            while(rs.next()){
                System.out.print(rs.getString("Locations") + "\t");
                System.out.println(rs.getInt("Entities"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
