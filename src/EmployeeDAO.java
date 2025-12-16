import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private static final SecureRandom RNG = new SecureRandom();

    public int createEmployee(String fullName, String email, String jobTitle, Integer departmentId, boolean active)
            throws InvalidInputException, DuplicateRecordException, SQLException {

        validateFields(fullName, email, jobTitle);

        int publicId = generateUniquePublicId();

        String sql = "INSERT INTO Employee (public_id, full_name, email, job_title, department_id, active) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, publicId);
            ps.setString(2, fullName.trim());
            ps.setString(3, email.trim());
            ps.setString(4, jobTitle.trim());

            if (departmentId == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, departmentId);

            ps.setInt(6, active ? 1 : 0);

            ps.executeUpdate();
            return publicId;

        } catch (SQLException ex) {
            throw mapSqlException(ex);
        }
    }

    public Employee getEmployeeById(int publicId) throws RecordNotFoundException, SQLException {
        String sql = "SELECT employee_id, public_id, full_name, email, job_title, department_id, active " +
                     "FROM Employee WHERE public_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, publicId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new RecordNotFoundException("No employee found with ID: " + publicId);
                return mapEmployee(rs);
            }
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        String sql = "SELECT employee_id, public_id, full_name, email, job_title, department_id, active " +
                     "FROM Employee ORDER BY employee_id";
        List<Employee> result = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) result.add(mapEmployee(rs));
        }
        return result;
    }

    public List<Employee> getActiveEmployees() throws SQLException {
        String sql = "SELECT employee_id, public_id, full_name, email, job_title, department_id, active " +
                     "FROM Employee WHERE active = 1 ORDER BY employee_id";
        List<Employee> result = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) result.add(mapEmployee(rs));
        }
        return result;
    }

    public void updateEmployee(int publicId, String fullName, String email, String jobTitle, Integer departmentId, boolean active)
            throws InvalidInputException, RecordNotFoundException, DuplicateRecordException, SQLException {

        validateFields(fullName, email, jobTitle);
        getEmployeeById(publicId);

        String sql = "UPDATE Employee SET full_name = ?, email = ?, job_title = ?, department_id = ?, active = ? WHERE public_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fullName.trim());
            ps.setString(2, email.trim());
            ps.setString(3, jobTitle.trim());

            if (departmentId == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, departmentId);

            ps.setInt(5, active ? 1 : 0);
            ps.setInt(6, publicId);

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw mapSqlException(ex);
        }
    }

    public void deleteEmployee(int publicId) throws RecordNotFoundException, SQLException {
        getEmployeeById(publicId);

        String sql = "DELETE FROM Employee WHERE public_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, publicId);
            ps.executeUpdate();
        }
    }

    private Employee mapEmployee(ResultSet rs) throws SQLException {
        int employeeId = rs.getInt("employee_id");
        int publicId = rs.getInt("public_id");
        String fullName = rs.getString("full_name");
        String email = rs.getString("email");
        String jobTitle = rs.getString("job_title");

        int deptVal = rs.getInt("department_id");
        Integer deptId = rs.wasNull() ? null : deptVal;

        boolean active = rs.getInt("active") == 1;

        return new Employee(employeeId, publicId, fullName, email, jobTitle, deptId, active);
    }

    private void validateFields(String fullName, String email, String jobTitle) throws InvalidInputException {
        if (fullName == null || fullName.trim().isEmpty()) throw new InvalidInputException("Full name cannot be empty.");

        if (email == null || email.trim().isEmpty()) throw new InvalidInputException("Email cannot be empty.");

        String e = email.trim();
        if (!e.contains("@") || e.startsWith("@") || e.endsWith("@") || e.contains(" ")) {
            throw new InvalidInputException("Email looks invalid.");
        }

        if (jobTitle == null || jobTitle.trim().isEmpty()) throw new InvalidInputException("Job title cannot be empty.");
    }

    private SQLException mapSqlException(SQLException ex) throws DuplicateRecordException {
        String msg = ex.getMessage() == null ? "" : ex.getMessage().toLowerCase();
        if (msg.contains("unique") || msg.contains("constraint")) {
            throw new DuplicateRecordException("Duplicate value (likely email or public_id).");
        }
        return ex;
    }

    private int generateUniquePublicId() throws SQLException {
        for (int tries = 0; tries < 50; tries++) {
            int candidate = 10000 + RNG.nextInt(90000);
            if (!publicIdExists(candidate)) return candidate;
        }
        throw new SQLException("Could not generate a unique public id.");
    }

    private boolean publicIdExists(int publicId) throws SQLException {
        String sql = "SELECT 1 FROM Employee WHERE public_id = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, publicId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
