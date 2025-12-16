public class Employee {
    private final int employeeId;
    private final int publicId;
    private final String fullName;
    private final String email;
    private final String jobTitle;
    private final Integer departmentId;
    private final boolean active;

    public Employee(int employeeId, int publicId, String fullName, String email, String jobTitle, Integer departmentId, boolean active) {
        this.employeeId = employeeId;
        this.publicId = publicId;
        this.fullName = fullName;
        this.email = email;
        this.jobTitle = jobTitle;
        this.departmentId = departmentId;
        this.active = active;
    }

    public int getEmployeeId() { return employeeId; }
    public int getPublicId() { return publicId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getJobTitle() { return jobTitle; }
    public Integer getDepartmentId() { return departmentId; }
    public boolean isActive() { return active; }

    @Override
    public String toString() {
        return publicId + " | " + fullName + " | " + email + " | " + jobTitle +
                " | dept=" + (departmentId == null ? "none" : departmentId) +
                " | active=" + active;
    }
}
