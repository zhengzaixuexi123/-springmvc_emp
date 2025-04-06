import com.example.DBCPUtils.DBCPUtils;
import org.junit.Test;

import java.sql.Connection;

public class TestApp {
    @Test
    public void test() {
        Connection conn = DBCPUtils.getConnection();
        System.out.println("connection established");
        DBCPUtils.close(conn);
    }
}
