public class Employee extends BaseEntity implements Identifiable {
    private final int employeeId;
    private final String fullName;
    private final String email;
    private final String jobTitle;
    private final Integer departmentId;
    private final boolean active;

    public Employee(int employeeId, int publicId, String fullName, String email, String jobTitle, Integer departmentId, boolean active) {
        super(publicId);
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.email = email;
        this.jobTitle = jobTitle;
        this.departmentId = departmentId;
        this.active = active;
    }

    public int getEmployeeId() { return employeeId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getJobTitle() { return jobTitle; }
    public Integer getDepartmentId() { return departmentId; }
    public boolean isActive() { return active; }

    @Override
    public String toString() {
        return getPublicId() + " | " + fullName + " | " + email + " | " + jobTitle +
                " | dept=" + (departmentId == null ? "none" : departmentId) +
                " | active=" + active;
    }
}
