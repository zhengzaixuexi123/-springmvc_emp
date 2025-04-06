package com.example.controller;

import com.example.dao.EmployeeDao;
import com.example.dao.EmployeeDaoImpl;
import com.example.modle.Employee;
import com.example.modle.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeDao employeeDao = new EmployeeDaoImpl();
    private final SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MM-yy", new Locale("zh", "CN"));
    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/list")
    public String listEmployees(@RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize,
                              Model model) {
        System.out.println("正在获取员工列表，页码：" + pageNum + "，每页大小：" + pageSize);
        Page<Employee> page = employeeDao.getEmployeesPage(pageNum, pageSize);
        System.out.println("总记录数: " + page.getTotal());
        System.out.println("当前页记录数: " + page.getList().size());
        if (page.getList() != null && !page.getList().isEmpty()) {
            System.out.println("第一条记录: " + page.getList().get(0));
        }
        model.addAttribute("page", page);
        return "list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "add";
    }

    @PostMapping("/add")
    public String addEmployee(
            @RequestParam String last,
            @RequestParam String first,
            @RequestParam String userId,
            @RequestParam String start,
            @RequestParam(required = false) String comments,
            @RequestParam(required = false) Integer manager,
            @RequestParam String title,
            @RequestParam Integer deptId,
            @RequestParam BigDecimal salary,
            @RequestParam(required = false) BigDecimal commissionPct,
            Model model) {
        try {
            Employee employee = new Employee();
            employee.setLast(last);
            employee.setFirst(first);
            employee.setUserId(userId);
            
            // 将输入的日期字符串解析为Date对象
            try {
                employee.setStart(inputDateFormat.parse(start));
            } catch (Exception e) {
                // 如果解析失败，尝试使用显示格式解析
                employee.setStart(displayDateFormat.parse(start));
            }
            
            employee.setComments(comments);
            employee.setManager(manager);
            employee.setTitle(title);
            employee.setDeptId(deptId);
            employee.setSalary(salary);
            employee.setCommissionPct(commissionPct);

            System.out.println("正在添加员工: " + employee);
            boolean success = employeeDao.add(employee);
            System.out.println("添加员工结果: " + success);

            if (success) {
                return "redirect:list";
            } else {
                model.addAttribute("error", "添加员工失败");
                model.addAttribute("employee", employee);
                return "add";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "添加员工时发生错误：" + e.getMessage());
            return "add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Employee employee = employeeDao.findById(id);
        model.addAttribute("employee", employee);
        return "edit";
    }

    @PostMapping("/edit")
    public String updateEmployee(
            @RequestParam Integer id,
            @RequestParam String last,
            @RequestParam String first,
            @RequestParam String userId,
            @RequestParam String start,
            @RequestParam(required = false) String comments,
            @RequestParam(required = false) Integer manager,
            @RequestParam String title,
            @RequestParam Integer deptId,
            @RequestParam BigDecimal salary,
            @RequestParam(required = false) BigDecimal commissionPct) {
        try {
            Employee employee = new Employee();
            employee.setId(id);
            employee.setLast(last);
            employee.setFirst(first);
            employee.setUserId(userId);
            
            // 将输入的日期字符串解析为Date对象
            try {
                employee.setStart(inputDateFormat.parse(start));
            } catch (Exception e) {
                // 如果解析失败，尝试使用显示格式解析
                employee.setStart(displayDateFormat.parse(start));
            }
            
            employee.setComments(comments);
            employee.setManager(manager);
            employee.setTitle(title);
            employee.setDeptId(deptId);
            employee.setSalary(salary);
            employee.setCommissionPct(commissionPct);

            System.out.println("正在更新员工: " + employee);
            boolean success = employeeDao.updateById(employee);
            System.out.println("更新员工结果: " + success);

            return "redirect:/employee/list";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/employee/edit/" + id + "?error=" + e.getMessage();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Integer id) {
        employeeDao.delete(id);
        return "redirect:/employee/list";
    }

    @GetMapping("/view/{id}")
    public String viewEmployee(@PathVariable Integer id, Model model) {
        Employee employee = employeeDao.findById(id);
        model.addAttribute("employee", employee);
        return "view";
    }

    @GetMapping("/page")
    @ResponseBody
    public Page<Employee> getEmployeesPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return employeeDao.getEmployeesPage(pageNum, pageSize);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Employee findById(@PathVariable Integer id) {
        return employeeDao.findById(id);
    }

    @PutMapping("/update")
    @ResponseBody
    public String updateById(@RequestBody Employee employee) {
        return employeeDao.updateById(employee) ? "更新成功" : "更新失败";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String delete(@PathVariable Integer id) {
        return employeeDao.delete(id) ? "删除成功" : "删除失败";
    }

    @GetMapping("/manager/{managerId}")
    @ResponseBody
    public List<Employee> getEmployeesByManagerId(@PathVariable Integer managerId) {
        return employeeDao.getEmployeesByManagerId(managerId);
    }

    @GetMapping("/no-manager")
    @ResponseBody
    public List<Employee> getEmployeesWithoutManager() {
        return employeeDao.getEmployeesWithoutManager();
    }
}