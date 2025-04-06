package com.example.dao;

import com.example.DBCPUtils.DBCPUtils;
import com.example.modle.Employee;
import com.example.modle.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class EmployeeDaoImpl implements EmployeeDao {

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, m.FIRST_NAME as manager_first_name, m.LAST_NAME as manager_last_name " +
                     "FROM s_emp e LEFT JOIN s_emp m ON e.MANAGER_ID = m.ID";

        try (Connection conn = DBCPUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public Page<Employee> getEmployeesPage(int pageNum, int pageSize) {
        List<Employee> employees = new ArrayList<>();
        long totalRecords = 0;
        String countSql = "SELECT COUNT(*) FROM s_emp";
        String pageSql = "SELECT * FROM (" +
                        "    SELECT a.*, ROWNUM rn FROM (" +
                        "        SELECT e.ID, e.LAST_NAME, e.FIRST_NAME, e.USERID, " +
                        "               e.START_DATE, e.COMMENTS, e.MANAGER_ID, " +
                        "               e.TITLE, e.DEPT_ID, e.SALARY, e.COMMISSION_PCT " +
                        "        FROM s_emp e " +
                        "        ORDER BY e.ID" +
                        "    ) a WHERE ROWNUM <= ?" +
                        ") WHERE rn > ?";

        try (Connection conn = DBCPUtils.getConnection()) {
            // Get total count
            try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                ResultSet countRs = countStmt.executeQuery();
                if (countRs.next()) {
                    totalRecords = countRs.getLong(1);
                }
            }

            // Get paginated data
            try (PreparedStatement pageStmt = conn.prepareStatement(pageSql)) {
                int endRow = pageNum * pageSize;
                int startRow = (pageNum - 1) * pageSize;
                pageStmt.setInt(1, endRow);
                pageStmt.setInt(2, startRow);
                
                try (ResultSet rs = pageStmt.executeQuery()) {
                    while (rs.next()) {
                        Employee employee = new Employee();
                        employee.setId(rs.getInt("ID"));
                        employee.setLast(rs.getString("LAST_NAME"));
                        employee.setFirst(rs.getString("FIRST_NAME"));
                        employee.setUserId(rs.getString("USERID"));
                        employee.setStart(rs.getDate("START_DATE"));
                        employee.setComments(rs.getString("COMMENTS"));
                        
                        int managerId = rs.getInt("MANAGER_ID");
                        if (!rs.wasNull()) {
                            employee.setManager(managerId);
                        }
                        
                        employee.setTitle(rs.getString("TITLE"));
                        employee.setDeptId(rs.getInt("DEPT_ID"));
                        employee.setSalary(rs.getBigDecimal("SALARY"));
                        
                        BigDecimal commissionPct = rs.getBigDecimal("COMMISSION_PCT");
                        if (!rs.wasNull()) {
                            employee.setCommissionPct(commissionPct);
                        }
                        
                        employees.add(employee);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
        }

        return new Page<>(employees, pageNum, pageSize, (int) totalRecords);
    }

    @Override
    public Employee findById(Integer id) {
        String sql = "SELECT e.*, m.FIRST_NAME as manager_first_name, m.LAST_NAME as manager_last_name " +
                     "FROM s_emp e LEFT JOIN s_emp m ON e.MANAGER_ID = m.ID WHERE e.ID = ?";
        try (Connection conn = DBCPUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(Employee employee) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBCPUtils.getConnection();
            conn.setAutoCommit(false);
            
            // 查找最小的可用ID
            String findMinIdSql = "SELECT MIN(t.ID) + 1 AS next_id FROM s_emp t WHERE NOT EXISTS (SELECT 1 FROM s_emp WHERE ID = t.ID + 1)";
            ps = conn.prepareStatement(findMinIdSql);
            rs = ps.executeQuery();
            int newId = 1;
            if (rs.next()) {
                newId = rs.getInt("next_id");
                if (rs.wasNull()) {
                    // 如果表为空，使用1作为第一个ID
                    newId = 1;
                }
            }
            
            // 关闭获取ID的PreparedStatement和ResultSet
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            
            // 插入新员工数据，包含ID
            String sql = "INSERT INTO s_emp (ID, LAST_NAME, FIRST_NAME, USERID, START_DATE, COMMENTS, MANAGER_ID, TITLE, DEPT_ID, SALARY, COMMISSION_PCT) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, newId);
            ps.setString(2, employee.getLast());
            ps.setString(3, employee.getFirst());
            ps.setString(4, employee.getUserId());
            ps.setDate(5, new java.sql.Date(employee.getStart().getTime()));
            ps.setString(6, employee.getComments());
            if (employee.getManager() != null) {
                ps.setInt(7, employee.getManager());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setString(8, employee.getTitle());
            ps.setInt(9, employee.getDeptId());
            ps.setBigDecimal(10, employee.getSalary());
            if (employee.getCommissionPct() != null) {
                ps.setBigDecimal(11, employee.getCommissionPct());
            } else {
                ps.setNull(11, Types.DECIMAL);
            }
            
            System.out.println("执行SQL: " + sql);
            System.out.println("参数值: " + 
                "ID=" + newId + 
                ", LAST_NAME=" + employee.getLast() + 
                ", FIRST_NAME=" + employee.getFirst() + 
                ", USERID=" + employee.getUserId() + 
                ", START_DATE=" + employee.getStart() + 
                ", COMMENTS=" + employee.getComments() + 
                ", MANAGER_ID=" + employee.getManager() + 
                ", TITLE=" + employee.getTitle() + 
                ", DEPT_ID=" + employee.getDeptId() + 
                ", SALARY=" + employee.getSalary() + 
                ", COMMISSION_PCT=" + employee.getCommissionPct());
            
            int rows = ps.executeUpdate();
            
            if (rows > 0) {
                conn.commit();
                // 设置生成的ID回对象中
                employee.setId(newId);
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL错误: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean updateById(Employee employee) {
        String sql = "UPDATE s_emp SET LAST_NAME=?, FIRST_NAME=?, USERID=?, START_DATE=?, COMMENTS=?, " +
                    "MANAGER_ID=?, TITLE=?, DEPT_ID=?, SALARY=?, COMMISSION_PCT=? WHERE ID=?";
        try (Connection conn = DBCPUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getLast());
            stmt.setString(2, employee.getFirst());
            stmt.setString(3, employee.getUserId());
            stmt.setDate(4, new java.sql.Date(employee.getStart().getTime()));
            if (employee.getComments() != null) {
                stmt.setString(5, employee.getComments());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            if (employee.getManager() != null) {
                stmt.setInt(6, employee.getManager());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setString(7, employee.getTitle());
            stmt.setInt(8, employee.getDeptId());
            stmt.setBigDecimal(9, employee.getSalary());
            if (employee.getCommissionPct() != null) {
                stmt.setBigDecimal(10, employee.getCommissionPct());
            } else {
                stmt.setNull(10, Types.DECIMAL);
            }
            stmt.setInt(11, employee.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM s_emp WHERE id = ?";
        try (Connection conn = DBCPUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Employee> getEmployeesByManagerId(Integer managerId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, m.FIRST_NAME as manager_first_name, m.LAST_NAME as manager_last_name " +
                     "FROM s_emp e LEFT JOIN s_emp m ON e.MANAGER_ID = m.ID WHERE e.MANAGER_ID = ?";

        try (Connection conn = DBCPUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public List<Employee> getEmployeesWithoutManager() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, m.FIRST_NAME as manager_first_name, m.LAST_NAME as manager_last_name " +
                     "FROM s_emp e LEFT JOIN s_emp m ON e.MANAGER_ID = m.ID WHERE e.MANAGER_ID IS NULL";

        try (Connection conn = DBCPUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // 辅助方法：将ResultSet映射到Employee对象
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        
        employee.setId(rs.getInt("ID"));
        employee.setLast(rs.getString("LAST_NAME"));
        employee.setFirst(rs.getString("FIRST_NAME"));
        employee.setUserId(rs.getString("USERID"));
        employee.setStart(rs.getDate("START_DATE"));
        employee.setComments(rs.getString("COMMENTS"));
        
        int managerId = rs.getInt("MANAGER_ID");
        if (!rs.wasNull()) {
            employee.setManager(managerId);
        }
        
        employee.setTitle(rs.getString("TITLE"));
        employee.setDeptId(rs.getInt("DEPT_ID"));
        employee.setSalary(rs.getBigDecimal("SALARY"));
        
        BigDecimal commissionPct = rs.getBigDecimal("COMMISSION_PCT");
        if (!rs.wasNull()) {
            employee.setCommissionPct(commissionPct);
        }
        
        return employee;
    }
}