package dialect;
 
import java.sql.*;
import org.hibernate.dialect.*;
import org.hibernate.exception.*;
import org.hibernate.exception.spi.*;
 
public class XMySQL5Dialect extends MySQL5Dialect {
 
    public XMySQL5Dialect(){}
 
    private static ViolatedConstraintNameExtracter EXTRACTER = new TemplatedViolatedConstraintNameExtracter() {
        public String extractConstraintName(SQLException sqle) {
            try {
                int sqlState = Integer.valueOf( org.hibernate.internal.util.JdbcExceptionHelper.extractSqlState(sqle)).intValue();
                switch (sqlState) {
                    case 23000: return extractUsingTemplate("for key '","'", sqle.getMessage());
                    default: return null;
                }
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
    };
 
    @Override
    public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
        return EXTRACTER;
    }
}
