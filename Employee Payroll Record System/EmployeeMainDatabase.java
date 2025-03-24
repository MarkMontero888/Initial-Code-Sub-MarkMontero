import java.io.*;
import java.util.*;
//Saved Function Using Serializable and Implements Functions
//Recommendation Codes from CHATGPT for Accuracy
class Employee implements Serializable {
	private static final long serialVersionUID = 1L;
	private int employeeID;
	private String Name;
	private double Salary;
	private double NetPay;
	
	public Employee(int employeeID, String Name, double Salary) {
		this.employeeID = employeeID;
		this.Name = Name;
		this.Salary = Salary;
		calculateNetPay();
	}
	
	public void calculateNetPay() {
		//simplecalculator 
		this.NetPay = Salary * 1.1;
	}
	
	public int getEmployeeID() {
		return employeeID;
	}
	
	public String getName() {
		return Name;
	}
	
	public double getSalary() {
		return Salary;
	}
	
	public double getNetPay() {
		return NetPay;
	}
	
	public void setName(String Name) {
		this.Name = Name;
	}
	
	public void setSalary(double Salary) {
		this.Salary = Salary;
		calculateNetPay();
	}
	
	@Override
	public String toString() {
		return String.format("ID: %d | Name: %s | Salary: %.2f | Net Pay: %.2f",
				             employeeID, Name, Salary, NetPay);
	}
}
//EmployeeDatabase na conflict sa pangalan ng java file kaya naglagay ako ng "1" sa gitna.
//Employee1Database class gamit ng line (134)
class Employee1Database implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<Integer, Employee> employees;
	private final String DATA_FILE = System.getProperty("user.dir") + "/payroll_data.txt"; //pumalit ng file directory
	
	public Employee1Database() {
		this.employees = new HashMap<>(); // Dito ma saved ang mga Input
		loadData();
	}
	
	public boolean addEmployee(int ID, String name, double Salary) {
		if (employees.containsKey(ID)) {
			return false;
		}
		employees.put(ID, new Employee(ID, name, Salary));
		saveData();
		return true;
	}
	
	public Employee getEmployee(int ID) {
		return employees.get(ID);
	}
	
	public List<Employee> getAllEmployees() {
		return new ArrayList<>(employees.values());
	}
	
	public boolean updateEmployee(int ID, String Name, double Salary) {
		if (!employees.containsKey(ID) ) {
			return false;
		}
		Employee emp = employees.get(ID);
		emp.setName(Name);
		emp.setSalary(Salary);
		saveData();
		return true;
	}
	
	
	public boolean deleteEmployee(int ID) {
		if(!employees.containsKey(ID)) {
			return false;
		}
		employees.remove(ID);
		saveData();
		return true;
	}
	
	private void saveData() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
			oos.writeObject(employees);
			System.out.println("Successfully Saved");
		} catch (IOException e) {
			System.out.println("Error Data Save: " + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadData() {
		File file = new File(DATA_FILE);
		if (!file.exists()) {
			return;
		}
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
			employees = (Map<Integer, Employee>) ois.readObject();
			System.out.println("Data Load Successfully");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error Loading Data: " + e.getMessage());
			employees = new HashMap<>();
		}
	}
}

// Pinaka importante 
public class EmployeeMainDatabase {
	private static Scanner scanner = new Scanner(System.in);
	private static Employee1Database employeeDatabase = new Employee1Database();
	
	public static void main(String[] args) {
		boolean exit = false;
		
		
		while (!exit) {
			System.out.println("\n===== EMPLOYEE PAYROLL MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. View Employee by ID");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            //getValidIntInput sa baba ilagay
            int choice = getValidIntInput();
            
            switch (choice) {
                case 1:
                	addEmployee();
                	break;
                case 2:
                	viewAllEmployees();
                	break;
                case 3:
                	viewEmployeeByID();
                	break;
                case 4:
                    updateEmployee();
                    break;
                case 5:
                    deleteEmployee();
                    break;
                case 6:
                    exit = true;
                    System.out.println("Thank you for using the Employee Payroll Record System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
		}
		scanner.close();
	}
	
	private static void addEmployee() {
		System.out.println("\n----- ADD NEW EMPLOYEE -----");
		
		System.out.print("Enter Employee ID: ");
		int ID = getValidIntInput();
		
		// Para Makita Kung Nakalagay yung Employee
		// specific name employeeDatabase from line (134)
		if (employeeDatabase.getEmployee(ID) != null) {
			System.out.println("Employee with ID " + ID + " already exists!");
			return;
		}
		
		System.out.print("Enter Employee Name: ");
		String Name = scanner.nextLine();
		
		System.out.print("Enter Salary: ");
		//getValidDoubleInput sa baba ilagay
		double Salary = getValidDoubleInput();
		
		if (employeeDatabase.addEmployee(ID, Name, Salary)) {
			System.out.println("Employee Added Successfully");
		} else {
			System.out.println("Failed to Add Employee.");
			
		}
	}
	
	private static void viewAllEmployees() {
		System.out.println("\n----- ALL EMPLOYEES -----");
		List<Employee> allEmployees = employeeDatabase.getAllEmployees();
		
		if (allEmployees.isEmpty()) {
			System.out.println("No Employees Found in the Sytem.");
			return;
		}
		
		System.out.println("===========================================================================================");
		System.out.printf("%-10s | %-30s | %-15s | %-15s\n", "ID", "NAME", "SALARY", "NET PAY");
		System.out.println("===========================================================================================");
		
		for (Employee emp : allEmployees) {
			System.out.printf("%-10d | %-30s | ₱%-14.2f | ₱%-14.2f\n",
					emp.getEmployeeID(), emp.getName(), emp.getSalary(), emp.getNetPay());
		}
		System.out.println("===========================================================================================");
	}
	
	private static void viewEmployeeByID() {
		System.out.println("\n----- VIEW EMPLOYEE BY ID -----");
		System.out.print("Enter Employee ID: ");
		int ID = getValidIntInput();
		
	    Employee emp = employeeDatabase.getEmployee(ID);
	    if (emp == null) {
	    	System.out.println("Employee with ID " + ID + " Not Found!");
	    	return;
	    }
	    
	    System.out.println("Employee Details:");
	    System.out.println("==========================================================================");
	    System.out.printf("%-10s | %-30s | %-15s | %-15s\n", "ID", "NAME", "BASIC SALARY", "NET PAY");
	    System.out.println("==========================================================================");
	    System.out.printf("%-10d | %-30s | ₱%-14.2f | ₱%-14.2f\n",
	    		emp.getEmployeeID(), emp.getName(), emp.getSalary(), emp.getNetPay());
	    System.out.println("=========================================================================="); 		
	}
	
	private static void updateEmployee( ) {
		System.out.println("\n----- UPDATE EMPLOYEE -----");
		System.out.print("ENTER EMPLOYEE I.D TO UPDATE");
		int ID = getValidIntInput();
		
		Employee emp = employeeDatabase.getEmployee(ID);
		if (emp == null) {
			System.out.println("Employee with ID " + ID + " not found");
			return;
		}
		
		System.out.println("Current Employee Details: " + emp);
		
		System.out.print("ENTER NEW NAME (or press Enter to Keep): ");
		String Name = scanner.nextLine().trim();
		if (Name.isEmpty()) {
			Name = emp.getName();
		}
		
		System.out.print("ENTER NEW SALARY (or press Enter to Keep): ");
		String SalaryInput = scanner.nextLine().trim();
		double Salary = emp.getSalary();
		
		if (!SalaryInput.isEmpty()) {
			try {
				Salary = Double.parseDouble(SalaryInput);
			} catch (NumberFormatException e) {
				System.out.println("INVALID INPUT. KEEP EXISTING SALARY.");
			}
		}
			
		if (employeeDatabase.updateEmployee(ID, Name, Salary) ) {
			System.out.println("EMPLOYEE UPDATED SUCCESSFULLY");
		} else {
			System.out.println("FAILED TO UPDATED EMPLOYEE.");
		}
	}
	
	
	private static void deleteEmployee() {
		System.out.println("\n----- DELETE EMPLOYEE -----");
		System.out.print("Enter Employee ID to Delete: ");
		int ID = getValidIntInput();
		
		Employee emp = employeeDatabase.getEmployee(ID);
		if (emp == null) {
			System.out.println("EMPLOYEE WITH ID " + ID + "NOT FOUND");
			return;
		}
		
		System.out.println("EMPLOYEE TO DELETE: " + emp);
		System.out.print("ARE YOU SURE YOU WANT TO DELETE THIS EMPLOYEE? (y/n): ");
		String confirmation = scanner.nextLine().trim().toLowerCase();
		
		
		if (confirmation.equals("y") || confirmation.equals("yes")) {
			if (employeeDatabase.deleteEmployee(ID)) {
				System.out.println("EMPLOYEE DELETED SUCCESSFUL");
			} else {
				System.out.println("FAILED TO DELETE EMPLOYEE.");
			}
		} else {
			System.out.println("DELETION CANCELLED.");
		}
	}
	//Recommended Code from GPT
	private static int getValidIntInput() {
		while (true) {
			try {
				String input = scanner.nextLine();
				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.print("Please Enter a Valid Number: ");
			}
		}
	}
	//Recommended Code from GPT
	private static double getValidDoubleInput() {
		while (true) {
			try {
				String input = scanner.nextLine();
				return Double.parseDouble(input);
			} catch (NumberFormatException e) {
				System.out.print("Please Enter a Valid Decimal Number: ");
			}
		}
	}
}