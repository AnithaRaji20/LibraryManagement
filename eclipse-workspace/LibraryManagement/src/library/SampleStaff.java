package library;

import java.util.ArrayList;
import java.util.List;

public class SampleStaff {
	public static List<Staff> createStaff() {
        List<Staff> staffList = new ArrayList<>();
        
        Staff staff1 = new Staff("Eve");
        Staff staff2 = new Staff("John");
        Staff staff3 = new Staff("Kathy");
        
        staffList.add(staff1);
        staffList.add(staff2);
        staffList.add(staff3);
        
        return staffList;
    }
}
